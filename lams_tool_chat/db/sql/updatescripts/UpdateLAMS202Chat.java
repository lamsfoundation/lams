import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.Properties;

import org.hibernate.Hibernate;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.UUIDHexGenerator;

public class UpdateLAMS202Chat {

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
		String XMPPConference = args[0];
		me.execute(XMPPConference);
	}

	public void execute(String XMPPConference) throws Exception {

		dbDriverClass = "com.mysql.jdbc.Driver";
		dbDriverUrl = "jdbc:mysql://localhost/lams_demo?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&autoReconnect=true&useUnicode=true";
		dbUsername = "root";
		dbPassword = "secret4141";
		crPath = "/var/opt/lams/repository";

        String getChatSessions = "SELECT uid, jabber_room, room_created from tl_lachat11_session";

        String updateChatSessions = "UPDATE tl_lachat11_session set room_created=?, jabber_room=? where uid=?";

        Class.forName(dbDriverClass);
        Connection conn = DriverManager.getConnection(dbDriverUrl, dbUsername, dbPassword);
        conn.setAutoCommit(false);
        PreparedStatement stmt  = conn.prepareStatement(getChatSessions);
        ResultSet results = stmt.executeQuery();

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