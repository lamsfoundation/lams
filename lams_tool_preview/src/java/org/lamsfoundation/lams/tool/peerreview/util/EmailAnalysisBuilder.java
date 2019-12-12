package org.lamsfoundation.lams.tool.peerreview.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.rating.dto.StyledCriteriaRatingDTO;
import org.lamsfoundation.lams.rating.dto.StyledRatingDTO;
import org.lamsfoundation.lams.rating.model.Rating;
import org.lamsfoundation.lams.rating.model.RatingCriteria;
import org.lamsfoundation.lams.rating.service.IRatingService;
import org.lamsfoundation.lams.tool.peerreview.PeerreviewConstants;
import org.lamsfoundation.lams.tool.peerreview.dao.PeerreviewSessionDAO;
import org.lamsfoundation.lams.tool.peerreview.dao.PeerreviewUserDAO;
import org.lamsfoundation.lams.tool.peerreview.model.Peerreview;
import org.lamsfoundation.lams.tool.peerreview.model.PeerreviewSession;
import org.lamsfoundation.lams.tool.peerreview.model.PeerreviewUser;
import org.lamsfoundation.lams.tool.peerreview.service.IPeerreviewService;
import org.lamsfoundation.lams.tool.peerreview.util.EmailAnalysisBuilder.LearnerData.SingleCriteriaData;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.NumberUtil;
import org.springframework.web.util.HtmlUtils;

/**
 * Creates the Self and Peer Assessment email, comparing the learner's self assessment against their peers' assessment
 * of their contribution.
 */
public class EmailAnalysisBuilder {

    private static Logger log = Logger.getLogger(EmailAnalysisBuilder.class.getName());

    private Peerreview peerreview;
    private PeerreviewSession session;
    private IRatingService ratingService;
    private List<RatingCriteria> criterias;
    private PeerreviewUserDAO peerreviewUserDao;
    private IPeerreviewService service;
    private MessageService messageService;

    // same across the whole lesson 
    private String htmlCriteriaTableStart;
    private String htmlCriteriaTableEnd;
    private String htmlOverallResultsTable;
    // htmlORTHighlightedCellReplacementText used to highlight the correct cell in the final table. This allows the table to be generated once for the session and then
    // updated for each user
    private String htmlORTHighlightedCellReplacementText = "%%%%REPLACESTYLEWITHHIGHLIGHT%%%%"; 
    
    // Criterias to report on in the top table. Does not include comments. Always use this for the criteria table so that the order is the same and the data matches the heading!
    private List<RatingCriteria> criteriaForCriteriaTable = new ArrayList<RatingCriteria>();
    private Map<Long, LearnerData> learnerDataMap = new TreeMap<Long, LearnerData>(); // lams user id, learner data
    private Map<Long, Double> groupAveragePerCriteria = new HashMap<Long, Double>(); // key activity id, session wide average of the learner's averages (peerRatingIncSelf)
    private Double averageOfAverages; // Average of individualCriteriaAverage across all activities
    private int countNonCommentCriteria = 0;

    // cache some labels as they are used repeatedly
    private String selfRatingString;
    private String peerRatingString;

    // HTML Styles
    private static final String width100pc = "width:100%;";
    private static final String tableBorderedStyle = "border-collapse:collapse;border-width:1px;border-style:solid;border-color:#777;";
    private static final String centeredStyle = "text-align:center;";
    private static final String headerBackgroundStyle = "background:#B9CDE5;";
    private static final String highlightBackgroundStyle = "background:#D7E4BD;";
    private static final String borderBelow = "border-bottom-width:1px;border-bottom-style:solid;border-bottom-color:#777;";
    private static final String bold = "font-weight:bold";
    private static final String zebraEvenRow = "border-width:1px;border-style:solid;border-color:#777;background:#FCD5B5;";
    private static final String zebraOddRow = "border-width:1px;border-style:solid;border-color:#777;background:#B9CDE5;";

    public EmailAnalysisBuilder(Peerreview peerreview, PeerreviewSession session, IRatingService ratingService,
	    PeerreviewSessionDAO peerreviewSessionDao, PeerreviewUserDAO peerreviewUserDao, IPeerreviewService service,
	    MessageService messageService) {
	this.peerreview = peerreview;
	this.session = session;
	this.ratingService = ratingService;
	this.peerreviewUserDao = peerreviewUserDao;
	this.service = service;
	this.messageService = messageService;

	criterias = ratingService.getCriteriasByToolContentId(peerreview.getContentId());
	preGenerateBoilerplate(); 
	selfRatingString = getLocalisedMessage("email.self.rating");
	peerRatingString = getLocalisedMessage("email.peers.rating");

    }

    public Map<Long, String> generateHTMLEmailsForSession() {

	HashMap<Long, String> emailMap = new HashMap<Long, String>();
	generateTeamData();
	for (Map.Entry<Long, LearnerData> entry : learnerDataMap.entrySet()) {
	    emailMap.put(entry.getKey(), buildEmail(entry.getKey()));
	}
	return emailMap;

    }

    public String generateHTMLEMailForLearner(Long userId) {
	generateTeamData();
	return buildEmail(userId);
    }

    private String buildEmail(Long userId) {

	processComments(userId);

	StringBuilder htmlText; // new for each user
	htmlText = new StringBuilder();
	htmlText.append("<body>\n");

	LearnerData learnerData = learnerDataMap.get(userId);
	generateCriteriaTable(htmlText, learnerData);
	htmlText.append("<p>&nbsp;</p>\n");
	generateFeedbackComments(htmlText, learnerData);
	htmlText.append("<p>&nbsp;</p>\n");
	generateSelfPeerTable(htmlText, learnerData);
	htmlText.append("<p>&nbsp;</p>\n");
	generateSAPASPAExplanation(htmlText, learnerData.getSPAFactor(), learnerData.getSAPAFactor());

	htmlText.append("<p>").append(getLocalisedMessage("email.send.automatically")).append("</p>\n");
	htmlText.append("</body>\n");
	return htmlText.toString();
    }

    public class LearnerData {

	public class SingleCriteriaData {
	    Double selfRating;
	    Double peerRatingExcSelf;
	    Double peerRatingIncSelf;

	    SingleCriteriaData(Double selfRating, Double peerRatingExcSelf, Double peerRatingIncSelf) {
		this.selfRating = selfRating;
		this.peerRatingExcSelf = peerRatingExcSelf;
		this.peerRatingIncSelf = peerRatingIncSelf;
	    }

	}

	public String name;
	public Map<Long, SingleCriteriaData> criteriaDataMap = new HashMap<Long, SingleCriteriaData>(); // Criteria id as key
	public Double individualCriteriaAverage; // average of peerRatingIncSelf across all activities
	public List<StyledCriteriaRatingDTO> commentDTOs = new ArrayList<StyledCriteriaRatingDTO>();
	private Double spa = null;
	private Double sapa = null;

	LearnerData(String name) {
	    this.name = name;
	}

	protected void addCriteraDataRow(Long criteraId, Double selfRating,
		Double peerRatingExcSelf, Double peerRatingIncSelf) {
	    criteriaDataMap.put(criteraId,
		    new SingleCriteriaData(selfRating, peerRatingExcSelf, peerRatingIncSelf));
	}

	// must not be called until data analysis calculations are done - needs averageOfAverages
	protected Double getSPAFactor() {
	    if (spa == null) {
		spa = (averageOfAverages > 0 && individualCriteriaAverage != null) ? individualCriteriaAverage / averageOfAverages : 0D;
	    }
	    return spa;
	}

	protected Double getSAPAFactor() {
	    if (sapa == null) {
		double sumSelfRatings = 0d;
		double sumPeerRatings = 0d;
		for (SingleCriteriaData criteriaData : criteriaDataMap.values()) {
		    sumSelfRatings += criteriaData.selfRating;
		    sumPeerRatings += criteriaData.peerRatingExcSelf;
		}
		sapa = sumPeerRatings > 0 ? Math.sqrt(sumSelfRatings / sumPeerRatings) : 0d;
	    }
	    return sapa;
	}

	protected Double getCriteriaComparison(Long ratingCriteriaId) {
	    Double groupAverage = groupAveragePerCriteria.get(ratingCriteriaId);
	    SingleCriteriaData data = criteriaDataMap.get(ratingCriteriaId);
	    if (data != null && groupAverage != null && groupAverage > 0) {
		return data.peerRatingIncSelf / groupAverage;
	    } else {
		return 0d;
	    }
	}
    }

    private String roundTo2Places(Double d) {
	if (d == null || Double.isNaN(d))
	    return "0";

	BigDecimal bd = new BigDecimal(d);
	bd = bd.setScale(2, RoundingMode.HALF_UP);

	return NumberUtil.formatLocalisedNumberForceDecimalPlaces(bd.doubleValue(), (Locale) null, 2);
    }

    /* Only needs to be called once for the session - works out all the basic data for the whole team */
    private void generateTeamData() {

	List<PeerreviewUser> users = peerreviewUserDao.getBySessionID(session.getSessionId());
	for (PeerreviewUser user : users) {
	    learnerDataMap.put(user.getUserId(),
		    new LearnerData(StringEscapeUtils.escapeCsv(user.getFirstName() + " " + user.getLastName())));
	}

	Map<Long, Double> criteriaMarkSumMap = new HashMap<Long, Double>();
	Map<Long, Integer> criteriaMarkCountMap = new HashMap<Long, Integer>();
	double averageOfAveragesSum = 0d;
	int averageOfAveragesCount = 0;

	// Process all the criterias and build up the summary data for each rated user. Store in temporary map.
	HashMap<Long, HashMap<Long, SummingData>> tally = processRawRatingData();

	// Now calculate the averages / self values and store ready for output
	for ( Map.Entry<Long, HashMap<Long, SummingData>> itemEntry : tally.entrySet()) {
	    Long itemId = itemEntry.getKey();
	    HashMap<Long, SummingData> itemMap = itemEntry.getValue();
	    Double userMarkSum = 0D;
	    LearnerData learnerData = learnerDataMap.get(itemId);

	    for ( Map.Entry<Long, SummingData> criteriaEntry : itemMap.entrySet() ) {

		Long criteriaId = criteriaEntry.getKey();
		SummingData sd = criteriaEntry.getValue();

		double peerRatingIncSelf = sd.numRatingsIncSelf > 0 ? sd.peerRatingIncSelf / sd.numRatingsIncSelf : 0;
		double peerRatingExcSelf = sd.numRatingsExcSelf > 0 ? sd.peerRatingExcSelf / sd.numRatingsExcSelf : 0;
		double selfRating = sd.selfRating;
		learnerData.addCriteraDataRow(criteriaId, selfRating, peerRatingExcSelf, peerRatingIncSelf);

		Double criteriaMarkSum = criteriaMarkSumMap.get(criteriaId);
		if (criteriaMarkSum == null) {
		    criteriaMarkSumMap.put(criteriaId, peerRatingIncSelf);
		} else {
		    criteriaMarkSumMap.put(criteriaId, criteriaMarkSum + peerRatingIncSelf);
		}

		Integer criteriaMarkCount = criteriaMarkCountMap.get(criteriaId);
		if (criteriaMarkCount == null) {
		    criteriaMarkCountMap.put(criteriaId, 1);
		} else {
		    criteriaMarkCountMap.put(criteriaId, criteriaMarkCount + 1);
		}
		userMarkSum += peerRatingIncSelf;
	    }

	    double individualCriteriaAverage = countNonCommentCriteria > 0 ? userMarkSum / countNonCommentCriteria : 0D;
	    learnerData.individualCriteriaAverage = individualCriteriaAverage;
	    averageOfAveragesSum += individualCriteriaAverage;
	    averageOfAveragesCount++;

	}

	// calculate the group averages for each Criteria, then the average of all the group averages
	for (Map.Entry<Long, Double> entry : criteriaMarkSumMap.entrySet()) {
	    Integer markCount = criteriaMarkCountMap.get(entry.getKey());
	    Double groupAvg = entry.getValue() / markCount;
	    groupAveragePerCriteria.put(entry.getKey(), groupAvg);
	}
	averageOfAverages = averageOfAveragesCount > 0 ? averageOfAveragesSum / averageOfAveragesCount : 0D;

    }

    class SummingData {
 	float selfRating = 0f;
 	float peerRatingExcSelf = 0f;
 	int numRatingsExcSelf = 0;
 	float peerRatingIncSelf = 0f;
 	int numRatingsIncSelf = 0;
     }

    /** 
     * tally: hashmap<item id, hashmap<criteria id, summingData>>
     * tallying done in Java as the code to do three calcs in one SQL is complex (and hence risks errors)
     * and hopefully we are only dealing with small numbers of users in each session. The code that gets the existing data
     * does a lot of processing that is not needed for the SPA/SAPA calculations so getting the raw data
     * avoids that processing.
     */
    @SuppressWarnings("unchecked")
    private HashMap<Long, HashMap<Long, SummingData>> processRawRatingData() {
	Collection<Long> criteriaIds = new ArrayList<Long>();
	for (RatingCriteria criteria : criteriaForCriteriaTable) {
	    criteriaIds.add(criteria.getRatingCriteriaId());
	}
	HashMap<Long, HashMap<Long, SummingData>> tally = new HashMap<Long, HashMap<Long, SummingData>>();
	if (criteriaIds.size() > 0) {
	    List<Object> rawRatingsForSession = ratingService.getRatingsByCriteriasAndItems(criteriaIds,
		    learnerDataMap.keySet());
	    for (Object obj : rawRatingsForSession) {
		Rating rating = (Rating) obj;
		SummingData sd = null;
		Long itemId = rating.getItemId();
		HashMap<Long, SummingData> itemMap = tally.get(itemId);
		if (itemMap == null) {
		    itemMap = new HashMap<Long, SummingData>();
		    sd = new SummingData();
		    itemMap.put(rating.getRatingCriteria().getRatingCriteriaId(), sd);
		    tally.put(itemId, itemMap);
		} else {
		    sd = itemMap.get(rating.getRatingCriteria().getRatingCriteriaId());
		    if (sd == null) {
			sd = new SummingData();
			itemMap.put(rating.getRatingCriteria().getRatingCriteriaId(), sd);
		    }
		}
		Float newRating = rating.getRating() != null ? rating.getRating() : 0.0f;
		sd.peerRatingIncSelf += newRating;
		sd.numRatingsIncSelf++;
		if (rating.getItemId().longValue() == rating.getLearner().getUserId().longValue()) {
		    sd.selfRating = newRating;
		} else {
		    sd.peerRatingExcSelf += newRating;
		    sd.numRatingsExcSelf++;
		}
	    }
	}
	return tally;
    }

    private void processComments(Long userId) {
	LearnerData learnerData = learnerDataMap.get(userId);
	for (RatingCriteria criteria : criterias) {
	    int sorting = (criteria.isStarStyleRating() || criteria.isHedgeStyleRating())
		    ? PeerreviewConstants.SORT_BY_AVERAGE_RESULT_DESC
		    : PeerreviewConstants.SORT_BY_AVERAGE_RESULT_ASC;

	    StyledCriteriaRatingDTO dto = service.getUsersRatingsCommentsByCriteriaIdDTO(peerreview.getContentId(),
		    session.getSessionId(), criteria, userId.longValue(), false, sorting, null, true, false);

	    if (criteria.isCommentRating() || criteria.isCommentsEnabled()) {
		learnerData.commentDTOs.add(dto);
	    }
	}
    }

    private void generateCriteriaTable(StringBuilder htmlText, LearnerData learnerData) {
	htmlText.append(htmlCriteriaTableStart).append("<tr><td style=\"").append(tableBorderedStyle).append("\">")
		.append(learnerData.name).append("</td>");
	for (RatingCriteria criteria : criteriaForCriteriaTable) {
	    String criteriaComparison = roundTo2Places(
		    learnerData.getCriteriaComparison(criteria.getRatingCriteriaId()));
	    htmlText.append("<td style=\"").append(tableBorderedStyle).append(centeredStyle).append("\">")
		    .append(criteriaComparison).append("</td>");
	}
	htmlText.append("</tr>\n");
	htmlText.append(htmlCriteriaTableEnd);

    }

    private void generateSelfPeerTable(StringBuilder htmlText, LearnerData learnerData) {
	boolean spaDone = false;
	boolean evenRow = false;
	String spaFactor = roundTo2Places(learnerData.getSPAFactor());
	String sapaFactor = roundTo2Places(learnerData.getSAPAFactor());
	htmlText.append("<table style=\"border-collapse:collapse;").append(width100pc).append("\">");
	for (RatingCriteria criteria : criteriaForCriteriaTable) {
	    SingleCriteriaData criteriaData = learnerData.criteriaDataMap.get(criteria.getRatingCriteriaId());
	    if (criteriaData != null) {
		String selfRating = roundTo2Places(criteriaData.selfRating);
		String peerRatingExcSelf = roundTo2Places(criteriaData.peerRatingExcSelf);
		htmlText.append("<tr><td rowspan=\"3\" style=\"width:25%;").append(evenRow ? zebraEvenRow : zebraOddRow)
			.append("\"><span style=\"").append(bold).append("\">").append(criteria.getTitle())
			.append("</span></td><td style=\"width:20%;").append(evenRow ? zebraEvenRow : zebraOddRow)
			.append("\"><span style=\"").append(bold).append("\">").append(selfRatingString)
			.append("</span></td><td style=\"").append(evenRow ? zebraEvenRow : zebraOddRow).append("\">")
			.append(selfRating).append("</td>");
		if (!spaDone) {
		    htmlText.append("<td>&nbsp;</td><td style=\"width:20%;")
			    .append(evenRow ? zebraEvenRow : zebraOddRow).append("\"><span style=\"").append(bold)
			    .append("\">").append(getLocalisedMessage("email.SPA.factor"))
			    .append("</span></td><td style=\"width:20%;").append(evenRow ? zebraEvenRow : zebraOddRow)
			    .append("\"><span style=\"").append(bold).append("\">").append(spaFactor)
			    .append("</span></td>");
		}
		htmlText.append("</tr>\n<tr><td style=\"").append(evenRow ? zebraEvenRow : zebraOddRow)
			.append("\"><span style=\"").append(bold).append("\">").append(peerRatingString)
			.append("</span></td><td style=\"").append(evenRow ? zebraEvenRow : zebraOddRow).append("\">")
			.append(peerRatingExcSelf).append("</td>");
		if (!spaDone) {
		    htmlText.append("<td>&nbsp;</td><td style=\"width:20%;")
			    .append(evenRow ? zebraEvenRow : zebraOddRow).append("\"><span style=\"").append(bold)
			    .append("\">").append(getLocalisedMessage("email.SAPA.factor"))
			    .append("</span></td><td style=\"").append(evenRow ? zebraEvenRow : zebraOddRow)
			    .append("\"><span style=\"").append(bold).append("\">").append(sapaFactor)
			    .append("</span></td>");
		    spaDone = true;
		}
		htmlText.append("</tr>\n<tr><td style=\"width:20%;").append(evenRow ? zebraEvenRow : zebraOddRow)
			.append("\"><span style=\"").append(bold).append("\">").append("Self & Peers' Rating")
			.append("</span></td><td style=\"").append(evenRow ? zebraEvenRow : zebraOddRow).append("\">")
			.append(roundTo2Places(criteriaData.peerRatingIncSelf)).append("</td>");
		htmlText.append("</tr>\n");
		evenRow = !evenRow;
	    } else {
		log.warn("Unable to report on criteria " + criteria
			+ " as there is no data in the processed data tables. Peer review session "
			+ session.getSessionId());
	    }
	}
	htmlText.append("</table>\n");
    }

    private void generateFeedbackComments(StringBuilder htmlText, LearnerData learnerData) {
	if (learnerData.commentDTOs.size() > 0) {
	    htmlText.append("<div style=\"").append(bold).append("\">")
		    .append(getLocalisedMessage("email.label.feedback", new String[] { learnerData.name }))
		    .append("</div>\n");
	    htmlText.append("<table style=\"").append(width100pc).append("\">");
	    boolean showCommentTitle = learnerData.commentDTOs.size() > 1;
	    for (StyledCriteriaRatingDTO dto : learnerData.commentDTOs) {
		generateCommentEntry(htmlText, dto, showCommentTitle);
	    }
	    htmlText.append("</table>\n");
	}
    }

    private void generateCommentEntry(StringBuilder htmlText, StyledCriteriaRatingDTO dto, boolean showCommentTitle) {

	int rowCount = 1;
	String escapedTitle = HtmlUtils.htmlEscape(dto.getRatingCriteria().getTitle());
	if (dto.getRatingCriteria().isCommentRating() || dto.getRatingCriteria().isCommentsEnabled()) {
	    if (dto.getRatingDtos().size() < 1) {
		htmlText.append("<tr><td style=\"width:25%\"><span style=\"").append(bold).append("\">")
			.append(escapedTitle).append("</span></td><td>")
			.append(getLocalisedMessage("event.sent.results.no.results")).append("</td></tr>");
	    } else {
		for (StyledRatingDTO ratingDto : dto.getRatingDtos()) {
		    if (ratingDto.getComment() != null) {
			String escapedComment = HtmlUtils.htmlEscape(ratingDto.getComment());
			escapedComment = StringUtils.replace(escapedComment, "&lt;BR&gt;", "<BR>");
			htmlText.append("<tr>");
			if (showCommentTitle) {
			    htmlText.append("<td style=\"width:25%;vertical-align:top;\">");
			    if (rowCount == 1) {
				htmlText.append("<span style=\"").append(bold).append("\">").append(escapedTitle)
					.append("</span>");
			    }
			    htmlText.append("</td>");
			}
			htmlText.append("<td style=\"").append(borderBelow).append("\">").append(escapedComment)
				.append("</td></tr>\n");
			rowCount++;
		    }
		}
	    }
	}
    }

    private void generateSAPASPAExplanation(StringBuilder htmlText, double rawSPA, double rawSAPA) {

	// The SPA/SAPA comparisons are based on values rounded to 1 decimal place. So a value of 0.98 or 1.02 is considered 1, 
	// while 1.06 is considered > 1.
	double spa = new BigDecimal(rawSPA).setScale(1, RoundingMode.HALF_UP).doubleValue();
	double sapa = new BigDecimal(rawSAPA).setScale(1, RoundingMode.HALF_UP).doubleValue();
		
	int cellToHighlight;
	if (sapa < 1.0) {
	    if (spa < 1.0)
		cellToHighlight = 1;
	    else if (spa > 1.0)
		cellToHighlight = 3;
	    else
		cellToHighlight = 2;
	} else if (sapa > 1.0) {
	    if (spa < 1.0)
		cellToHighlight = 7;
	    else if (spa > 1.0)
		cellToHighlight = 9;
	    else
		cellToHighlight = 8;
	} else {
	    if (spa < 1.0)
		cellToHighlight = 4;
	    else if (spa > 1.0)
		cellToHighlight = 6;
	    else
		cellToHighlight = 5;
	}
	
	String customisedtableTextToModify = htmlOverallResultsTable.replace(htmlORTHighlightedCellReplacementText+cellToHighlight, highlightBackgroundStyle);
	for ( int i=1; i<10; i++ ) {
	    if ( i != cellToHighlight )
		customisedtableTextToModify = customisedtableTextToModify.replaceAll(htmlORTHighlightedCellReplacementText+i, "");
	}

	htmlText.append(customisedtableTextToModify);
    }
    
    // only needs to be run once for a session 
    private void preGenerateBoilerplate() {

	int numCol = 1;
	for (RatingCriteria criteria : criterias) {
	    if (!criteria.isCommentRating()) {
		numCol++;
	    }
	}
	int colWidth = 100 / numCol;

	StringBuilder tempBuffer = new StringBuilder("<table style=\"").append(tableBorderedStyle).append(width100pc)
		.append("\">").append("<tr style=\"").append(tableBorderedStyle).append("\"><td style=\"")
		.append(tableBorderedStyle).append(headerBackgroundStyle).append("\"><span style=\"").append(bold)
		.append("\">").append(getLocalisedMessage("label.learner")).append("</span></td>");

	for (RatingCriteria criteria : criterias) {
	    if (!criteria.isCommentRating()) {
		tempBuffer.append("<td style=\"width:").append(colWidth).append("%;").append(tableBorderedStyle)
			.append(headerBackgroundStyle).append(centeredStyle).append("\"><span style=\"").append(bold)
			.append("\">").append(criteria.getTitle()).append("</span></td>");
		countNonCommentCriteria++;
		criteriaForCriteriaTable.add(criteria);
	    }
	}
	tempBuffer.append("</tr>\n");
	htmlCriteriaTableStart = tempBuffer.toString();

	htmlCriteriaTableEnd = "</table>\n";

	// SPA / SAPA comparison results table
	tempBuffer = new StringBuilder();
	
	tempBuffer.append("<table style=\"").append(tableBorderedStyle).append("\">\n");
	tempBuffer.append("<tr style=\"").append(tableBorderedStyle).append("\"><td style=\"").append(tableBorderedStyle)
		.append(headerBackgroundStyle).append(centeredStyle).append("\">&nbsp;</td><td style=\"")
		.append(tableBorderedStyle).append(headerBackgroundStyle).append(centeredStyle).append("\">")
		.append(getLocalisedMessage("email.explanation.SPA.less.one")).append("</td><td style=\"")
		.append(tableBorderedStyle).append(headerBackgroundStyle).append(centeredStyle).append("\">")
		.append(getLocalisedMessage("email.explanation.SPA.one")).append("</td><td style=\"")
		.append(tableBorderedStyle).append(headerBackgroundStyle).append(centeredStyle).append("\">")
		.append(getLocalisedMessage("email.explanation.SPA.greater.one")).append("</td>");

	tempBuffer.append("</tr>\n<tr style=\"").append(tableBorderedStyle).append("\"><td style=\"")
		.append(tableBorderedStyle).append(headerBackgroundStyle).append(centeredStyle).append("\">")
		.append(getLocalisedMessage("email.explanation.SAPA.less.one")).append("</td>");
	addExplanationCell(tempBuffer, 1, "email.explanation.1");
	addExplanationCell(tempBuffer, 2, "email.explanation.2");
	addExplanationCell(tempBuffer, 3, "email.explanation.3");

	tempBuffer.append("</tr>\n<tr style=\"").append(tableBorderedStyle).append("\"><td style=\"")
		.append(tableBorderedStyle).append(headerBackgroundStyle).append(centeredStyle).append("\">")
		.append(getLocalisedMessage("email.explanation.SAPA.one")).append("</td>");
	addExplanationCell(tempBuffer, 4, "email.explanation.4");
	addExplanationCell(tempBuffer, 5, "email.explanation.5");
	addExplanationCell(tempBuffer, 6, "email.explanation.6");

	tempBuffer.append("</tr>\n<tr style=\"").append(tableBorderedStyle).append("\"><td style=\"")
		.append(tableBorderedStyle).append(headerBackgroundStyle).append(centeredStyle).append("\">")
		.append(getLocalisedMessage("email.explanation.SAPA.greater.one")).append("</td>");
	addExplanationCell(tempBuffer, 7, "email.explanation.7");
	addExplanationCell(tempBuffer, 8, "email.explanation.8");
	addExplanationCell(tempBuffer, 9, "email.explanation.9");

	tempBuffer.append("</tr>\n</table>\n");
	htmlOverallResultsTable = tempBuffer.toString();
    }

    private void addExplanationCell(StringBuilder htmlText, int cellNumber, String messageKey) {
	htmlText.append("<td style=\"").append(tableBorderedStyle).append(centeredStyle)
		.append(htmlORTHighlightedCellReplacementText).append(cellNumber).append("\"><font color=\"red\">")
		.append(cellNumber).append(". </font>").append(getLocalisedMessage(messageKey)).append("</td>");
    }

    private String getLocalisedMessage(String key, Object[] args) {
	return messageService.getMessage(key, args);
    }

    private String getLocalisedMessage(String key) {
	return messageService.getMessage(key);
    }

}
