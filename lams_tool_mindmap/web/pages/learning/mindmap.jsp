<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/swfobject.js"></script>
<script type="text/javascript" src="includes/javascript/jquery.timer.js"></script>
<script type="text/javascript" src="includes/javascript/mindmap.resize.js"></script>

<script type="text/javascript">
	var mode = "${mode}";

	function disableFinishButton() {
		document.getElementById("finishButton").disabled = true;
	}

	function submitForm() {
		var hasFlash = ((typeof navigator.plugins != "undefined" && typeof navigator.plugins["Shockwave Flash"] == "object") || (window.ActiveXObject && (new ActiveXObject("ShockwaveFlash.ShockwaveFlash")) != false));
		if(hasFlash == true){
			// Sets mindmap content in Flash
			setMindmapContent();
			}
 	 	var f = document.getElementById('submitForm');
 		f.submit();
	}

	var multiMode = ${multiMode};
	// saving Mindmap every one minute 
	$.timer(60000, function (timer) {
		if (!multiMode)
			$.post("${get}", { dispatch: "${dispatch}", mindmapId: "${mindmapId}", userId: "${userId}", 
				sessionId: "${sessionId}", content: document['flashContent'].getMindmap() } );
	});

	function validateForm() {
		// Validates that there's input from the user. 
		// disables the Finish button to avoid double submittion 
		disableFinishButton();
	}
	
	<c:set var="MindmapUser">
		<c:out value="${currentMindmapUser}" escapeXml="true"/>
	</c:set>
	
	flashvars = { xml: "${mindmapContentPath}", user: "${MindmapUser}", 
				  pollServer: "${pollServerParam}", notifyServer: "${notifyServerParam}",
				  dictionary: "${localizationPath}" }
	
	function setMindmapContent() {
		var mindmapContent = document.getElementById("mindmapContent");
		mindmapContent.value = document['flashContent'].getMindmap();
	}
	
	function embedFlashObject(x, y)	{
		swfobject.embedSWF("${mindmapType}", "flashContent", x, y, "9.0.0", false, flashvars);
	}

	$(window).resize(makeNice);
	
	embedFlashObject(540, 405);
</script>

<html:form action="/learning" method="post" onsubmit="return validateForm();" styleId="submitForm">
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
		
		<%--MindMap Flash-----------------------------------%>

		<center id="center12">
			<div id="flashContent">
				<fmt:message key="message.enableFlash"/>
			</div>
		</center>
		
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
