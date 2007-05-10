import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.util.Properties;
import java.io.*;

public class checkmysql
{
	private String dbname, dbuser, dbpass, dburl;

	private static final String reqLamsVersion = "2.0";

	public static void main(String args[])
	{
		new checkmysql();
	}


	public checkmysql()
	{

		readProperties();
		System.out.println("Checking mysql installation");
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection(dburl, dbuser, dbpass);
			PreparedStatement stmt = conn.prepareStatement("select config_value from lams_configuration where config_key=\"Version\"");
			//PreparedStatement stmt = conn.prepareStatement("show tables");
			ResultSet result = stmt.executeQuery();

			if (result.first())
			{
				String currVersion = result.getString("config_value");
				if (currVersion.equals(reqLamsVersion))
				{
					System.out.println("Mysql databse check completed\n\nCurrent lams version: " + result.getString("config_value"));
				}
				else
				{
					throw new SQLException("The current LAMS version (LAMS " +currVersion+ " is not compatible with this update, LAMS " + reqLamsVersion+ " is required");

				}
			}
			else
			{
				throw new SQLException("Result for test set was empty, your lams database is not valid");
			}

		}
		catch (SQLException e)
		{
			System.out.println("Error detecting mysql: " + e.getMessage());
			System.exit(1);
		}
		catch (ClassNotFoundException c)
		{
			System.out.println("Error: " + c.getMessage());
			c.printStackTrace();
			System.exit(1);
		}

	}

	public void readProperties()
	{
		try
		{
			Properties lamsProperties = new Properties();
			lamsProperties.load(new FileInputStream("lams.properties"));
			dbname = lamsProperties.getProperty("DB_NAME");
			dbuser = lamsProperties.getProperty("DB_USER");
			dbpass = lamsProperties.getProperty("DB_PASS");
			dburl = "jdbc:mysql://localhost/" +dbname+ "?characterEncoding=utf8";

		}
		catch (IOException e)
		{

			System.out.println("Error: " + e.getMessage());
			System.exit(1);

		}
	}


}
