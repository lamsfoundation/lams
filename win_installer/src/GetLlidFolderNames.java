import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;


public class GetLlidFolderNames {

    private String title;
	private String dbDriverClass;
    private String dbDriverUrl;
    private String dbUsername;
    private String dbPassword;

	/**
	 * Dump out a list of all the nodes in the content repository, with their type and expected paths
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		GetLlidFolderNames me = new GetLlidFolderNames();
		
		if (args.length < 4)
		{
			System.out.println("Usage: Java GetLlidFolderNames title dbDriverUrl dbUsername dbPassword");
			System.exit(1);
		}
		
		String title = args[0];
		String dbDriverUrl = args[1];
		String dbUsername = args[2];
		String dbPassword = args[3];
		me.execute(title, dbDriverUrl, dbUsername, dbPassword);
	}

	public void execute(String title, String dbDriverUrl, String dbUsername, String dbPassword) throws Exception 
	{
		

		this.dbDriverClass = "com.mysql.jdbc.Driver";
		this.dbDriverUrl = dbDriverUrl;
		this.dbUsername = dbUsername;
		this.dbPassword = dbPassword;
		this.title = title;
		
        String getLlid = "SELECT learning_library_id FROM lams_learning_library WHERE title = \'" + title +"\'";
        // SELECT learning_library_id FROM lams_learning_library WHERE title = $\'Chat and Scribe$\';"
        
        
        
        
        
        
        Class.forName(dbDriverClass);
        Connection conn = DriverManager.getConnection(dbDriverUrl, dbUsername, dbPassword);
        conn.setAutoCommit(false);
        PreparedStatement stmt  = conn.prepareStatement(getLlid);
        ResultSet results = stmt.executeQuery();

        if (results.first())
        {
        	System.out.print(results.getString("learning_library_id"));
        }
        else
        {
        	System.out.println("Could not find llid for:" + title);
        	System.exit(1);
        }
        /*
        while (results.next())
        {
			Long uid = results.getLong("uid");
			Boolean room_created = results.getBoolean("room_created");
        	String jabber_room = results.getString("jabber_room");

			String set_jabber_room;
			Boolean set_room_created;
			// checking if jabber_room is null
        	if (results.wasNull()) {
				// tool session was created by the room doesnt exist on the jabber server
				// generating a unique jabber room name
				set_room_created = false;
				set_jabber_room = (String) idGenerator.generate(null, null) + "@" + XMPPConference;

			} else {
				// jabber has already been created
				set_room_created = true;
				set_jabber_room = jabber_room;
			}

			stmtUpdate.setBoolean(1, set_room_created);
			stmtUpdate.setString(2, set_jabber_room);
			stmtUpdate.setLong(3, uid);
			stmtUpdate.addBatch();

        	System.out.print(uid + "\t");
        	System.out.print(room_created + "\t");
        	System.out.print(jabber_room + "\n");

        }
        int[] upCount = stmtUpdate.executeBatch();
        */
        
        
		conn.commit();
        conn.close();
	}

}