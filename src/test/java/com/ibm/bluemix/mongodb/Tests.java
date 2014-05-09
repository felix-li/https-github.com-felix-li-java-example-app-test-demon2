package com.ibm.bluemix.mongodb;

import java.util.*;

import org.junit.Before;
import org.junit.Test;

import org.cloudfoundry.runtime.env.*;

import java.net.UnknownHostException;
import java.util.Date;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.*;

public class Tests {

  private final Map envs = new HashMap();
  private CloudEnvironment environment;

  @Before
  public void setup() {
    environment = new CloudEnvironment();
  }

  @Test
  public void test1() {
    CloudEnvironment environment = new CloudEnvironment();
    if ( environment.getServices().size() == 0 ) {
      return;
    }
    
    Map credential = (Map)((Map)environment.getServiceDataByLabels("mongodb").get(0)).get( "credentials" );
    String url = (String)credential.get( "url" );

    try {
        System.out.println( "================= URL: " + url );
        MongoClient mongo = new MongoClient(new MongoClientURI(url));
     
        DB db = mongo.getDB("db");
     
        DBCollection table = db.getCollection("user");
     

        BasicDBObject document = new BasicDBObject();
        document.put("name", "Tom");
        document.put("age", 30);
        document.put("createdDate", new Date());
        table.insert(document);
     
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("name", "Tom");
     
        DBCursor cursor = table.find(searchQuery);
     
        while (cursor.hasNext()) {
            System.out.println( "Inserted: " + cursor.next());
        }
     
        BasicDBObject query = new BasicDBObject();
        query.put("name", "Tom");
     
        BasicDBObject newDocument = new BasicDBObject();
        newDocument.put("name", "Tina");
     
        BasicDBObject updateObj = new BasicDBObject();
        updateObj.put("$set", newDocument);
     
        table.update(query, updateObj);
     
        BasicDBObject searchQuery2 = new BasicDBObject().append("name", "Tina");
     
        DBCursor cursor2 = table.find(searchQuery2);
     
        while (cursor2.hasNext()) {
            System.out.println( "Updated: " + cursor2.next());
        }
     
        System.out.println("Successfully");
 
    } catch (Exception e) {
        System.out.println("Failed: " + e.getMessage());
	e.printStackTrace();
    }
    
  }

}
