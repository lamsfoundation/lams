<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/swfobject.js"></script>
<script type="text/javascript" src="includes/javascript/jquery.timer.js"></script>
<script type="text/javascript" src="includes/javascript/mindmap.resize.js"></script>

<script type="text/javascript">
<!--
	var mode = "${mode}";

	function disableFinishButton() {
		document.getElementById("finishButton").disabled = true;
	}

	function submitForm() {
		// Sets mindmap content in Flash
		setMindmapContent();

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
	
	flashvars = { xml: "${mindmapContentPath}", user: "${currentMindmapUser}", 
				  pollServer: "${pollServerParam}", notifyServer: "${notifyServerParam}",
				  dictionary: "${localizationPath}" }
	
	function setMindmapContent()
	{
		var mindmapContent = document.getElementById("mindmapContent");
		mindmapContent.value = document['flashContent'].getMindmap();
	}
	
	function embedFlashObject(x, y)
	{
		swfobject.embedSWF("${mindmapType}", "flashContent", x, y, "9.0.0", false, flashvars);
	}

	$(window).resize(makeNice);
	
	embedFlashObject(540, 405);
-->
</script>

<div id="content">
	<h1>
		<c:out value="${mindmapDTO.title}" escapeXml="true"/>
	</h1>

	<p>
		<c:out value="${mindmapDTO.instructions}" escapeXml="false"/>
	</p>

	<c:if test="${mindmapDTO.lockOnFinish and mode == 'learner'}">
		<div class="info">
			<c:choose>
				<c:when test="${finishedActivity}">
					<fmt:message key="message.activityLocked" />
				</c:when>
				<c:otherwise>
					<fmt:message key="message.warnLockOnFinish" />
				</c:otherwise>
			</c:choose>
		</div>
	</c:if>
	
	 <c:if test="${not empty submissionDeadline && (mode == 'author' || mode == 'learner')}">
		 <div class="info">
		  	<fmt:message key="authoring.info.teacher.set.restriction" >
		  		<fmt:param><lams:Date value="${submissionDeadline}" /></fmt:param>
		  	</fmt:message>
		 </div>
	 </c:if>

	&nbsp;

	<html:form action="/learning" method="post" onsubmit="return validateForm();" styleId="submitForm">
		<html:hidden property="userId" value="${userIdParam}" />
		<html:hidden property="toolContentId" value="${toolContentIdParam}" />
		<html:hidden property="toolSessionID" />
		<html:hidden property="mindmapContent" styleId="mindmapContent" />
		
		<c:choose>
			<c:when test="${reflectOnActivity}">
				<input type="hidden" name="dispatch" value="reflect" />
			</c:when>
			<c:otherwise>
				<input type="hidden" name="dispatch" value="finishActivity" />
			</c:otherwise>
		</c:choose>
		
		<c:set var="lrnForm" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />

		<center id="center12">
			<div id="flashContent">
				<fmt:message>message.enableJavaScript</fmt:message>
			</div>
		</center>
		
		<c:choose>
			<c:when test="${isMonitor}">
				<div class="space-bottom-top align-right">
					<html:button styleClass="button" property="backButton" onclick="history.go(-1)">
						<fmt:message>button.back</fmt:message>
					</html:button>
				</div>
			</c:when>
			
			<c:otherwise>
				<c:choose>
					<c:when test="${reflectOnActivity}">
						<div class="space-bottom-top align-right">
							<html:link href="javascript:submitForm();" styleClass="button" styleId="finishButton">
								   <span class="nextActivity"><fmt:message>button.continue</fmt:message></span>
							</html:link>
						</div>
					</c:when>
		
					<c:otherwise>
						<div class="space-bottom-top align-right">
							<html:link href="javascript:submitForm();" styleClass="button" styleId="finishButton">
								<span class="nextActivity">
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
						</div>
					</c:otherwise>
				</c:choose>
			</c:otherwise>
			
		</c:choose>

	</html:form>
</div>
