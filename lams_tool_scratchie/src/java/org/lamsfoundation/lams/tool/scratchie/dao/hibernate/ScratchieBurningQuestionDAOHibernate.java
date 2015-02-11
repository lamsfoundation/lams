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

import org.lamsfoundation.lams.tool.scratchie.dao.ScratchieBurningQuestionDAO;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieBurningQuestion;

public class ScratchieBurningQuestionDAOHibernate extends BaseDAOHibernate implements ScratchieBurningQuestionDAO {

    private static final String FIND_BY_SESSION_AND_ANSWER = "from " + ScratchieBurningQuestion.class.getName()
	    + " as r where r.sessionId = ? and r.scratchieItem.uid=?";
    
    private static final String FIND_BY_SESSION_AND_ITEM = "from " + ScratchieBurningQuestion.class.getName()
	    + " as r where r.sessionId=? and r.scratchieItem.uid = ?";
    
    private static final String FIND_BY_SESSION = "from " + ScratchieBurningQuestion.class.getName()
	    + " as r where r.sessionId=? order by r.scratchieItem.orderId asc";

    private static final String FIND_VIEW_COUNT_BY_SESSION = "select count(*) from "
	    + ScratchieBurningQuestion.class.getName() + " as r where  r.sessionId=?";
    
    @Override
    public ScratchieBurningQuestion getBurningQuestion(Long answerUid, Long sessionId) {
	List list = getHibernateTemplate().find(FIND_BY_SESSION_AND_ANSWER, new Object[] { sessionId, answerUid });
	if (list == null || list.size() == 0)
	    return null;
	return (ScratchieBurningQuestion) list.get(0);
    }

    @Override
    public int getBurningQuestionCountTotal(Long sessionId) {
	List list = getHibernateTemplate().find(FIND_VIEW_COUNT_BY_SESSION, new Object[] { sessionId});
	if (list == null || list.size() == 0)
	    return 0;
	return ((Number) list.get(0)).intValue();
    }
    
    @Override
    public ScratchieBurningQuestion getBurningQuestionBySessionAndItem(Long sessionId, Long itemUid) {
	List list = getHibernateTemplate().find(FIND_BY_SESSION_AND_ITEM, new Object[] {  sessionId, itemUid });
	if (list == null || list.size() == 0)
	    return null;
	return (ScratchieBurningQuestion) list.get(0);
    }
    
    @Override
    public List<ScratchieBurningQuestion> getBurningQuestionsBySession(Long sessionId) {
	return getHibernateTemplate().find(FIND_BY_SESSION, new Object[] { sessionId });
    }

}
