<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>

<!DOCTYPE html>
<lams:html>
<lams:head>
	<title>SVG Generator</title>
	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/raphael/raphael.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/authoring/authoringGeneral.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/authoring/authoringActivity.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/authoring/authoringDecoration.js"></script>
	<script type="text/javascript">
		var LAMS_URL = '<lams:LAMSURL/>',
			LABELS = {
				// ActivityLib
				DEFAULT_GROUPING_TITLE : 'Grouping',
				DEFAULT_GROUP_PREFIX : 'Group ',
				DEFAULT_BRANCHING_TITLE :'Branching',
				DEFAULT_BRANCH_PREFIX : 'Branch ',
				DEFAULT_OPTIONAL_ACTIVITY_TITLE : 'Optional Activity',
				SUPPORT_ACTIVITY_TITLE : 'Support Activity',
				GATE_ACTIVITY_LABEL : 'STOP',
				BRANCHING_START_SUFFIX : 'start',
				BRANCHING_END_SUFFIX : 'end',
				
				// DecorationLib
				DEFAULT_ANNOTATION_LABEL_TITLE : 'Label'
			},
			
			isReadOnlyMode = true,
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