package org.lamsfoundation.lams.tool.scratchie.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.tool.scratchie.dao.BurningQuestionLikeDAO;
import org.lamsfoundation.lams.tool.scratchie.model.BurningQuestionLike;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class BurningQuestionLikeDAOHibernate extends HibernateDaoSupport implements BurningQuestionLikeDAO {
    
    private static String INSERT_LIKE = "INSERT IGNORE INTO tl_lascrt11_burning_que_like(burning_question_uid, session_id) VALUES (:burningQuestionUid,:sessionId);";
	    
    public boolean addLike(Long burningQuestionUid, Long sessionId) {
	int status = getSession().createSQLQuery(INSERT_LIKE)
		.setParameter("burningQuestionUid", burningQuestionUid)
		.setParameter("sessionId", sessionId)
		.executeUpdate();
	return status == 1;
    }
    
    public void removeLike(Long burningQuestionUid, Long sessionId) {

	final String FIND_BY_SESSION_AND_BURNING_QUESTION = "from " + BurningQuestionLike.class.getName()
		+ " as l where l.sessionId=? and l.burningQuestion.uid = ?";
	
	List<BurningQuestionLike> list = getHibernateTemplate().find(FIND_BY_SESSION_AND_BURNING_QUESTION, new Object[] {sessionId, burningQuestionUid});
	if (list == null || list.size() == 0) {
	    return;
	}
	BurningQuestionLike like = list.get(0);
	
	if (like != null) {
	    getHibernateTemplate().delete(like);
	}
    }

}
