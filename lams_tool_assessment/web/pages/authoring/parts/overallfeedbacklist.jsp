<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
<lams:head>
	<%@ include file="/common/header.jsp"%>
	<c:set var="overallFeedbackListSize" value="${fn:length(overallFeedbackList)}" />
	
	<script lang="javascript">
		var overallFeedbackTargetDiv = "overallFeedbackArea";
		function addOverallFeedback() {
			$("#" + overallFeedbackTargetDiv).load(
				"<c:url value='/authoring/newOverallFeedback.do'/>?" + $("#overallFeedbackForm").serialize(true)
			);
		}
	</script>
</lams:head>
<body class="tabpart">

<div id="overallFeedbackArea">
	<form id="overallFeedbackForm">
		<input type="hidden" name="overallFeedbackCount" id="overallFeedbackCount" value="${overallFeedbackListSize}">
		<i class="fa fa-refresh fa-spin fa-2x fa-fw" style="display:none;" id="overallFeedbackArea_Busy"></i>
		
		<table class="table table-condensed">
			<tr>
				<td style="text-align:right;">
					<fmt:message key="label.authoring.advance.grade.boundary"></fmt:message>
				</td>
				<td>
					<input type="hidden" name="overallFeedbackGradeBoundary0" id="overallFeedbackGradeBoundary0" value="100">	
					100%
				</td>									
			</tr>
				
			<c:forEach var="overallFeedback" items="${overallFeedbackList}" varStatus="status">
				<c:if test="${status.index != 0}">
					<tr id="overallFeedback${status.index}">
						<td style="  text-align:right; ">
							<fmt:message key="label.authoring.advance.grade.boundary"></fmt:message>
						</td>
						<td>	
							<input type="text" name="overallFeedbackGradeBoundary${status.index}" size="5" value="${overallFeedback.gradeBoundary}"
								id="overallFeedbackGradeBoundary${status.index}" class="form-control form-control-inline">
							&nbsp;%
						</td>									
					</tr>			
				</c:if>

				<tr>
					<td style="text-align:right;">
						<fmt:message key="label.authoring.advance.feedback"></fmt:message>
					</td>
					<td>
						<input type="hidden" name="overallFeedbackSequenceId${status.index}" id="overallFeedbackSequenceId${status.index}" value="${overallFeedback.sequenceId}">									
						<input type="text" name="overallFeedbackFeedback${status.index}" class="form-control"
							id="overallFeedbackFeedback${status.index}" size="57" value="${overallFeedback.feedback}">
					</td>
				</tr>				
			</c:forEach>
			
			<tr>
				<td style="text-align:right;">
					<fmt:message key="label.authoring.advance.grade.boundary"></fmt:message>
				</td>
				<td>	
					0%
				</td>									
			</tr>
		</table>
		
		<a href="javascript:;" onclick="addOverallFeedback();" class="btn btn-default btn-sm button-add-item pull-right">
			<fmt:message key="label.authoring.advance.add.feedback.field" /> 
		</a>
		
	</form>
</div>

<%-- This script will adjust assessment item input area height according to the new instruction item amount. --%>
<script type="text/javascript">
	var obj = window.document.getElementById('advancedInputArea');
	if (!obj && window.parent) {
		 obj = window.parent.document.getElementById('advancedInputArea');
	}  
	if (!obj) {
		obj = window.top.document.getElementById('advancedInputArea');
	}
	obj.style.height = eval(75 + ${overallFeedbackListSize}*90) + 'px';
</script>	
	
</body>
</lams:html>
