/*
 * Created on 2005-1-6
 *
 * Last modified on 2005-1-6
 */
package org.lamsfoundation.lams.util;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 * TODO Add description here
 *
 * <p>
 * <a href="ConfigurationLoader.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public class ConfigurationLoader {
	
	private static final String CONFIG_FILE_PATH_WINDOWS = "c:/lamsconf/lams.xml";

	private static final String CONFIG_FILE_PATH_UNIX = "/usr/local/lamsconf/lams.xml";
	
	private static String configFilePath;
	
	/**
	 * @return Returns the configFilePath.
	 */
	public static String getConfigFilePath() {
		setConfigFilePath();
		return configFilePath;
	}
	/**
	 * @param configFilePath The configFilePath to set.
	 */
	private static void setConfigFilePath() {
		//Suppose windows and unix are the only platforms where lams will be installed
		//TODO make it complete
		String osName = System.getProperty("os.name");
		if((osName.indexOf("Windows")!=-1)||(osName.indexOf("windows")!=-1))
		{
			ConfigurationLoader.configFilePath = CONFIG_FILE_PATH_WINDOWS;
		}else
		{
			ConfigurationLoader.configFilePath = CONFIG_FILE_PATH_UNIX;
		}
	}

	public static Map load() 
	{
		setConfigFilePath();
		Map items = Collections.synchronizedMap(new HashMap());
		try{
			Document configureDoc = XmlFileLoader.getDocumentFromFilePath(configFilePath);
			Element root = (Element)configureDoc.getElementsByTagName("Lams").item(0);
			NodeList nodeList = root.getChildNodes();
			for(int i=0; i<nodeList.getLength(); i++){
				Node node = nodeList.item(i);
				if(node.getNodeType()== Node.ELEMENT_NODE){
					Element ele = (Element)node;
					if(ele.getLastChild()!=null){
						items.put(ele.getNodeName(),ele.getLastChild().getNodeValue());
					}
				}
			}
		}catch(IOException e)
		{
System.out.println("===>IOExcpetion in ConfigurationLoader "+e);			
			//nothing to do
		}catch(SAXException e)
		{
System.out.println("===>SAXExcpetion in ConfigurationLoader "+e);
			//nothing to do
		}catch(ParserConfigurationException e)
		{
System.out.println("===>ParserConfigurationException in ConfigurationLoader "+e);
			//nothing to do
		}
		return items;
	}

}
