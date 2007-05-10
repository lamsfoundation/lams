import java.io.*;
import java.util.Properties;

public class getJbossDir
{
	private String jbossdir;

	public static void main(String args[])
	{
		new getJbossDir();
	}


	public getJbossDir()
	{
		readProperties();
		writeScript();
	}

	public void writeScript() 
	{
		try
		{

			FileWriter outfile = new FileWriter("bin/shutdown.sh");
			outfile.write(jbossdir + "/bin/shutdown.sh -S");
			outfile.close();
		}
		catch (Exception e)
		{
			System.out.println("Error shutting down lams: " + e.getMessage());
			System.exit(1);
		}
		
	}

	public void readProperties()
	{
		try 
		{
			Properties lamsProperties = new Properties();
			lamsProperties.load(new FileInputStream("lams.properties"));		
			jbossdir = lamsProperties.getProperty("JBOSS_DIR");
			
			
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
}
