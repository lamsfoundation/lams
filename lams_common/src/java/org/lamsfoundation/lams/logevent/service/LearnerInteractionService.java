package org.lamsfoundation.lams.logevent.service;

import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.logevent.LearnerInteractionEvent;

public class LearnerInteractionService implements ILearnerInteractionService {

    private IBaseDAO baseDAO;

    @Override
    public void saveEvent(LearnerInteractionEvent event) {
	baseDAO.insert(event);
    }

    public void setBaseDAO(IBaseDAO baseDAO) {
	this.baseDAO = baseDAO;
    }
}