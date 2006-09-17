/***************************************************************************
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
 * ***********************************************************************/
/* $$Id$$ */
package org.lamsfoundation.lams.web.tag;

import java.io.IOException;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

import org.apache.log4j.Logger;
import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;

import java.util.Random;

/**
 * Output a random number for the learner and passon flash movies to communicate directly.
 * 
 * Seed by a time value in milliseconds
 * 
 * @jsp.tag name="generateID"
 * 			body-content="empty"
 * 			display-name="generate unique ID"
 * 			description="Output a random number for the learner and passon flash movies to communicate directly."
 * 	
 * @author Mitchell Seaton
 */
public class GenerateIDTag extends TagSupport {

	private static final long serialVersionUID = -2801719186682639858L;

	private static final Logger log = Logger.getLogger(GenerateIDTag.class);

	/** The random number to be output */
	private int number = -1;
	private Integer lessonID;
	
	public GenerateIDTag() {
		super();
	}
	
	/**
	 * @param id Lesson ID
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return Lesson ID
	 * 
	 * @jsp.attribute required="false"
	 * 				  rtexprvalue="true"
	 * 				  description="Lesson Identifier"
	 */
	public String getId() {
		return this.id;
	}
	
	
	public int doStartTag() throws JspException {
		String uniqueID;
		HttpSession ss = SessionManager.getSession();
		JspWriter writer = pageContext.getOut();
		try {
			if(ss != null){
				if(getId() != null && getId().startsWith("$"))
					ExpressionEvaluatorManager.evaluate("id", getId(), String.class, this, pageContext);
				
				if((uniqueID = (String)ss.getAttribute(AttributeNames.UID)) != null){
					if(getId() != null)
						writer.print(uniqueID + "_" + getId());
					else
						writer.print(uniqueID);
				} else {
						long seed = System.currentTimeMillis();
						Random rand = new Random(seed);
						number = rand.nextInt();
						
						// ensure positive value
						int pos = Math.abs(number);
						
						if (number != -1) {
					        ss.setAttribute(AttributeNames.UID, Integer.toString(pos));
					        if(getId() != null)
					        	writer.print(ss.getAttribute(AttributeNames.UID) + "_" + getId());
					        else
					        	writer.print(ss.getAttribute(AttributeNames.UID));
					    } else {
					    	log.warn("GenerateIDTag could not write out random number because no new integer value was assigned.");
					    }
				}
			} else {
				log.warn("GenerateTag unable to access shared session as it is missing");
			}
		} catch(Exception e) {
	   		log.error("GenerateIDTag unable to write out random number due to Exception.");
	   		throw new JspException(e);
	   	}
    	return SKIP_BODY;
	}

	public int doEndTag() {
		return EVAL_PAGE;
	}

}
