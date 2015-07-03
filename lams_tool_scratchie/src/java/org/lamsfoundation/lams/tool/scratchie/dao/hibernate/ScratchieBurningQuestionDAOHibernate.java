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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA 
 * 
 * http://www.gnu.org/licenses/gpl.txt 
 * **************************************************************** 
 */  
 
/* $Id$ */  
package org.lamsfoundation.lams.tool.scratchie.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.scratchie.dao.ScratchieBurningQuestionDAO;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieBurningQuestion;

public class ScratchieBurningQuestionDAOHibernate extends LAMSBaseDAO implements ScratchieBurningQuestionDAO {
    
    private static final String FIND_BY_SESSION_AND_ITEM = "from " + ScratchieBurningQuestion.class.getName()
	    + " as r where r.sessionId=? and r.scratchieItem.uid = ?";
    
    private static final String FIND_GENERAL_QUESTION_BY_SESSION = "from " + ScratchieBurningQuestion.class.getName()
	    + " as r where r.sessionId=? and r.generalQuestion = 1";
    
    private static final String FIND_BY_SESSION = "from " + ScratchieBurningQuestion.class.getName()
	    + " as r where r.sessionId=?";

    private static final String FIND_BY_ITEM_UID = "from " + ScratchieBurningQuestion.class.getName()
	    + " as r where r.scratchieItem.uid=? order by r.sessionId asc";

//  @Override
//  public List<ScratchieBurningQuestion> getBurningQuestionsByContentId(Long contentId) {
//	return getHibernateTemplate().find(FIND_BY_CONTENT_ID, new Object[] { contentId});
//  }

    @Override
    public List<ScratchieBurningQuestion> getBurningQuestionsByItemUid(Long itemUid) {
	return (List<ScratchieBurningQuestion>) this.doFind(FIND_BY_ITEM_UID, new Object[] { itemUid});
    }
    
    @Override
    public ScratchieBurningQuestion getBurningQuestionBySessionAndItem(Long sessionId, Long itemUid) {
	List list = this.doFind(FIND_BY_SESSION_AND_ITEM, new Object[] { sessionId, itemUid });
	if (list == null || list.size() == 0)
	    return null;
	return (ScratchieBurningQuestion) list.get(0);
    }
    
    @Override
    public ScratchieBurningQuestion getGeneralBurningQuestionBySession(Long sessionId) {
	List list = this.find(FIND_GENERAL_QUESTION_BY_SESSION, new Object[] { sessionId });
	if (list == null || list.size() == 0)
	    return null;
	return (ScratchieBurningQuestion) list.get(0);
    }
    
    @Override
    public List<ScratchieBurningQuestion> getBurningQuestionsBySession(Long sessionId) {
	return (List<ScratchieBurningQuestion>) this.doFind(FIND_BY_SESSION, new Object[] { sessionId });
    }

}
