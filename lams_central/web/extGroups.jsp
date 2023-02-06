<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL/></c:set>
<c:if test="${lessonID == null}">
	<c:set var="lessonID"  value="${param.lessonID}"/>
</c:if>

<lams:html>
<lams:head>
	<lams:css/>
	<link rel="stylesheet" href="${lams}css/jquery-ui-bootstrap-theme.css" type="text/css" media="screen" />
	<link rel="stylesheet" href="${lams}css/orgGrouping.css" type="text/css" media="screen" />
	
	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>
</lams:head>
<body>

<div id="titleDiv">
	<fmt:message key="index.course.groups.title" />
</div>

<form id="ext-groups-form" action="<lams:LAMSURL/>organisationGroup/viewGroups.do" method="POST">
	<input name="lessonID" value='<c:out value="${lessonID}" />' type="hidden"/>
	<input name="activityID" value='<c:out value="${param.activityID}" />' type="hidden"/>

	<c:forEach var="group" items="${extGroups}">
		<div class="groupingContainer">
			<input name="extGroupIds" id="extGroupIds" value="${group.groupId}" type="checkbox">
			<label for="extGroupIds"><c:out value="${group.groupName}" /></label>
			
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