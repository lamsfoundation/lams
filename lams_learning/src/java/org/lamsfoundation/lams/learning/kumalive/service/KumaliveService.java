/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.learning.kumalive.service;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.gradebook.util.GradebookConstants;
import org.lamsfoundation.lams.learning.kumalive.dao.IKumaliveDAO;
import org.lamsfoundation.lams.learning.kumalive.model.Kumalive;
import org.lamsfoundation.lams.learning.kumalive.model.KumaliveRubric;
import org.lamsfoundation.lams.learning.kumalive.model.KumaliveScore;
import org.lamsfoundation.lams.security.ISecurityService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.util.ExcelCell;
import org.lamsfoundation.lams.util.MessageService;

public class KumaliveService implements IKumaliveService {
    private static Logger logger = Logger.getLogger(KumaliveService.class);

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");
    private static final ExcelCell[] EMPTY_ROW = new ExcelCell[1];

    private IKumaliveDAO kumaliveDAO;
    private ISecurityService securityService;
    private MessageService messageService;

    static {
	DECIMAL_FORMAT.setRoundingMode(RoundingMode.HALF_UP);
    }

    @Override
    public Kumalive getKumalive(Long id) {
	return (Kumalive) kumaliveDAO.find(Kumalive.class, id);
    }

    /**
     * Fetches or creates a Kumalive
     *
     * @throws JSONException
     */
    @Override
    public Kumalive startKumalive(Integer organisationId, Integer userId, String name, JSONArray rubricsJSON,
	    boolean isTeacher) throws JSONException {
	if (isTeacher) {
	    securityService.isGroupMonitor(organisationId, userId, "start kumalive", true);
	}
	Kumalive kumalive = kumaliveDAO.findKumalive(organisationId);
	if (kumalive == null) {
	    if (!isTeacher) {
		return null;
	    }
	} else {
	    return kumalive;
	}

	Organisation organisation = (Organisation) kumaliveDAO.find(Organisation.class, organisationId);
	User createdBy = (User) kumaliveDAO.find(User.class, userId);
	kumalive = new Kumalive(organisation, createdBy, name);
	kumaliveDAO.insert(kumalive);

	Set<KumaliveRubric> rubrics = new HashSet<>();
	if (rubricsJSON == null) {
	    KumaliveRubric rubric = new KumaliveRubric(organisation, kumalive, (short) 0, null);
	    kumaliveDAO.insert(rubric);
	    rubrics.add(rubric);
	} else {
	    for (Short rubricIndex = 0; rubricIndex < rubricsJSON.length(); rubricIndex++) {
		String rubricName = rubricsJSON.getString(rubricIndex.intValue());
		KumaliveRubric rubric = new KumaliveRubric(organisation, kumalive, rubricIndex, rubricName);
		kumaliveDAO.insert(rubric);
		rubrics.add(rubric);
	    }
	}
	kumalive.setRubrics(rubrics);
	kumaliveDAO.update(kumalive);

	if (logger.isDebugEnabled()) {
	    logger.debug("Teacher " + userId + " started Kumalive " + kumalive.getKumaliveId());
	}

	return kumalive;
    }

    /**
     * Ends Kumalive
     */
    @Override
    public void finishKumalive(Long id) {
	Kumalive kumalive = (Kumalive) kumaliveDAO.find(Kumalive.class, id);
	kumalive.setFinished(true);
	kumaliveDAO.update(kumalive);
    }

    /**
     * Save Kumalive score
     */
    @Override
    public void scoreKumalive(Long rubricId, Integer userId, Long batch, Short score) {
	KumaliveRubric rubric = (KumaliveRubric) kumaliveDAO.find(KumaliveRubric.class, rubricId);
	User user = (User) kumaliveDAO.find(User.class, userId);
	KumaliveScore kumaliveScore = new KumaliveScore(rubric, user, batch, score);
	kumaliveDAO.insert(kumaliveScore);
    }

    @Override
    public List<KumaliveRubric> getRubrics(Integer organisationId) {
	return kumaliveDAO.findRubrics(organisationId);
    }

    @Override
    public void saveRubrics(Integer organisationId, JSONArray rubricsJSON) throws JSONException {
	Organisation organisation = (Organisation) kumaliveDAO.find(Organisation.class, organisationId);
	kumaliveDAO.deleteByProperty(KumaliveRubric.class, "organisation", organisation);
	for (Short rubricIndex = 0; rubricIndex < rubricsJSON.length(); rubricIndex++) {
	    String name = rubricsJSON.getString(rubricIndex.intValue());
	    KumaliveRubric rubric = new KumaliveRubric(organisation, null, rubricIndex, name);
	    kumaliveDAO.insert(rubric);
	}
    }

    /**
     * Gets Kumalives for the given organisation packed into jqGrid JSON format
     */
    @Override
    public JSONObject getReportOrganisationData(Integer organisationId, String sortColumn, boolean isAscending,
	    int rowLimit, int page) throws JSONException {
	List<Kumalive> kumalives = kumaliveDAO.findKumalives(organisationId, sortColumn, isAscending);

	// paging
	int totalPages = 1;
	if (rowLimit < kumalives.size()) {
	    totalPages = new Double(
		    Math.ceil(new Integer(kumalives.size()).doubleValue() / new Integer(rowLimit).doubleValue()))
			    .intValue();
	    int firstRow = (page - 1) * rowLimit;
	    int lastRow = firstRow + rowLimit;

	    if (lastRow > kumalives.size()) {
		kumalives = kumalives.subList(firstRow, kumalives.size());
	    } else {
		kumalives = kumalives.subList(firstRow, lastRow);
	    }
	}

	JSONObject resultJSON = new JSONObject();

	resultJSON.put(GradebookConstants.ELEMENT_PAGE, page);
	resultJSON.put(GradebookConstants.ELEMENT_TOTAL, totalPages);
	resultJSON.put(GradebookConstants.ELEMENT_RECORDS, kumalives.size());

	JSONArray rowsJSON = new JSONArray();

	// IDs are arbitrary, so generate order starting from 1
	int order = (page - 1) * rowLimit + (isAscending ? 1 : kumalives.size());

	for (Kumalive kumalive : kumalives) {
	    JSONObject rowJSON = new JSONObject();
	    rowJSON.put(GradebookConstants.ELEMENT_ID, kumalive.getKumaliveId());

	    JSONArray cellJSON = new JSONArray();
	    cellJSON.put(order);
	    cellJSON.put(kumalive.getName());

	    rowJSON.put(GradebookConstants.ELEMENT_CELL, cellJSON);
	    rowsJSON.put(rowJSON);

	    if (isAscending) {
		order++;
	    } else {
		order--;
	    }
	}

	resultJSON.put(GradebookConstants.ELEMENT_ROWS, rowsJSON);

	return resultJSON;
    }

    /**
     * Gets learners who answered to question in the given Kumalive, packed into jqGrid JSON format
     */
    @Override
    public JSONObject getReportKumaliveData(Long kumaliveId, boolean isAscending) throws JSONException {
	Kumalive kumalive = getKumalive(kumaliveId);
	List<Long> rubrics = new LinkedList<Long>();
	for (KumaliveRubric rubric : kumalive.getRubrics()) {
	    rubrics.add(rubric.getRubricId());
	}

	// mapping learner -> rubric ID -> scores
	Map<User, Map<Long, List<Short>>> scores = kumaliveDAO.findKumaliveScore(kumaliveId, isAscending).stream()
		.collect(Collectors.groupingBy(KumaliveScore::getUser, LinkedHashMap::new,
			Collectors.groupingBy(score -> score.getRubric().getRubricId(),
				Collectors.mapping(KumaliveScore::getScore, Collectors.toList()))));

	JSONObject resultJSON = new JSONObject();
	resultJSON.put(GradebookConstants.ELEMENT_RECORDS, scores.size());

	JSONArray rowsJSON = new JSONArray();
	for (Entry<User, Map<Long, List<Short>>> userEntry : scores.entrySet()) {
	    JSONObject rowJSON = new JSONObject();
	    User user = userEntry.getKey();
	    rowJSON.put(GradebookConstants.ELEMENT_ID, user.getUserId());

	    JSONArray cellJSON = new JSONArray();
	    cellJSON.put(user.getFirstName() + " " + user.getLastName());
	    // calculate average of scores for the given rubric
	    for (Long rubric : rubrics) {
		Double score = null;
		List<Short> attempts = userEntry.getValue().get(rubric);
		if (attempts != null) {
		    for (Short attempt : attempts) {
			if (score == null) {
			    score = attempt.doubleValue();
			} else {
			    score += attempt;
			}
		    }
		}
		// format nicely
		cellJSON.put(score == null ? "" : DECIMAL_FORMAT.format(score / attempts.size()));
	    }

	    rowJSON.put(GradebookConstants.ELEMENT_CELL, cellJSON);
	    rowsJSON.put(rowJSON);
	}

	resultJSON.put(GradebookConstants.ELEMENT_ROWS, rowsJSON);

	return resultJSON;
    }

    /**
     * Gets scores for the given Kumalive and learner, packed into jqGrid JSON format
     */
    @Override
    public JSONObject getReportUserData(Long kumaliveId, Integer userId) throws JSONException {
	Kumalive kumalive = getKumalive(kumaliveId);
	List<Long> rubrics = new LinkedList<Long>();
	for (KumaliveRubric rubric : kumalive.getRubrics()) {
	    rubrics.add(rubric.getRubricId());
	}

	// mapping batch (question ID) -> rubric ID -> score
	Map<Long, Map<Long, Short>> scores = kumaliveDAO.findKumaliveScore(kumaliveId, userId).stream()
		.collect(Collectors.groupingBy(KumaliveScore::getBatch, LinkedHashMap::new,
			Collectors.toMap(score -> score.getRubric().getRubricId(), KumaliveScore::getScore)));

	JSONObject resultJSON = new JSONObject();
	resultJSON.put(GradebookConstants.ELEMENT_RECORDS, scores.size());

	JSONArray rowsJSON = new JSONArray();
	// just normal ordering of questions
	short order = 1;
	for (Entry<Long, Map<Long, Short>> batchEntry : scores.entrySet()) {
	    JSONObject rowJSON = new JSONObject();
	    rowJSON.put(GradebookConstants.ELEMENT_ID, order);

	    JSONArray cellJSON = new JSONArray();
	    cellJSON.put(order);
	    order++;
	    for (Long rubric : rubrics) {
		Short score = batchEntry.getValue().get(rubric);
		cellJSON.put(score == null ? "" : score);
	    }

	    rowJSON.put(GradebookConstants.ELEMENT_CELL, cellJSON);
	    rowsJSON.put(rowJSON);
	}

	resultJSON.put(GradebookConstants.ELEMENT_ROWS, rowsJSON);

	return resultJSON;
    }

    /**
     * Exports to Excel all Kumalives in the given organisation.
     */
    @Override
    public LinkedHashMap<String, ExcelCell[][]> exportKumalives(Integer organisationId) {
	List<Kumalive> kumalives = kumaliveDAO.findKumalives(organisationId, "", true);
	return export(kumalives);
    }

    /**
     * Exports to Excel Kumalives with given IDs.
     */
    @Override
    public LinkedHashMap<String, ExcelCell[][]> exportKumalives(List<Long> kumaliveIds) {
	List<Kumalive> kumalives = kumaliveDAO.findKumalives(kumaliveIds);
	return export(kumalives);
    }

    /**
     * Exports to Excel given Kumalives.
     */
    private LinkedHashMap<String, ExcelCell[][]> export(List<Kumalive> kumalives) {
	List<ExcelCell[]> rows = new LinkedList<ExcelCell[]>();

	// iterate over Kumalives and add them to the report
	for (Kumalive kumalive : kumalives) {
	    ExcelCell[] kumaliveHeaderRow = new ExcelCell[1];
	    kumaliveHeaderRow[0] = new ExcelCell(messageService.getMessage("label.kumalive.report.name"), true);
	    rows.add(kumaliveHeaderRow);

	    ExcelCell[] kumaliveNameRow = new ExcelCell[1];
	    kumaliveNameRow[0] = new ExcelCell(kumalive.getName(), false);
	    rows.add(kumaliveNameRow);

	    // mapping user -> batch -> rubric -> score
	    Map<User, Map<Long, Map<Long, Short>>> scores = kumaliveDAO
		    .findKumaliveScore(kumalive.getKumaliveId(), true).stream()
		    .collect(Collectors.groupingBy(KumaliveScore::getUser, LinkedHashMap::new, Collectors.groupingBy(
			    KumaliveScore::getBatch, LinkedHashMap::new,
			    Collectors.toMap(score -> score.getRubric().getRubricId(), KumaliveScore::getScore))));

	    if (scores.size() == 0) {
		// no learners answered to question, carry on
		ExcelCell[] noMarksRow = new ExcelCell[1];
		noMarksRow[0] = new ExcelCell(messageService.getMessage("label.kumalive.report.mark.none"), true);
		rows.add(noMarksRow);
		rows.add(EMPTY_ROW);
		continue;
	    }

	    // headers for learners
	    ExcelCell[] marksRow = new ExcelCell[1];
	    marksRow[0] = new ExcelCell(messageService.getMessage("label.kumalive.report.mark"), true);
	    rows.add(marksRow);

	    ExcelCell[] userHeaderRow = new ExcelCell[4 + kumalive.getRubrics().size()];
	    userHeaderRow[0] = new ExcelCell(messageService.getMessage("label.kumalive.report.first.name"), true);
	    userHeaderRow[1] = new ExcelCell(messageService.getMessage("label.kumalive.report.last.name"), true);
	    userHeaderRow[2] = new ExcelCell(messageService.getMessage("label.kumalive.report.login"), true);
	    userHeaderRow[3] = new ExcelCell(messageService.getMessage("label.kumalive.report.attempt"), true);
	    // iterate over rubrics and make them columns
	    int userRowLength = 4;
	    for (KumaliveRubric rubric : kumalive.getRubrics()) {
		userHeaderRow[userRowLength++] = new ExcelCell(rubric.getName(), true);
	    }
	    rows.add(userHeaderRow);

	    for (Entry<User, Map<Long, Map<Long, Short>>> learnerEntry : scores.entrySet()) {
		// learner details and average mark
		User learner = learnerEntry.getKey();
		ExcelCell[] userRow = new ExcelCell[userRowLength];
		userRow[0] = new ExcelCell(learner.getFirstName(), false);
		userRow[1] = new ExcelCell(learner.getLastName(), false);
		userRow[2] = new ExcelCell(learner.getLogin(), false);
		userRow[3] = new ExcelCell(messageService.getMessage("label.kumalive.report.average"), false);
		rows.add(userRow);

		// build rows for each attempt (answer to a question, batch)
		Short[][] resultsByRubric = new Short[learnerEntry.getValue().size()][userRowLength - 4];
		int attempt = 0;
		for (Map<Long, Short> results : scores.get(learner).values()) {
		    ExcelCell[] attemptRow = new ExcelCell[userRowLength];
		    attemptRow[3] = new ExcelCell(
			    messageService.getMessage("label.kumalive.report.attempt") + " " + (attempt + 1), false);
		    int rubricIndex = 0;
		    for (KumaliveRubric rubric : kumalive.getRubrics()) {
			Short result = results.get(rubric.getRubricId());
			attemptRow[rubricIndex + 4] = new ExcelCell(result, false);
			// store mark to calculate average later
			resultsByRubric[attempt][rubricIndex] = result;
			rubricIndex++;
		    }
		    attempt++;
		    rows.add(attemptRow);
		}
		// calculate average per each rubric and update the first learner row
		for (int rubricIndex = 0; rubricIndex < userRowLength - 4; rubricIndex++) {
		    int count = 0;
		    double score = 0;
		    for (int attemptIndex = 0; attemptIndex < resultsByRubric.length; attemptIndex++) {
			Short result = resultsByRubric[attemptIndex][rubricIndex];
			if (result == null) {
			    continue;
			}
			score += result;
			count++;
		    }
		    if (count > 0) {
			userRow[rubricIndex + 4] = new ExcelCell(DECIMAL_FORMAT.format(score / count), false);
		    }
		}
	    }
	    rows.add(EMPTY_ROW);
	}

	LinkedHashMap<String, ExcelCell[][]> dataToExport = new LinkedHashMap<String, ExcelCell[][]>();
	dataToExport.put(messageService.getMessage("label.kumalive.report.header"), rows.toArray(new ExcelCell[][] {}));
	return dataToExport;
    }

    public void setSecurityService(ISecurityService securityService) {
	this.securityService = securityService;
    }

    public void setKumaliveDAO(IKumaliveDAO kumaliveDAO) {
	this.kumaliveDAO = kumaliveDAO;
    }

    public void setMessageService(MessageService messageService) {
	this.messageService = messageService;
    }
}