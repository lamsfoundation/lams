package org.lamsfoundation.lams.outcome.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.lamsfoundation.lams.outcome.Outcome;
import org.lamsfoundation.lams.outcome.OutcomeMapping;
import org.lamsfoundation.lams.outcome.OutcomeScale;
import org.lamsfoundation.lams.outcome.dao.IOutcomeDAO;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

public class OutcomeService implements IOutcomeService {
    private IOutcomeDAO outcomeDAO;

    public String getContentFolderId(Integer organisationId) {
	String contentFolderId = outcomeDAO.getContentFolderID(organisationId);
	return contentFolderId == null ? FileUtil.generateUniqueContentFolderID() : contentFolderId;
    }

    public List<Outcome> getOutcomes(Integer organisationId) {
	return outcomeDAO.getOutcomesSortedByName(organisationId);
    }

    public List<OutcomeScale> getScales(Integer organisationId) {
	return outcomeDAO.getScalesSortedByName(organisationId);
    }

    public List<Outcome> getOutcomes(String search, Set<Integer> organisationIds) {
	if (organisationIds == null) {
	    Integer userId = getUserDTO().getUserID();
	    organisationIds = new HashSet<Integer>(outcomeDAO.getAuthorOrganisations(userId));
	}
	return outcomeDAO.getOutcomesSortedByName(search, organisationIds);
    }

    public List<OutcomeMapping> getOutcomeMappings(Long lessonId, Long toolContentId, Long itemId) {
	return outcomeDAO.getOutcomeMappings(lessonId, toolContentId, itemId);
    }

    public void setOutcomeDAO(IOutcomeDAO outcomeDAO) {
	this.outcomeDAO = outcomeDAO;
    }

    private UserDTO getUserDTO() {
	HttpSession ss = SessionManager.getSession();
	return (UserDTO) ss.getAttribute(AttributeNames.USER);
    }
}