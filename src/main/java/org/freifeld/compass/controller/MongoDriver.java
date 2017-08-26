package org.freifeld.compass.controller;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.freifeld.compass.controller.configuration.EnvironmentVariable;

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
	private MongoClient client;

	@Inject
	@EnvironmentVariable("MONGO_IP_ADDRESS")
	private String mongoIPAddress;

	@Inject
	@EnvironmentVariable("MONGO_PORT")
	private int mongoPort;

	public static void main(String[] args)
	{
		MongoClient client = new MongoClient("localhost", 27017);
		MongoDatabase tester = client.getDatabase("TestingDatabase");
		tester.createCollection("testingCollection");
		MongoCollection<Document> collection = tester.getCollection("testingCollection");
		Document document = new Document();
		document.append("myKey", "{\"prop\":\"val\"}");
		collection.insertOne(document);
		client.listDatabaseNames().iterator().forEachRemaining(System.out::println);
	}
}
