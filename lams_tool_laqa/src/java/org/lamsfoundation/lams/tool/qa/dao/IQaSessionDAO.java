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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ***********************************************************************/
/* $$Id$$ */
package org.lamsfoundation.lams.tool.qa.dao;

import java.util.List;

import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.QaSession;


/**
 * 
 *  * @author Ozgur Demirtas
 * 
 */
public interface IQaSessionDAO
{
	public int countSessionIncomplete();
	
	public int countSessionComplete();
		    
	public QaSession getQaSessionById(long qaSessionId);
	
	public List getToolSessionsForContent(QaContent qa);
	
	public QaSession getQaSessionOrNullById(long qaSessionId);
	
	public int studentActivityOccurred(QaContent qa);
	
	public void CreateQaSession(QaSession session);
    
    public void UpdateQaSession(QaSession session);
    
    public void deleteQaSession(QaSession session);
    
    public List getSessionsFromContent(QaContent qaContent);
    
    public String getSessionNameById(long qaSessionId);
    
    public List getSessionNamesFromContent(QaContent qaContent);
}





