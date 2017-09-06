package org.freifeld.consolidator.controller;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.BsonDateTime;
import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.json.JsonWriterSettings;
import org.freifeld.consolidator.controller.configuration.ConfigVariable;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonReader;
import java.io.StringReader;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;
import static com.mongodb.client.model.Updates.addEachToSet;
import static com.mongodb.client.model.Updates.pullByFilter;
import static org.freifeld.consolidator.entity.TagMetaData.*;

/**
 * @author royif
 * @since 15/08/17.
 */
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class MongoDriver
{
	private static final JsonWriterSettings TO_JSON_SETTINGS = JsonWriterSettings.builder()
			.dateTimeConverter((value, writer) -> writer.writeString(Instant.ofEpochMilli(value).toString()))
			.build();

	private MongoClient client;

	private MongoDatabase database;

	@Inject
	@ConfigVariable("MONGO_IP_ADDRESS")
	private String mongoIPAddress;

	@Inject
	@ConfigVariable("MONGO_PORT")
	private int mongoPort;

	@Inject
	@ConfigVariable("MONGO_DATABASE_NAME")
	private String mongoDatabaseName;

	@Inject
	@ConfigVariable("MONGO_COLLECTION_NAME")
	private String mongoCollectionName;

	@PostConstruct
	private void setUp()
	{
		ServerAddress serverAddress = new ServerAddress(this.mongoIPAddress, this.mongoPort);
		MongoClientOptions clientOptions = MongoClientOptions.builder()
				.applicationName("Consolidator")
				.addCommandListener(new MongoCommandListener())
				.build();

		this.client = new MongoClient(serverAddress, clientOptions);
		this.database = this.client.getDatabase(this.mongoDatabaseName);
	}

	@PreDestroy
	public void destroy()
	{
		this.client.close();
	}

	public JsonArray getAllTags()
	{
		MongoCollection<BsonDocument> collection = this.database.getCollection(this.mongoCollectionName, BsonDocument.class);

		JsonArrayBuilder builder = Json.createArrayBuilder();
		collection.find().map(doc ->
		{
			doc.remove("_id");
			return doc.toJson(TO_JSON_SETTINGS);
		}).forEach((Consumer<? super String>) s ->
		{
			try (JsonReader reader = Json.createReader(new StringReader(s)))
			{
				builder.add(reader.readObject());
			}
		});

		return builder.build();
	}

	public void addTags(String... tags)
	{
		MongoCollection<BsonDocument> collection = this.database.getCollection(this.mongoCollectionName, BsonDocument.class);
		List<BsonDocument> docs = Stream.of(tags).map(s ->
		{
			BsonDocument doc = new BsonDocument();
			doc.append(NAME, new BsonString(s)).append(CREATION_DATE, new BsonDateTime(System.currentTimeMillis()));
			return doc;
		}).collect(Collectors.toList());
		collection.insertMany(docs);
	}

	public void removeTags(String... tags)
	{
		MongoCollection<BsonDocument> collection = this.database.getCollection(this.mongoCollectionName, BsonDocument.class);
		collection.deleteMany(in(NAME, tags));
	}

	public void addContainer(String tagName, String... containers)
	{
		MongoCollection<BsonDocument> collection = this.database.getCollection(this.mongoCollectionName, BsonDocument.class);
		collection.findOneAndUpdate(eq(NAME, tagName), addEachToSet(CONTAINERS, Arrays.asList(containers)));
	}

	public void removeContainer(String tagName, String... containers)
	{
		MongoCollection<BsonDocument> collection = this.database.getCollection(this.mongoCollectionName, BsonDocument.class);
		collection.findOneAndUpdate(eq(NAME, tagName), pullByFilter(in(CONTAINERS, containers)));

	}
}
