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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import org.lamsfoundation.lams.usermanagement.AuthenticationMethodParameter;
import org.lamsfoundation.lams.usermanagement.AuthenticationMethod;
import org.lamsfoundation.lams.util.XmlFileLoader;

/**
 * <p>
 * <a href="AuthenticationMethodConfigurer.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public class AuthenticationMethodConfigurer {

	private static Document authConfigureDoc = null;
	
	private static String configFilePath;
	
	/**
	 * @return Returns the configFilePath.
	 */
	public static String getConfigFilePath() {
		return configFilePath;
	}
	/**
	 * @param configFilePath The configFilePath to set.
	 */
	public static void setConfigFilePath(String configFilePath) {
		AuthenticationMethodConfigurer.configFilePath = configFilePath;
	}

	private static void loadConfiguration() 
		throws IOException,SAXException,SAXParseException,ParserConfigurationException{
		
		if(authConfigureDoc==null){
			authConfigureDoc = XmlFileLoader.getDocumentFromFilePath(configFilePath);
		}
	}
	
	private static Element findMethodElement(String methodName)
		throws IOException,SAXException,SAXParseException,ParserConfigurationException
	{
		NodeList nodeList = authConfigureDoc.getElementsByTagName("Method");
		for(int i=0; i<nodeList.getLength(); i++){
			Node node = nodeList.item(i);
			if(node.getNodeType()== Node.ELEMENT_NODE){
				Element ele = (Element)node;
				if(ele.getAttribute("Name").equals(methodName)){
					return ele;
				}
			}
		}
		return null;
	}
	
	private static boolean isEnabled(String methodName)
		throws IOException,SAXException,SAXParseException,ParserConfigurationException
	{
		String enabled = findMethodElement(methodName).getAttribute("Enabled");
		if(enabled.equals("true")){
			return true;
		}
		return false;
	}
	
	private static List getMethodParameters(String methodName)
		throws IOException,SAXException,SAXParseException,ParserConfigurationException{
		
		List list = new ArrayList();
		NodeList nodeList = findMethodElement(methodName).getChildNodes();
		for(int i=0; i<nodeList.getLength(); i++){
			Node node = nodeList.item(i);
			if(node.getNodeType()==Node.ELEMENT_NODE){
				Element ele = (Element)node;
				if(ele.getTagName().equals("Param")){
					AuthenticationMethodParameter parameter = 
						new AuthenticationMethodParameter(ele.getAttribute("Name"),ele.getLastChild().getNodeValue());
					list.add(parameter);
				}
			}
		}
		return list;
	}
	
	public static void configure(AuthenticationMethod method)
		throws IOException,SAXException,SAXParseException,ParserConfigurationException{

		loadConfiguration();
		method.setEnabled(isEnabled(method.getAuthenticationMethodName()));
		method.setAuthenticationMethodParameters(getMethodParameters(method.getAuthenticationMethodName()));
	}

}
