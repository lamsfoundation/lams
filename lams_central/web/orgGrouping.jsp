<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>

<!DOCTYPE HTML>
<lams:html>
<lams:head>
	<lams:css style="main" />
	<link rel="stylesheet" href="css/jquery-ui-redmond-theme.css" type="text/css" media="screen" />
	<style type="text/css">
		div#titleDiv {
		    font-size: small;
		    font-weight: bold;
		    padding: 5px 0;
		    text-align: center;
			border-bottom: thin dotted #2E6E9E;
		}
	
		.removeGroupingButton {
			float: right;
			cursor: pointer;
			padding-right: 3px;
			width: 16px;
			height: 16px;
		}
		
		#addGroupingButton {
			float: right;
			margin: 20px 10px 10px 0;
		}
		
		.groupingContainer {
			padding: 10px 10px 0 10px;
		}
		
		.groupingName {
			font-size: 12px;
		 	border-bottom: 1px dotted #0087E5;
    		color: #0087E5;
    		text-decoration: none;
		}
		
		.groupingName:hover {
			border-bottom: 1px solid #0087E5;
    		color: #0087E5;
		}
		
		.groupCount {
			font-size: 12px;
		 	padding-left: 10px;
		}
	</style>
	
	<script type="text/javascript" src="includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript">;
		$(document).ready(function(){
			$(".ui-button").button();
		});
		
		function removeGrouping(groupingId) {
			if (confirm("<fmt:message key='label.course.groups.grouping.remove.confirm' />")) {
				document.location.href = "<lams:LAMSURL/>OrganisationGroup.do?method=removeGrouping&organisationID=${organisationID}&groupingId=" + groupingId;
			}
		}
	</script>
</lams:head>
<body>

<div id="titleDiv">
	<fmt:message key="label.course.groups.grouping.title" />
</div>

<c:forEach var="grouping" items="${groupings}">
	<div class="groupingContainer">
		<a href="#" class="groupingName"
		onClick="javascript:window.parent.loadDialogContents(null, 880, 460, '<lams:LAMSURL/>OrganisationGroup.do?method=viewGroups&organisationID=${organisationID}&groupingId=${grouping.groupingId}')" >
			<c:out value="${grouping.name}" />
		</a>
		<span class="groupCount">(${grouping.groupCount})</span>
		<c:if test="${canEdit}">
			<img class="removeGroupingButton" src="<lams:LAMSURL/>images/css/delete.png"
		    	 title="<fmt:message key='label.course.groups.remove.tooltip' />"
		    	 onClick="javascript:removeGrouping(${grouping.groupingId})" />
		</c:if>
	</div>
</c:forEach>

<c:if test="${canEdit}">
	<div id="addGroupingButton" class="ui-button"
	     onClick="javascript:window.parent.loadDialogContents(null, 880, 460, '<lams:LAMSURL/>OrganisationGroup.do?method=viewGroups&organisationID=${organisationID}')">
	 <fmt:message key='label.course.groups.grouping.create' />
	</div>
</c:if>
</body>
</lams:html>