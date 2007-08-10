import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import java.sql.SQLException;
import java.net.UnknownHostException;

public class checkmysqlversion
{
	private String dbDriverClass;
    private String dbDriverUrl;
    private String dbUsername;
	private String dbPassword;
	
	private double minMysqlVersion;
	
	// Check that the mysql version is valid
	public static void main(String[] args) throws SQLException, UnknownHostException
	{
		System.out.println("Java program invoked");
		checkmysqlversion me = new checkmysqlversion();

		System.out.println("args.length: " + args.length);
		if (args.length < 3)
		{
			System.out.println("Usage: Java checkmysqlversion dbDriverUrl dbUsername dbPassword");
			System.exit(1);
		}
		else
		{
			try
			{
				me.execute(args[0], args[1], args[2]);
			}
			catch (UnknownHostException e)
			{	
				System.out.println("MySql Host is not valid, please enter a new value");
				System.exit(1);
			}
		}
	}

	public void execute(String url, String user, String pass) throws SQLException, UnknownHostException
	{
		try{
	        this.dbDriverClass = "com.mysql.jdbc.Driver";
        	this.dbDriverUrl = url;
        	this.dbUsername = user;
        	this.dbPassword = pass;
			this.minMysqlVersion = 5.0;
	
			Class.forName(dbDriverClass);
       		Connection conn = DriverManager.getConnection(dbDriverUrl, dbUsername, dbPassword);
       		conn.setAutoCommit(false);
       		//PreparedStatement stmt  = conn.prepareStatement("SELECT * FROM lams_configuration WHERE config_key= \"Version\"");
       		PreparedStatement stmt  = conn.prepareStatement("SHOW variables WHERE Variable_name=\"version\"");
       		ResultSet results = stmt.executeQuery();
	
			if (results.first() == false)
			{
				throw new SQLException("Could not find mysql version usring url: " + dbDriverUrl);
			}
			else
			{
				String mysqlVersion = results.getString("Value");
				// If mysqlversion >= 5.0
				if (Double.parseDouble(mysqlVersion.substring(0,3)) >= minMysqlVersion )
				{
					System.out.println("MySQL version " + mysqlVersion + " is compatible with this version of LAMS.\n");	
				}
				else
				{
					throw new SQLException("The MySQL version you are attemping to use: " +mysqlVersion+ " is not compatible with this version of LAMS. Required version: " + minMysqlVersion);
				}
			}
			conn.close();
		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
			System.out.println("Upgrade failed. MySQL check failed.\n");
			System.exit(1); 
		}
	
		catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Upgrade failed. Unknown failure checking MySQL version.\n");
            e.printStackTrace();
            System.exit(1);
		}
	}
	
}
