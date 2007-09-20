package org.lams.toolbuilder.renameTool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.ArrayList;
import org.lams.toolbuilder.util.LamsToolBuilderLog;

/**
 * 
 * @author Luke Foxton - Based on code written by: Anthony Sukar
 * 
 */
public class RenameTool {

	//$$$$$$$$$$$$$$$$$$$$$$TO BE IMPLEMENTED DYNAMICALLY
	private final String DEFAULT_LANGUAGE = "en_AU";
	
	private String sourceDirStr;
	
	private Set<String> txtType = new HashSet<String>();

	private List<String[]> nameList;
	
	private String regexIgnorePrefix;
	private String regexIgnoreSuffix;
	
	private static final int MAX_REPLACEMENTS = 20;

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
	
	public boolean renameTool(List<String[]> nameList, String source) throws Exception
	{
		this.nameList = nameList;
		
		this.sourceDirStr = source;
		
		File sourceDir = new File(sourceDirStr);
		if (!sourceDir.exists())
		{		
			LamsToolBuilderLog.logError(new FileNotFoundException("Source file: " + sourceDirStr + "not found."));
			throw new FileNotFoundException("Source file: " + sourceDirStr + "not found.");
		}

		visitFile(sourceDir, "rename");
		return true;
	}
	

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
				regexIgnorePrefix = "";
				regexIgnoreSuffix = "";
				String regex;
				ArrayList<String> replacedStrings = new ArrayList<String>();
				
				
				for (String[] pair : nameList) 
				{
					
					regex = constructRegex(pair[0], pair[1], replacedStrings);
					System.out.println("REGEX: " + regex);
					/*
					 * First check that the string to be replaced is not a substring of the replacement string
					 * Ie if we want to replace forum with testforum, and the searcher finds testforum,
					 * we dont want to end up with testtestforum.
					 */
					/*
					if (pair[1].indexOf(pair[0])>0 || pair[1].startsWith(pair[0]))
					{
						// The string to be replaced is a substring of the replacement string
						System.out.println("SUB STRING: " + pair[1] + " " + pair[0] );
						Pattern p = Pattern.compile(pair[0]);
						String startAndEnd[]= p.split(pair[1]);
						System.out.println("SUB STRING startAndEnd: "+startAndEnd[0] +" " +startAndEnd[startAndEnd.length -1]);
						
						
						if (pair[1].startsWith(startAndEnd[0])&&startAndEnd[0].length()>0)
						{
							regexIgnorePrefix += addRegexIgnoreConstruct(startAndEnd[0], true);
							System.out.println("Prefix regex changed: " + regexIgnorePrefix);
						}
							
						if (pair[1].endsWith(startAndEnd[startAndEnd.length -1]) && startAndEnd[startAndEnd.length -1].length()>0)
						{
							regexIgnoreSuffix += addRegexIgnoreConstruct(startAndEnd[startAndEnd.length -1], false);
							System.out.println("Suffix regex changed: " + regexIgnoreSuffix);
						}
					}
					
					/* TODO:
					 * Next check all strings that have been replaced and dont replace them again
					 */
					/*
					for (String replaceString: replaceStrings)
					{
						if (pair[1].indexOf(replaceString)>0)
						{
							System.out.println("REPLACE STRING: "+pair[1] + " " + replaceString);
							Pattern p = Pattern.compile(pair[1]);
							String startAndEnd[]= p.split(replaceString);
							System.out.println("REPLACE startAndEnd: "+startAndEnd[0] +" " +startAndEnd[startAndEnd.length -1]);
							
							if (pair[1].startsWith(startAndEnd[0]))
								regexIgnorePrefix += addRegexIgnoreConstruct(startAndEnd[0], true);
							
							if (pair[1].endsWith(startAndEnd[startAndEnd.length -1]))
								regexIgnoreSuffix += addRegexIgnoreConstruct(startAndEnd[startAndEnd.length -1], false);
						}
					}
					
					regex = "(" +regexIgnorePrefix+ ")" +pair[0]+ "(" +regexIgnoreSuffix+ ")";
					*/
					
					
					
					
					
					String temp = line;
					line = line.replaceAll(regex, pair[1]);
					
					if (!temp.equals(line))
						System.out.println("REGEX: " +regex+ " TEXT REPLACEMENT: " + temp + " replaced with: " + line);
					
					replacedStrings.add(pair[1]);
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

	public void visitFile(File file, String mode) 
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
			if (file.getName().contains(this.DEFAULT_LANGUAGE))
			{
				return;
			}
			else
			{
				file.delete();
				System.out.println("Deleted non-default language file: " + file.getPath());
			}	
		}
		
		if (file.isDirectory()) {
			String[] children = file.list();
			for (int i = 0; i < children.length; i++) {
				visitFile(new File(file, children[i]), mode);
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
	 * This function constructs a safe regular expression for search and replace
	 * @param startRegex the starting regex that needs to be validated
	 * @param rename the string that will be replacing the strings found by regex
	 * @param replacedStrings a list of strings that have already been used for replacement
	 * @return
	 */
	public String constructRegex(String startRegex, String rename, ArrayList<String> replacedStrings)
	{
		//System.out.println("HELLO: " + startRegex + " " + rename);
		if (rename.contains(startRegex))
		{
			// The string to be replaced is a substring of the replacement string
			//System.out.println("SUB STRING: " + rename + " " + startRegex );
			Pattern p = Pattern.compile(startRegex);
			String startAndEnd[]= p.split(rename);
			//System.out.println("SUB STRING startAndEnd: "+startAndEnd[0] +" " +startAndEnd[startAndEnd.length -1]);
			
			
			if (startAndEnd.length>0)
			{
				if (startAndEnd[0].length()>0 && rename.startsWith(startAndEnd[0]))
				{
					String prefix = addRegexIgnoreConstruct(startAndEnd[0], true);
					if (!regexIgnoreSuffix.contains(prefix))
					{
						regexIgnorePrefix += prefix;
						//System.out.println("Prefix regex changed: " + regexIgnorePrefix);
					}
				}
					
				if (startAndEnd[startAndEnd.length -1].length()>0 && rename.endsWith(startAndEnd[startAndEnd.length -1]))
				{
					String suffix = addRegexIgnoreConstruct(startAndEnd[startAndEnd.length -1], false);
					if (!regexIgnoreSuffix.contains(suffix))
					{
						regexIgnoreSuffix += suffix;
						//System.out.println("Suffix regex changed: " + regexIgnoreSuffix);
					}
				}
			}
		}
		
		/* TODO: add regex prefix and suffix into separate methods to reduce code size
		 * Next check all strings that have been replaced and dont replace them again
		 */
		for (String replacedString: replacedStrings)
		{
			System.out.println("REPLACE STRING: "+startRegex + " " + replacedString);
			if (startRegex.contains(replacedString))
			{
				System.out.println("REPLACE STRING TRUE: "+startRegex + " " + replacedString);
				Pattern p = Pattern.compile(startRegex);
				String startAndEnd[]= p.split(replacedString);
			
				
				
				if (startAndEnd.length>0)
				{
					if (startAndEnd[0].length()>0 && startRegex.startsWith(startAndEnd[0]))
					{
						String prefix = addRegexIgnoreConstruct(startAndEnd[0], true);
						if (!regexIgnoreSuffix.contains(prefix))
						{
							regexIgnorePrefix += prefix;
							System.out.println("Prefix regex changed: " + regexIgnorePrefix);
						}
					}
						
					
					if (startAndEnd[startAndEnd.length -1].length()>0 && startRegex.endsWith(startAndEnd[startAndEnd.length -1]))
					{
						String suffix = addRegexIgnoreConstruct(startAndEnd[startAndEnd.length -1], false);
						if (!regexIgnoreSuffix.contains(suffix))
						{
							regexIgnoreSuffix += suffix;
							System.out.println("Suffix regex changed: " + regexIgnoreSuffix);
						}
					}
				}
			}
		}
		
		return "(" +regexIgnorePrefix+ ")" +startRegex+ "(" +regexIgnoreSuffix+ ")";
	}
}
