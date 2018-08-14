package org.lamsfoundation.lams.outcome.service;

import java.util.List;
import java.util.Set;

import org.lamsfoundation.lams.outcome.Outcome;
import org.lamsfoundation.lams.outcome.OutcomeMapping;
import org.lamsfoundation.lams.outcome.OutcomeScale;

public interface IOutcomeService {
    String getContentFolderId(Integer organisationId);

    List<Outcome> getOutcomes(Integer organisationId);

    List<Outcome> getOutcomes(String search, Set<Integer> organisationIds);

    List<OutcomeMapping> getOutcomeMappings(Long lessonId, Long toolContentId, Long itemId);

    List<OutcomeScale> getScales(Integer organisationId);
}
