package org.freifeld.compass.entity;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.freifeld.compass.entity.configuration.ConfigVariable;

import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.inject.Inject;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author royif
 * @since 15/08/17.
 */
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class MongoDriver
{
	private MongoClient client;

	private MongoDatabase database;

	@Inject
	@ConfigVariable("MONGO_IP_ADDRESS")
	private String mongoIPAddress;

	@Inject
	@ConfigVariable("MONGO_PORT")
	private int mongoPort;

	@Inject
	@ConfigVariable("MONGO_COLLECTION_NAME")
	private String mongoCollectionName;

	@Inject
	@ConfigVariable("MONGO_DATABASE_NAME")
	private String mongoDatabaseName;

	@PostConstruct
	private void init()
	{
		this.client = new MongoClient(this.mongoIPAddress, this.mongoPort);
		this.database = this.client.getDatabase(this.mongoDatabaseName);

		this.database.createCollection(this.mongoCollectionName);
	}

	public void addMultipleTags(Pair<String, String>... tags)
	{
		MongoCollection<Document> collection = this.database.getCollection(this.mongoCollectionName);
		List<Document> documents = Stream.of(tags).map(p ->
		{
			Document doc = new Document();
			doc.append("tag", p.first).append("container", p.second).append("timestamp", Instant.now().toString());
			return doc;
		}).collect(Collectors.toList());
		collection.insertMany(documents);
	}
}
