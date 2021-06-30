package org.lamsfoundation.lams.tool.peerreview.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.lamsfoundation.lams.rating.dto.ItemRatingCriteriaDTO;
import org.lamsfoundation.lams.rating.dto.ItemRatingDTO;
import org.lamsfoundation.lams.rating.model.Rating;
import org.lamsfoundation.lams.rating.model.RatingCriteria;
import org.lamsfoundation.lams.rating.service.IRatingService;
import org.lamsfoundation.lams.tool.peerreview.dao.PeerreviewSessionDAO;
import org.lamsfoundation.lams.tool.peerreview.dao.PeerreviewUserDAO;
import org.lamsfoundation.lams.tool.peerreview.model.Peerreview;
import org.lamsfoundation.lams.tool.peerreview.model.PeerreviewSession;
import org.lamsfoundation.lams.tool.peerreview.model.PeerreviewUser;
import org.lamsfoundation.lams.tool.peerreview.service.IPeerreviewService;
import org.lamsfoundation.lams.util.excel.ExcelCell;
import org.lamsfoundation.lams.util.excel.ExcelRow;
import org.lamsfoundation.lams.util.excel.ExcelSheet;
import org.springframework.util.StringUtils;

/**
 * Creates a Spreadsheet that reports the averages, with each team/group shown on a separate worksheet.
 * Calculates averages for each rating and for each user. The SPA factor then calculates the user's overall average
 * against the team average.
 * Has a empty Mark column so that the teacher can add a manual mark if they want.
 */
public class SpreadsheetBuilder {
    private Peerreview peerreview;
    private IRatingService ratingService;
    private List<RatingCriteria> criterias;
    private PeerreviewSessionDAO peerreviewSessionDao;
    private PeerreviewUserDAO peerreviewUserDao;
    private IPeerreviewService service;

    private List<ExcelSheet> sheets;

    public SpreadsheetBuilder(Peerreview peerreview, IRatingService ratingService,
	    PeerreviewSessionDAO peerreviewSessionDao, PeerreviewUserDAO peerreviewUserDao,
	    IPeerreviewService service) {
	this.peerreview = peerreview;
	this.ratingService = ratingService;
	this.peerreviewSessionDao = peerreviewSessionDao;
	this.peerreviewUserDao = peerreviewUserDao;
	this.service = service;
    }

    public List<ExcelSheet> generateTeamReport() {
	// this one is guaranteed to give a consistent order over peerreview.getRatingCriterias()
	criterias = ratingService.getCriteriasByToolContentId(peerreview.getContentId());
	sheets = new LinkedList<>();

	List<PeerreviewSession> sessions = peerreviewSessionDao.getByContentId(peerreview.getContentId());
	for (PeerreviewSession session : sessions) {
	    generateTeamSheet(session);
	}

	return sheets;
    }

    @SuppressWarnings("unchecked")
    private void generateTeamSheet(PeerreviewSession session) {
	ExcelSheet sessionSheet = new ExcelSheet(session.getSessionName());
	sheets.add(sessionSheet);

	//Title row
	ExcelRow titleRow = sessionSheet.initRow();
	titleRow.addCell(service.getLocalisedMessage("label.learner", null), true, ExcelCell.BORDER_STYLE_BOTTOM_THIN);
	Map<Long, Integer> criteriaIndexMap = new HashMap<>();
	int countNonCommentCriteria = 0;
	Integer previousRatingCriteriaGroupId = null;

	for (RatingCriteria criteria : criterias) {
	    if (!criteria.isCommentRating()) {
		// for each rubrics we separate its rows with a left border
		Integer ratingCriteriaGroupId = criteria.getRatingCriteriaGroupId();
		int[] borders = null;
		if (ratingCriteriaGroupId == null ? previousRatingCriteriaGroupId != null
			: !ratingCriteriaGroupId.equals(previousRatingCriteriaGroupId)) {
		    borders = new int[] { ExcelCell.BORDER_STYLE_BOTTOM_THIN, ExcelCell.BORDER_STYLE_LEFT_THIN };
		} else {
		    borders = new int[] { ExcelCell.BORDER_STYLE_BOTTOM_THIN };
		}
		previousRatingCriteriaGroupId = ratingCriteriaGroupId;
		titleRow.addCell(criteria.getTitle(), true, borders);

		criteriaIndexMap.put(criteria.getRatingCriteriaId(), countNonCommentCriteria);
		countNonCommentCriteria++;
	    }
	}
	titleRow.addCell(service.getLocalisedMessage("label.average", null), true, ExcelCell.BORDER_STYLE_BOTTOM_THIN,
		ExcelCell.BORDER_STYLE_LEFT_THIN);
	if (peerreview.isSelfReview()) {
	    titleRow.addCell(service.getLocalisedMessage("label.sa", null), true, ExcelCell.BORDER_STYLE_BOTTOM_THIN);
	}
	titleRow.addCell(service.getLocalisedMessage("label.pa", null), true, ExcelCell.BORDER_STYLE_BOTTOM_THIN);
	titleRow.addCell(service.getLocalisedMessage("label.spa.factor", null), true,
		ExcelCell.BORDER_STYLE_BOTTOM_THIN);
	if (peerreview.isSelfReview()) {
	    titleRow.addCell(service.getLocalisedMessage("label.sapa.factor", null), true,
		    ExcelCell.BORDER_STYLE_BOTTOM_THIN);
	}
	titleRow.addCell(service.getLocalisedMessage("label.total.group.mark", null), true,
		ExcelCell.BORDER_STYLE_BOTTOM_THIN);
	titleRow.addCell(service.getLocalisedMessage("label.individual.mark", null), true,
		ExcelCell.BORDER_STYLE_BOTTOM_THIN);

	List<PeerreviewUser> users = peerreviewUserDao.getBySessionID(session.getSessionId());
	Map<Long, String> userNames = new TreeMap<>();
	for (PeerreviewUser user : users) {
	    userNames.put(user.getUserId(),
		    StringEscapeUtils.escapeCsv(user.getFirstName() + " " + user.getLastName()));
	}

	ExcelRow numberOfTeamsRow = sessionSheet.initRow();
	numberOfTeamsRow.addCell(service.getLocalisedMessage("label.number.of.team.members", null), true);
	numberOfTeamsRow.addCell(users.size(), IndexedColors.YELLOW);
	sessionSheet.addEmptyRow();

	// uses same index as the user row, so allow for the name in the first column
	Double[] criteriaMarkSum = new Double[countNonCommentCriteria + 1];
	Integer[] criteriaMarkCount = new Integer[countNonCommentCriteria + 1];
	for (int i = 0; i < criteriaMarkSum.length - 1; i++) {
	    criteriaMarkSum[i] = 0D;
	    criteriaMarkCount[i] = 0;
	}

	Map<Long, ExcelRow> userRowMap = new HashMap<>();
	// Process all the criterias and build up rows for each rated user. Store in temporary map.
	List<ItemRatingDTO> ratingDtos = service.getRatingCriteriaDtos(session.getPeerreview().getContentId(),
		session.getSessionId(), userNames.keySet(), true, -1L);
	for (ItemRatingDTO ratingDto : ratingDtos) {
	    Double userMarkSum = 0D;
	    double[] userRowData = new double[countNonCommentCriteria];
	    for (ItemRatingCriteriaDTO itemRatingCriteriaDTO : ratingDto.getCriteriaDtos()) {
		if (itemRatingCriteriaDTO.getAverageRatingAsNumber() != null
			&& !itemRatingCriteriaDTO.getAverageRatingAsNumber().equals(0)) {
		    Integer criteriaIndex = criteriaIndexMap
			    .get(itemRatingCriteriaDTO.getRatingCriteria().getRatingCriteriaId());
		    double db = itemRatingCriteriaDTO.getAverageRatingAsNumber().doubleValue();
		    userRowData[criteriaIndex] = roundTo2Places(db);
		    criteriaMarkSum[criteriaIndex] += db;
		    criteriaMarkCount[criteriaIndex] = criteriaMarkCount[criteriaIndex] + 1;
		    userMarkSum += db;
		}
	    }

	    ExcelRow userRow = new ExcelRow();
	    Long userId = ratingDto.getItemId();

	    userRow.addCell(userNames.get(userId));
	    for (double userRowDataIter : userRowData) {
		userRow.addCell(userRowDataIter);
	    }
	    userRow.addCell(countNonCommentCriteria > 0 ? roundTo2Places(userMarkSum / countNonCommentCriteria) : 0D,
		    true);
	    userRowMap.put(userId, userRow);
	}

	// calculate the group averages
	ExcelRow avgRow = new ExcelRow();
	avgRow.addCell(service.getLocalisedMessage("label.average", null), true);
	Double averageMarkSum = 0D;
	for (int i = 0; i < criteriaMarkSum.length - 1; i++) {
	    if (criteriaMarkCount[i] > 0) {
		Double d = criteriaMarkSum[i] / criteriaMarkCount[i];
		avgRow.addCell(roundTo2Places(d), true);
		averageMarkSum += d;
	    } else {
		avgRow.addEmptyCell();
	    }
	}
	Double finalGroupAverage = countNonCommentCriteria > 0
		? roundTo2Places(averageMarkSum / countNonCommentCriteria)
		: 0D;
	avgRow.addCell(finalGroupAverage, true);

	// the map is: itemId (who was rated) -> userId (who rated) -> rating from all categories
	Map<Long, Map<Long, Set<Rating>>> ratings = ratingService
		.getRatingsByCriteriasAndItems(criteriaIndexMap.keySet(), userNames.keySet()).stream()
		.filter(rating -> rating.getRating() != null)
		.collect(Collectors.groupingBy(Rating::getItemId, Collectors
			.groupingBy(rating -> rating.getLearner().getUserId().longValue(), Collectors.toSet())));

	// Combine rated rows with rows with users not yet rated, to make up complete list, and write out to rowList.
	for (PeerreviewUser user : users) {
	    ExcelRow userRow = userRowMap.get(user.getUserId());
	    if (userRow == null) {
		userRow = sessionSheet.initRow();
		userRow.addCell(userNames.get(user.getUserId()));

	    } else {
		Double learnerAverage = (Double) userRow.getCell(userRow.getCells().size() - 1);
		Double spa = countNonCommentCriteria > 0 ? roundTo2Places(learnerAverage / finalGroupAverage) : 0D;
		Double sa = null;
		Double pa = null;
		Double sapa = null;

		if (ratings.containsKey(user.getUserId())) {
		    // calculate SAPA factor
		    double sumSelfRatings = 0;
		    double sumPeerRatings = 0;
		    int selfRatingCriteriaCount = 0;
		    int peerRatingCriteriaCount = 0;
		    int peerRatingCount = 0;

		    for (Entry<Long, Set<Rating>> ratingEntry : ratings.get(user.getUserId()).entrySet()) {
			double sumRatingFromAllCategories = ratingEntry.getValue().stream()
				.collect(Collectors.summingDouble(Rating::getRating));
			int countRatingFromAllCategories = ratingEntry.getValue().size();
			if (ratingEntry.getKey().equals(user.getUserId())) {
			    sumSelfRatings = sumRatingFromAllCategories;
			    selfRatingCriteriaCount = countRatingFromAllCategories;
			} else {
			    sumPeerRatings += sumRatingFromAllCategories;
			    peerRatingCriteriaCount += countRatingFromAllCategories;
			    peerRatingCount++;
			}
		    }

		    pa = peerRatingCriteriaCount == 0 ? null : roundTo2Places(sumPeerRatings / peerRatingCriteriaCount);
		    if (peerreview.isSelfReview()) {
			sa = selfRatingCriteriaCount == 0 ? null
				: roundTo2Places(sumSelfRatings / selfRatingCriteriaCount);
			sapa = sumPeerRatings > 0
				? roundTo2Places(Math.sqrt(sumSelfRatings / (sumPeerRatings / peerRatingCount)))
				: 0;
		    }
		}

		if (peerreview.isSelfReview()) {
		    userRow.addCell(sa, true);
		}
		userRow.addCell(pa, true);

		userRow.addCell(spa, true);

		if (peerreview.isSelfReview()) {
		    userRow.addCell(sapa, true);
		}

		userRow.addCell("", IndexedColors.YELLOW);
		userRow.addCell("", IndexedColors.GREEN);

		sessionSheet.addRow(userRow);
	    }
	}

	// Learners marks done, write out the group average
	sessionSheet.addRow(avgRow);

	// now do all the comments
	for (RatingCriteria criteria : criterias) {
	    if (criteria.isCommentsEnabled()) {
		sessionSheet.addEmptyRow();
		sessionSheet.addEmptyRow();
		ExcelRow criteriaTitleRow = sessionSheet.initRow();
		criteriaTitleRow.addCell(criteria.getTitle(), true);

		if (criteria.isHedgeStyleRating()) {
		    // just need the first entry as it is the same for everyone - the justification
		    if (users.size() > 0) {
			generateUsersComments(session, sessionSheet, userNames, criteria, users.get(0), false);
		    }
		} else {
		    for (PeerreviewUser user : users) {
			generateUsersComments(session, sessionSheet, userNames, criteria, user, true);
		    }
		}
	    }
	}
    }

    private void generateUsersComments(PeerreviewSession session, ExcelSheet sessionSheet, Map<Long, String> userNames,
	    RatingCriteria criteria, PeerreviewUser user, boolean showForName) {
	sessionSheet.addEmptyRow();

	List<Object[]> comments = peerreviewUserDao.getDetailedRatingsComments(session.getPeerreview().getContentId(),
		session.getSessionId(), criteria.getRatingCriteriaId(), user.getUserId());
	boolean isUserNameRowPrinted = false;
	for (Object[] comment : comments) {
	    if (comment[1] != null) {
		if (showForName && !isUserNameRowPrinted) {
		    ExcelRow userNameRow = sessionSheet.initRow();
		    userNameRow.addCell(service.getLocalisedMessage("label.for.user",
			    new Object[] { userNames.get(user.getUserId()) }));
		    isUserNameRowPrinted = true;
		}

		ExcelRow commentRow = sessionSheet.initRow();
		commentRow.addCell(userNames.get(((BigInteger) comment[0]).longValue()));
		commentRow.addCell(StringUtils.replace((String) comment[1], "<BR>", "\n"));
	    }
	}
    }

    private double roundTo2Places(double d) {
	if (Double.isNaN(d)) {
	    return 0D;
	}

	BigDecimal bd = new BigDecimal(d);
	bd = bd.setScale(2, RoundingMode.HALF_UP);
	return bd.doubleValue();
    }
}
