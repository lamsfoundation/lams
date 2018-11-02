package org.lamsfoundation.lams.tool.scratchie.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.scratchie.dao.BurningQuestionLikeDAO;
import org.lamsfoundation.lams.tool.scratchie.model.BurningQuestionLike;

public class BurningQuestionLikeDAOHibernate extends LAMSBaseDAO implements BurningQuestionLikeDAO {

    private static String INSERT_LIKE = "INSERT IGNORE INTO tl_lascrt11_burning_que_like(burning_question_uid, session_id) VALUES (:burningQuestionUid,:sessionId);";

    @Override
    public boolean addLike(Long burningQuestionUid, Long sessionId) {
	int status = getSession().createNativeQuery(INSERT_LIKE).setParameter("burningQuestionUid", burningQuestionUid)
		.setParameter("sessionId", sessionId).executeUpdate();
	return status == 1;
    }

    @Override
    public void removeLike(Long burningQuestionUid, Long sessionId) {
	final String FIND_BY_SESSION_AND_BURNING_QUESTION = "FROM " + BurningQuestionLike.class.getName()
		+ " AS l WHERE l.sessionId=? AND l.burningQuestion.uid = ?";

	List<BurningQuestionLike> list = find(FIND_BY_SESSION_AND_BURNING_QUESTION,
		new Object[] { sessionId, burningQuestionUid });
	if (list == null || list.size() == 0) {
	    return;
	}
	BurningQuestionLike like = list.get(0);

	if (like != null) {
	    delete(like);
	}
    }

}
