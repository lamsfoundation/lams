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


package org.lamsfoundation.lams.tool.scratchie.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.query.NativeQuery;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.scratchie.dao.ScratchieBurningQuestionDAO;
import org.lamsfoundation.lams.tool.scratchie.dto.BurningQuestionDTO;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieBurningQuestion;

public class ScratchieBurningQuestionDAOHibernate extends LAMSBaseDAO implements ScratchieBurningQuestionDAO {

    private static final String FIND_BY_SESSION_AND_ITEM = "from " + ScratchieBurningQuestion.class.getName()
	    + " as r where r.sessionId=? and r.scratchieItem.uid = ?";

    private static final String FIND_GENERAL_QUESTION_BY_SESSION = "from " + ScratchieBurningQuestion.class.getName()
	    + " as r where r.sessionId=? and r.generalQuestion = 1";

    private static final String FIND_BY_SESSION = "from " + ScratchieBurningQuestion.class.getName()
	    + " as r where r.sessionId=?";

    @Override
    @SuppressWarnings("unchecked")
    public List<BurningQuestionDTO> getBurningQuestionsByContentId(Long scratchieUid, Long sessionId) {

	/*
	 * Thread based lookups - Returns a complex structure so that the likes information can be passed
	 * back with it.
	 */
	String GET_BURNING_QUESTIONS_WITH_LIKES = "SELECT bq.*, ANY_VALUE(session.session_name) sessionName, count(like1.uid) total_likes ";
	//in case sessionId is provided - we need to also return which burning questions leader has liked
	if (sessionId != null) {
	    GET_BURNING_QUESTIONS_WITH_LIKES += ", EXISTS(select * from tl_lascrt11_burning_que_like like2 where bq.uid = like2.burning_question_uid AND like2.session_id=:sessionId) user_liked";
	}
	GET_BURNING_QUESTIONS_WITH_LIKES += " FROM tl_lascrt11_burning_question bq "
		+ " JOIN tl_lascrt11_session session"
		+ " 	ON session.scratchie_uid = :scratchieUid AND bq.session_id = session.session_id "
		+ " LEFT JOIN tl_lascrt11_burning_que_like like1 ON bq.uid = like1.burning_question_uid "
		+ " WHERE bq.question IS NOT NULL AND bq.question != ''" + " GROUP BY bq.uid";

	NativeQuery<Object[]> query = getSession().createNativeQuery(GET_BURNING_QUESTIONS_WITH_LIKES);
	query.addEntity("bq", ScratchieBurningQuestion.class).addScalar("sessionName", StringType.INSTANCE)
		.addScalar("total_likes", IntegerType.INSTANCE).setParameter("scratchieUid", scratchieUid);
	if (sessionId != null) {
	    query.addScalar("user_liked", IntegerType.INSTANCE).setParameter("sessionId", sessionId);
	}
	List<Object[]> rawObjects = query.list();

//	Comparator<Comment> comparator = ICommentService.SORT_BY_LIKE.equals(sortBy) ? new TopicComparatorLike() : new TopicComparator();
//	SortedSet<Comment> results = new TreeSet<Comment>(comparator);
	List<BurningQuestionDTO> results = new ArrayList<BurningQuestionDTO>();
	for (Object[] rawObject : rawObjects) {

	    ScratchieBurningQuestion burningQuestion = (ScratchieBurningQuestion) rawObject[0];
	    String sessionName = (String) rawObject[1];
	    Integer likeCount = (Integer) rawObject[2];

	    BurningQuestionDTO burningQuestionDTO = new BurningQuestionDTO();
	    burningQuestionDTO.setBurningQuestion(burningQuestion);
	    burningQuestionDTO.setLikeCount(likeCount != null ? likeCount : 0);
	    if (sessionId != null) {
		boolean userLiked = (Integer) rawObject[3] == 1;
		burningQuestionDTO.setUserLiked(userLiked);
		
		//sets whether the leader of specified group created this burningQuestion
		burningQuestionDTO.setUserAuthor(sessionId.equals(burningQuestion.getSessionId()));
	    }
	    burningQuestionDTO.setSessionName(sessionName);
	    results.add(burningQuestionDTO);
	}
	return results;
    }

    @Override
    public ScratchieBurningQuestion getBurningQuestionBySessionAndItem(Long sessionId, Long itemUid) {
	List<?> list = this.doFind(FIND_BY_SESSION_AND_ITEM, new Object[] { sessionId, itemUid });
	if (list == null || list.size() == 0) {
	    return null;
	}
	return (ScratchieBurningQuestion) list.get(0);
    }

    @Override
    public ScratchieBurningQuestion getGeneralBurningQuestionBySession(Long sessionId) {
	List<?> list = this.find(FIND_GENERAL_QUESTION_BY_SESSION, new Object[] { sessionId });
	if (list == null || list.size() == 0) {
	    return null;
	}
	return (ScratchieBurningQuestion) list.get(0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ScratchieBurningQuestion> getBurningQuestionsBySession(Long sessionId) {
	return (List<ScratchieBurningQuestion>) this.doFind(FIND_BY_SESSION, new Object[] { sessionId });
    }

}
