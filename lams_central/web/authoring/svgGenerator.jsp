<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>

<!DOCTYPE html>
<lams:html>
<lams:head>
	<title><fmt:message key="authoring.fla.page.svg.generator.title" /></title>
	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/raphael/raphael.js"></script>
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
DEFAULT_GROUPING_TITLE : '<fmt:message key="authoring.fla.default.group.title" />',
				DEFAULT_GROUP_PREFIX : '<fmt:message key="authoring.fla.default.group.prefix" />',
				DEFAULT_BRANCHING_TITLE : '<fmt:message key="authoring.fla.default.branching.title" />',
				DEFAULT_BRANCH_PREFIX : '<fmt:message key="authoring.fla.default.branch.prefix" />',
				DEFAULT_OPTIONAL_ACTIVITY_TITLE : '<fmt:message key="authoring.fla.default.optional.activity.title" />',
				SUPPORT_ACTIVITY_TITLE : '<fmt:message key="authoring.fla.support.activity.title" />',
				GATE_ACTIVITY_LABEL : '<fmt:message key="authoring.fla.gate.activity.label" />',
				BRANCHING_START_SUFFIX : '<fmt:message key="authoring.fla.branching.start.suffix" />',
				BRANCHING_END_SUFFIX : '<fmt:message key="authoring.fla.branching.end.suffix" />',
				
				// DecorationLib
				DEFAULT_ANNOTATION_LABEL_TITLE : '<fmt:message key="authoring.fla.default.annotation.label.title" />'
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