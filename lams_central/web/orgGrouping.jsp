<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>

<!DOCTYPE HTML>
<lams:html>
<lams:head>
	<lams:css style="main" />
	<link rel="stylesheet" href="css/jquery-ui-redmond-theme.css" type="text/css" media="screen" />
	<link rel="stylesheet" href="css/orgGrouping.css" type="text/css" media="screen" />
	
	<c:set var="lessonMode" value="${not empty param.activityID}" />
	
	<script type="text/javascript" src="includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="includes/javascript/orgGrouping.js"></script>
	<script type="text/javascript">
		var groupingActivityId = '${param.activityID}',
			lessonMode = ${lessonMode},
		// parameter can be null and the ID available only as a JSP attribute
			organisationId = ${empty organisationID ? param.organisationID : organisationID},
			lessonId = '${param.lessonID}',
		
			LAMS_URL = '<lams:LAMSURL/>',
			
			decoderDiv = $('<div />'),
			LABELS = {
				<fmt:message key="label.course.groups.grouping.remove.confirm" var="REMOVE_GROUPING_CONFIRM_LABEL_VAR"/>
				REMOVE_GROUPING_CONFIRM_LABEL : decoderDiv.html('<c:out value="${REMOVE_GROUPING_CONFIRM_LABEL_VAR}" />').text(),
				<fmt:message key="label.course.groups.grouping.use.confirm" var="USE_GROUPING_CONFIRM_LABEL_VAR"/>
				USE_GROUPING_CONFIRM_LABEL : decoderDiv.html('<c:out value="${USE_GROUPING_CONFIRM_LABEL_VAR}" />').text()
			};
	</script>
</lams:head>
<body>

<div id="titleDiv">
	<c:choose>
		<c:when test="${lessonMode}">
			<fmt:message key="index.course.groups.title" />
		</c:when>
		<c:otherwise>
			<fmt:message key="label.course.groups.grouping.title" />
		</c:otherwise>
	</c:choose>
</div>

<c:forEach var="grouping" items="${groupings}">
	<%-- In lesson mode do not show groupings with zero groups --%>
	<c:if test="${not lessonMode or grouping.groupCount > 0}">
		<div id="grouping-${grouping.groupingId}" class="groupingContainer">
			<a href="#" class="groupingName" onClick="javascript:showGroups(${grouping.groupingId})">
				<c:out value="${grouping.name}" />
			</a>
			<span class="groupCount"
			      title='<fmt:message key="label.course.groups.grouping.count.label" />'>
				(${grouping.groupCount})
			</span>
			<c:if test="${canEdit and not lessonMode}">
				<img class="removeGroupingButton" src="<lams:LAMSURL/>images/css/delete.png"
			    	 title="<fmt:message key='label.course.groups.remove.tooltip' />"
			    	 onClick="javascript:removeGrouping(${grouping.groupingId})" />
			</c:if>
		</div>
	</c:if>
</c:forEach>

<c:if test="${canEdit}">
	<div id="addGroupingButton" class="ui-button" onClick="javascript:showGroups()">
		<fmt:message key='label.course.groups.grouping.create' />
	</div>
</c:if>
</body>
</lams:html>