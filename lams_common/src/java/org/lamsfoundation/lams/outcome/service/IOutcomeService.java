package org.lamsfoundation.lams.outcome.service;

import java.util.List;

import org.lamsfoundation.lams.outcome.Outcome;

public interface IOutcomeService {
    String getContentFolderId(Integer organisationId);

    List<Outcome> getOutcomesForManagement(Integer organisationId);
}
