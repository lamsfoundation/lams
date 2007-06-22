import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import java.sql.SQLException;

public class Update204
{

	private String dbDriverClass;
    private String dbDriverUrl;
    private String dbUsername;
    private String dbPassword;
    private String version;
    private String serverVersion;
	
	// Check that the mysql version is valid
	public static void main(String[] args) throws SQLException
	{
		Update204 me = new Update204();

		if (args.length != 5 )
		{
			System.out.println("Usage: Java checkmysql dbDriverUrl dbUsername dbPassword Version ServerVersion ");
			System.exit(1);
		}
		else
		{
			me.execute(args[0], args[1], args[2], args[3], args[4]);
		}
	}

	public void execute(String url, String user, String pass, String version, String serverVersion) throws SQLException
	{

		try{
			
		        this.dbDriverClass = "com.mysql.jdbc.Driver";
	        	this.dbDriverUrl = url;
	        	this.dbUsername = user;
	        	this.dbPassword = pass;
	        	this.version = version;
	        	this.serverVersion = serverVersion;
	
	        	Class.forName(dbDriverClass);
	       		Connection conn = DriverManager.getConnection(dbDriverUrl, dbUsername, dbPassword);
	       		conn.setAutoCommit(false);
	       		PreparedStatement stmt  = conn.prepareStatement("SELECT count(*) from lams_system_tool");
		       	ResultSet results = stmt.executeQuery();
	
			if (results.first() == false)
			{
				throw new SQLException("Could not find LAMS database using url: " + dbDriverUrl);
			}
			else
			{
				int rows = results.getInt(1);
				if (rows == 5)
				{
					// Tells the 2.0.4 updater that the database does not need updating
					System.out.print("TRUE");		
				}
				else
				{
					//Tells the 2.0.4 updater that the database does need updating
					//Update db
					
					
					String insertStmt="INSERT INTO lams_system_tool (system_tool_id, learning_activity_type_id, tool_display_name, description, learner_url, learner_preview_url, learner_progress_url, export_pfolio_learner_url, export_pfolio_class_url, monitor_url, contribute_url, create_date_time) VALUES (5, 9, \'System Gate\', \'Gate: Opens under system control.\', \'learning/gate.do?method=knockGate\', \'learning/gate.do?method=knockGate\', null, null, \'monitoring/gateExportPortfolio?mode=teacher\', \'monitoring/gate.do?method=viewGate\', \'monitoring/gate.do?method=viewGate\', now()	)";
					PreparedStatement stmt2  = conn.prepareStatement(insertStmt);
					stmt2.executeUpdate();
					
					System.out.print("FALSE");
				}
				
				String insertStmt2="update lams_configuration set config_value=\"" +version+ "\" where config_key=\"Version\"";
				PreparedStatement stmt3  = conn.prepareStatement(insertStmt2);
				stmt3.executeUpdate();
				
				insertStmt2="update lams_configuration set config_value=\"" +serverVersion+ "\" where config_key=\"ServerVersionNumber\" OR config_key=\"AuthoringClientVersion\" OR config_key=\"LearnerClientVersion\" OR config_key=\"MonitorClientVersion\"";
				stmt3  = conn.prepareStatement(insertStmt2);
				stmt3.executeUpdate();
			}
			conn.commit();
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
