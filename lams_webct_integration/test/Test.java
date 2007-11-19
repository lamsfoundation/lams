package org.lamsfoundation.integration.webct;

import java.sql.*;

import com.microsoft.sqlserver.jdbc.*;

/**
 * Microsoft SQL Server JDBC test program
 */
public class Test {
 public Test() throws Exception {
   // Get connection
   
	 SQLServerDataSource ds = new SQLServerDataSource();
	 ds.setServerName("localhost");
	 ds.setUser("sa");
	 ds.setPassword("hernamewaslola");
	 ds.setPortNumber(1433); 
	 ds.setDatabaseName("webctdatabase");
	 Connection connection = ds.getConnection();
	 
//DriverManager.registerDriver(new Driver());
   //Connection connection = DriverManager.getConnection(
   //"jdbc:microsoft:sqlserver://localhost:1433","sa","hernamewaslola");
   if (connection != null) {
    System.out.println();
     System.out.println("Successfully connected");
     System.out.println();
     // Meta data
     DatabaseMetaData meta = connection.getMetaData();
     System.out.println("\nDriver Information");
     System.out.println("Driver Name: "
      + meta.getDriverName());
     System.out.println("Driver Version: "
      + meta.getDriverVersion());
     System.out.println("\nDatabase Information ");
     System.out.println("Database Name: "
      + meta.getDatabaseProductName());
     System.out.println("Database Version: "+
     meta.getDatabaseProductVersion());
   }
} // Test
public static void main (String args[]) throws Exception {
 Test test = new Test();
 while(true){}
}
}