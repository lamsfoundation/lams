<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>
	
<c:if test="${lessonID == null}">
	<c:set var="lessonID"  value="${param.lessonID}"/>
</c:if>

<!DOCTYPE html>
<lams:html>
<lams:head>
	<lams:css/>
	<link rel="stylesheet" href="css/jquery-ui-redmond-theme.css" type="text/css" media="screen" />
	<link rel="stylesheet" href="css/orgGrouping.css" type="text/css" media="screen" />
	
	<script type="text/javascript" src="includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="includes/javascript/jquery-ui.js"></script>
</lams:head>
<body>

<div id="titleDiv">
	<fmt:message key="index.course.groups.title" />
</div>

<form id="ext-groups-form" action="<lams:LAMSURL/>OrganisationGroup.do?method=viewGroups" method="POST">
	<input name="lessonID" value="${lessonID}" type="hidden"/>
	<input name="activityID" value="${param.activityID}" type="hidden"/>

	<c:forEach var="group" items="${extGroups}">
		<div class="groupingContainer">
			<input name="extGroupIds" value="${group.groupId}" type="checkbox">
				<c:out value="${group.groupName}" />
			</input>
			
			<span class="groupCount" title='<fmt:message key="label.course.groups.grouping.count.label" />'>
				(<fmt:message key='authoring.fla.page.prop.groups.learners' /> ${group.numberUsers})
			</span>
		</div>
	</c:forEach>
	
	<button type="submit"  id="get-lms-course-groups-button" class="ui-button space-top" > 
		<fmt:message key='label.select.groups' />
	</button>
	
</form>
</body>
</lams:html>