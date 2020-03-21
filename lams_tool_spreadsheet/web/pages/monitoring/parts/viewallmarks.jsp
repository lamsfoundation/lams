<!DOCTYPE html>
<%@include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.spreadsheet.SpreadsheetConstants"%>

<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>

<lams:html>
<lams:head>
	<title><fmt:message key="activity.title" /></title>
	<lams:css/>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/common.js"></script>
</lams:head>
<body class="stripes">

		<c:set var="title"><fmt:message key="label.monitoring.vieawallmarks.heading.marking"/></c:set>
		<lams:Page type="learner" title="${title}">
				<div class="row no-gutter">
				<div class="col-12">

				<c:forEach var="user" items="${userList}">
						
					<div class="panel panel-default">
						<div class="panel-heading panel-title">
							<c:out value="${user.fullUsername}"/>
						</div>
						<div class="panel-body">
						<div class="row no-gutter">
							<div class="col-md-2"><fmt:message key="label.monitoring.vieawallmarks.spreadsheet.submitted" /></div>
							<div class="col-md-10">
								<c:choose>
								<c:when test="${user.userModifiedSpreadsheet != null}">
									<fmt:message key="label.monitoring.vieawallmarks.true" />
								</c:when>
								<c:otherwise>
									<fmt:message key="label.monitoring.vieawallmarks.false" />
								</c:otherwise>
								</c:choose>
							</div>
						</div>
						<div class="row no-gutter">
							<div class="col-md-2"><fmt:message key="label.monitoring.vieawallmarks.marks" /></div>
							<div class="col-md-10">
								<c:choose>
									<c:when test="${(user.userModifiedSpreadsheet != null) && (user.userModifiedSpreadsheet.mark != null)}">
										<fmt:formatNumber type="number" maxFractionDigits="<%= SpreadsheetConstants.MARK_NUM_DEC_PLACES %>" value="${user.userModifiedSpreadsheet.mark.marks}"/>
									</c:when>
									<c:otherwise>
										<fmt:message key="label.learning.not.available" />
									</c:otherwise>
								</c:choose>
								<c:if test="${user.userModifiedSpreadsheet != null}">
									<c:set var="editUrl"><c:url value="/monitoring/editMark.do"/>?userUid=${user.uid}&toolContentID=${sessionMap.toolContentID}&sessionMapID=${sessionMapID}</c:set>
									<a href="javascript:launchPopup('${editUrl}');" 
										name="submit" class="btn btn-default btn-sm loffset5">
										<fmt:message key="label.monitoring.vieawallmarks.update.marks" />
									</a>
								</c:if>
							</div>
						</div>
						<div class="row no-gutter">
							<div class="col-md-2"><fmt:message key="label.monitoring.vieawallmarks.comments" /></div>
							<div class="col-md-10">
								<c:choose>
									<c:when test="${(user.userModifiedSpreadsheet != null) && (user.userModifiedSpreadsheet.mark != null)}">
										<c:out value="${user.userModifiedSpreadsheet.mark.comments}" escapeXml="false" />
									</c:when>
									<c:otherwise>
										<fmt:message key="label.learning.not.available" />
									</c:otherwise>
								</c:choose>
							</div>
						</div>
						</div>
					</div>

				</c:forEach>
				
				</div>
				</div>
				
				<a href="javascript:window.close();" name="submit" class="btn btn-default">
						<fmt:message key="button.close" />
				</a>

		<div id="footer"></div>

		</lams:Page>
</body>
</lams:html>