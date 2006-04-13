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
public class RemoveContextWebXmlTask extends UpdateWebXmlTask
{
    
 	/**
 	 * Remove the tool's applicationContext entry from the param-value node of the context-param entry.
	 * Should find and remove all the matching entries (should it somehow have have got in there m  
	 * @param doc
	 * @param children
	 * @param childNode
	 */
	protected void updateValue(Document doc, Node contextParamElement) {
		
		NodeList valueChildren = contextParamElement.getChildNodes();
		for ( int i=0; i<valueChildren.getLength(); i++ ) {
			Node valueChild = valueChildren.item(i);
			if ( valueChild instanceof Text) {
				String value = valueChild.getNodeValue();
				String newValue = StringUtils.replace(value,getApplicationContextPathWithClasspath(),"");
				if ( newValue.length() < value.length() ) {
					valueChild.setTextContent(newValue);
					System.out.println("Removed context entry "+getApplicationContextPathWithClasspath()
							+" from document.");
				}
			}
		}


	}
 
    
}
