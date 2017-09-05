package org.freifeld.compass.controller;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.BsonDateTime;
import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.json.JsonWriterSettings;
import org.freifeld.compass.controller.configuration.ConfigVariable;

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
import static com.mongodb.client.model.Updates.addEachToSet;

/**
 * @author royif
 * @since 15/08/17.
 */
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class MongoDriver
{
	private static final JsonWriterSettings SETTINGS = JsonWriterSettings.builder().objectIdConverter((value, writer) -> writer.writeString(value.toString()))
			.dateTimeConverter((value, writer) -> writer.writeString(Instant.ofEpochMilli(value).toString())).build();

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

	private static JsonWriterSettings settings()
	{
		return JsonWriterSettings.builder().objectIdConverter((value, writer) -> writer.writeRaw(value.toString())).build();
	}

	@PostConstruct
	private void setUp()
	{
		this.client = new MongoClient(this.mongoIPAddress, this.mongoPort);
		this.database = this.client.getDatabase(this.mongoDatabaseName);
	}

	@PreDestroy
	public void destroy()
	{
		this.client.close();
	}

	public void addTags(String... tags)
	{
		MongoCollection<BsonDocument> collection = this.database.getCollection(this.mongoCollectionName, BsonDocument.class);
		List<BsonDocument> docs = Stream.of(tags).map(s ->
		{
			BsonDocument doc = new BsonDocument();
			doc.append("name", new BsonString(s)).append("creationDate", new BsonDateTime(System.currentTimeMillis()));
			return doc;
		}).collect(Collectors.toList());
		collection.insertMany(docs);
	}

	public void addContainer(String tagName, String... containers)
	{
		MongoCollection<BsonDocument> collection = this.database.getCollection(this.mongoCollectionName, BsonDocument.class);
		collection.findOneAndUpdate(eq("name", tagName), addEachToSet("containers", Arrays.asList(containers)));
	}

	public JsonArray allTags()
	{
		MongoCollection<BsonDocument> collection = this.database.getCollection(this.mongoCollectionName, BsonDocument.class);

		JsonArrayBuilder builder = Json.createArrayBuilder();
		collection.find().map(doc -> doc.toJson(SETTINGS)).forEach((Consumer<? super String>) s ->
		{
			try (JsonReader reader = Json.createReader(new StringReader(s)))
			{
				builder.add(reader.readObject());
			}
		});

		return builder.build();
	}
}
