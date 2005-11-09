/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
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
package org.lamsfoundation.lams.util;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 * <p>
 * <a href="ConfigurationLoader.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public class ConfigurationLoader {   
    private static Logger log = Logger.getLogger(ConfigurationLoader.class);
    
    public static final String ENV_CONFIG_PARAMETER = "LAMS_CONF_FILE"; 
    
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
        //check if environment variable is set
        String envConfigFilePath = System.getenv(ENV_CONFIG_PARAMETER);
        if(envConfigFilePath != null){
            ConfigurationLoader.configFilePath = envConfigFilePath;
        }
        else{
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
        log.debug("Loading Configuration File From: " + ConfigurationLoader.configFilePath);
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
            log.error("===>IOExcpetion in ConfigurationLoader", e);
			//nothing to do
		}catch(SAXException e)
		{
		    log.error("===>SAXExcpetion in ConfigurationLoader", e);
			//nothing to do
		}catch(ParserConfigurationException e)
		{
            log.error("===>ParserConfigurationException in ConfigurationLoader ", e);
			//nothing to do
		}
		return items;
	}

}
