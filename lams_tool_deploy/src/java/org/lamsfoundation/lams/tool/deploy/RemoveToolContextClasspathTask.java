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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */


package org.lamsfoundation.lams.tool.deploy;

import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 * Remove a tool's application context file from the web.xml of various core modules.
 *
 * @author Fiona Malikoff
 */
public class RemoveToolContextClasspathTask extends UpdateWarTask {

    /**
     * Remove the tool's applicationContext entry from the param-value node of the context-param entry.
     * Should find and remove all the matching entries (should it somehow have have got in there m
     * 
     * @param doc
     * @param children
     * @param childNode
     */
    @Override
    protected void updateParamValue(Document doc, Node contextParamElement) {

	NodeList valueChildren = contextParamElement.getChildNodes();
	for (int i = 0; i < valueChildren.getLength(); i++) {
	    Node valueChild = valueChildren.item(i);
	    if (valueChild instanceof Text) {
		String value = valueChild.getNodeValue();
		String newValue = StringUtils.replace(value, getApplicationContextPathWithClasspath(), "");
		if (newValue.length() < value.length()) {
		    valueChild.setTextContent(newValue);
		    System.out.println(
			    "Removed context entry " + getApplicationContextPathWithClasspath() + " from document.");
		}
	    }
	}
    }

    /** Remove the jar file to the classpath in the MANIFEST.MF file */
    @Override
    protected void updateClasspath(Manifest manifest) throws DeployException {
	Attributes mainAttributes = manifest.getMainAttributes();
	String classpath = null;
	if (mainAttributes != null) {
	    classpath = mainAttributes.getValue(Attributes.Name.CLASS_PATH);
	}

	String newJar = getJarFileNameWithDotSlash();
	String newClasspath = null;
	if (classpath != null) {
	    newClasspath = StringUtils.replace(classpath, newJar, "");
	    mainAttributes.put(Attributes.Name.CLASS_PATH, newClasspath);
	    if (classpath.length() < newClasspath.length()) {
		System.out.println("Removed " + newJar + " from classpath");
	    }
	}
    }

}
