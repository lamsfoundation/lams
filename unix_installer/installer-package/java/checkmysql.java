import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import java.sql.SQLException;

public class checkmysql
{
	private String version;
	private String dbDriverClass;
    	private String dbDriverUrl;
    	private String dbUsername;
	private String dbPassword;	
	
	// Check that the mysql version is valid
	public static void main(String[] args) throws SQLException
	{
		checkmysql me = new checkmysql();

		if (args.length < 4)
		{
			System.out.println("Usage: Java checkmysql dbDriverUrl dbUsername dbPassword version");
			System.exit(1);
		}
		else
		{
			me.execute(args[0], args[1], args[2], args[3]);
		}
	}

	public void execute(String url, String user, String pass, String version) throws SQLException
	{

		try{
			this.version = version;
		        this.dbDriverClass = "com.mysql.jdbc.Driver";
	        	this.dbDriverUrl = url;
	        	this.dbUsername = user;
	        	this.dbPassword = pass;
	
			Class.forName(dbDriverClass);
	       		Connection conn = DriverManager.getConnection(dbDriverUrl, dbUsername, dbPassword);
	       		conn.setAutoCommit(false);
	       		PreparedStatement stmt  = conn.prepareStatement("show variables where Variable_name=\"version\"");
		       	ResultSet results = stmt.executeQuery();
	
			if (results.first() == false)
			{
				throw new SQLException("No version row found in database");
			}
			else
			{
				String dbVersion = results.getString("Value");
				if (dbVersion.contains(version) == false)
				{
					throw new SQLException("Your MySql Version: \"" + dbVersion + "\" is not compatible LAMS");		
				}
				else
				{
					System.out.println("MySql host is compatible with LAMS.");
				}

			}
			conn.close();
		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
			System.out.println("MySql check failed. Check that your MYSQL_HOST variable in lams.properties points to a MySql 5.x installation");
			System.exit(1); 
		}
		catch (Exception e)
                {
                        System.out.println(e.getMessage());
                        System.out.println("Unknown failure finding MySql version");
                        e.printStackTrace();
			System.exit(1);
		}
	}
	
}
