<%@ include file="/common/taglibs.jsp"%>

<link rel="stylesheet" type="text/css" href="${tool}includes/css/mapjs.css"></link>
<link rel="stylesheet" type="text/css" href="${tool}includes/css/mindmap.css"></link>

<script src="${lams}includes/javascript/jquery.timer.js"></script>
<script src="${tool}includes/javascript/mapjs/main.js"></script>
<script src="${tool}includes/javascript/mapjs/underscore-min.js"></script>

<script type="text/javascript">

	var mode = "${mode}";		// learner, teacher, ...
	
	function disableButtons() {
		$("#finishButton").attr("disabled", true);
		// show the waiting area during the upload
		$("#spinnerArea_Busy").show();
	}

	function enableButtons() {
		$("#spinnerArea_Busy").hide();
		$("#finishButton").removeAttr("disabled");
	}

	function submitForm() {
		var mindmapContent = document.getElementById("mindmapContent");
		mindmapContent.value = JSON.stringify(contentAggregate);
 	 	var f = document.getElementById('submitForm');
 		f.submit();
	}
	
	</script>

<html:form action="/learning" method="post" onsubmit="return false;" styleId="submitForm">
	<c:set var="lrnForm" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
	<html:hidden property="userId" value="${userIdParam}" />
	<html:hidden property="toolContentId" value="${toolContentIdParam}" />
	<html:hidden property="toolSessionID" />
	<html:hidden property="mindmapContent" styleId="mindmapContent" />

	<lams:Page type="learner" title="${mindmapDTO.title}" hideProgressBar="${isMonitor}">
	
		<%--Advanced settings and notices-----------------------------------%>
		
		<div class="panel">
			<c:out value="${mindmapDTO.instructions}" escapeXml="false"/>
		</div>
	
		<c:if test="${mindmapDTO.lockOnFinish and mode == 'learner'}">
			<lams:Alert type="danger" id="lock-on-finish" close="false">
				<c:choose>
					<c:when test="${finishedActivity}">
						<fmt:message key="message.activityLocked" />
					</c:when>
					<c:otherwise>
						<fmt:message key="message.warnLockOnFinish" />
					</c:otherwise>
				</c:choose>
			</lams:Alert>
		</c:if>
		
		 <c:if test="${not empty submissionDeadline && (mode == 'author' || mode == 'learner')}">
			 <lams:Alert id="submission-deadline" close="true" type="info">
			  	<fmt:message key="authoring.info.teacher.set.restriction" >
			  		<fmt:param><lams:Date value="${submissionDeadline}" /></fmt:param>
			  	</fmt:message>
			 </lams:Alert>
		 </c:if>
	
		&nbsp;
		
		<c:choose>
			<c:when test="${reflectOnActivity}">
				<input type="hidden" name="dispatch" value="reflect" />
			</c:when>
			<c:otherwise>
				<input type="hidden" name="dispatch" value="finishActivity" />
			</c:otherwise>
		</c:choose>
		
		<%--MindMup -----------------------------------%>
		<%@ include file="/common/mapjs.jsp"%>
 		<%-- End MindMup -----------------------------------%>
 		
		<div class="voffset10 pull-right">
			<c:choose>
				<c:when test="${isMonitor}">
					<html:button styleClass="btn btn-primary" property="backButton" onclick="history.go(-1)">
						<fmt:message key="button.back"/>
					</html:button>
				</c:when>
			
				<c:otherwise>
					<c:choose>
						<c:when test="${reflectOnActivity}">
							<html:link href="javascript:submitForm();" styleClass="btn btn-primary" styleId="finishButton">
								   <span class="nextActivity"><fmt:message key="button.continue"/></span>
							</html:link>
						</c:when>
		
						<c:otherwise>
							<html:link href="javascript:submitForm();" styleClass="btn btn-primary" styleId="finishButton">
								<span class="na">
									<c:choose>
					 					<c:when test="${activityPosition.last}">
					 						<fmt:message key="button.submit" />
					 					</c:when>
					 					<c:otherwise>
					 		 				<fmt:message key="button.finish" />
					 					</c:otherwise>
					 				</c:choose>
							 	</span>
							</html:link>
						</c:otherwise>
					</c:choose>
				</c:otherwise>
			</c:choose>
		</div>

	</lams:Page>
</html:form>
