<% 
 /**
  * Rating.tag
  *	Author: Andrey Balan
  *	Description: Shows rating stars widget
  */
 %>
<%@ tag body-content="scriptless" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-function" prefix="fn" %>
<c:set var="lams"><lams:LAMSURL/></c:set>

<%@ attribute name="itemRatingDto" required="true" rtexprvalue="true" type="org.lamsfoundation.lams.rating.dto.ItemRatingDTO" %>

<%-- Optional attribute --%>
<%@ attribute name="disabled" required="false" rtexprvalue="true" %>
<%@ attribute name="isItemAuthoredByUser" required="false" rtexprvalue="true" %>
<%@ attribute name="maxRates" required="false" rtexprvalue="true" %>
<%@ attribute name="minRates" required="false" rtexprvalue="true" %>
<%@ attribute name="countRatedItems" required="false" rtexprvalue="true" %>
<%@ attribute name="yourRatingLabel" required="false" rtexprvalue="true" %>
<%@ attribute name="averageRatingLabel" required="false" rtexprvalue="true" %>
<%@ attribute name="warnCommentIsBlankLabel" required="false" rtexprvalue="true" %>
<%@ attribute name="minNumberWordsLabel" required="false" rtexprvalue="true" %>
<%@ attribute name="warnMinNumberWordsLabel" required="false" rtexprvalue="true" %>

<%-- Default value for message key --%>
<c:if test="${empty disabled}">
	<c:set var="disabled" value="false" scope="request"/>
</c:if>
<c:if test="${empty isItemAuthoredByUser}">
	<c:set var="isItemAuthoredByUser" value="false" scope="request"/>
</c:if>
<c:if test="${empty maxRates}">
	<c:set var="maxRates" value="0" scope="request"/>
</c:if>
<c:if test="${empty minRates}">
	<c:set var="minRates" value="0" scope="request"/>
</c:if>
<c:if test="${empty countRatedItems}">
	<c:set var="countRatedItems" value="0" scope="request"/>
</c:if>
<c:if test="${empty yourRatingLabel}">
	<c:set var="yourRatingLabel" value="label.your.rating" scope="request"/>
</c:if>
<c:if test="${empty averageRatingLabel}">
	<c:set var="averageRatingLabel" value="label.average.rating" scope="request"/>
</c:if>
<c:if test="${empty warnCommentIsBlankLabel}">
	<c:set var="warnCommentIsBlankLabel" value="warning.comment.blank" scope="request"/>
</c:if>
<c:if test="${empty minNumberWordsLabel}">
	<c:set var="minNumberWordsLabel" value="label.comment.minimum.number.words" scope="request"/>
</c:if>
<c:if test="${empty warnMinNumberWordsLabel}">
	<c:set var="warnMinNumberWordsLabel" value="warning.minimum.number.words" scope="request"/>
</c:if>

<c:set var="isCommentsEnabled" value="${itemRatingDto.commentsEnabled}"/>

<script type="text/javascript">
	//var for jquery.jRating.js
	var pathToImageFolder = "${lams}images/css/";
	
	//vars for rating.js
	var MAX_RATES = ${maxRates};
	var MIN_RATES = ${minRates};
	var COMMENTS_MIN_WORDS_LIMIT = ${itemRatingDto.commentsMinWordsLimit};
	var LAMS_URL = '${lams}';
	var COUNT_RATED_ITEMS = ${countRatedItems};
	var COMMENT_TEXTAREA_TIP_LABEL = '<fmt:message key="label.comment.textarea.tip"/>';
	var WARN_COMMENTS_IS_BLANK_LABEL = '<fmt:message key="${warnCommentIsBlankLabel}"/>';
	var WARN_MIN_NUMBER_WORDS_LABEL = '<fmt:message key="${warnMinNumberWordsLabel}"><fmt:param value="${itemRatingDto.commentsMinWordsLimit}"/></fmt:message>';
</script>

<%--Rating stars area---------------------------------------%>

<div class="extra-controls-inner">
<div class="rating-stars-holder">
	<c:forEach var="criteriaDto" items="${itemRatingDto.criteriaDtos}" varStatus="status">
		<c:set var="objectId" value="${criteriaDto.ratingCriteria.ratingCriteriaId}-${itemRatingDto.itemId}"/>
		<c:set var="isCriteriaNotRatedByUser" value='${criteriaDto.userRating == ""}'/>
	
		<c:choose>
			<c:when test='${disabled || isItemAuthoredByUser || (maxRates > 0) && (countRatedItems >= maxRates) || !isCriteriaNotRatedByUser}'>
				<c:set var="ratingStarsClass" value="rating-stars-disabled"/>
			</c:when>
			<c:otherwise>
				<c:set var="ratingStarsClass" value="rating-stars"/>
			</c:otherwise>
		</c:choose>
			
		<h4>
			${criteriaDto.ratingCriteria.title}
		</h4>
			
		<c:choose>
			<c:when test='${isItemAuthoredByUser || not isCriteriaNotRatedByUser}'>
				<c:set var="ratingDataAverage" value="${criteriaDto.averageRating}"/>
			</c:when>
			<c:otherwise>
				<c:set var="ratingDataAverage" value="0"/>
			</c:otherwise>
		</c:choose>
		
		<div class="${ratingStarsClass} rating-stars-new" data-average="${ratingDataAverage}" data-id="${objectId}">
		</div>
			
		<c:choose>
			<c:when test="${isItemAuthoredByUser}">
				<div class="rating-stars-caption">
					<fmt:message key="${averageRatingLabel}" >
						<fmt:param>
							<fmt:formatNumber value="${criteriaDto.averageRating}" type="number" maxFractionDigits="1" />
						</fmt:param>
						<fmt:param>
							${criteriaDto.numberOfVotes}
						</fmt:param>
					</fmt:message>
				</div>
			</c:when>
				
			<c:otherwise>
				<div class="rating-stars-caption" id="rating-stars-caption-${objectId}"
					<c:if test="${isCriteriaNotRatedByUser}">style="visibility: hidden;"</c:if>
				>
					<fmt:message key="${yourRatingLabel}" >
						<fmt:param>
							<span id="user-rating-${objectId}">
								<fmt:formatNumber value="${criteriaDto.userRating}" type="number" maxFractionDigits="1" />
							</span>
						</fmt:param>			
						<fmt:param>
							<span id="average-rating-${objectId}">
								<fmt:formatNumber value="${criteriaDto.averageRating}" type="number" maxFractionDigits="1" />
							</span>
						</fmt:param>
						<fmt:param>
							<span id="number-of-votes-${objectId}">${criteriaDto.numberOfVotes}</span>
						</fmt:param>
					</fmt:message>
				</div>
				
			</c:otherwise>
		</c:choose>
			
	</c:forEach>

</div>

</div>

<%--Comments area---------------------------------------%>
<c:if test="${isCommentsEnabled && not empty itemRatingDto.commentDtos}">
	<div id="comments-area-${itemRatingDto.itemId}">
	
		<c:set var="userId"><lams:user property="userID" /></c:set>
		<c:forEach var="comment" items="${itemRatingDto.commentDtos}">
			<c:if test="${comment.userId == userId}">
				<c:set var="commentLeftByUser" value="${comment}"/>
			</c:if>
		</c:forEach>
		
		<c:choose>
			<c:when test='${isItemAuthoredByUser}'>
				<c:forEach var="comment" items="${itemRatingDto.commentDtos}">
					<div class="rating-comment">
						<c:out value="${comment.comment}" escapeXml="false" />
					</div>
				</c:forEach>
			</c:when>
			
			<c:when test='${not empty commentLeftByUser}'>
				<div class="rating-comment">
					<c:out value="${commentLeftByUser.comment}" escapeXml="false" />
				</div>
			</c:when>
			
			<c:when test='${not disabled}'>
				<div id="add-comment-area-${itemRatingDto.itemId}">
			
					<!-- Rating limits info -->
					<c:if test="${itemRatingDto.commentsMinWordsLimit ne 0}">
					
						<div class="info rating-info">
							<fmt:message key="${minNumberWordsLabel}">
								: <fmt:param value="${itemRatingDto.commentsMinWordsLimit}"/>
							</fmt:message>
						</div>
					</c:if>		
				
					<textarea name="comment" rows="2" id="comment-textarea-${itemRatingDto.itemId}" onfocus="if(this.value==this.defaultValue)this.value='';" onblur="if(this.value=='')this.value=this.defaultValue;">
						<fmt:message key="label.comment.textarea.tip"/>
					</textarea>
				
					<div class="button add-comment add-comment-new" data-item-id="${itemRatingDto.itemId}" data-comment-criteria-id="${itemRatingDto.commentsCriteriaId}">
					</div>			
				</div>			
			</c:when>
		</c:choose>
			
	</div>	
</c:if>