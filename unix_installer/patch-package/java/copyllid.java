import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.io.*;
import java.lang.Runtime;



public class copyllid
{
	public static void main(String args[]) throws Exception
	{
		System.out.println("Copying combined task language files");
		
		if (args.length < 5)
		{
			System.out.println("Usage");
			System.out.println("Make sure mysql-connector-java-3.x.x-bin.jar is in your classpath.");
			System.exit(1);
		}

		String llid = args[0] + "/server/default/deploy/lams.ear/lams-dictionary.jar/org/lamsfoundation/lams/library/";
		
		new copyllid(llid, args[1], args[2], args[3], args[4]);
	}

	
	private String dbname, dbuser, dbpass, dburl, dbdriver, lliddir;

	private String forumScribe, resourceForum, chatScribe; 

	private Connection conn;
	
	public copyllid(String lliddir, String dbuser, String dbpass, String dbname, String dburl)
	{
		dbdriver = "com.mysql.jdbc.Driver";

		try
		{	
			Class.forName(dbdriver);
			conn = DriverManager.getConnection(dburl, dbuser, dbpass);	
		
			PreparedStatement stmt = null;
			ResultSet result = null;


			stmt = conn.prepareStatement ("select learning_library_id from lams_learning_library where title = \"Chat and Scribe\"");
			result = stmt.executeQuery();

			if (result.first())
			{
				chatScribe = "llid" + result.getString("learning_library_id");
			}

			stmt = conn.prepareStatement ("select learning_library_id from lams_learning_library where title = \"Resources and Forum\"");
			result = stmt.executeQuery();

			if (result.first())
			{
				resourceForum = "llid" + result.getString("learning_library_id");
			}

			stmt = conn.prepareStatement ("select learning_library_id from lams_learning_library where title = \"Forum and Scribe\"");
			result = stmt.executeQuery();

			if (result.first())
			{
				forumScribe = "llid" + result.getString("learning_library_id");
			}

		}
		catch (SQLException se)
		{
			System.out.println(se.getMessage());
			se.printStackTrace();
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}


		chatScribe = lliddir + chatScribe;
		resourceForum = lliddir + resourceForum;
		forumScribe = lliddir + forumScribe;
	
		try
		{
			Runtime run = Runtime.getRuntime();
			run.exec("mkdir " + chatScribe);
			run.exec("mkdir " + resourceForum);
			run.exec("mkdir " + forumScribe);
			

			copyFiles("../language-pack/library/ChatAndScribe/", chatScribe);
			copyFiles("../language-pack/library/ForumAndScribe/", forumScribe);
			copyFiles("../language-pack/library/ResourcesAndForum/", resourceForum);
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		System.out.println("Language files for combined tasks successfully updated");
	}

	// The method copyFiles being defined
	public static void copyFiles(String strPath, String dstPath) throws IOException
	{

		File src = new File(strPath);
		File dest = new File(dstPath);
	
		if (src.isDirectory())
		{
			//if(dest.exists()!=true)
			dest.mkdirs();
			String list[] = src.list();
		
			for (int i = 0; i < list.length; i++)
			{
				String dest1 = dest.getAbsolutePath() + "/" + list[i];
				String src1 = src.getAbsolutePath() + "/" + list[i];
				copyFiles(src1 , dest1);
			}
		}
		else
		{
			FileInputStream fin = new FileInputStream(src);
			FileOutputStream fout = new FileOutputStream (dest);
			int c;
			while ((c = fin.read()) >= 0)
				fout.write(c);
			fin.close();
			fout.close();
		}
	}

}
