import java.io.*;
import java.nio.channels.FileChannel;
import java.util.Properties;

public class backup
{
	
	private BufferedReader in;
	
	private String backupdir, jbossdir;

	public String mysqldir, dbuser, dbname, dbpass, dburl;

	public static void main(String args[])
	{
		new backup();
	}

	public backup(boolean mysql)
	{

	}

	public backup()
	{
		readProperties();
		promptUser();
		
		try
		{
			System.out.println("Copying files from " + jbossdir + "/ to " + backupdir + "/jboss-4.0.2/ ...");
			copyFiles(jbossdir + "/", backupdir+"/jboss-4.0.2/");
			System.out.println("Done.\n");
		}
		catch (IOException e)
		{
			System.out.println("\nError copying files: " + e.getMessage());
			System.exit(1);
		}


			System.out.println("Dumping database to: " + backupdir + " ...");
			dumpDatabase();
			System.out.println("\nDone. \n");
		
	}
	
	public void readProperties()
	{
		try 
		{
			Properties lamsProperties = new Properties();
			lamsProperties.load(new FileInputStream("lams.properties"));		
			jbossdir = lamsProperties.getProperty("JBOSS_DIR");
			mysqldir = lamsProperties.getProperty("SQL_DIR");
			dbname = lamsProperties.getProperty("DB_NAME");
			dbuser = lamsProperties.getProperty("DB_USER");
			dbpass = lamsProperties.getProperty("DB_PASS");
			dburl = "jdbc:mysql://localhost/" +dbname+ "?characterEncoding=utf8";
			
			File lams = new File(jbossdir + "/server/default/deploy/lams.ear/lams.jar");
			if (lams.exists() == false)
			{
				throw new IOException("The jboss directory in lams.properties does not contain a lams installation: " + jbossdir);
			}

		}
		catch (IOException e)
		{

			System.out.println("Error: " + e.getMessage());
			System.out.println("Please ensure you have correctly configured the lams.properties file in your root package directory before you continue");
			System.exit(1);

		}

	}

	public void promptUser()
	{
		try 
		{
			
			in = new BufferedReader(new InputStreamReader(System.in));

			System.out.println("Please enter the full path of where you wish to backup lams");
			System.out.print("> ");
			backupdir = in.readLine();

			File bak = new File(backupdir);

			if (bak.exists()==true)
			{
				System.out.print("A file or directory already exists at that location, do you wish to continue and remove existing files? (y/n/q): ");
				String cont = null;
				cont = in.readLine();


				switch (cont.charAt(0))
				{
					case 'y':
						if(!(deleteDir(bak)))
						{
							throw new IOException("Could not delete file or directory: " + backupdir + ". Please try an unused directory path to backup." );
						}
						break;
	
					case 'n':
						promptUser();
						break;
					default:
						System.out.println("Bye!");
						System.exit(1);
						break;
				}

				

			}

			
				
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
			System.exit(1);
		}
	}

	// deleting the backup directory if it already exists
	public boolean deleteDir(File dir)
	{
		if (dir.isDirectory())
		{
			String[] children = dir.list();
			for(int i=0; i<children.length; i++)
			{
				
				if (!(deleteDir(new File(dir, children[i]))))
				{
					return false;
				}
			}
		}
		
		// returns true if the directory is empty, and deleted
		return dir.delete();		
	}

	
	

	// The method copyFiles being defined
	public void copyFiles(String strPath, String dstPath) throws IOException
	{
		
  
		File src = new File(strPath);
		File dest = new File(dstPath);
	
		if (src.isDirectory())
		{
			System.out.println("Copying: " + src.getAbsolutePath());
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
 			copy(src, dest);
		}
	}

	public void copy(File source, File target) throws IOException
   	{ 
      		FileChannel sourceChannel = new FileInputStream(source).getChannel();
      		FileChannel targetChannel = new FileOutputStream(target).getChannel();
     		sourceChannel.transferTo(0, sourceChannel.size(), targetChannel);
      		sourceChannel.close();
      		targetChannel.close();
   	}


	public void dumpDatabase() 
	{
		try
		{

			FileWriter outfile = new FileWriter("bin/lamsdump.sql");
			outfile.write(mysqldir+ "/mysqldump -u" +dbuser+ " -p" +dbpass+ " " +dbname+ " > " +backupdir+ "/lams.dump");
			outfile.close();
		}
		catch (Exception e)
		{
			System.out.println("Error preparing sql dump: " + e.getMessage());
			System.exit(1);
		}
		
	}	

}
