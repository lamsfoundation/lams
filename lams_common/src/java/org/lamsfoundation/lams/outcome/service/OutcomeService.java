package org.lamsfoundation.lams.outcome.service;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.lamsfoundation.lams.outcome.Outcome;
import org.lamsfoundation.lams.outcome.OutcomeMapping;
import org.lamsfoundation.lams.outcome.OutcomeResult;
import org.lamsfoundation.lams.outcome.OutcomeScale;
import org.lamsfoundation.lams.outcome.dao.IOutcomeDAO;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.ExcelCell;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

public class OutcomeService implements IOutcomeService {
    private IOutcomeDAO outcomeDAO;
    private MessageService messageService;

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
	    Integer userId = OutcomeService.getUserDTO().getUserID();
	    organisationIds = new HashSet<Integer>(outcomeDAO.getAuthorOrganisations(userId));
	}
	return outcomeDAO.getOutcomesSortedByName(search, organisationIds);
    }

    public List<OutcomeMapping> getOutcomeMappings(Long lessonId, Long toolContentId, Long itemId) {
	return outcomeDAO.getOutcomeMappings(lessonId, toolContentId, itemId);
    }

    public List<OutcomeResult> getOutcomeResults(Integer userId, Long lessonId, Long toolContentId, Long itemId) {
	return outcomeDAO.getOutcomeResults(userId, lessonId, toolContentId, itemId);
    }

    public OutcomeResult getOutcomeResult(Integer userId, Long mappingId) {
	return outcomeDAO.getOutcomeResult(userId, mappingId);
    }

    public OutcomeScale getDefaultScale() {
	return (OutcomeScale) outcomeDAO.find(OutcomeScale.class, DEFAULT_SCALE_ID);
    }

    public boolean isDefaultScale(Long scaleId) {
	return scaleId != null && DEFAULT_SCALE_ID == scaleId;
    }

    public void copyOutcomeMappings(Long sourceLessonId, Long sourceToolContentId, Long sourceItemId,
	    Long targetLessonId, Long targetToolContentId, Long targetItemId) {
	List<OutcomeMapping> sourceMappings = getOutcomeMappings(sourceLessonId, sourceToolContentId, sourceItemId);
	for (OutcomeMapping sourceMapping : sourceMappings) {
	    OutcomeMapping targetMapping = new OutcomeMapping();
	    targetMapping.setOutcome(sourceMapping.getOutcome());
	    targetMapping.setLessonId(targetLessonId);
	    targetMapping.setToolContentId(targetToolContentId);
	    targetMapping.setItemId(targetItemId);
	    outcomeDAO.insert(targetMapping);
	}
    }

    public LinkedHashMap<String, ExcelCell[][]> exportScales() {
	LinkedHashMap<String, ExcelCell[][]> dataToExport = new LinkedHashMap<String, ExcelCell[][]>();

	// The entire data list
	List<ExcelCell[]> rowList = new LinkedList<ExcelCell[]>();
	ExcelCell[] row = new ExcelCell[4];
	row[0] = new ExcelCell(messageService.getMessage("outcome.manage.add.name"), true);
	row[1] = new ExcelCell(messageService.getMessage("outcome.manage.add.code"), true);
	row[2] = new ExcelCell(messageService.getMessage("outcome.manage.add.description"), true);
	row[3] = new ExcelCell(messageService.getMessage("scale.manage.add.value"), true);
	rowList.add(row);

	List<OutcomeScale> scales = getScales(null);
	for (OutcomeScale scale : scales) {
	    row = new ExcelCell[4];
	    row[0] = new ExcelCell(scale.getName(), false);
	    row[1] = new ExcelCell(scale.getCode(), false);
	    row[2] = new ExcelCell(scale.getDescription(), false);
	    row[3] = new ExcelCell(scale.getItemString(), false);
	    rowList.add(row);
	}

	ExcelCell[][] data = rowList.toArray(new ExcelCell[][] {});
	dataToExport.put(messageService.getMessage("scale.title"), data);
	return dataToExport;
    }

    private static UserDTO getUserDTO() {
	HttpSession ss = SessionManager.getSession();
	return (UserDTO) ss.getAttribute(AttributeNames.USER);
    }

    public void setOutcomeDAO(IOutcomeDAO outcomeDAO) {
	this.outcomeDAO = outcomeDAO;
    }

    public void setMessageService(MessageService messageService) {
	this.messageService = messageService;
    }
}