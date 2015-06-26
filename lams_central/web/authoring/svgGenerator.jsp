<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>

<!DOCTYPE html>
<lams:html>
<lams:head>
	<title><fmt:message key="authoring.fla.page.svg.generator.title" /></title>
	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/snap.svg.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/authoring/authoringGeneral.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/authoring/authoringActivity.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/authoring/authoringDecoration.js"></script>
	<c:if test="${param.selectable}">
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/authoring/authoringHandler.js"></script>
	</c:if>
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
			
			isReadOnlyMode = true,
			activitiesOnlySelectable = ${param.selectable eq 'true'},
			initLearningDesignID = '${param.learningDesignID}';
	</script>
</lams:head>

<body>
	<div style="display: none">
		<c:forEach var="tool" items="${tools}">
			<div
				 toolId="${tool.toolId}"
				 learningLibraryId="${tool.learningLibraryId}"
				 supportsOutputs="${tool.supportsOutputs}"
				 activityCategoryId="${tool.activityCategoryID}"
				 childToolIds="
				 <c:forEach var='childId' items='${tool.childToolIds}'>
				 	${childId},
				 </c:forEach>
				 "
				 class="template">
				<c:if test="${not empty tool.iconPath}">
					<img src="<lams:LAMSURL/>${tool.iconPath}" />
				</c:if>
				<div><c:out value="${tool.toolDisplayName}" /></div>
			</div>
		</c:forEach>
	</div>

	<div id="canvas"></div>
</body>
</lams:html>