 package com.ibm.wasdev.cassandra;
 
 import java.util.HashMap;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.AlreadyExistsException;
 
 public class ConnectionManager
 {
    public  static final String STRING_SEPARATOR = "---";
	private static volatile ConnectionManager connMgr = null;
   private static final String QUERY_CREATE_KEYSPACE = "CREATE KEYSPACE itemkeyspace WITH replication = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 }";
   private static final String QUERY_USE_KEYSPACE = "USE itemkeyspace";
   private static final String QUERY_CREATE_TABLE = "CREATE TABLE item ( item text PRIMARY KEY, price text, trend text)";
    private String host = "localhost";
    private Cluster cluster = null;
    private Session session = null;
   
   public static ConnectionManager getInstance(String host)
     throws Exception
   {
      if (connMgr == null) {
        synchronized (ConnectionManager.class)
       {
          if (connMgr == null) {
           try
           {
              connMgr = new ConnectionManager();
              if ((host != null) && (!host.isEmpty())) {
                connMgr.host = host;
             }
              connMgr.init();
           }
           catch (Exception exp)
           {
              connMgr = null;
              throw exp;
           }
         }
       }
     }
      return connMgr;
   }
   
   private Cluster getCluster(String host)
   {
      return new Cluster.Builder().addContactPoints(new String[] { host }).build();
   }
   
   private void init()
   {
      this.cluster = getCluster(this.host);
     
      this.session = this.cluster.connect();
     
      Metadata metadata = this.cluster.getMetadata();
      System.out.println(String.format("Connected to cluster '%s' on %s.", new Object[] { metadata.getClusterName(), metadata.getAllHosts() }));
     try
     {
        this.session.execute("CREATE KEYSPACE itemkeyspace WITH replication = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 }");
        this.session.execute("USE itemkeyspace");
        this.session.execute("CREATE TABLE item ( item text PRIMARY KEY, price text, trend text)");
     }
     catch (AlreadyExistsException e)
     {
        System.out.println("Either the keyspace \"itemkeyspace\" or the table \"item\" exists already... ");
     }
   }
   
   public HashMap<String, String> addItem(String item, String price, String trend)
   {
      this.session.execute("USE itemkeyspace");
      //System.out.println("DGB: INSERT INTO item (item,  price, trend)  VALUES ('" + item + "', '" +price + "', '" + trend + "')");
      this.session.executeAsync("INSERT INTO item (item,  price, trend)  VALUES ('" + item + "', '" +price + "', '" + trend + "')");
 
      return getItems(this.session);
   }
   
   public ResultSet getItem(String item)
   {
      this.session.execute("USE itemkeyspace");
      return this.session.execute("SELECT * FROM ITEM WHERE item = '" + item + "'");
   }
   
   public HashMap<String, String> addItem(String item)
   {
      this.session.execute("USE itemkeyspace");
      this.session.executeAsync("DELETE FROM ITEM WHERE item = '" + item + "'");
      return getItems(this.session);
   }
   
   public HashMap<String, String> getItems(Session session)
   {
      ResultSet resultSet = session.execute("Select * from item");
     
      HashMap<String, String> data = new HashMap();
      for (Row row : resultSet) {
        data.put(row.getString("item"), row.getString("price") + STRING_SEPARATOR + row.getString("trend"));
     }
      return data;
   }
   
   public HashMap<String, String> removeItem(String item)
   {
      this.session.execute("USE itemkeyspace");
      this.session.execute("DELETE FROM ITEM WHERE item = '" + item + "'");
      return getItems(this.session);
   }
   
   public void shutdown()
   {
      if (!this.session.isClosed()) {
        this.session.close();
     }
      if (!this.cluster.isClosed()) {
        this.cluster.close();
     }
   }
 }
