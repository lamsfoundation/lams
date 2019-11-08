package org.lamsfoundation.lams.tool.peerreview.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.lamsfoundation.lams.rating.dto.ItemRatingCriteriaDTO;
import org.lamsfoundation.lams.rating.dto.ItemRatingDTO;
import org.lamsfoundation.lams.rating.model.RatingCriteria;
import org.lamsfoundation.lams.rating.service.IRatingService;
import org.lamsfoundation.lams.tool.peerreview.dao.PeerreviewSessionDAO;
import org.lamsfoundation.lams.tool.peerreview.dao.PeerreviewUserDAO;
import org.lamsfoundation.lams.tool.peerreview.model.Peerreview;
import org.lamsfoundation.lams.tool.peerreview.model.PeerreviewSession;
import org.lamsfoundation.lams.tool.peerreview.model.PeerreviewUser;
import org.lamsfoundation.lams.tool.peerreview.service.IPeerreviewService;
import org.lamsfoundation.lams.util.excel.ExcelCell;
import org.springframework.util.StringUtils;

/** Creates a Spreadsheet that reports the averages, with each team/group shown on a separate worksheet.
 * Calculates averages for each rating and for each user. The SPA factor then calculates the user's overall average 
 * against the team average. 
 * Has a empty Mark column so that the teacher can add a manual mark if they want. 
 */
public class SpreadsheetBuilder {

    private static final ExcelCell[] EMPTY_ROW = new ExcelCell[4];

    private Peerreview peerreview;
    private IRatingService ratingService;
    private List<RatingCriteria> criterias;
    private PeerreviewSessionDAO peerreviewSessionDao;
    private PeerreviewUserDAO peerreviewUserDao;
    private IPeerreviewService service;
    
    private LinkedHashMap<String, ExcelCell[][]> dataToExport;
    
    // processing details set up when header is created, then used for each session
    private ExcelCell[] titleRow;
    private int countNonCommentCriteria = 0;
    private int fullColumnCount = 0;
    private int averageColumnIndex = 0;
    private Map<Long, Integer> criteriaOffset;

    
    public SpreadsheetBuilder(Peerreview peerreview, IRatingService ratingService, PeerreviewSessionDAO peerreviewSessionDao, 
	    PeerreviewUserDAO peerreviewUserDao, IPeerreviewService service) {
	this.peerreview = peerreview;
	this.ratingService = ratingService;
	this.peerreviewSessionDao = peerreviewSessionDao;
	this.peerreviewUserDao = peerreviewUserDao;
	this.service = service;
    }

    public LinkedHashMap<String, ExcelCell[][]> generateTeamReport() {

	// this one is guaranteed to give a consistent order over peerreview.getRatingCriterias()
	criterias = ratingService.getCriteriasByToolContentId(peerreview.getContentId());

	dataToExport = new LinkedHashMap<String, ExcelCell[][]>();

	titleRow = generateWorksheetHeading(criterias);

	List<PeerreviewSession> sessions = peerreviewSessionDao.getByContentId(peerreview.getContentId());
	for (PeerreviewSession session : sessions) {
	    List<ExcelCell[]> rowList = generateTeamWorksheet(session);
	    ExcelCell[][] worksheetData = rowList.toArray(new ExcelCell[][] {});
	    dataToExport.put(session.getSessionName(), worksheetData);
	}
	
	
	return dataToExport;
    }
    
    private List<ExcelCell[]> generateTeamWorksheet(PeerreviewSession session) {

	List<ExcelCell[]> rowList = new LinkedList<ExcelCell[]>();
	rowList.add(titleRow);
	
	List<PeerreviewUser> users = peerreviewUserDao.getBySessionID(session.getSessionId());
	Map<Long, String> userNames = new TreeMap<Long, String>();
	for (PeerreviewUser user : users) {
	    userNames.put(user.getUserId(), StringEscapeUtils.escapeCsv(user.getFirstName() + " " + user.getLastName()));
	}
	
	Map<Long, ExcelCell[]> userRowMap = new HashMap<Long, ExcelCell[]>();

	ExcelCell[] userRow = new ExcelCell[2];
	userRow[0] = new ExcelCell(service.getLocalisedMessage("label.number.of.team.members", null), true); 
	userRow[1] = new ExcelCell(users.size(), IndexedColors.YELLOW); 
	rowList.add(userRow);

	rowList.add(EMPTY_ROW);

	// uses same index as the user row, so allow for the name in the first column
	Double[] criteriaMarkSum = new Double[countNonCommentCriteria+1]; 
	Integer[] criteriaMarkCount = new Integer[countNonCommentCriteria+1];
	for (int index = 1; index < criteriaMarkSum.length; index++) {
	    criteriaMarkSum[index] = 0D;
	    criteriaMarkCount[index] = 0;
	}

	// Process all the criterias and build up rows for each rated user. Store in temporary map.
	List<ItemRatingDTO> ratingDtos = service.getRatingCriteriaDtos(session.getPeerreview().getContentId(), 
		session.getSessionId(), userNames.keySet(), true, -1L);
	for (ItemRatingDTO ratingDto : ratingDtos) {
	    Double userMarkSum = 0D;
	    userRow = new ExcelCell[fullColumnCount];

	    for (ItemRatingCriteriaDTO cDto : ratingDto.getCriteriaDtos()) {
		if (cDto.getAverageRatingAsNumber() != null && ! cDto.getAverageRatingAsNumber().equals(0)) {
		    Integer criteriaIndex = criteriaOffset.get(cDto.getRatingCriteria().getRatingCriteriaId());
		    double db = cDto.getAverageRatingAsNumber().doubleValue();
		    userRow[criteriaIndex] = new ExcelCell(roundTo2Places(db), false);
		    criteriaMarkSum[criteriaIndex] += db;
		    criteriaMarkCount[criteriaIndex] = criteriaMarkCount[criteriaIndex] + 1;
		    userMarkSum += db;
		}
	    }
	    
	    userRow[averageColumnIndex] = new ExcelCell(countNonCommentCriteria > 0 ? roundTo2Places(userMarkSum / countNonCommentCriteria) : 0D, true);
	    userRowMap.put(ratingDto.getItemId(), userRow);
	}

	// calculate the group averages
	ExcelCell[] avgRow = new ExcelCell[fullColumnCount];
	avgRow[0] = new ExcelCell(service.getLocalisedMessage("label.average", null), true);
	Double averageMarkSum = 0D;
	for (int index = 1; index < criteriaMarkSum.length; index++) { 
	    if ( criteriaMarkCount[index] > 0 ) {
		Double d =  criteriaMarkSum[index] / criteriaMarkCount[index];
		avgRow[index] = new ExcelCell(roundTo2Places(d), true);
		averageMarkSum += d;
	    } 
	}
	Double finalGroupAverage = countNonCommentCriteria > 0 ? roundTo2Places(averageMarkSum / countNonCommentCriteria) : 0D;
	avgRow[averageColumnIndex] = new ExcelCell(finalGroupAverage, true);
	
	// Combine rated rows with rows with users not yet rated, to make up complete list, and write out to rowList.
	for (PeerreviewUser user : users) {
	    userRow = userRowMap.get(user.getUserId());
	    if (userRow == null) {
		userRow = new ExcelCell[fullColumnCount];
	    } else {
		ExcelCell averageCell = userRow[averageColumnIndex];
		Double learnerAverage = (Double) averageCell.getCellValue();
		Double spa = countNonCommentCriteria > 0 ? roundTo2Places(learnerAverage / finalGroupAverage): 0D;
		userRow[averageColumnIndex+1] =  new ExcelCell(spa, true);
		userRow[averageColumnIndex+2] = new ExcelCell("", IndexedColors.YELLOW); 
		userRow[averageColumnIndex+3] = new ExcelCell("", IndexedColors.GREEN);
	    }
	    userRow[0] = new ExcelCell(userNames.get(user.getUserId()), false);
	    rowList.add(userRow);
	}

	// Learners marks done, write out the group average
	rowList.add(avgRow);
	
	// now do all the comments 
	for ( RatingCriteria criteria : criterias ) {
	    if ( criteria.isCommentsEnabled() ) {
		rowList.add(EMPTY_ROW);
		rowList.add(EMPTY_ROW);
		userRow = new ExcelCell[1];
		userRow[0] = new ExcelCell(criteria.getTitle(), true); 
		rowList.add(userRow);
		
		if ( criteria.isHedgeStyleRating() ) {
		    // just need the first entry as it is the same for everyone - the justification
		    if ( users.size() > 0 ) {
			generateUsersComments(session, rowList, userNames, criteria, users.get(0), false);
		    }
		} else {
		    for ( PeerreviewUser user : users ) {
			generateUsersComments(session, rowList, userNames, criteria, user, true);
		    }
		}
	    }
	} 
	    
	return rowList;
    }

    private void generateUsersComments(PeerreviewSession session, List<ExcelCell[]> rowList,
	    Map<Long, String> userNames, RatingCriteria criteria, PeerreviewUser user, boolean showForName) {

	List<ExcelCell[]> commentRowList = new LinkedList<ExcelCell[]>();
	List<Object[]> comments = peerreviewUserDao.getDetailedRatingsComments(session.getPeerreview().getContentId(),
		session.getSessionId(), criteria.getRatingCriteriaId(), user.getUserId());
	for (Object[] comment : comments) {
	    if (comment[1] != null) {
		ExcelCell[] commentRow = new ExcelCell[2];
		commentRow[0] = new ExcelCell(userNames.get(((BigInteger) comment[0]).longValue()), false);
		commentRow[1] = new ExcelCell(StringUtils.replace((String) comment[1], "<BR>", "\n"), false);
		commentRowList.add(commentRow);
	    }
	}

	if (commentRowList.size() > 0) {

	    rowList.add(EMPTY_ROW);

	    if (showForName) {
		ExcelCell[] userRow = new ExcelCell[1];
		userRow[0] = new ExcelCell(service.getLocalisedMessage("label.for.user",
			new Object[] { userNames.get(user.getUserId()) }), false);
		rowList.add(userRow);
	    }

	    rowList.addAll(commentRowList);
	}

    }
    
    private double roundTo2Places(double d) {
	if ( Double.isNaN(d) )
	    return 0D;
	
	BigDecimal bd = new BigDecimal(d);
	bd = bd.setScale(2, RoundingMode.HALF_UP);
	return bd.doubleValue();
    }
    
    private ExcelCell[] generateWorksheetHeading(List<RatingCriteria> criterias) {
	ExcelCell[] titleRow = new ExcelCell[criterias.size() + 5];
	titleRow[0] = new ExcelCell(service.getLocalisedMessage("label.learner", null), true, ExcelCell.BORDER_STYLE_BOTTOM_THIN); 
	int index = 1;

	criteriaOffset = new HashMap<Long, Integer>();
	
	for ( RatingCriteria criteria : criterias ) {
	    if ( ! criteria.isCommentRating() ) {
		titleRow[index] = new ExcelCell(criteria.getTitle(), true, ExcelCell.BORDER_STYLE_BOTTOM_THIN); 
		criteriaOffset.put(criteria.getRatingCriteriaId(),  index);
		countNonCommentCriteria++;
		index++;
	    }
	}
	averageColumnIndex = index;
	    
	titleRow[index++] = new ExcelCell(service.getLocalisedMessage("label.average", null), true, ExcelCell.BORDER_STYLE_BOTTOM_THIN); 
	titleRow[index++] = new ExcelCell(service.getLocalisedMessage("label.spa.factor", null), true, ExcelCell.BORDER_STYLE_BOTTOM_THIN); 
	titleRow[index++] = new ExcelCell(service.getLocalisedMessage("label.total.group.mark", null), true, ExcelCell.BORDER_STYLE_BOTTOM_THIN);
	titleRow[index++] = new ExcelCell(service.getLocalisedMessage("label.individual.mark", null), true, ExcelCell.BORDER_STYLE_BOTTOM_THIN); 
	fullColumnCount = index;

	return titleRow;
    }
}
