package org.lamsfoundation.lams.outcome.service;

import java.util.List;

import org.lamsfoundation.lams.outcome.Outcome;
import org.lamsfoundation.lams.outcome.OutcomeScale;

public interface IOutcomeService {
    String getContentFolderId(Integer organisationId);

    List<Outcome> getOutcomesForManagement(Integer organisationId);

    List<OutcomeScale> getScalesForManagement(Integer organisationId);
}
