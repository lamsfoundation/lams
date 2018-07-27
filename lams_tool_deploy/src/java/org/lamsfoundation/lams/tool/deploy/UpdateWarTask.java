/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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


package org.lamsfoundation.lams.tool.deploy;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

/**
 * Base class of ant tasks that changes a jar file or a war file
 * 
 * @author Fiona Malikoff modified by Luke Foxton
 */
public abstract class UpdateWarTask implements Task {

    protected static final String WEBXML_PATH = "WEB-INF/web.xml";
    protected static final String MANIFEST_PATH = "META-INF/MANIFEST.MF";

    protected static final int BUFFER_SIZE = 8192;
    /**
     * The lams.ear path file.
     */
    protected String lamsEarPath;

    protected List<String> archivesToUpdate;

    protected Map<File, File> filesToRename;

    /**
     * The value of the tool application context file, including the path
     * e.g. /org/lamsfoundation/lams/tool/vote/voteApplicationContext.xml
     * It should not include the "classpath:" prefix as this will be added
     * automatically.
     */
    protected String applicationContextPath;

    /**
     * The name of the tool's jar file e.g. "lams-tool-lanb11.jar".
     * This will be added to / removed from the classpath as ./[jarfilename]
     * e.g. "./lams-tool-lanb11.jar"
     */
    protected String jarFileName;

    /**
     * Sets the location of the application xml file to be modified.
     * 
     * @param appxml
     *            New value of property appxml.
     */
    public void setLamsEarPath(final String lamsEarPath) {
	this.lamsEarPath = lamsEarPath;
    }

    public List<String> getArchivesToUpdate() {
	return archivesToUpdate;
    }

    public void setArchivesToUpdate(List<String> warsToUpdate) {
	this.archivesToUpdate = warsToUpdate;
    }

    /**
     * Returns the value of the tool application context file, including the path
     * e.g. /org/lamsfoundation/lams/tool/vote/voteApplicationContext.xml
     * It should not include the "classpath:" prefix as this will be added
     * automatically.
     */
    public String getApplicationContextPath() {
	return applicationContextPath;
    }

    /**
     * Returns the value of the tool application context file with the "classpath:" prepended.
     * e.g. "classpath:/org/lamsfoundation/lams/tool/vote/voteApplicationContext.xml"
     */
    protected String getApplicationContextPathWithClasspath() {
	return "classpath:" + applicationContextPath;
    }

    /**
     * The value of the tool application context file, including the path
     * e.g. /org/lamsfoundation/lams/tool/vote/voteApplicationContext.xml
     * It should not include the "classpath:" prefix as this will be added
     * automatically.
     */
    public void setApplicationContextPath(String applicationContextPath) {
	this.applicationContextPath = applicationContextPath;
    }

    /**
     * Gets the name of the tool's jar file e.g. "lams-tool-lanb11.jar".
     * This will be added to / removed from the classpath as ./[jarfilename]
     * e.g. "./lams-tool-lanb11.jar"
     */
    public String getJarFileName() {
	return jarFileName;
    }

    /**
     * Gets the name of the tool's jar file converted to ./[jarfilename]
     * e.g. "./lams-tool-lanb11.jar"
     */
    protected String getJarFileNameWithDotSlash() {
	return "./" + jarFileName;
    }

    /**
     * Sets the name of the tool's jar file e.g. "lams-tool-lanb11.jar".
     * This will be added to / removed from the classpath as ./[jarfilename]
     * e.g. "./lams-tool-lanb11.jar"
     */
    public void setJarFileName(String jarFileName) {
	this.jarFileName = jarFileName;
    }

    /**
     * Execute the task
     * 
     * @throws org.apache.tools.ant.DeployException
     *             In case of any problems
     */
    @Override
    public void execute() throws DeployException {

	if (applicationContextPath == null) {
	    throw new DeployException(
		    "UpdateWebXmTask: Unable to update web.xml as the application content path is missing (applicationContextPath).");
	}

	if (archivesToUpdate != null) {

	    // map the old File to the new File e.g. lams_learning.war is the key, lams_learning.war.new is the value  
	    filesToRename = new HashMap<File, File>(archivesToUpdate.size());

	    for (String warFileName : archivesToUpdate) {

		File warFile = new File(lamsEarPath + File.separator + warFileName);
		if (!warFile.canRead()) {
		    throw new DeployException("Unable to access war file " + warFile.getAbsolutePath()
			    + ". May be missing or not readable");
		}
		if (warFile.isDirectory()) {
		    updateExpandedWar(warFileName, warFile);
		} else {
		    updateZippedWar(warFileName, warFile);
		}
	    }

	    copyNewFilesToOldNames();
	}
    }

    /**
     * 
     * Update the war file which is assumed to be in expanded format (ie a directory)
     * 
     * @param warFileName
     * @param warFile
     */
    protected void updateExpandedWar(String warFileName, File warFileDirectory) {

	System.out.println("Updating expanded war " + warFileName);

	String webXMLFilename = lamsEarPath + File.separator + warFileName + File.separator + WEBXML_PATH;
	File webXMLFile = new File(webXMLFilename);
	File newWebXMLFile = new File(webXMLFilename + ".new");
	String manifestFilename = lamsEarPath + File.separator + warFileName + File.separator + MANIFEST_PATH;
	File manifestFile = new File(manifestFilename);
	File newManifestFilename = new File(manifestFilename + ".new");

	FileInputStream webXMLFileis = null;
	FileOutputStream webXMLFileos = null;
	FileInputStream manifestFileis = null;
	FileOutputStream manifestFileos = null;

	try {
	    webXMLFileis = new FileInputStream(webXMLFile);
	    webXMLFileos = new FileOutputStream(newWebXMLFile);
	    manifestFileis = new FileInputStream(manifestFile);
	    manifestFileos = new FileOutputStream(newManifestFilename);

	    Document doc = parseWebXml(webXMLFileis);
	    updateWebXml(doc);
	    writeWebXml(doc, webXMLFileos);

	    Manifest manifest = new Manifest(manifestFileis);
	    updateClasspath(manifest);
	    manifest.write(manifestFileos);

	} catch (IOException e) {
	    throw new DeployException("Unable to process war file " + warFileName, e);
	} finally {
	    try {
		if (webXMLFileis != null) {
		    webXMLFileis.close();
		}
		if (webXMLFileos != null) {
		    webXMLFileos.close();
		}
		if (manifestFileis != null) {
		    manifestFileis.close();
		}
		if (manifestFileos != null) {
		    manifestFileos.close();
		}
	    } catch (IOException e2) {
	    }
	}

	filesToRename.put(webXMLFile, newWebXMLFile);
	filesToRename.put(manifestFile, newManifestFilename);
    }

    /**
     * 
     * Update the war file, which is assumed to be in zip file format.
     * 
     * @param warFileName
     * @param warFile
     * @return new zipped war file
     */
    protected void updateZippedWar(String warFileName, File warFile) {

	System.out.println("Updating zipped war " + warFileName);

	String newFilename = lamsEarPath + File.separator + warFileName + ".new";
	File outputFile = new File(newFilename);
	JarOutputStream newWarOutputStream = null;
	ZipInputStream warInputStream = null;
	try {

	    newWarOutputStream = new JarOutputStream(new BufferedOutputStream(new FileOutputStream(outputFile)));
	    newWarOutputStream.setMethod(ZipOutputStream.DEFLATED);
	    newWarOutputStream.setLevel(Deflater.DEFAULT_COMPRESSION);
	    //newWarOutputStream.setMethod(JarOutputStream.STORED);

	    warInputStream = new ZipInputStream(new BufferedInputStream(new FileInputStream(warFile)));
	    ZipEntry entry = null;

	    // work through each entry, copying across to the output stream. 
	    // when we hit the web.xml, we have to modify it.
	    while ((entry = warInputStream.getNextEntry()) != null) {
		if (WEBXML_PATH.equals(entry.getName())) {
		    updateWebXML(newWarOutputStream, warInputStream, entry);
		} else if (MANIFEST_PATH.equals(entry.getName())) {
		    updateManifest(newWarOutputStream, warInputStream, entry);
		} else {
		    copyEntryToWar(newWarOutputStream, warInputStream, entry);
		}
	    }

	    warInputStream.close();
	    newWarOutputStream.close();

	} catch (IOException e) {
	    throw new DeployException("Unable to process war file " + warFileName, e);
	} finally {
	    try {
		if (warInputStream != null) {
		    warInputStream.close();
		}
		if (newWarOutputStream != null) {
		    newWarOutputStream.close();
		}
	    } catch (IOException e2) {
	    }
	}

	filesToRename.put(warFile, outputFile);
    }

    /* ********* Manage the archive updates, copy files, etc *********/

    /** This entry in the JAR/WAR file doesn't change so copy it from the old archive to the new archive */
    protected void copyEntryToWar(JarOutputStream newWarOutputStream, ZipInputStream warInputStream, ZipEntry entry)
	    throws IOException {

	ZipEntry newEntry = new ZipEntry(entry.getName());
	newWarOutputStream.putNextEntry(newEntry);

	byte[] data = new byte[BUFFER_SIZE];
	int count = -1;
	while ((count = warInputStream.read(data, 0, BUFFER_SIZE)) != -1) {
	    newWarOutputStream.write(data, 0, count);
	}

	newWarOutputStream.closeEntry();
    }

    /**
     * Everything worked okay so rename the files
     * 
     * @param filesToRename
     */
    protected void copyNewFilesToOldNames() throws DeployException {
	// clean up any old backup files first.
	for (Map.Entry<File, File> mapEntry : filesToRename.entrySet()) {
	    File origFile = mapEntry.getKey();
	    File backup = new File(origFile.getAbsoluteFile() + ".bak");
	    if (backup.exists()) {
		backup.delete();
	    }
	    if (backup.exists()) {
		throw new DeployException("Error occured removing an old backup file. Please remove the file "
			+ backup.getAbsolutePath() + " and run the installation again.");
	    }
	}

	// copy blah.war to blah.war.bak, and copy blah.war.new to blah.war
	for (Map.Entry<File, File> mapEntry : filesToRename.entrySet()) {
	    Map<File, File> renamed = new HashMap<File, File>(archivesToUpdate.size());

	    File origFile = mapEntry.getKey();
	    File backup = new File(origFile.getAbsoluteFile() + ".bak");

	    boolean successful = origFile.renameTo(backup);
	    if (successful) {
		renamed.put(origFile, backup);
		successful = mapEntry.getValue().renameTo(origFile);
	    }

	    if (successful) {
		System.out.println("Updated file " + origFile.getName());

	    } else {
		// something has gone wrong so restore the backup files
		for (Map.Entry<File, File> renamedMapEntry : renamed.entrySet()) {
		    File updatedFile = renamedMapEntry.getKey();
		    File backupFile = renamedMapEntry.getValue();
		    backupFile.renameTo(updatedFile);
		}
		String message = "Error occured renaming the war/web.xml/manifest files. Tried to go back to old files but may or may not have succeeded. Check files:";
		for (String warFileName : archivesToUpdate) {
		    message += " " + warFileName;
		}
		throw new DeployException(message);
	    }

	}
    }

    protected ByteArrayInputStream copyToByteArrayInputStream(ZipInputStream warInputStream) throws IOException {

	ByteArrayOutputStream os = new ByteArrayOutputStream();
	byte[] data = new byte[BUFFER_SIZE];
	int count = -1;
	while ((count = warInputStream.read(data, 0, BUFFER_SIZE)) != -1) {
	    os.write(data, 0, count);
	}
	os.close();

	return new ByteArrayInputStream(os.toByteArray());
    }

    /* ********* Update the web.xml etc *********/

    /** Update the param-value entry in the contextConfigLocation entry in the web.xml file */
    protected abstract void updateParamValue(Document doc, Node contextParamElement) throws DeployException;

    /**
     * Given the web.xml from the war file, update the file and write it to the new war file.
     * 
     * @param newWarOutputStream
     *            new war file
     * @param warInputStream
     *            existing war file
     * @param entry
     *            web.xml entry
     * @throws IOException
     */
    protected void updateWebXML(JarOutputStream newWarOutputStream, ZipInputStream warInputStream, ZipEntry entry)
	    throws IOException {

	ZipEntry newEntry = new ZipEntry(WEBXML_PATH);
	newWarOutputStream.putNextEntry(newEntry);

	// can't just pass the stream to the parser, as the parser will close the stream.
	InputStream copyInputStream = copyToByteArrayInputStream(warInputStream);

	Document doc = parseWebXml(copyInputStream);
	updateWebXml(doc);
	writeWebXml(doc, newWarOutputStream);

	newWarOutputStream.closeEntry();
    }

    protected void updateWebXml(Document doc) throws DeployException {

	NodeList contextParamNodeList = doc.getElementsByTagName("context-param");
	Element matchingContextParamElement = findContextParamWithMatchingParamName("contextConfigLocation",
		contextParamNodeList);
	if (matchingContextParamElement == null) {
	    throw new DeployException("No contextConfigLocation can be found in the web.xml in the war");
	}

	NodeList contextParamElements = matchingContextParamElement.getChildNodes();
	for (int c = 0; c < contextParamElements.getLength(); c++) {
	    Node contextParamElement = contextParamElements.item(c);
	    if (contextParamElement instanceof Element) {
		if ("param-value".equals(contextParamElement.getNodeName())) {
		    updateParamValue(doc, contextParamElement);
		}
	    }
	}

    }

    /**
     * Writes the modified web.xml back out to war file
     * 
     * @param doc
     *            The application.xml DOM Document
     * @throws org.apache.tools.ant.DeployException
     *             in case of any problems
     */
    protected void writeWebXml(final Document doc, final OutputStream outputStream) throws DeployException {
	try {
	    doc.normalize();

	    // Prepare the DOM document for writing
	    DOMSource source = new DOMSource(doc);

	    // Prepare the output file
	    StreamResult result = new StreamResult(outputStream);

	    // Write the DOM document to the file
	    // Get Transformer
	    Transformer xformer = TransformerFactory.newInstance().newTransformer();
	    // Write to a file
	    xformer.transform(source, result);
	} catch (TransformerException tex) {
	    throw new DeployException("Error writing out modified web xml ", tex);
	}
    }

    /**
     * Parses the web xml into a Dom document
     * 
     * @throws org.apache.tools.ant.DeployException
     *             in case of errors
     * @return A DOM Document of the web xml
     */
    protected Document parseWebXml(final InputStream webXmlPath) throws DeployException {
	try {
	    //get application xml as dom doc
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    return builder.parse(webXmlPath);
	} catch (ParserConfigurationException pex) {
	    throw new DeployException("Could not configure parser", pex);
	} catch (SAXException saxex) {
	    throw new DeployException("Error parsing web xml" + webXmlPath, saxex);
	} catch (IOException ioex) {
	    throw new DeployException("Error reading web xml" + webXmlPath, ioex);
	}
    }

    /**
     * Finds the node element with the param-name matching the given param-name.
     * 
     * @param searchParamName
     *            param-name to match
     * @param nodeList
     *            the nodeList to look in
     * @throws org.apache.tools.ant.DeployException
     *             in case of errors
     * @return the matching Element
     */
    protected Element findContextParamWithMatchingParamName(String searchParamName, NodeList nodeList)
	    throws DeployException {
	/*
	 * Looking for node like:
	 * <context-param>
	 * <param-name>contextConfigLocation</param-name>
	 * <param-value>
	 * classpath:/org/lamsfoundation/lams/applicationContext.xml
	 * </param-value>
	 * </context-param>
	 */
	for (int i = 0, length = nodeList.getLength(); i < length; i++) {
	    Node node = nodeList.item(i);
	    if (node instanceof Element) {

		NodeList children = node.getChildNodes();
		String paramName = null;
		for (int c = 0; c < children.getLength(); c++) {

		    Node childNode = children.item(c);
		    if (childNode instanceof Element) {
			if ("param-name".equals(childNode.getNodeName())) {

			    NodeList nameChildren = childNode.getChildNodes();
			    if ((nameChildren != null) && (nameChildren.getLength() > 0)
				    && nameChildren.item(0) instanceof Text) {
				paramName = nameChildren.item(0).getNodeValue();
				if (paramName != null && paramName.equals(searchParamName)) {
				    return (Element) node;
				}
			    }
			}
		    }
		}

	    }

	}

	return null;
    }

    /* ********* Update the MANIFEST.MF *********/

    /** Update the classpath entry in the MANIFEST.MF file */
    protected abstract void updateClasspath(Manifest manifest) throws DeployException;

    /**
     * Given the MANIFEST.MF from the war file, update the file and write it to the new war file.
     * 
     * @param newWarOutputStream
     *            new war file
     * @param warInputStream
     *            existing war file
     * @param entry
     *            web.xml entry
     * @throws IOException
     */
    protected void updateManifest(JarOutputStream newWarOutputStream, ZipInputStream warInputStream, ZipEntry entry)
	    throws IOException {

	ZipEntry newEntry = new ZipEntry(MANIFEST_PATH);
	newWarOutputStream.putNextEntry(newEntry);

	Manifest manifest = new Manifest(warInputStream);
	updateClasspath(manifest);
	manifest.write(newWarOutputStream);

	newWarOutputStream.closeEntry();
    }

}
