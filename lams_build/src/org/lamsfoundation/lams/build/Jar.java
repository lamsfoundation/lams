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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;

/**
 * <p>
 * <a href="Jar.java.html"><i>View Source</i><a>
 * </p>
 * 
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public class Jar implements Comparable {

	static final String[] VERSION_ATT_NAMES = { "Implementation-Version", "HttpUnit-Version",
			"Hibernate-Version" };

	static final String[] VENDOR_ATT_NAMES = { "Implementation-Vendor" };

	static final String[] LICENSE_FILE_NAMES = { "LICENSE", "LICENSE.txt", "License",
			"License.txt", "license.txt" };

	static final String[] DESC_ATT_NAMES = { "Specification-Title", "Implementation-Title" };

	static final String[] LICENSES = { "Apache License", "Apache Software License",
			"Joda Software License", "GPL", "LGPL", "GNU General Public License",
			"GNU Lesser General Public License", "Open Software License", "Academic Free License" };

	String name;

	String version;

	String license;

	String vendor;

	String description;

	public int compareTo(Object o) {
		return name.compareTo(((Jar) o).name);
	}

	static Jar buildJar(File file) throws IOException {
		Jar jar = new Jar();
		jar.name = file.getName();
		JarFile jarFile = new JarFile(file);
		Manifest manifest = jarFile.getManifest();
		Attributes mainAttributes = manifest.getMainAttributes();
		for (String versionAtt : VERSION_ATT_NAMES) {
			jar.version = mainAttributes.getValue(versionAtt);
			if (jar.version != null) {
				int index = jar.version.indexOf(' ');
				if (index != -1)
					jar.version = jar.version.substring(0, index).trim();
				break;
			}
		}
		if (jar.version == null) {// try parsing file name
			int index1 = jar.name.lastIndexOf('-');
			if (index1 != -1) {
				int index2 = jar.name.indexOf(".jar");
				jar.version = jar.name.substring(index1 + 1, index2);
				int index = index1;
				while (jar.version.indexOf('.') == -1) {
					String s = jar.name.substring(0, index);
					index = s.lastIndexOf('-');
					if (index == -1)
						break;
					else {
						jar.version = jar.name.substring(index + 1, index2);
					}
				}
				if (jar.version.indexOf('.') == -1) {
					jar.version = null;
				}
			}
		}
		for (String vendorAtt : VENDOR_ATT_NAMES) {
			jar.vendor = mainAttributes.getValue(vendorAtt);
			if (jar.vendor != null)
				break;
		}
		for (String descAtt : DESC_ATT_NAMES) {
			jar.description = mainAttributes.getValue(descAtt);
			if (jar.description != null)
				break;
		}
		ZipEntry licenseZipEntry = null;
		for (String licenseFilename : LICENSE_FILE_NAMES) {
			licenseZipEntry = jarFile.getEntry("META-INF/" + licenseFilename);
			if (licenseZipEntry == null) {
				licenseZipEntry = jarFile.getEntry(licenseFilename);
			}
			if (licenseZipEntry != null && !licenseZipEntry.isDirectory())
				break;
		}
		if (licenseZipEntry != null && !licenseZipEntry.isDirectory()) {
			BufferedReader br = new BufferedReader(new InputStreamReader(jarFile
					.getInputStream(licenseZipEntry)));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			String licenseTxt = sb.toString();
			jar.license = parse(licenseTxt);
			br.close();
		}
		if (jar.version != null)
			jar.version = cleanQuotes(jar.version);
		if (jar.description != null)
			jar.description = cleanQuotes(jar.description);
		if (jar.vendor != null)
			jar.vendor = cleanQuotes(jar.vendor);
		return jar;
	}

	static String parse(String licenseTxt) {
		String licenseName = "";
		int licenseIndex = -1;
		for (String license : LICENSES) {
			licenseIndex = licenseTxt.indexOf(license);
			if (licenseIndex != -1) {
				licenseName = license;
				break;
			}
		}
		int versionIndex = licenseTxt.indexOf("Version");
		String version = "";
		if (versionIndex != -1) {
			int i = licenseTxt.indexOf(' ', versionIndex);
			int i1 = licenseTxt.indexOf(',', i + 1);
			int i2 = licenseTxt.indexOf(' ', i + 1);
			version = licenseTxt.substring(i, Math.min(i1, i2)).trim();
		}
		StringBuilder result = new StringBuilder();
		if (licenseName.length() > 0) {
			result.append(licenseName);
			if (version.length() > 0) {
				result.append(' ').append(version);
			}
		}
		return result.toString();
	}

	static String cleanQuotes(String txt) {
		if (txt.indexOf('"') != -1 && txt.indexOf('"') == 0
				&& txt.lastIndexOf('"') == txt.length() - 1) {
			txt = txt.substring(1, txt.length() - 1);
		}
		return txt;
	}

}
