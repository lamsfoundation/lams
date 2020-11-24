package org.lamsfoundation.lams.logevent.service;

import java.util.Map;

import org.lamsfoundation.lams.logevent.LearnerInteractionEvent;
import org.lamsfoundation.lams.logevent.dao.ILearnerInteractionDAO;

public class LearnerInteractionService implements ILearnerInteractionService {

    private ILearnerInteractionDAO learnerInteractionDAO;

    @Override
    public void saveEvent(LearnerInteractionEvent event) {
	learnerInteractionDAO.insert(event);
    }

    /**
     * Returns a map of QbToolQuestion UID -> learner interaction event with the question the first time
     */
    @Override
    public Map<Long, LearnerInteractionEvent> getFirstLearnerInteractions(long toolContentId, int userId) {
	return learnerInteractionDAO.getFirstLearnerInteractions(toolContentId, userId);
    }

    public void setLearnerInteractionDAO(ILearnerInteractionDAO learnerInteractionDAO) {
	this.learnerInteractionDAO = learnerInteractionDAO;
    }
}