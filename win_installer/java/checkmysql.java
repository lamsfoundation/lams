import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import java.sql.SQLException;
import java.net.UnknownHostException;

public class checkmysql
{
	private String version;
	private String dbDriverClass;
    private String dbDriverUrl;
    private String dbUsername;
	private String dbPassword;	
	
	// Check that the mysql version is valid
	public static void main(String[] args) throws SQLException, UnknownHostException
	{
		checkmysql me = new checkmysql();

		if (args.length < 4)
		{
			System.out.println("Usage: Java checkmysql dbDriverUrl dbUsername dbPassword version");
			System.exit(1);
		}
		else
		{
			try
			{
				me.execute(args[0], args[1], args[2], args[3]);
			}
			catch (UnknownHostException e)
			{	
				System.out.println("MySql Host is not valid, please enter a new value");
				System.exit(1);
			}
		}
	}

	public void execute(String url, String user, String pass, String version) throws SQLException, UnknownHostException
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
       		PreparedStatement stmt  = conn.prepareStatement("SELECT * FROM lams_configuration WHERE config_key= \"Version\"");
	       	ResultSet results = stmt.executeQuery();
	
			if (results.first() == false)
			{
				throw new SQLException("Could not find LAMS database using url: " + dbDriverUrl);
			}
			else
			{
				String dbVersion = results.getString("config_value");
				if (dbVersion.equals(version) == false)
				{
					throw new SQLException("Your current LAMS version: " +dbVersion+ " is not compatible with this upgrade. Required version: " +version);		
				}
				else
				{
					System.out.println("LAMS version is compatible with this upgrade.\n");
				}
			}
			conn.close();
		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
			System.out.println("Upgrade failed. LAMS database check failed.\n");
			System.exit(1); 
		}
	
		catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Upgrade failed. Unknown failure checking LAMS database version.\n");
            e.printStackTrace();
            System.exit(1);
		}
	}
	
}
