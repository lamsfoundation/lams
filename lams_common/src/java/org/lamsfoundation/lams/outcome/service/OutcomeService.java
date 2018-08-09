package org.lamsfoundation.lams.outcome.service;

import java.util.List;

import org.lamsfoundation.lams.outcome.Outcome;
import org.lamsfoundation.lams.outcome.OutcomeScale;
import org.lamsfoundation.lams.outcome.dao.IOutcomeDAO;
import org.lamsfoundation.lams.util.FileUtil;

public class OutcomeService implements IOutcomeService {
    private IOutcomeDAO outcomeDAO;

    public String getContentFolderId(Integer organisationId) {
	String contentFolderId = outcomeDAO.getContentFolderID(organisationId);
	return contentFolderId == null ? FileUtil.generateUniqueContentFolderID() : contentFolderId;
    }

    public List<Outcome> getOutcomesForManagement(Integer organisationId) {
	return outcomeDAO.getOutcomesSortedByName(organisationId);
    }

    public List<OutcomeScale> getScalesForManagement(Integer organisationId) {
	return outcomeDAO.getScalesSortedByName(organisationId);
    }

    public void setOutcomeDAO(IOutcomeDAO outcomeDAO) {
	this.outcomeDAO = outcomeDAO;
    }
}