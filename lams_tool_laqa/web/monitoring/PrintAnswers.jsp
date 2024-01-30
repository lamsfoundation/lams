<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.qa.QaAppConstants"%>

 <c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>
<lams:html>
	<lams:head>
	<title>${content.title}</title>

	<lams:css />
	<link href="${lams}css/rating.css" rel="stylesheet" type="text/css">
	
	<style media="screen,projection" type="text/css">
		body { margin: 20px;}
		.table>tbody>tr>td {vertical-align:top;}
		th {min-width: 150px;}
	</style>
 
</lams:head>
<body>

	<h4>${content.title}</h4>
	
	<p>${questionDTO.name}</p>
	
	<p>${questionDTO.description}</p>

	<table class="table table-striped table-bordered table-condensed">
	<tr>
		<th><fmt:message key="label.username"/></th>
		<th><fmt:message key="label.fullname"/></th>
		<th>	<fmt:message key="label.learning.answer"/></th>
		<th><fmt:message key="label.learning.attemptTime"/></th>
		<c:if test="${content.allowRateAnswers}">
		<th><fmt:message key="label.rating.criterias" /></th>
		</c:if>
		<c:if test="${isCommentsEnabled}">
		<th><fmt:message key="label.comment" /></th>
		</c:if>
	</tr>
	<c:forEach var="response" items="${responses}">
		<tr>
			<td>
				<c:out value="${response.qaQueUser.username}" escapeXml="true"/> 
			</td>
			<td>
				<c:out value="${response.qaQueUser.fullname}" escapeXml="true"/> 
			</td>
			<td>
				${response.answer}
			</td>
			<td>
				<lams:Date value="${response.attemptTime}"/> 
			</td>
	
			<c:if test="${content.allowRateAnswers}">
				<td>
					<c:forEach var="criteriaDTO" items="${criteriaMap[response.responseId]}">
						<div class="starability-holder">
							${criteriaDTO.ratingCriteria.title}
							<br/>
							<fmt:message key="label.average.rating">	
								<fmt:param value="${criteriaDTO.averageRating}"/>
								<fmt:param value="${criteriaDTO.numberOfVotes}"/>
							</fmt:message>
							<br/><br/>
						</div>
					</c:forEach>
				</td>
			</c:if>
			
			<c:if test="${isCommentsEnabled}">
				<td>
					<c:forEach var="commentDTO" items="${commentMap[response.responseId]}">
						<div class="rating-comment">
							<c:set var="postedDate"><lams:Date value="${commentDTO.postedDate}"/></c:set>
							<fmt:message key="label.posted.by">
								<fmt:param value="${commentDTO.userFullName}"/>
								<fmt:param value="${postedDate}"/>
							</fmt:message>
							<br/>
							${commentDTO.comment}
							<br/><br/>
						</div>
					</c:forEach>
				</td>
			</c:if>
		</tr>
	</c:forEach>
	</table>
	<div id="footer"></div>
</body>
</lams:html>
