package org.freifeld.compass.controller;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.freifeld.compass.controller.configuration.ConfigVariable;

import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.inject.Inject;

/**
 * @author royif
 * @since 15/08/17.
 */
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class MongoDriver
{
	private static final String DATABASE_NAME = "ContainerDB";

	private MongoClient client;

	@Inject
	@ConfigVariable("MONGO_IP_ADDRESS")
	private String mongoIPAddress;

	@Inject
	@ConfigVariable("MONGO_PORT")
	private int mongoPort;

	@PostConstruct
	private void init()
	{
		MongoClient client = new MongoClient(this.mongoIPAddress, this.mongoPort);
		MongoDatabase tester = client.getDatabase(DATABASE_NAME);
		tester.createCollection("testingCollection");
		MongoCollection<Document> collection = tester.getCollection("testingCollection");
		Document document = new Document();
		document.append("myKey", "{\"prop\":\"val\"}");
		collection.insertOne(document);
		client.listDatabaseNames().iterator().forEachRemaining(System.out::println);
	}
}
