<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>

<lams:html> 
<lams:head>
		<%-- This page/learning/listRecords.jsp page modifies its content depending on the page it was included from. --%>
		<c:if test="${not empty param.includeMode}">
			<c:set var="includeMode" value="${param.includeMode}" />
		</c:if>
		<c:if test="${empty includeMode}">
			<c:set var="includeMode" value="monitoring" />
		</c:if>
		<c:if test="${not empty param.sessionMapID}">
			<c:set var="sessionMapID" value="${param.sessionMapID}" />
		</c:if>
		<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
		<c:set var="userGroup" value="${sessionMap.monitoringSummary}" />
			
		<%@ include file="/common/header.jsp"%>

		<lams:JSImport src="includes/javascript/dacoMonitoring.js" relative="true" />
		<script type="text/javascript">
			function checkCheckbox(checkboxName){
				var checkbox = document.getElementById(checkboxName);
				checkbox.checked=true;
			}	
		</script> 
		<title>
			<fmt:message key="title.monitoring.recordlist" />
		</title>
</lams:head> 
<body class="stripes">

<c:set var="title"><fmt:message key="title.monitoring.recordlist" /></c:set>
<lams:Page type="monitor" title="${title}">

	<%-- Switch between the horizontal and vertical views --%>
	<c:url var="changeViewUrl" value='/monitoring/changeView.do'>
		<c:param name="sessionMapID" value="${sessionMapID}" />
		<c:param name="toolSessionID" value="${toolSessionID}" />
		<c:param name="userId" value="${userId}" />
		<c:param name="sort" value="${sort}" />
	</c:url>

	<span class="pull-right voffset5">
	<c:choose>
		<c:when test="${sessionMap.learningView=='horizontal'}">
		<i class="fa fa-ellipsis-h loffset10" title="<fmt:message key="label.common.view.change" />"
		 onclick="javascript:document.location.href='${changeViewUrl}'" id="ellipsis"></i>
		</c:when>
		<c:otherwise>
		<i class="fa fa-ellipsis-v loffset10" title="<fmt:message key="label.common.view.change" />"
		 onclick="javascript:document.location.href='${changeViewUrl}'" id="ellipsis"></i>
		</c:otherwise>
	</c:choose>
	</span>

	<h4>
	<c:if test="${sessionMap.isGroupedActivity}">
		<fmt:message key="label.monitoring.group" />: ${userGroup.sessionName}
	</c:if>
	</h4>
	
 	<c:forEach var="user" items="${userGroup.users}">
		<table class="table table-striped table-condensed">
		<tr>
			<th><fmt:message key="label.monitoring.fullname" /></th>
			<th><fmt:message key="label.monitoring.recordcount" /></th>
		</tr>
		<tr>
			<td style="height: 20px;">
				 <c:out value="${user.fullName}" escapeXml="true"/>
			</td>
			<td>
				 ${user.recordCount}
			</td>
		</tr>
		</table>
 		<c:if test="${not empty user.records}">
			<c:set var="recordList" value="${user.records}" />
 			<%@ include file="/pages/learning/listRecords.jsp" %>
		</c:if>
	</c:forEach> 

	<div id="footer">
	</div>
</lams:Page>
</body>
</lams:html>
