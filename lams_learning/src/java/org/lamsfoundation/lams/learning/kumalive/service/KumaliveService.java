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
import java.text.DecimalFormatSymbols;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learning.kumalive.dao.IKumaliveDAO;
import org.lamsfoundation.lams.learning.kumalive.model.Kumalive;
import org.lamsfoundation.lams.learning.kumalive.model.KumaliveLog;
import org.lamsfoundation.lams.learning.kumalive.model.KumalivePoll;
import org.lamsfoundation.lams.learning.kumalive.model.KumalivePollAnswer;
import org.lamsfoundation.lams.learning.kumalive.model.KumaliveRubric;
import org.lamsfoundation.lams.learning.kumalive.model.KumaliveScore;
import org.lamsfoundation.lams.security.ISecurityService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.util.LastNameAlphabeticComparator;
import org.lamsfoundation.lams.util.CommonConstants;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.excel.ExcelCell;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class KumaliveService implements IKumaliveService {
    private static Logger logger = Logger.getLogger(KumaliveService.class);

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##",
	    new DecimalFormatSymbols(Locale.ENGLISH));
    private static final ExcelCell[] EMPTY_ROW = new ExcelCell[1];
    private static final Comparator<User> USER_COMPARATOR = new LastNameAlphabeticComparator();

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

    @Override
    public Kumalive getKumaliveByOrganisation(Integer organisationId) {
	return kumaliveDAO.findKumalive(organisationId);
    }

    /**
     * Fetches or creates a Kumalive
     */
    @Override
    public Kumalive startKumalive(Integer organisationId, Integer userId, String name, ArrayNode rubricsJSON,
	    boolean isTeacher) {
	if (isTeacher) {
	    securityService.isGroupMonitor(organisationId, userId, "start kumalive", true);
	}
	Kumalive kumalive = getKumaliveByOrganisation(organisationId);
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
	    for (Short rubricIndex = 0; rubricIndex < rubricsJSON.size(); rubricIndex++) {
		String rubricName = rubricsJSON.get(rubricIndex.intValue()).asText();
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
    public void saveRubrics(Integer organisationId, ArrayNode rubricsJSON) {
	Organisation organisation = (Organisation) kumaliveDAO.find(Organisation.class, organisationId);
	kumaliveDAO.deleteByProperty(KumaliveRubric.class, "organisation", organisation);
	for (Short rubricIndex = 0; rubricIndex < rubricsJSON.size(); rubricIndex++) {
	    String name = rubricsJSON.get(rubricIndex.intValue()).asText();
	    KumaliveRubric rubric = new KumaliveRubric(organisation, null, rubricIndex, name);
	    kumaliveDAO.insert(rubric);
	}
    }

    /**
     * Gets Kumalives for the given organisation packed into jqGrid JSON format
     */
    @Override
    public ObjectNode getReportOrganisationData(Integer organisationId, String sortColumn, boolean isAscending,
	    int rowLimit, int page) {
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

	ObjectNode resultJSON = JsonNodeFactory.instance.objectNode();

	resultJSON.put(CommonConstants.ELEMENT_PAGE, page);
	resultJSON.put(CommonConstants.ELEMENT_TOTAL, totalPages);
	resultJSON.put(CommonConstants.ELEMENT_RECORDS, kumalives.size());

	ArrayNode rowsJSON = JsonNodeFactory.instance.arrayNode();

	// IDs are arbitrary, so generate order starting from 1
	int order = (page - 1) * rowLimit + (isAscending ? 1 : kumalives.size());

	for (Kumalive kumalive : kumalives) {
	    ObjectNode rowJSON = JsonNodeFactory.instance.objectNode();
	    rowJSON.put(CommonConstants.ELEMENT_ID, kumalive.getKumaliveId());

	    ArrayNode cellJSON = JsonNodeFactory.instance.arrayNode();
	    cellJSON.add(order);
	    cellJSON.add(kumalive.getName());

	    rowJSON.set(CommonConstants.ELEMENT_CELL, cellJSON);
	    rowsJSON.add(rowJSON);

	    if (isAscending) {
		order++;
	    } else {
		order--;
	    }
	}

	resultJSON.set(CommonConstants.ELEMENT_ROWS, rowsJSON);

	return resultJSON;
    }

    /**
     * Gets learners who answered to question in the given Kumalive, packed into jqGrid JSON format
     */
    @Override
    public ObjectNode getReportKumaliveData(Long kumaliveId, boolean isAscending) {
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

	ObjectNode resultJSON = JsonNodeFactory.instance.objectNode();
	resultJSON.put(CommonConstants.ELEMENT_RECORDS, scores.size());

	ArrayNode rowsJSON = JsonNodeFactory.instance.arrayNode();
	for (Entry<User, Map<Long, List<Short>>> userEntry : scores.entrySet()) {
	    ObjectNode rowJSON = JsonNodeFactory.instance.objectNode();
	    User user = userEntry.getKey();
	    rowJSON.put(CommonConstants.ELEMENT_ID, user.getUserId());

	    ArrayNode cellJSON = JsonNodeFactory.instance.arrayNode();
	    cellJSON.add(user.getFirstName() + " " + user.getLastName());
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
		cellJSON.add(score == null ? "" : DECIMAL_FORMAT.format(score / attempts.size()));
	    }

	    rowJSON.set(CommonConstants.ELEMENT_CELL, cellJSON);
	    rowsJSON.add(rowJSON);
	}

	resultJSON.set(CommonConstants.ELEMENT_ROWS, rowsJSON);

	return resultJSON;
    }

    /**
     * Gets scores for the given Kumalive and learner, packed into jqGrid JSON format
     */
    @Override
    public ObjectNode getReportUserData(Long kumaliveId, Integer userId) {
	Kumalive kumalive = getKumalive(kumaliveId);
	List<Long> rubrics = new LinkedList<Long>();
	for (KumaliveRubric rubric : kumalive.getRubrics()) {
	    rubrics.add(rubric.getRubricId());
	}

	// mapping batch (question ID) -> rubric ID -> score
	Map<Long, Map<Long, Short>> scores = kumaliveDAO.findKumaliveScore(kumaliveId, userId).stream()
		.collect(Collectors.groupingBy(KumaliveScore::getBatch, LinkedHashMap::new,
			Collectors.toMap(score -> score.getRubric().getRubricId(), KumaliveScore::getScore)));

	ObjectNode resultJSON = JsonNodeFactory.instance.objectNode();
	resultJSON.put(CommonConstants.ELEMENT_RECORDS, scores.size());

	ArrayNode rowsJSON = JsonNodeFactory.instance.arrayNode();
	// just normal ordering of questions
	short order = 1;
	for (Entry<Long, Map<Long, Short>> batchEntry : scores.entrySet()) {
	    ObjectNode rowJSON = JsonNodeFactory.instance.objectNode();
	    rowJSON.put(CommonConstants.ELEMENT_ID, order);

	    ArrayNode cellJSON = JsonNodeFactory.instance.arrayNode();
	    cellJSON.add(order);
	    order++;
	    for (Long rubric : rubrics) {
		Short score = batchEntry.getValue().get(rubric);
		cellJSON.add(score == null ? "" : score.toString());
	    }

	    rowJSON.set(CommonConstants.ELEMENT_CELL, cellJSON);
	    rowsJSON.add(rowJSON);
	}

	resultJSON.set(CommonConstants.ELEMENT_ROWS, rowsJSON);

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
	Map<User, Map<String, Map<Long, Double>>> learnerSummaries = new TreeMap<>(USER_COMPARATOR);

	Organisation organisation = kumalives.get(0).getOrganisation();
	LinkedHashMap<String, ExcelCell[][]> dataToExport = new LinkedHashMap<String, ExcelCell[][]>();

	ExcelCell[][] kumalivesSheet = buildReportKumalivesSheet(kumalives, learnerSummaries);
	dataToExport.put(messageService.getMessage("label.kumalive.report.sheet.header",
		new Object[] { organisation.getName() }), kumalivesSheet);

	ExcelCell[][] learnersSheet = buildReportLearnersSheet(kumalives, learnerSummaries);
	dataToExport.put(messageService.getMessage("label.kumalive.report.sheet.header.learners"), learnersSheet);
	return dataToExport;
    }

    /**
     * Builds Kumalives summary sheet for the report
     */
    private ExcelCell[][] buildReportKumalivesSheet(List<Kumalive> kumalives,
	    Map<User, Map<String, Map<Long, Double>>> learnerSummaries) {
	List<ExcelCell[]> rows = new LinkedList<ExcelCell[]>();

	// iterate over Kumalives and add them to the report
	for (Kumalive kumalive : kumalives) {
	    ExcelCell[] kumaliveHeaderRow = new ExcelCell[1];
	    kumaliveHeaderRow[0] = new ExcelCell(messageService.getMessage("label.kumalive.report.name"), true);
	    rows.add(kumaliveHeaderRow);

	    ExcelCell[] kumaliveNameRow = new ExcelCell[1];
	    kumaliveNameRow[0] = new ExcelCell(kumalive.getName(), false);
	    rows.add(kumaliveNameRow);

	    // mapping user (sorted by name) -> batch (i.e. question ID) -> rubric -> score
	    TreeMap<User, Map<Long, Map<Long, Short>>> scores = kumaliveDAO
		    .findKumaliveScore(kumalive.getKumaliveId(), true).stream()
		    .collect(Collectors.groupingBy(KumaliveScore::getUser,
			    () -> new TreeMap<User, Map<Long, Map<Long, Short>>>(USER_COMPARATOR),
			    Collectors.groupingBy(KumaliveScore::getBatch, TreeMap::new, Collectors
				    .toMap(score -> score.getRubric().getRubricId(), KumaliveScore::getScore))));

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

	    ExcelCell[] userHeaderRow = new ExcelCell[5 + kumalive.getRubrics().size()];
	    userHeaderRow[0] = new ExcelCell(messageService.getMessage("label.kumalive.report.last.name"), true);
	    userHeaderRow[1] = new ExcelCell(messageService.getMessage("label.kumalive.report.first.name"), true);
	    userHeaderRow[2] = new ExcelCell(messageService.getMessage("label.kumalive.report.login"), true);
	    userHeaderRow[3] = new ExcelCell(messageService.getMessage("label.kumalive.report.attempt"), true);
	    // iterate over rubrics and make them columns
	    int userRowLength = 4;
	    for (KumaliveRubric rubric : kumalive.getRubrics()) {
		userHeaderRow[userRowLength++] = new ExcelCell(rubric.getName(), true);
	    }
	    userHeaderRow[userRowLength] = new ExcelCell(messageService.getMessage("label.kumalive.report.time"), true);
	    rows.add(userHeaderRow);

	    for (Entry<User, Map<Long, Map<Long, Short>>> learnerEntry : scores.entrySet()) {
		// learner details and average mark
		User learner = learnerEntry.getKey();
		ExcelCell[] userRow = new ExcelCell[userRowLength + 1];
		userRow[0] = new ExcelCell(learner.getFirstName(), false);
		userRow[1] = new ExcelCell(learner.getLastName(), false);
		userRow[2] = new ExcelCell(learner.getLogin(), false);
		userRow[3] = new ExcelCell(messageService.getMessage("label.kumalive.report.average"), false);
		rows.add(userRow);

		// build rows for each attempt (answer to a question, batch)
		Short[][] resultsByRubric = new Short[learnerEntry.getValue().size()][userRowLength - 4];
		int attempt = 0;
		Long[] rubricIds = new Long[kumalive.getRubrics().size()];
		for (Entry<Long, Map<Long, Short>> batchEntry : scores.get(learner).entrySet()) {
		    ExcelCell[] attemptRow = new ExcelCell[userRowLength + 1];
		    attemptRow[3] = new ExcelCell(
			    messageService.getMessage("label.kumalive.report.attempt") + " " + (attempt + 1), false);
		    int rubricIndex = 0;
		    Map<Long, Short> results = batchEntry.getValue();
		    for (KumaliveRubric rubric : kumalive.getRubrics()) {
			rubricIds[rubricIndex] = rubric.getRubricId();
			Short result = results.get(rubric.getRubricId());
			attemptRow[rubricIndex + 4] = new ExcelCell(result == null ? null : result.intValue(), false);
			// store mark to calculate average later
			resultsByRubric[attempt][rubricIndex] = result;
			rubricIndex++;
		    }
		    attempt++;
		    Date attemptDate = new Date(batchEntry.getKey() * 1000);
		    attemptRow[userRowLength] = new ExcelCell(
			    FileUtil.EXPORT_TO_SPREADSHEET_TITLE_DATE_FORMAT.format(attemptDate), false);
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
			double average = Double.valueOf(DECIMAL_FORMAT.format(score / count));
			userRow[rubricIndex + 4] = new ExcelCell(average, false);
			// populate data for learners sheet
			Map<String, Map<Long, Double>> learnerSummary = learnerSummaries.get(learner);
			if (learnerSummary == null) {
			    learnerSummary = new HashMap<String, Map<Long, Double>>();
			    learnerSummaries.put(learner, learnerSummary);
			}
			Map<Long, Double> learnerKumaliveSummary = learnerSummary.get(kumalive.getName());
			if (learnerKumaliveSummary == null) {
			    learnerKumaliveSummary = new HashMap<Long, Double>();
			    learnerSummary.put(kumalive.getName(), learnerKumaliveSummary);
			}
			learnerKumaliveSummary.put(rubricIds[rubricIndex], average);
		    }
		}
	    }
	    rows.add(EMPTY_ROW);
	}

	return rows.toArray(new ExcelCell[][] {});
    }

    /**
     * Builds Kumalives summary sheet for the report
     */
    private ExcelCell[][] buildReportLearnersSheet(List<Kumalive> kumalives,
	    Map<User, Map<String, Map<Long, Double>>> learnerSummaries) {
	List<ExcelCell[]> rows = new LinkedList<ExcelCell[]>();
	Map<String, Integer> kumaliveNamePosition = new HashMap<String, Integer>();
	List<ExcelCell> userHeaderRow = new LinkedList<ExcelCell>();
	userHeaderRow.add(new ExcelCell(messageService.getMessage("label.kumalive.report.last.name"), true));
	userHeaderRow.add(new ExcelCell(messageService.getMessage("label.kumalive.report.first.name"), true));
	userHeaderRow.add(new ExcelCell(messageService.getMessage("label.kumalive.report.login"), true));
	// count cells for kumalives and their rubrics
	int userRowLength = 3;
	for (Kumalive kumalive : kumalives) {
	    kumaliveNamePosition.put(kumalive.getName(), userRowLength);
	    // use border only on first cell in kumalive
	    boolean border = true;
	    for (KumaliveRubric rubric : kumalive.getRubrics()) {
		userHeaderRow.add(new ExcelCell(rubric.getName(), true, border ? 1 : 0));
		border = false;
		userRowLength++;
	    }
	}

	// now that we know how long is the whole user row, we can build kumalive names row and add it first
	ExcelCell[] kumaliveNameRow = new ExcelCell[userRowLength];
	for (Entry<String, Integer> kumaliveNameEntry : kumaliveNamePosition.entrySet()) {
	    kumaliveNameRow[kumaliveNameEntry.getValue()] = new ExcelCell(kumaliveNameEntry.getKey(), true, 1);
	}
	rows.add(kumaliveNameRow);
	rows.add(userHeaderRow.toArray(EMPTY_ROW));

	for (Entry<User, Map<String, Map<Long, Double>>> learnerSummary : learnerSummaries.entrySet()) {
	    ExcelCell[] userRow = new ExcelCell[userRowLength];
	    User learner = learnerSummary.getKey();
	    userRow[0] = new ExcelCell(learner.getFirstName(), false);
	    userRow[1] = new ExcelCell(learner.getLastName(), false);
	    userRow[2] = new ExcelCell(learner.getLogin(), false);
	    for (Kumalive kumalive : kumalives) {
		Map<Long, Double> learnerKumaliveSummary = learnerSummary.getValue().get(kumalive.getName());
		if (learnerKumaliveSummary == null) {
		    continue;
		}
		int position = kumaliveNamePosition.get(kumalive.getName());
		boolean border = true;
		for (KumaliveRubric rubric : kumalive.getRubrics()) {
		    Double average = learnerKumaliveSummary.get(rubric.getRubricId());
		    if (average != null) {
			userRow[position] = new ExcelCell(average, false, border ? 1 : 0);
		    }
		    border = false;
		    position++;
		}
	    }
	    rows.add(userRow);
	}

	return rows.toArray(new ExcelCell[][] {});
    }

    @Override
    public KumalivePoll getPollByKumaliveId(Long kumaliveId) {
	return kumaliveDAO.findPollByKumaliveId(kumaliveId);
    }

    /**
     * Creates a poll
     */
    @Override
    public KumalivePoll startPoll(Long kumaliveId, String name, ArrayNode answersJSON) {
	Kumalive kumalive = getKumalive(kumaliveId);
	if (kumalive == null) {
	    return null;
	}
	KumalivePoll poll = new KumalivePoll(kumalive, name);
	kumaliveDAO.insert(poll);

	Set<KumalivePollAnswer> answers = new LinkedHashSet<>();
	for (Short answerIndex = 0; answerIndex < answersJSON.size(); answerIndex++) {
	    String answerName = answersJSON.get(answerIndex.intValue()).asText();
	    KumalivePollAnswer answer = new KumalivePollAnswer(poll, answerIndex, answerName);
	    kumaliveDAO.insert(answer);
	    answers.add(answer);
	}
	poll.setAnswers(answers);
	kumaliveDAO.update(poll);

	if (logger.isDebugEnabled()) {
	    logger.debug("Teacher started poll " + poll.getPollId());
	}

	return poll;
    }

    /**
     * Store a user's vote for a poll
     */
    @Override
    public void saveVote(Long answerId, Integer userId) {
	KumalivePollAnswer answer = (KumalivePollAnswer) kumaliveDAO.find(KumalivePollAnswer.class, answerId);
	if (answer.getVotes().containsKey(userId)) {
	    logger.warn("Learner " + userId + " tried to vote for answer ID " + answerId + " but he already voted");
	    return;
	}
	answer.getVotes().put(userId, new Date());
	kumaliveDAO.update(answer);

	if (logger.isDebugEnabled()) {
	    logger.debug("Learner " + userId + " voted for answer ID " + answerId);
	}
    }

    /**
     * Set whether learners can see vote results and voters' names
     */
    @Override
    public void releasePollResults(Long pollId, boolean votesReleased, boolean votersReleased) {
	KumalivePoll poll = (KumalivePoll) kumaliveDAO.find(KumalivePoll.class, pollId);
	poll.setVotesReleased(votesReleased || votersReleased);
	poll.setVotersReleased(votersReleased);
	kumaliveDAO.update(poll);
    }

    /**
     * Finishes a poll, i.e. prevents learners from voting
     */
    @Override
    public void finishPoll(Long pollId) {
	KumalivePoll poll = (KumalivePoll) kumaliveDAO.find(KumalivePoll.class, pollId);
	if (poll.getFinishDate() != null) {
	    logger.warn("Trying to finish poll " + pollId + " which is already finished");
	    return;
	}
	poll.setFinishDate(new Date());
	kumaliveDAO.update(poll);

	if (logger.isDebugEnabled()) {
	    logger.debug("Teacher finished poll " + poll.getPollId());
	}
    }

    /**
     * Logs an activity in the given Kumalive
     */
    @Override
    public void log(Long kumaliveId, Integer userId, Date date, short type) {
	KumaliveLog log = new KumaliveLog(kumaliveId, userId, date, type);
	kumaliveDAO.insert(log);
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