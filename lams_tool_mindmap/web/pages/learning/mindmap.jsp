<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-latest.pack.js"></script>
<script type="text/javascript" src="includes/javascript/swfobject.js"></script>
<script type="text/javascript" src="includes/javascript/jquery.timer.js"></script>
<script type="text/javascript" src="includes/javascript/mindmap.resize.js"></script>

<script type="text/javascript">
<!--
	var mode = "${mode}";

	function disableFinishButton() {
		document.getElementById("finishButton").disabled = true;
	}

	var multiMode = ${multiMode};
	// saving Mindmap every one minute 
	$.timer(60000, function (timer) {
		if (!multiMode)
			$.post("${get}", { dispatch: "${dispatch}", mindmapId: "${mindmapId}", userId: "${userId}", 
				content: getFlashMovie('flashContent').getMindmap() } );
	});

	function validateForm() {
		// Validates that there's input from the user. 
		// disables the Finish button to avoid double submittion 
		disableFinishButton();

		// Sets mindmap content in Flash
		setMindmapContent();
	}
	
	flashvars = { xml: "${mindmapContentPath}", user: "${currentMindmapUser}", 
				  pollServer: "${pollServerParam}", notifyServer: "${notifyServerParam}",
				  dictionary: "${localizationPath}" }
	
	function setMindmapContent()
	{
		var mindmapContent = document.getElementById("mindmapContent");
		mindmapContent.value = getFlashMovie('flashContent').getMindmap();
	}
	
	function embedFlashObject(x, y)
	{
		swfobject.embedSWF("${mindmapType}", "flashContent", x, y, "9.0.0", false, flashvars);
	}
	
	function getFlashMovie(movieName) {
		var isIE = navigator.appName.indexOf("Microsoft") != -1;
		return (isIE) ? window[movieName] : document[movieName];
	}

	$(window).resize(makeNice);

	function makeNice(){
		flash = document.getElementById('flashContent');
		container = document.getElementById('container');
		flash.style.width = container.clientWidth+"px";
		flash.style.height = (container.clientWidth*0.75)+"px";
	}
	
	embedFlashObject(540, 405);
-->
</script>

<div id="content">
	<h1>
		${mindmapDTO.title}
	</h1>

	<p>
		${mindmapDTO.instructions}
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

	&nbsp;

	<html:form action="/learning" method="post" onsubmit="return validateForm();" styleId="submitForm">
		<html:hidden property="userId" value="${userIdParam}" />
		<html:hidden property="toolContentId" value="${toolContentIdParam}" />
		<html:hidden property="toolSessionID" />
		
		<c:choose>
			<c:when test="${reflectOnActivity}">
				<input type="hidden" name="dispatch" value="reflect" />
			</c:when>
			<c:otherwise>
				<input type="hidden" name="dispatch" value="finishActivity" />
			</c:otherwise>
		</c:choose>
		
		<html:hidden property="mindmapContent" styleId="mindmapContent" />
		<c:set var="lrnForm" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />

		<center id="container">
			<div id="flashContent">
				<fmt:message>message.enableJavaScript</fmt:message>
			</div>
		</center>
		
		<c:choose>
			<c:when test="${reflectOnActivity}">
				<div class="space-bottom-top align-right">
					<html:submit styleClass="button" styleId="finishButton">
						<fmt:message>button.continue</fmt:message>
					</html:submit>
				</div>
			</c:when>
		
			<c:otherwise>
				<div class="space-bottom-top align-right">
					<html:submit styleClass="button" styleId="finishButton">
						<fmt:message>button.finish</fmt:message>
					</html:submit>
				</div>
			</c:otherwise>
		</c:choose>

	</html:form>
</div>