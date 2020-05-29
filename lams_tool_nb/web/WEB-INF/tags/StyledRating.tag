<% 
 /**
  * StyledRating.tag
  *	Author: Andrey Balan
  *	Description: Shows the results of a styled rating. Does not allow for update. 
  */
 %>
<%@ tag body-content="scriptless" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %> 
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-function" prefix="fn" %>
<c:set var="lams"><lams:LAMSURL/></c:set>

<%@ attribute name="criteriaRatings" required="true" rtexprvalue="true" type="org.lamsfoundation.lams.rating.dto.StyledCriteriaRatingDTO" %>
<%@ attribute name="showJustification" required="true" %>
<%@ attribute name="alwaysShowAverage" required="true" %>

<!-- On the results page, set to true for current users summary at the bottom, set to false "other users results".
When true, hides the names and groups the comments.  -->
<%@ attribute name="currentUserDisplay" required="true" %> 

<c:choose>
<c:when test="${criteriaRatings.ratingCriteria.ratingStyle == 0}">

	<table class="tablesorter">
	<thead>
		<tr>
			<c:if test="${not currentUserDisplay}">
			<th class="username" title="<fmt:message key='label.sort.by.user.name'/>" > 
				<fmt:message key="label.user.name" />
			</th>
			</c:if>
			<th class="comment">
				<fmt:message key="label.comment" />
			</th>
		</tr>
	</thead>
	<tbody>
	
	<c:forEach var="rating" items="${criteriaRatings.ratingDtos}">
		<c:if test="${ not empty rating.comment }">
		<tr>
			<c:if test="${not currentUserDisplay}">
			<td>
				<lams:Portrait userId="${rating.itemId}"/><span class="portrait-sm-lineheight"><c:out value="${rating.itemDescription}" escapeXml="true"/></span>
			</td>
			</c:if>
			<td>
				<c:out value="${rating.comment}" escapeXml="false"/>
			</td>
		</tr>
		</c:if>
	</c:forEach>
	</tbody>
	</table>
</c:when>

<c:when test="${criteriaRatings.ratingCriteria.ratingStyle == 1}">

	<c:choose>
	<c:when test="${not currentUserDisplay}">
		<table class="tablesorter">
		<thead>
			<tr>
				<th class="username" title="<fmt:message key='label.sort.by.user.name'/>" width="${criteriaRatings.ratingCriteria.commentsEnabled ? '25%' : '40%'}" > 
					<fmt:message key="label.user.name" />
				</th>
				<th class="rating text-center ">
					<fmt:message key="label.rating" />
				</th>
				<c:if test="${criteriaRatings.ratingCriteria.commentsEnabled}">
				<th class="comment">
					<fmt:message key="label.comment" />
				</th>
				</c:if>
			</tr>
		</thead>
		<tbody>
		<c:forEach var="rating" items="${criteriaRatings.ratingDtos}">
			<c:if test="${not empty rating.averageRating}">
			<tr>
				<td>
					<lams:Portrait userId="${rating.itemId}"/><span class="portrait-sm-lineheight"><c:out value="${rating.itemDescription}" escapeXml="true"/></span>
				</td>
				<td class="rating">
					<div class="rating-stars-holder text-center center-block">
					<c:set var="objectId">${criteriaRatings.ratingCriteria.ratingCriteriaId}-${rating.itemId}</c:set>
					<div class="rating-stars-disabled rating-stars-new" data-average="${rating.averageRating}" data-id="${objectId}"></div>
					<c:set var="userRating">${rating.userRating}</c:set>
					<div class="rating-stars-caption" id="rating-stars-caption-${objectId}">
						<c:choose>
							<c:when test="${empty userRating}">
								<fmt:message key="label.not.rated"></fmt:message>
							</c:when>
							<c:otherwise>
								<fmt:message key="label.you.gave.rating"><fmt:param><span id="user-rating-${objectId}">${userRating}</span></fmt:param></fmt:message>
							</c:otherwise>
						</c:choose>
						<br/>
						<fmt:message key="label.avg.rating">
							<fmt:param><span id="average-rating-${objectId}">${rating.averageRating}</span></fmt:param>
							<fmt:param><span id="number-of-votes-${objectId}">${rating.numberOfVotes}</span></fmt:param>
						</fmt:message>
					</div>
				</td>
				<c:if test="${criteriaRatings.ratingCriteria.commentsEnabled}">
				<td>
					<c:out value="${rating.comment}" escapeXml="false"/>
				</td>
				</c:if>
			</tr>
			</c:if>
		</c:forEach>
		</tbody>
		</table>
	</c:when>
	
	<c:otherwise>
		<table class="tablesorter" width="100%">
		<tbody>
		<c:forEach var="rating" items="${criteriaRatings.ratingDtos}" varStatus="status">
			<c:if test="${status.first && not empty rating.averageRating}">
			<tr>
				<td class="rating">
					<div class="rating-stars-holder text-center center-block">
					<c:set var="objectId">${criteriaRatings.ratingCriteria.ratingCriteriaId}-${rating.itemId}</c:set>
					<div class="rating-stars-disabled rating-stars-new" data-average="${rating.averageRating}" data-id="${objectId}"></div>
					<div class="rating-stars-caption" id="rating-stars-caption-${objectId}">
						<fmt:message key="label.avg.rating">
							<fmt:param><span id="average-rating-${objectId}">${rating.averageRating}</span></fmt:param>
							<fmt:param><span id="number-of-votes-${objectId}">${rating.numberOfVotes}</span></fmt:param>
						</fmt:message>
					</div>
				</td>
			</tr>
			</c:if>
			<c:if test="${criteriaRatings.ratingCriteria.commentsEnabled && not empty rating.comment}">
			<tr>
				<td>
					<div class="rating-comment"><c:out value="${rating.comment}" escapeXml="false"/></div>
				</td>
			</tr>
			</c:if>
		</c:forEach>
		</tbody>
		</table>
	</c:otherwise>
	</c:choose>
	
</c:when> 
 			 
<c:when test="${criteriaRatings.ratingCriteria.ratingStyle == 2}">
	<fmt:message key="label.learning.average.rankings"/>&nbsp;<c:if test="${not currentUserDisplay}"><fmt:message key="label.learning.your.rankings.shown.in.brackets"/></c:if>
	<ul class="list-group">
	<c:forEach var="rating" items="${criteriaRatings.ratingDtos}">
		<c:if test="${alwaysShowAverage || not empty rating.averageRating }">
			<li class="list-group-item"><strong>
				<c:choose>
					<c:when test="${empty rating.averageRating}">-</c:when>
					<c:otherwise><fmt:formatNumber value="${rating.averageRating}" type="number" maxFractionDigits="0" /></c:otherwise>
				</c:choose>
			:</strong>
			&nbsp;<c:if test="${not currentUserDisplay}"><lams:Portrait userId="${rating.itemId}"/></c:if>
			<span class="portrait-sm-lineheight"><c:out value="${rating.itemDescription}" escapeXml="true"/>&nbsp;</span>
				<c:if test="${not currentUserDisplay && not empty rating.userRating}">
					(${rating.userRating})
				</c:if>
			</li>
		</c:if>
		</c:forEach>
	</ul>
</c:when> 

<c:when test="${criteriaRatings.ratingCriteria.ratingStyle == 3}">
	<fmt:message key="label.learning.average.marks"/>&nbsp;<c:if test="${not currentUserDisplay}"><fmt:message key="label.learning.your.marks.shown.in.brackets"/></c:if>
	<ul class="list-group">
 	<c:forEach var="rating" items="${criteriaRatings.ratingDtos}">
		<c:if test="${alwaysShowAverage || not empty rating.averageRating }">
			<li class="list-group-item"><strong>
				<c:choose>
					<c:when test="${empty rating.averageRating}">-</c:when>
					<c:otherwise>${rating.averageRating}</c:otherwise>
				</c:choose>
			:</strong>
			&nbsp;<c:if test="${not currentUserDisplay}"><lams:Portrait userId="${rating.itemId}"/></c:if>
			<span class="portrait-sm-lineheight"><c:out value="${rating.itemDescription}" escapeXml="true"/>&nbsp;</span>
				<c:if test="${not currentUserDisplay && not empty rating.userRating}">
					(${rating.userRating})
				</c:if>
			</li>
		</c:if>
  	</c:forEach>
 	</ul>
 	<c:if test="${showJustification && criteriaRatings.ratingCriteria.commentsEnabled}">
	<fmt:message key="label.learning.your.justification"/>&nbsp;
	<div class="rating-comment"><lams:out value="${criteriaRatings.justificationComment}" /></div>
	</c:if>
</c:when> 


</c:choose>