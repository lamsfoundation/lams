package org.lamsfoundation.lams.logevent.service;

import java.util.Map;

import org.lamsfoundation.lams.logevent.LearnerInteractionEvent;

public interface ILearnerInteractionService {
    void saveEvent(LearnerInteractionEvent event);

    Map<Long, LearnerInteractionEvent> getFirstLearnerInteractions(long toolContentId, int userId);
}