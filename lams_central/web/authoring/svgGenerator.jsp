<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
<lams:head>
	<title><fmt:message key="authoring.fla.page.svg.generator.title" /></title>
	
	<link rel="stylesheet" href="<lams:LAMSURL/>css/authoring-svg.css" type="text/css" media="screen" />
	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/snap.svg.js"></script>
	<lams:JSImport src="includes/javascript/authoring/authoringGeneral.js" />
	<lams:JSImport src="includes/javascript/authoring/authoringActivity.js" />
	<lams:JSImport src="includes/javascript/authoring/authoringDecoration.js" />
	<c:if test="${param.selectable}">
		<lams:JSImport src="includes/javascript/authoring/authoringHandler.js" />
	</c:if>
	<lams:JSImport src="includes/javascript/authoring/authoringMenu.js" />
	
	<script type="text/javascript">
		var LAMS_URL = '<lams:LAMSURL/>',
			LABELS = {
				// ActivityLib
				<fmt:message key="authoring.fla.default.group.title" var="DEFAULT_GROUPING_TITLE_VAR"/>
				DEFAULT_GROUPING_TITLE : '<c:out value="${DEFAULT_GROUPING_TITLE_VAR}" />',
				<fmt:message key="authoring.fla.default.group.prefix" var="DEFAULT_GROUP_PREFIX_VAR"/>
				DEFAULT_GROUP_PREFIX : '<c:out value="${DEFAULT_GROUP_PREFIX_VAR}" />',
				<fmt:message key="authoring.fla.default.branching.title" var="DEFAULT_BRANCHING_TITLE_VAR"/>
				DEFAULT_BRANCHING_TITLE : '<c:out value="${DEFAULT_BRANCHING_TITLE_VAR}" />',
				<fmt:message key="authoring.fla.default.branch.prefix" var="DEFAULT_BRANCH_PREFIX_VAR"/>
				DEFAULT_BRANCH_PREFIX : '<c:out value="${DEFAULT_BRANCH_PREFIX_VAR}" />',
				<fmt:message key="authoring.fla.default.optional.activity.title" var="DEFAULT_OPTIONAL_ACTIVITY_TITLE_VAR"/>
				DEFAULT_OPTIONAL_ACTIVITY_TITLE : '<c:out value="${DEFAULT_OPTIONAL_ACTIVITY_TITLE_VAR}" />',
				<fmt:message key="authoring.fla.support.activity.title" var="SUPPORT_ACTIVITY_TITLE_VAR"/>
				SUPPORT_ACTIVITY_TITLE : '<c:out value="${SUPPORT_ACTIVITY_TITLE_VAR}" />',
				<fmt:message key="authoring.fla.gate.activity.label" var="GATE_ACTIVITY_LABEL_VAR"/>
				GATE_ACTIVITY_LABEL : '<c:out value="${GATE_ACTIVITY_LABEL_VAR}" />',
				<fmt:message key="authoring.fla.branching.start.suffix" var="BRANCHING_START_SUFFIX_VAR"/>
				BRANCHING_START_SUFFIX : '<c:out value="${BRANCHING_START_SUFFIX_VAR}" />',
				<fmt:message key="authoring.fla.branching.end.suffix" var="BRANCHING_END_SUFFIX_VAR"/>
				BRANCHING_END_SUFFIX : '<c:out value="${BRANCHING_END_SUFFIX_VAR}" />',
				
				// DecorationLib
				<fmt:message key="authoring.fla.default.annotation.label.title" var="DEFAULT_ANNOTATION_LABEL_TITLE_VAR"/>
				DEFAULT_ANNOTATION_LABEL_TITLE : '<c:out value="${DEFAULT_ANNOTATION_LABEL_TITLE_VAR}" />'
			},
			
			paperMinWidth = 1000,
			canSetReadOnly = false,
			isReadOnlyMode = true,
			activitiesOnlySelectable = ${param.selectable eq 'true'},
			isLtiContentSelection = false,
			initLearningDesignID = '<c:out value="${param.learningDesignID}" />',
			initRelaunchMonitorLessonID = null,
			csrfTokenName = '<csrf:tokenname/>',
			csrfTokenValue = '<csrf:tokenvalue/>';
	</script>
</lams:head>

<body>
	<div id="templateContainerCell" style="display: none">
		<%-- Shared with svgGenerator.jsp --%>
		<%@ include file="authoringTemplatePart.jsp"%> 
	</div>

	<div id="canvas"></div>
</body>
</lams:html>