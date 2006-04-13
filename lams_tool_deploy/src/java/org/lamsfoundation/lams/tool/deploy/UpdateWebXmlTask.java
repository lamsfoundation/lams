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


import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang.StringUtils;
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
    
    private String centralWebXmlPath;
    private String learningWebXmlPath;
    private String monitoringWebXmlPath;
    
    /** Creates a new instance of UpdateApplicationXmlTask */
    public UpdateWebXmlTask(final String applicationContextURI, final String lamsEarPath, final String centralWebXmlPath,
    		final String learningWebXmlPath, final String  monitoringWebXmlPath)
    {
    	this.applicationContextPath = applicationContextURI;
        this.lamsEarPath = lamsEarPath;
        this.centralWebXmlPath = centralWebXmlPath;
        this.learningWebXmlPath = learningWebXmlPath;
        this.monitoringWebXmlPath = monitoringWebXmlPath;
    }
    
    
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

		Document doc = null;
    	
    	if ( centralWebXmlPath!=null && ! StringUtils.isEmpty(centralWebXmlPath) ) {
	        doc = parseWebXml(centralWebXmlPath);
	        updateWebXml(doc, centralWebXmlPath);
	        writeWebXml(doc, centralWebXmlPath);
    	}
        
    	if ( learningWebXmlPath!=null && ! StringUtils.isEmpty(learningWebXmlPath) ) {
	        doc = parseWebXml(learningWebXmlPath);
	        updateWebXml(doc, learningWebXmlPath);
	        writeWebXml(doc, learningWebXmlPath);
    	}

    	if ( monitoringWebXmlPath!=null && ! StringUtils.isEmpty(monitoringWebXmlPath) ) {
	        doc = parseWebXml(monitoringWebXmlPath);
	        updateWebXml(doc, monitoringWebXmlPath);
	        writeWebXml(doc, monitoringWebXmlPath);
    	}
    }
    
    protected void updateWebXml(Document doc, String documentPath) throws DeployException
    {
        
        NodeList contextParamNodeList = doc.getElementsByTagName("context-param");
        Element matchingContextParamElement = findContextParamWithMatchingParamName("contextConfigLocation", contextParamNodeList);
        if ( matchingContextParamElement == null ) {
        	throw new DeployException("No contextConfigLocation can be found in the file "+documentPath);
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
     * Writes the modified web.xml back out to the file system.
     * @param doc The application.xml DOM Document
     * @throws org.apache.tools.ant.DeployException in case of any problems
     */
    protected void writeWebXml(final Document doc, final String webXmlPath) throws DeployException
    {
        try
        {
            doc.normalize();
            
            // Prepare the DOM document for writing
            DOMSource source = new DOMSource(doc);
            
            // Prepare the output file
            StreamResult result = new StreamResult(webXmlPath);
            
            // Write the DOM document to the file
            // Get Transformer
            Transformer xformer = TransformerFactory.newInstance().newTransformer();
            // Write to a file
            xformer.transform(source, result);
        }
        catch (TransformerException tex)
        {
            throw new DeployException("Error writing out modified web xml "+webXmlPath, tex);
        }
    }
    

    /**
     * Parses the web xml into a Dom document
     * @throws org.apache.tools.ant.DeployException in case of errors
     * @return A DOM Document of the web xml
     */
    protected Document parseWebXml(final String webXmlPath) throws DeployException
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


	public String getCentralWebXmlPath() {
		return centralWebXmlPath;
	}


	public void setCentralWebXmlPath(String centralWebXmlPath) {
		this.centralWebXmlPath = centralWebXmlPath;
	}


	public String getLearningWebXmlPath() {
		return learningWebXmlPath;
	}


	public void setLearningWebXmlPath(String learningWebXmlPath) {
		this.learningWebXmlPath = learningWebXmlPath;
	}


	public String getMonitoringWebXmlPath() {
		return monitoringWebXmlPath;
	}


	public void setMonitoringWebXmlPath(String monitoringWebXmlPath) {
		this.monitoringWebXmlPath = monitoringWebXmlPath;
	}
    
    
}
