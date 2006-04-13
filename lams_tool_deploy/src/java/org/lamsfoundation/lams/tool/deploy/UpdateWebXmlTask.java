/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $$Id$$ */
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
 * Base class of ant tasks that change a webxml
 * @author Fiona Malikoff
 */
public abstract class UpdateWebXmlTask implements Task
{
	private static final int BUFFER_SIZE = 8192;
	protected static final String WEBXML_PATH = "WEB-INF/web.xml"; 

    /**
     * The lams.ear path file.
     */
    protected String lamsEarPath;
    
    /**
     * The value of the tool application context file, including the path
     * e.g. /org/lamsfoundation/lams/tool/vote/voteApplicationContext.xml
     * It should not include the "classpath:" prefix as this will be added
     * automatically.
     */
    protected String applicationContextPath;
    
    private List<String> warsToUpdate;
    
    /**
     * Sets the location of the application xml file to be modified.
     * @param appxml New value of property appxml.
     */
    public void setLamsEarPath(final String lamsEarPath)
    {
        this.lamsEarPath = lamsEarPath;
    }
    
    /**
     * Execute the task
     * @throws org.apache.tools.ant.DeployException In case of any problems
     */
    public void execute() throws DeployException
    {
		if ( applicationContextPath == null ) { 
			throw new DeployException("UpdateWebXmTask: Unable to update web.xml as the application content path is missing (applicationContextPath).");
		}

		if ( warsToUpdate != null ) {
			
			// map the old File to the new File e.g. lams_learning.war is the key, lams_learning.war.new is the value  
			Map<File,File> filesToRename = new HashMap<File,File>(warsToUpdate.size());
			
			for ( String warFileName: warsToUpdate ) {
				
		    	File warFile = new File(warFileName);
				if ( ! warFile.canRead() || warFile.isDirectory() ) {
					throw new DeployException("Unable to access war file "+warFileName+". May be missing, a directory or not readable");
				}
				String newFilename = lamsEarPath+warFileName+".new";
				File outputFile = new File(newFilename);
				ZipOutputStream newWarOutputStream = null;
				ZipInputStream warInputStream = null;
				try {
					
					newWarOutputStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(outputFile)));
					newWarOutputStream.setMethod(ZipOutputStream.DEFLATED);
					newWarOutputStream.setLevel(Deflater.DEFAULT_COMPRESSION);
					//newWarOutputStream.setMethod(ZipOutputStream.STORED);

					warInputStream = new ZipInputStream(new BufferedInputStream(new FileInputStream(warFile)));
					ZipEntry entry = null; 
					
			 		// work through each entry, copying across to the output stream. 
					// when we hit the web.xml, we have to modify it.
			        while( ( entry = warInputStream.getNextEntry() ) != null ) {
			        	processEntry(newWarOutputStream, warInputStream, entry);
			        }
			        
					warInputStream.close();
					newWarOutputStream.close();
					
				} catch (IOException e) {
					throw new DeployException("Unable to process war file "+warFileName,e);
				} finally {
					try { 
						if ( warInputStream != null )
							warInputStream.close();
						if ( newWarOutputStream != null )
							newWarOutputStream.close();
					} catch ( IOException e2 ) {}
				}
				
				filesToRename.put(warFile, outputFile);
		    }
			
			copyNewFilesToOldNames(filesToRename);
		}
		
    }

	/**
	 * Everything worked okay so rename the files
	 * @param filesToRename
	 */
	private void copyNewFilesToOldNames(Map<File, File> filesToRename) throws DeployException {
		for ( Map.Entry<File,File> mapEntry : filesToRename.entrySet() ) {
			Map<File,File> renamed = new HashMap<File,File>(warsToUpdate.size());
			
			File origFile = mapEntry.getKey();
			File backup = new File ( origFile.getAbsoluteFile() + ".bak");
			boolean successful = origFile.renameTo(backup);
			if ( successful ) {
				renamed.put(origFile, backup);
				successful = mapEntry.getValue().renameTo(origFile);
			} 
			
			if ( successful ) {
				System.out.println("Updated web.xml in war file "+origFile.getName());
				
			} else {
				for ( Map.Entry<File,File> renamedMapEntry : renamed.entrySet() ) {
					File updatedFile = renamedMapEntry.getKey();
					File backupFile = renamedMapEntry.getValue();
					backupFile.renameTo(updatedFile);
				}
				String message = "Error occured renaming the war files. Tried to go back to old files but may or may not have succeeded. Check files:";
				for ( String warFileName: warsToUpdate ) {
					message += " " + warFileName;
				}
				throw new DeployException(message);
			}
			
		}
	}

	/**
	 * @param newWarOutputStream
	 * @param warInputStream
	 * @param entry
	 * @throws IOException
	 */
	private void processEntry(ZipOutputStream newWarOutputStream, ZipInputStream warInputStream, ZipEntry entry) throws IOException {
		
		if ( entry.getName().equals(WEBXML_PATH) ) {
			
			ZipEntry newEntry = new ZipEntry(WEBXML_PATH);
			newWarOutputStream.putNextEntry(newEntry);
			
			// can't just pass the stream to the parser, as the parser will close the stream.
			InputStream copyInputStream = copyToByteArrayInputStream(warInputStream);
			
			Document doc = parseWebXml(copyInputStream);
		    updateWebXml(doc);
		    writeWebXml(doc, newWarOutputStream);
		    
		} else {

			ZipEntry newEntry = new ZipEntry(entry.getName());
			newWarOutputStream.putNextEntry(newEntry);

			byte[] data = new byte[ BUFFER_SIZE ]; 
			int count = -1;
		    while( (count = warInputStream.read( data, 0, BUFFER_SIZE ) ) != -1 )
		    {
		    	newWarOutputStream.write( data, 0, count );
		    }
		}
		
		newWarOutputStream.closeEntry();
	}
	
	private ByteArrayInputStream copyToByteArrayInputStream(ZipInputStream warInputStream) throws IOException {
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		byte[] data = new byte[ BUFFER_SIZE ]; 
		int count = -1;
	    while( (count = warInputStream.read( data, 0, BUFFER_SIZE ) ) != -1 )
	    {
	    	os.write( data, 0, count );
	    }
	    os.close();
	    
	    return new ByteArrayInputStream(os.toByteArray());
	}
    protected void updateWebXml(Document doc) throws DeployException
    {
        
        NodeList contextParamNodeList = doc.getElementsByTagName("context-param");
        Element matchingContextParamElement = findContextParamWithMatchingParamName("contextConfigLocation", contextParamNodeList);
        if ( matchingContextParamElement == null ) {
        	throw new DeployException("No contextConfigLocation can be found in the web.xml in the war");
        }

        NodeList contextParamElements = matchingContextParamElement.getChildNodes();
        for ( int c=0; c<contextParamElements.getLength() ; c++ ){
        	Node contextParamElement = contextParamElements.item(c);
        	if ( contextParamElement instanceof Element ) {
           		if ( "param-value".equals(contextParamElement.getNodeName()) ) {
               		updateValue(doc, contextParamElement);
           		}
        	}
        }
       
    }


    protected abstract void updateValue(Document doc, Node contextParamElement) throws DeployException;

    /**
     * Writes the modified web.xml back out to war file
     * @param doc The application.xml DOM Document
     * @throws org.apache.tools.ant.DeployException in case of any problems
     */
    protected void writeWebXml(final Document doc, final OutputStream outputStream) throws DeployException
    {
        try
        {
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
        }
        catch (TransformerException tex)
        {
            throw new DeployException("Error writing out modified web xml ", tex);
        }
    }
    

    /**
     * Parses the web xml into a Dom document
     * @throws org.apache.tools.ant.DeployException in case of errors
     * @return A DOM Document of the web xml
     */
    protected Document parseWebXml(final InputStream webXmlPath) throws DeployException
    {
        try
        {
            //get application xml as dom doc
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(webXmlPath);
        }
        catch (ParserConfigurationException pex)
        {
            throw new DeployException("Could not configure parser", pex);
        }
        catch (SAXException saxex)
        {
            throw new DeployException("Error parsing web xml"+webXmlPath, saxex);
        }
        catch (IOException ioex)
        {
            throw new DeployException("Error reading web xml"+webXmlPath, ioex);
        }
    }
    
    /**
     * Finds the node element with the param-name matching the given param-name. 
     * @param searchParamName param-name to match
     * @param nodeList the nodeList to look in
     * @throws org.apache.tools.ant.DeployException in case of errors
     * @return the matching Element
     */
    protected Element findContextParamWithMatchingParamName(String searchParamName, NodeList nodeList) throws DeployException
    {
    	/* Looking for node like:
    	 * <context-param>
    	 *   <param-name>contextConfigLocation</param-name>
    	 *   <param-value>
    	 *   classpath:/org/lamsfoundation/lams/applicationContext.xml
    	 *   </param-value>
    	 *  </context-param>
    	 */
        for (int i = 0, length = nodeList.getLength(); i < length; i++)
        {
            Node node = nodeList.item(i);
            if (node instanceof Element)
            {
                
                NodeList children = node.getChildNodes();
                String paramName = null;
                for ( int c=0; c<children.getLength() ; c++ ){

                	Node childNode = children.item(c);
                	if ( childNode instanceof Element ) {
	               		if ( "param-name".equals(childNode.getNodeName()) ) { 
	                
	               			NodeList nameChildren = childNode.getChildNodes();
	                   		if ((nameChildren != null) && (nameChildren.getLength() > 0) && nameChildren.item(0) instanceof Text) {
	                   			paramName = nameChildren.item(0).getNodeValue();
	                   			if ( paramName!=null && paramName.equals(searchParamName) )
	                   				return (Element) node;
	                        }
	               		}
                	}
                }
                
           }
            
        }
        
        return null;
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
	public String getApplicationContextPathWithClasspath() {
		return  "classpath:"+applicationContextPath;
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

	public List<String> getWarsToUpdate() {
		return warsToUpdate;
	}

	public void setWarsToUpdate(List<String> warsToUpdate) {
		this.warsToUpdate = warsToUpdate;
	}
    
    
}
