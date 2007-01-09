/****************************************************************
 * Copyright (C) 2006 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0 
 * as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.build;

import static java.lang.System.out;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * <p>
 * This class is used to generate a list of the 3rd party libraries distributed
 * with LAMS 2.x. The list is published on the LAMS wiki page: <a
 * href="http://wiki.lamsfoundation.org/display/lams/Common+Java+Libraries">Common
 * Java Libraries</a>. The list generated will be persisted in the file
 * liblist.txt. Whenever new libraries are added or old libraries are updated or
 * deprecated libraries are removed, this program can be used to regenerate the
 * list so that you can copy it and update the wiki page easily.
 * </p>
 * <p>
 * <a href="ThirdPartyLibraryListGenerator.java.html"><i>View Source</i><a>
 * </p>
 * 
 * TODO add merge function 
 * 
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public class ThirdPartyLibraryListGenerator {

	static final String DEPLOYED_LIBS = "Deployed Libraries";

	static final String BUILD_RELATED_LIBS = "Build Related Libraries";

	static final String FOLDER = "Folder";

	static final String LIBRARY = "Library";

	static final String VERSION = "Version";

	static final String LICENSE = "License";

	static final String VENDOR = "Vendor";

	static final String DESC = "Description";

	static final String NEWLINE = "\n";

	static final String BUILD_RELATED = "build_related";

	static List<String> buildRelated = new LinkedList<String>();

	static List<JarFolder> deployedJarFolders = new LinkedList<JarFolder>();

	static List<JarFolder> buildRelatedJarFolders = new LinkedList<JarFolder>();

	static List<JarFolder> existingDeployedJarFolders = new LinkedList<JarFolder>();

	static List<JarFolder> existingBuildRelatedJarFolders = new LinkedList<JarFolder>();

	static final int FLD = 0;

	static final int LIB = 1;

	static final int VER = 2;

	static final int LIC = 3;

	static final int VDR = 4;

	static Properties loadProperties(String propertyFileName) {
		try {
			InputStream propFile = new FileInputStream(propertyFileName);
			Properties props = new Properties();
			props.load(propFile);
			return props;
		} catch (FileNotFoundException e) {
			String errorMsg = "File:" + propertyFileName + " was not found";
			throw new RuntimeException(errorMsg, e);
		} catch (IOException e) {
			String errorMsg = "IO error occured during loading file "
					+ propertyFileName;
			throw new RuntimeException(errorMsg, e);
		}
	}

	static void config() {
		Properties props = loadProperties("liblist.conf");
		String[] brs = props.getProperty(BUILD_RELATED).split(",");
		for (String br : brs) {
			buildRelated.add(br);
		}
	}

	// TODO Finish me
	static void init() throws IOException {
		File file = new File("liblist.txt");
		if (file.exists()) {
/*			final Integer NOT_STARTED = 0;
			final Integer DEPLOYED_STARTING = 1;
			final Integer DEPLOYED_STARTED = 2;
			final Integer BUILD_STARTING = 3;
			final Integer BUILD_STARTED = 4;
*/			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
/*			Integer status = NOT_STARTED;
*/			while ((line = br.readLine()) != null) {
				if (line.startsWith(DEPLOYED_LIBS)) {
					continue;
				}
				if (line.startsWith(FOLDER)) {

				}
			}
		}
	}

	static void scan() throws IOException {
		File lib = new File("lib");
		File[] folders = lib.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return !name.equals("CVS")
						&& !name.equals("lams")
						&& new File("lib" + File.separatorChar + name)
								.isDirectory();
			}
		});
		for (File folder : folders) {
			if (buildRelated.contains(folder.getName())) {
				buildRelatedJarFolders.add(JarFolder.buildJarFolder(folder));
			} else {
				deployedJarFolders.add(JarFolder.buildJarFolder(folder));
			}
		}
	}

	static void output() throws IOException {
		StringBuilder sb = new StringBuilder();
		int[] width = getWidthOfColumns(deployedJarFolders);
		outputHeader(sb, width, DEPLOYED_LIBS);
		for (JarFolder folder : deployedJarFolders) {
			outputJarFolder(sb, width, folder);
		}
		sb.append(NEWLINE).append(NEWLINE);
		width = getWidthOfColumns(buildRelatedJarFolders);
		outputHeader(sb, width, BUILD_RELATED_LIBS);
		for (JarFolder folder : buildRelatedJarFolders) {
			outputJarFolder(sb, width, folder);
		}
		writeFile(sb.toString());
	}

	static void writeFile(String content) throws IOException {
		BufferedWriter writer = new BufferedWriter(
				new FileWriter("liblist.txt"));
		writer.write(content);
		writer.close();
	}

	private static void outputJarFolder(StringBuilder sb, int[] width,
			JarFolder folder) {
		append(sb, folder.name, width[FLD]);
		if (folder.jars.size() > 0) {
			Jar jar = folder.jars.get(0);
			append(sb, jar.name, width[LIB]);
			append(sb, jar.version, width[VER]);
			append(sb, jar.license, width[LIC]);
			append(sb, jar.vendor, width[VDR]);
			append(sb, jar.description, 0);
			sb.append(NEWLINE);
			for (int i = 1; i < folder.jars.size(); i++) {
				jar = folder.jars.get(i);
				append(sb, "", width[FLD]);
				append(sb, jar.name, width[LIB]);
				append(sb, jar.version, width[VER]);
				append(sb, jar.license, width[LIC]);
				append(sb, jar.vendor, width[VDR]);
				append(sb, jar.description, 0);
				sb.append(NEWLINE);
			}
		} else {
			sb.append(NEWLINE);
		}
		sb.append(NEWLINE);
	}

	static void outputHeader(StringBuilder sb, int[] width, String header) {
		sb.append(header).append(NEWLINE).append(NEWLINE);
		append(sb, FOLDER, width[FLD]);
		append(sb, LIBRARY, width[LIB]);
		append(sb, VERSION, width[VER]);
		append(sb, LICENSE, width[LIC]);
		append(sb, VENDOR, width[VDR]);
		append(sb, DESC, 0);
		sb.append(NEWLINE);
		sb.append(NEWLINE);
	}

	static void append(StringBuilder sb, String s, int width) {
		if(s==null) s="";
		sb.append(s);
		for (int i = 0; i < width - s.length(); i++) {
			sb.append(' ');
		}
	}

	static int[] getWidthOfColumns(List<JarFolder> jarFolders) {
		int[] width = new int[] { FOLDER.length(), LIBRARY.length(),
				VERSION.length(), LICENSE.length(), VENDOR.length() };
		for (JarFolder folder : jarFolders) {
			if (folder.name.length() > width[FLD]) {
				width[FLD] = folder.name.length();
			}
			for (Jar jar : folder.jars) {
				if (jar.name.length() > width[LIB]) {
					width[LIB] = jar.name.length();
				}
				if (jar.version!=null && jar.version.length() > width[VER]) {
					width[VER] = jar.version.length();
				}
				if (jar.license!=null && jar.license.length() > width[LIC]) {
					width[LIC] = jar.license.length();
				}
				if (jar.vendor!=null && jar.vendor.length() > width[VDR]) {
					width[VDR] = jar.vendor.length();
				}
			}
		}
		for (int i = 0; i < 5; i++) {
			width[i] += 3;// extra 3 spaces between columns
		}
		return width;
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		config();
		//init();
		scan();
		output();
		out.println("Done!");
	}

}
