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
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %> 
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-function" prefix="fn" %>
<c:set var="lams"><lams:LAMSURL/></c:set>

<%@ attribute name="itemRatingDto" required="true" rtexprvalue="true" type="org.lamsfoundation.lams.rating.dto.ItemRatingDTO" %>

<%-- Optional attribute --%>
<%@ attribute name="disabled" required="false" rtexprvalue="true" %>
<%@ attribute name="isItemAuthoredByUser" required="false" rtexprvalue="true" %>
<%@ attribute name="maxRates" required="false" rtexprvalue="true" %>
<%@ attribute name="countRatedItems" required="false" rtexprvalue="true" %>
<%@ attribute name="yourRatingLabel" required="false" rtexprvalue="true" %>
<%@ attribute name="averageRatingLabel" required="false" rtexprvalue="true" %>
<%@ attribute name="minNumberWordsLabel" required="false" rtexprvalue="true" %>
<%@ attribute name="starsRateLabel" required="false" rtexprvalue="true" %>
<%@ attribute name="postCommentButtonLabel" required="false" rtexprvalue="true" %>
<%@ attribute name="hideCriteriaTitle" required="false" rtexprvalue="true" %>
<%@ attribute name="showComments" required="false" rtexprvalue="true" %>
<%@ attribute name="showAllComments" required="false" rtexprvalue="true" %>
<%@ attribute name="allowRetries" required="false" rtexprvalue="true" %>
<%-- ID of HTML element where to scroll after refresh after comment was submitted --%>
<%@ attribute name="refreshOnComment" required="false" rtexprvalue="true" %>

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
<c:if test="${empty countRatedItems}">
	<c:set var="countRatedItems" value="0" scope="request"/>
</c:if>
<c:if test="${empty yourRatingLabel}">
	<c:set var="yourRatingLabel" value="label.your.rating" scope="request"/>
</c:if>
<c:if test="${empty averageRatingLabel}">
	<c:set var="averageRatingLabel" value="label.average.rating" scope="request"/>
</c:if>
<c:if test="${empty minNumberWordsLabel}">
	<c:set var="minNumberWordsLabel" value="label.comment.minimum.number.words" scope="request"/>
</c:if>
<c:if test="${empty starsRateLabel}">
	<c:set var="starsRateLabel" value="label.stars.rate" scope="request"/>
</c:if>
<c:if test="${empty postCommentButtonLabel}">
	<c:set var="postCommentButtonLabel" value="button.post.comment" scope="request"/>
</c:if>
<c:if test="${empty hideCriteriaName}">
	<c:set var="hideCriteriaTitle" value="false" scope="request"/>
</c:if>
<c:if test="${empty showAllComments}">
	<c:set var="showAllComments" value="false" scope="request"/>
</c:if>
<c:if test="${empty showComments}">
	<c:set var="showComments" value="true" scope="request"/>
</c:if>
<c:set var="isCommentsEnabled" value="${itemRatingDto.commentsEnabled && showComments}"/>
<c:if test="${empty refreshOnComment}">
	<c:set var="refreshOnComment" value="" scope="request"/>
</c:if>

<c:if test="${isCommentsEnabled}">
	<c:set var="userId"><lams:user property="userID" /></c:set>
	<c:forEach var="comment" items="${itemRatingDto.commentDtos}">
		<c:if test="${comment.userId == userId}">
			<c:set var="commentLeftByUser" value="${comment}"/>
		</c:if>
	</c:forEach>
</c:if>

<c:if test="${empty allowRetries}">
	<c:set var="allowRetries" value="false" scope="request"/>
</c:if>

<%--Rating stars area---------------------------------------%>

<div class="extra-controls-inner">
<div class="rating-stars-holder text-center center-block">

	<c:set var="hasStartedRating" value="false"/>
	<c:forEach var="criteriaDto" items="${itemRatingDto.criteriaDtos}">
		<c:set var="hasStartedRating" value='${hasStartedRating || criteriaDto.userRating != ""}'/>
	</c:forEach>
	<c:set var="hasStartedRating" value='${hasStartedRating || not empty commentLeftByUser}'/>
	
	<c:forEach var="criteriaDto" items="${itemRatingDto.criteriaDtos}" varStatus="status">
		<c:set var="objectId" value="${criteriaDto.ratingCriteria.ratingCriteriaId}-${itemRatingDto.itemId}"/>
		<c:set var="isCriteriaNotRatedByUser" value='${criteriaDto.userRating == ""}'/>
	
		<c:choose>
			<c:when test='${disabled || isItemAuthoredByUser || ((maxRates > 0) && (countRatedItems >= maxRates)  && !hasStartedRating) || !(isCriteriaNotRatedByUser || allowRetries)}'>
				<c:set var="ratingStarsClass" value="rating-stars-disabled"/>
			</c:when>
			<c:otherwise>
				<c:set var="ratingStarsClass" value="rating-stars"/>
			</c:otherwise>
		</c:choose>
			
		<c:if test="${not hideCriteriaTitle}">
			<div class="text-muted fw-bold">
				${criteriaDto.ratingCriteria.title}
			</div>
		</c:if>
			
		<c:choose>
			<c:when test='${isItemAuthoredByUser || not isCriteriaNotRatedByUser}'>
				<c:set var="ratingDataAverage" value="${criteriaDto.averageRating}"/>
			</c:when>
			<c:otherwise>
				<c:set var="ratingDataAverage" value="0"/>
			</c:otherwise>
		</c:choose>
		
		<div class="${ratingStarsClass} rating-stars-new" data-average="${ratingDataAverage}" data-id="${objectId}" role="button" aria-label="<fmt:message key="${starsRateLabel}"/>">
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
<c:if test="${isCommentsEnabled}">
	<div id="comments-area-${itemRatingDto.itemId}">
		<c:choose>
			<c:when test='${isItemAuthoredByUser or (showAllComments and not empty commentLeftByUser)}'>
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
			
			<c:when test='${not ( disabled || (maxRates > 0) && (countRatedItems >= maxRates) && !hasStartedRating )}'>
				<div id="add-comment-area-${itemRatingDto.itemId}">
			
					<!-- Comment min words limit -->
					<c:if test="${itemRatingDto.commentsMinWordsLimit ne 0}">
					
						<lams:Alert type="info" id="comment-limit-${itemRatingDto.itemId}" close="false">
							<fmt:message key="${minNumberWordsLabel}">
								: <fmt:param value="${itemRatingDto.commentsMinWordsLimit}"/>
							</fmt:message>
						</lams:Alert>
					</c:if>
				
					<div class="row">
						<div class="col-xs-12 col-sm-11">
							<textarea name="comment" rows="2" id="comment-textarea-${itemRatingDto.itemId}" class="form-control comment-textarea"
									placeholder="<fmt:message key="label.comment.textarea.tip"/>"
									aria-label="<fmt:message key="label.comment.textarea.tip"/>"></textarea>
						</div>
						<div class="col-xs-12 col-sm-1 p-0">
							<button class="btn btn-success btn-sm add-comment add-comment-new"
									data-item-id="${itemRatingDto.itemId}"
									data-comment-criteria-id="${itemRatingDto.commentsCriteriaId}"
									data-show-all-comments="${showAllComments}"
									data-refresh-on-submit="${refreshOnComment}"
									aria-label="<fmt:message key="${postCommentButtonLabel}"/>">
								<i class="fa fa-check fa-lg"></i>
							</button>
						</div>
					</div>
						
				</div>			
			</c:when>
		</c:choose>
			
	</div>	
</c:if>