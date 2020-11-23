package org.lamsfoundation.lams.logevent.service;

import org.lamsfoundation.lams.logevent.LearnerInteractionEvent;

public interface ILearnerInteractionService {
    void saveEvent(LearnerInteractionEvent event);
}