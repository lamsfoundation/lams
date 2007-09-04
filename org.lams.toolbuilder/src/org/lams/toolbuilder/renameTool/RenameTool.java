package org.lams.toolbuilder.renameTool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.lams.toolbuilder.util.LamsToolBuilderLog;

/**
 * 
 * @author Anthony Sukkar
 * 
 */
public class RenameTool {

	private String taskliststr;
	
	private Set<String> txtType = new HashSet<String>();

	private List<String[]> nameList = new ArrayList<String[]>();

	public RenameTool(String taskliststr, String source)
	{
		this.taskliststr = taskliststr;
		txtType.add("clay");
		txtType.add("classpath");
		txtType.add("txt");
		txtType.add("cvsignore");
		txtType.add("html");
		txtType.add("htm");
		txtType.add("java");
		txtType.add("js");
		txtType.add("jsp");
		txtType.add("MF");
		txtType.add("bat");
		txtType.add("myd");
		txtType.add("mymetadata");
		txtType.add("project");
		txtType.add("properties");
		txtType.add("sh");
		txtType.add("sql");
		txtType.add("tag");
		txtType.add("log");
		txtType.add("tld");
		txtType.add("xml");
	}
	
	public boolean renameTool() throws Exception
	{
		File tasklist = new File(taskliststr);

		if (!tasklist.exists()) 
		{
			LamsToolBuilderLog.logError(new Exception("Cannot find tasklist configuration file"));
			throw new Exception("Cannot find tasklist configuration file");
		}
		
		File sourceDir = null;
		try {
			FileInputStream fis = new FileInputStream(tasklist);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));

			String line;
			while ((line = br.readLine()) != null) {

				line = line.trim();

				if (line.length() == 0 || (line.charAt(0) == '#'))
					continue;

				String[] strArray = line.split("\\s");

				String command = strArray[0];

				if (command.equals("Source")) {
					sourceDir = getFile(strArray);
				}

				if (command.equals("Rename")) {

					if (strArray.length != 3) {
						continue;
					} else {
						String[] pair = { strArray[1], strArray[2] };
						nameList.add(pair);
					}
				}

			}

			visitFile(sourceDir, "rename");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return true;
	}
	
	/*
	public static void main(String[] args) {
		
		txtType.add("clay");
		txtType.add("classpath");
		txtType.add("txt");
		txtType.add("cvsignore");
		txtType.add("html");
		txtType.add("htm");
		txtType.add("java");
		txtType.add("js");
		txtType.add("jsp");
		txtType.add("MF");
		txtType.add("bat");
		txtType.add("myd");
		txtType.add("mymetadata");
		txtType.add("project");
		txtType.add("properties");
		txtType.add("sh");
		txtType.add("sql");
		txtType.add("tag");
		txtType.add("log");
		txtType.add("tld");
		txtType.add("xml");
		
		
		File tasklist = new File("src/org/lams/toolbuilder/renameTool/tasklist.conf");

		if (!tasklist.exists()) {
			System.out.println("Can't find 'tasklist.conf'");
		}

		File sourceDir = null;
		try {
			FileInputStream fis = new FileInputStream(tasklist);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));

			String line;
			while ((line = br.readLine()) != null) {

				line = line.trim();

				if (line.length() == 0 || (line.charAt(0) == '#'))
					continue;

				String[] strArray = line.split("\\s");

				String command = strArray[0];

				if (command.equals("Source")) {
					sourceDir = getFile(strArray);
				}

				if (command.equals("Rename")) {

					if (strArray.length != 3) {
						continue;
					} else {
						String[] pair = { strArray[1], strArray[2] };
						nameList.add(pair);
					}
				}

			}

			visitFile(sourceDir, "rename");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	*/
	public void renameFile(File dir) {
		for (String[] pair : nameList) {
			String newFileName = updateFilename(dir.getName(), pair[0], pair[1]);
			dir.renameTo(new File(dir.getParentFile(), newFileName));
		}
	}

	private String updateFilename(String currentFileName, String regex,
			String replacement) {

		String newFileName = currentFileName.replaceAll(regex, replacement);

		if (!currentFileName.equals(newFileName)) {

			String format = "Renamed %1$-40s ---> %2$-1s\n";

			System.out.format(format, currentFileName, newFileName);

		}
		return newFileName;
	}

	public void replaceText(File file) {

		try {
			FileInputStream fis = new FileInputStream(file);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));

			File tmpfile = File.createTempFile("rename", "tmp", file
					.getParentFile());

			BufferedWriter bw = new BufferedWriter(new FileWriter(tmpfile));

			String line;
			while ((line = br.readLine()) != null) {

				for (String[] pair : nameList) {
					line = line.replaceAll(pair[0], pair[1]);
				}

				bw.write(line);
				bw.newLine();
			}

			bw.close();
			br.close();

			String fileName = file.getName();
			file.delete();
			tmpfile.renameTo(new File(tmpfile.getParentFile(), fileName));

		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void visitFile(File file, String mode) {

		if (file.isDirectory()
				&& (file.getName().equals("CVS") || file.getName().equals(
						"build"))) {
			mode = "delete";
		}

		if (file.isDirectory()) {
			String[] children = file.list();
			for (int i = 0; i < children.length; i++) {
				visitFile(new File(file, children[i]), mode);
			}
		}

		if (mode.equals("rename")) {

			if (file.isFile()) {
				
				String[] a = file.getName().split("\\.");
				
				if ((a.length == 1) || txtType.contains(a[a.length-1])) {
					replaceText(file);
				}
			}
			renameFile(file);
		}

		if (mode.equals("delete")) {
			file.delete();
			System.out.println("Deleted file: " + file.getPath());
		}
	}

	public File getFile(String[] array) {
		// merge remaining array elements and restoring spaces.
		String path = "";
		for (int i = 1; i < array.length; i++) {
			path += array[i] + " ";
		}
		return new File(path.trim());
	}
}
