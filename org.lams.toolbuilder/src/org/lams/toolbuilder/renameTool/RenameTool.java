package org.lams.toolbuilder.renameTool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.Properties;
import org.eclipse.core.runtime.IProgressMonitor;

import org.lams.toolbuilder.util.LamsToolBuilderLog;

/**
 * 
 * @author Luke Foxton - Modelled on code written by: Anthony Sukar
 * 
 */
public class RenameTool {

	private final String DEFAULT_LANGUAGE = "en_AU";
	
	private String sourceDirStr;
	
	private Set<String> txtType = new HashSet<String>();

	private List<String[]> nameList;
	
	private String vendor; // needs to be implicitly defined to replace the directory file

	public RenameTool()
	{
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
	
	public boolean renameTool(List<String[]> nameList, String source, String vendor, IProgressMonitor monitor) throws Exception
	{
		this.nameList = nameList;
		this.vendor = vendor.replaceAll(" " , "").toLowerCase();
		this.sourceDirStr = source;
		
		File sourceDir = new File(sourceDirStr);
		if (!sourceDir.exists())
		{		
			LamsToolBuilderLog.logError(new FileNotFoundException("Source file: " + sourceDirStr + "not found."));
			throw new FileNotFoundException("Source file: " + sourceDirStr + "not found.");
		}

		visitFile(sourceDir, "rename", monitor);
		return true;
	}
	

	public void renameFile(File dir) {
		String regex;
		for (String[] task : nameList) {
			regex = constructRegex(task[0], task[1], task[2], task[3]);
			String newFileName = updateFilename(dir.getName(), regex, task[3]);
			dir.renameTo(new File(dir.getParentFile(), newFileName));
		}
	}

	private String updateFilename(String currentFileName, String regex,
			String replacement) {

		String newFileName = currentFileName.replaceAll(regex, replacement);
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
				String regex;
				ArrayList<String> replacedStrings = new ArrayList<String>();
				
				
				for (String[] task : nameList) 
				{
					
					//regex = constructRegex(task[0], task[1], replacedStrings);
					//System.out.println("REGEX: " + regex);

					//System.out.println("AARGH: " + task[0] + "," + task[1] + "," + task[2] + "," + task[3]);
					regex = constructRegex(task[0], task[1], task[2], task[3]);
					
					
					String temp = line;
					line = line.replaceAll(regex, task[3]);
					
					if (!temp.equals(line))
						//System.out.println("REGEX: " +regex+ " TEXT REPLACEMENT: " + temp + " replaced with: " + line);
					
					replacedStrings.add(task[1]);
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

	public void visitFile(File file, String mode, IProgressMonitor monitor) 
	{
		boolean rename=true;
		
		if (file.isDirectory()
				&& (file.getName().equals("CVS") || file.getName().equals(
						"build"))) {
			mode = "delete";
		}
		
		// Do not rename the root directory of the tool
		if (file.isDirectory() && file.getName().equals(sourceDirStr.substring(sourceDirStr.lastIndexOf("/")+1)))
		{
			rename = false;
		}

		if (file.getName().contains("ApplicationResources"))
		{
			// it is a language file, delete if not the default language
			// if it is the default language, do not alter it
			if (file.getName().contains(this.DEFAULT_LANGUAGE) || file.getName().equals("ApplicationResources.properties"))
			{
				return;
			}
			else
			{
				file.delete();
				//System.out.println("Deleted non-default language file: " + file.getPath());
			}	
		}
		
		if (file.isDirectory()) {
			String[] children = file.list();
			for (int i = 0; i < children.length; i++) {
				visitFile(new File(file, children[i]), mode, monitor);
			}
		}

		if (mode.equals("rename") && rename==true) 
		{

			if (file.isFile()) {
				
				String[] a = file.getName().split("\\.");
				
				if ((a.length == 1) || txtType.contains(a[a.length-1])) {
					replaceText(file);
				}
			}
			renameFile(file);
		}
		
		/**
		 * This rename must be done after the recursion to prevent 
		 * an unexpected break in the recursion
		 */
		if (file.isDirectory() && file.getName().equals("lamsfoundation"))
		{
			// we need to implicitly replace this with the new vendor
			String newFileName = updateFilename(file.getName(), "lamsfoundation", vendor);
			file.renameTo(new File(file.getParentFile(), newFileName));
		}
		
		if (mode.equals("delete")) {
			file.delete();
			//System.out.println("Deleted file: " + file.getPath());
		}
		monitor.worked(1);
	}

	public File getFile(String[] array) {
		// merge remaining array elements and restoring spaces.
		String path = "";
		for (int i = 1; i < array.length; i++) {
			path += array[i] + " ";
		}
		return new File(path.trim());
	}
	
	/**
	 * 
	 * @param ignore the string you want to ignore
	 * @param isPrefix true if this is a prefix, false if it is a suffix
	 * @return the prefix/suffix regex to ignore this string
	 */
	public String addRegexIgnoreConstruct(String ignore, boolean isPrefix )
	{
		String operator;
		if(isPrefix)
		{
			operator = "?<!";
		}
		else
		{
			operator = "?!";
		}
		
		return "("+operator+ignore+")";
	}
	
	/**
	 * This method returns adds an ignore prefix or suffix to an existing  regex construct
	 * @param currRegex the current regex
	 * @param ignoreRegex the ignore string you wish to add
	 * @param isPrefix true if it is a prefix false if it is a suffix
	 * @return the regex with the ignore contruct added to it
	 */
	public String constructRegexPrefixOrSuffix(String currRegex, String ignoreRegex, boolean isPrefix)
	{
		String ret = addRegexIgnoreConstruct(ignoreRegex, isPrefix);
		

		if (currRegex.length()==0) // if it is an empty regex, return a new ignore regex
		{
			if (ignoreRegex.length()==0) //if there is nothing to add, return nothing
			{
				return "";
			}
			else
			{
				return ret;
			}	
		}
		else  // add an ignore construct to the existing regex
		{
			if(ignoreRegex.length()==0) //return the current regex if theres nothing to add
			{
				return currRegex;
			}
			else
			{
				return "("+ ret + currRegex + ")";
			}
		}
		
	}
	
	
	/**
	 * This function constructs a safe regular expression for search and replace
	 * @param startRegex the starting regex that needs to be validated
	 * @param rename the string that will be replacing the strings found by regex
	 * @param replacedStrings a list of strings that have already been used for replacement
	 * @return
	 */
	public String constructRegex(String regexPrefix, String regexSuffix, String startRegex, String rename)
	{
		//System.out.println("HELLO: " + startRegex + " " + rename);
		if (rename.contains(startRegex))
		{
			// The string to be replaced is a substring of the replacement string
			//System.out.println("SUB STRING: " + rename + " " + startRegex );
			Pattern p = Pattern.compile(startRegex);
			String startAndEnd[]= p.split(rename);
			//System.out.println("SUB STRING startAndEnd: "+startAndEnd[0] +" " +startAndEnd[startAndEnd.length -1]);
			
			//System.out.println("HELLO: " + regexPrefix + "," + regexSuffix + "," + startRegex + "," + rename);
			if (startAndEnd.length>0)
			{
				if (startAndEnd[0].length()>0 && rename.startsWith(startAndEnd[0]))
				{
					regexPrefix = constructRegexPrefixOrSuffix(regexPrefix, startAndEnd[0], true);
					//System.out.println("Prefix regex changed: " + regexPrefix);

				}
					
				if (startAndEnd[startAndEnd.length -1].length()>0 && rename.endsWith(startAndEnd[startAndEnd.length -1]))
				{

					regexSuffix = constructRegexPrefixOrSuffix(regexSuffix, startAndEnd[0], true);;
					//System.out.println("Suffix regex changed: " + regexSuffix);

				}
			}
		}
		return regexPrefix + startRegex + regexSuffix;

	}
	
	public void updateLanguageFile(String filePath, String rename)
	{
		
		File language = new File(filePath);
		
		nameList.clear();
		nameList.add(new String[] {"", "", "activity\\.title.+", "activity.title  =" + rename});
		
		this.replaceText(language);
	}
	
	
	public void renameProperties(String serverVersion, String hideTool, String toolVersion, IProgressMonitor monitor)
	{
		File properties = new File(sourceDirStr+ "/build.properties");
		
		nameList.clear();
		nameList.add(new String[] {"", "", "hideTool=.+", "hideTool=" + hideTool});
		nameList.add(new String[] {"", "", "min.server.version.number=.+", "min.server.version.number=" + serverVersion});
		nameList.add(new String[] {"", "", "tool.version=.+", "tool.version=" + toolVersion});
		
		this.replaceText(properties);
		
	}
	

}
