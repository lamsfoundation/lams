<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
		"http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
		<script type="text/javascript" src="${lams}includes/javascript/prototype.js"></script>
		<lams:css style="main" />
		<script lang="javascript">
			var overallFeedbackTargetDiv = "overallFeedbackArea";
			function addOverallFeedback(){
				var addOverallFeedbackUrl = "<c:url value='/authoring/newOverallFeedback.do'/>";
			    var param = Form.serialize("overallFeedbackForm");
			    overallFeedbackLoading();
			    var myAjax = new Ajax.Updater(
			    		overallFeedbackTargetDiv,
			    		addOverallFeedbackUrl,
				    	{
				    		method:'post',
				    		parameters:param,
				    		onComplete:overallFeedbackComplete,
				    		evalScripts:true 
				    	}
			    );
			}
			function overallFeedbackLoading(){
				showBusy(overallFeedbackTargetDiv);
			}
			function overallFeedbackComplete(){
				hideBusy(overallFeedbackTargetDiv);
			}

		</script>		
	</lams:head>
	<body class="tabpart">

<c:set var="ctxPath" value="${pageContext.request.contextPath}"	scope="request" />
<c:set var="overallFeedbackListSize" value="${fn:length(overallFeedbackList)}" />
<div id="overallFeedbackArea">
	<form id="overallFeedbackForm">
		<input type="hidden" name="overallFeedbackCount" id="overallFeedbackCount" value="${overallFeedbackListSize}">

		<div class="field-name space-top">
			<fmt:message key="label.authoring.advance.overall.feedback" />
			<img src="${ctxPath}/includes/images/indicator.gif"	style="display:none" id="overallFeedbackArea_Busy" />			
		</div>

		<table class="alternative-color" cellspacing="0">
			<tr>
				<td style="padding-left:0px; border-bottom:0px; text-align:right; background:none;">
					<fmt:message key="label.authoring.advance.grade.boundary"></fmt:message>
				</td>
				<td style="padding-left:5px; border-bottom:0px; background:none;">
					<input type="hidden" name="overallFeedbackGradeBoundary0" id="overallFeedbackGradeBoundary0" value="100">	
					100%
				</td>									
			</tr>		
			<c:forEach var="overallFeedback" items="${overallFeedbackList}" varStatus="status">
				<c:if test="${status.index != 0}">
					<tr id="overallFeedback${status.index}">
						<td style="padding-left:0px; border-bottom:0px; vertical-align:middle; text-align:right; background:none;">
							<fmt:message key="label.authoring.advance.grade.boundary"></fmt:message>
						</td>
						<td style="padding-left:5px; border-bottom:0px; background:none;">	
							<input type="text" name="overallFeedbackGradeBoundary${status.index}"
								id="overallFeedbackGradeBoundary${status.index}" size="5" value="${overallFeedback.gradeBoundary}">%
						</td>									
					</tr>			
				</c:if>

				<tr>
					<td style="padding-left:0px; border-bottom:0px; vertical-align:middle; text-align:right; background:none;">
						<fmt:message key="label.authoring.advance.feedback"></fmt:message>
					</td>
					<td style="padding-left:5px; border-bottom:0px; background:none;">
						<input type="hidden" name="overallFeedbackSequenceId${status.index}" id="overallFeedbackSequenceId${status.index}" value="${overallFeedback.sequenceId}">									
						<input type="text" name="overallFeedbackFeedback${status.index}"
							id="overallFeedbackFeedback${status.index}" size="57" value="${overallFeedback.feedback}">
					</td>									
				</tr>				
			</c:forEach>
			<tr>
				<td style="padding-left:0px; border-bottom:0px; vertical-align:middle; text-align:right; background:none;">
					<fmt:message key="label.authoring.advance.grade.boundary"></fmt:message>
				</td>
				<td style="padding-left:5px; border-bottom:0px; background:none;">	
					0%
				</td>									
			</tr>
		</table>
		
		<a href="javascript:;" onclick="addOverallFeedback();" class="button-add-item right-buttons" style="margin-right: 120px;">
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
	obj.style.height = eval(155 + ${overallFeedbackListSize}*72) + 'px';
</script>	
	
	</body>
</lams:html>

