import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import java.util.Properties;

import org.hibernate.Hibernate;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.UUIDHexGenerator;

public class UpdateLAMS202Chat {

    private String XMPPConference;
	private String dbDriverClass;
    private String dbDriverUrl;
    private String dbUsername;
    private String dbPassword;
    private String crPath;

	/**
	 * Dump out a list of all the nodes in the content repository, with their type and expected paths
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		UpdateLAMS202Chat me = new UpdateLAMS202Chat();
		
		if (args.length < 4)
		{
			System.out.println("Usage: Java XMPPConference dbDriverUrl dbUsername dbPassword crPath");
			System.exit(1);
		}
		
		String dbDriverUrl = args[0];
		String dbUsername = args[1];
		String dbPassword = args[2];
		String crPath = args[3];
		me.execute(dbDriverUrl, dbUsername, dbPassword, crPath);
	}

	public void execute(String dbDriverUrl, String dbUsername, String dbPassword, String crPath) throws Exception {
		
		dbDriverClass = "com.mysql.jdbc.Driver";
		/*
		dbDriverUrl = "jdbc:mysql://localhost/lams_demo?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&autoReconnect=true&useUnicode=true";
		dbUsername = "root";
		dbPassword = "secret4141";
		crPath = "/var/opt/lams/repository";
		*/
		
		this.dbDriverClass = dbDriverClass;
		this.dbDriverUrl = dbDriverUrl;
		this.dbUsername = dbUsername;
		this.dbPassword = dbPassword;
		this.crPath = crPath;
		
        String getChatSessions = "SELECT uid, jabber_room, room_created from tl_lachat11_session";
        String getXmppConference = "SELECT config_value FROM lams_configuration WHERE config_key='XmppConference'";
        String updateChatSessions = "UPDATE tl_lachat11_session set room_created=?, jabber_room=? where uid=?";

        Class.forName(dbDriverClass);
        Connection conn = DriverManager.getConnection(dbDriverUrl, dbUsername, dbPassword);
        conn.setAutoCommit(false);
        PreparedStatement stmt  = conn.prepareStatement(getChatSessions);
        ResultSet results = stmt.executeQuery();

        // getting XmppConference value from db.
        Statement xmppStmt = conn.createStatement();
        ResultSet xmppResult = xmppStmt.executeQuery(getXmppConference);
        
        if (xmppResult.next()) 
        {
	        XMPPConference = xmppResult.getString(1);
	        System.out.println(XMPPConference);        
        } 
        else
        {
	        conn.close();
	        System.out.println("Could not find XMPP Conference.");
	        System.exit(1);
        }
      
        PreparedStatement stmtUpdate = conn.prepareStatement(updateChatSessions);

		IdentifierGenerator idGenerator = new UUIDHexGenerator();
				((Configurable) idGenerator).configure(Hibernate.STRING,
				new Properties(), null);

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

		conn.commit();
        conn.close();
	}

}