package com.ibm.bluemix.mongodb;

import java.util.*;

import org.cloudfoundry.runtime.env.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

import com.mongodb.*;

public class MainServlet extends HttpServlet implements Servlet {


  protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    try{
        String connURL = getServiceURI();
        MongoClient mongo = new MongoClient(new MongoClientURI(connURL));

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
            out.println( "Inserted: " + cursor.next());
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
            out.println( "Updated: " + cursor2.next());
        }

        out.println("Success!!");

    } catch (Exception e) {
        out.println("Failed: " + e.getMessage());
        e.printStackTrace();
    }
  }

  public String getServiceURI() throws Exception {
    CloudEnvironment environment = new CloudEnvironment();
    if ( environment.getServiceDataByLabels("mongodb").size() == 0 ) {
        throw new Exception( "No MongoDB service is bund to this app!!" );
    } 

    Map credential = (Map)((Map)environment.getServiceDataByLabels("mongodb").get(0)).get( "credentials" );
 
    return (String)credential.get( "url" );
  }

}
