/**
 * Created by Luke Foxton 14/12/2006
 * A helper program for language-pack.nsi
 *
 * Takes the location of a .sql script file, runs it and prints the results
 * to standard output
 *
 */

import java.sql.*;
import java.util.*;
 
public class SQLQueryGenerator {
	
	 
	    public static void main(String argv[]) {
	        Connection con = null;
	        //ResourceBundle bundle =
	       //ResourceBundle.getBundle("SelectResource");
	 
	        
	                              

	        try {
	            //String url = bundle.getString("URL");
	            String url = "jdbc:mysql://localhost:3306/mysql";
	            String query = argv[0];
	        	String result ="";
	        	
	        	Statement stmt;
	            ResultSet rs; 
	 
	            //Class.forName(bundle.getString("Driver"));
	            // here is where the connection is made   
	            con = DriverManager.getConnection(url, "root", ""); 
	            stmt = con.createStatement();
	            rs = stmt.executeQuery(query); 
	    
	            while(rs.next()) {
	                result = result + rs.getString(0);
	            }
	            stmt.close(  );
	        }
	        catch( SQLException e ) {
	            e.printStackTrace(  );
	        }
	        finally {
	            if( con != null ) {
	                try { con.close(  ); }
	                catch( Exception e ) { }
	            }
	        }
	    }
	}

