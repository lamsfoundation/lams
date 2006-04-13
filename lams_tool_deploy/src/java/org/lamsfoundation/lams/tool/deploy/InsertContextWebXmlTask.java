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


import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 * Add a  tool's application context file to the web.xml of various core modules.
 * 
 * @author Fiona Malikoff
 */
public class InsertContextWebXmlTask extends UpdateWebXmlTask
{
    
    /** Creates a new instance of InsertContextWebXmlTask */
    public InsertContextWebXmlTask(final String applicationContextPath, final String lamsEarPath, final String centralWebXmlPath,
    		final String learningWebXmlPath, final String  monitoringWebXmlPath) {
    	super(applicationContextPath, lamsEarPath, centralWebXmlPath, learningWebXmlPath, monitoringWebXmlPath );
    }

	/**
	 * Update the param-value node of the context-param entry. Don't add it if it already exists. 
	 * @param doc
	 * @param children
	 * @param childNode
	 */
	protected void updateValue(Document doc, Node contextParamElement) {
		NodeList valueChildren = contextParamElement.getChildNodes();
		boolean foundEntry = false;
		for ( int i=0; i<valueChildren.getLength() && ! foundEntry ; i++ ) {
			Node valueChild = valueChildren.item(i);
			if ( valueChild instanceof Text) {
				String value = valueChild.getNodeValue();  
				int index = value.indexOf(applicationContextPath);
				if ( index >= 0 ) {
					System.out.println("Application context entry "+getApplicationContextPathWithClasspath()
							+" already in document.");
					foundEntry = true;
				}
			}
		}

		if ( ! foundEntry  ) {
			System.out.println("Adding "+getApplicationContextPathWithClasspath()+" to context");
			contextParamElement.appendChild(doc.createTextNode(" "+getApplicationContextPathWithClasspath()+"\n"));
		}
	}

 
    
}
