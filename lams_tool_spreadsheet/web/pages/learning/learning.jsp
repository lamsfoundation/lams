<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
<lams:head>
	<title>
		<fmt:message key="label.learning.title" />
	</title>
	<%@ include file="/common/header.jsp"%>

	<%-- param has higher level for request attribute --%>
	<c:if test="${not empty param.sessionMapID}">
		<c:set var="sessionMapID" value="${param.sessionMapID}" />
	</c:if>

	<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
	<c:set var="mode" value="${sessionMap.mode}" />
	<c:set var="toolSessionID" value="${sessionMap.toolSessionID}" />
	<c:set var="spreadsheet" value="${sessionMap.spreadsheet}" />
	<c:set var="finishedLock" value="${sessionMap.finishedLock}" />

	<script type="text/javascript">
	<!--
		function finishSession(){
			document.getElementById("finishButton").disabled = true;
        	//var url = '<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}&toolSessionID=${toolSessionID}"/>';
        	saveUserSpreadsheet(true);
			return false;
		}
		
		function continueReflect(){
			//var url = '<c:url value="/learning/newReflection.do?sessionMapID=${sessionMapID}"/>';
			saveUserSpreadsheet(false);
		}
		
		//save changes made in spreadsheet
		function saveUserSpreadsheet(finishSession){
			var code = window.frames["externalSpreadsheet"].cellsToJS();
			document.getElementById("spreadsheet.code").value = code;
		
		    var learningForm = $("learningForm");
        	learningForm.action = "<c:url value='/learning/saveUserSpreadsheet.do?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${toolSessionID}&finishSession='/>" + finishSession;
        	learningForm.submit();
		}
		
	-->        
    </script>
</lams:head>
<body class="stripes">

	<div id="content">
		<h1>
			${spreadsheet.title}
		</h1>

		<p>
			${spreadsheet.instructions}
		</p>

		<c:if test="${sessionMap.lockOnFinish and mode != 'teacher'}">
				<div class="info">
					<c:choose>
						<c:when test="${sessionMap.userFinished}">
							<fmt:message key="message.activityLocked" />
						</c:when>
						<c:otherwise>
							<fmt:message key="message.warnLockOnFinish" />
						</c:otherwise>
					</c:choose>
				</div>
		</c:if>

		<%@ include file="/common/messages.jsp"%>
		
		<html:form action="learning/saveUserSpreadsheet" method="post" styleId="learningForm" enctype="multipart/form-data">
			<html:hidden property="spreadsheet.code" styleId="spreadsheet.code"/>	
		</html:form>
		
		<br><br>
		<iframe
			id="externalSpreadsheet" name="externalSpreadsheet" src="<html:rewrite page='/includes/javascript/simple_spreadsheet/spreadsheet_offline.html'/>?lang=<%=request.getLocale().getLanguage()%>"
			style="width:99%;" frameborder="no" height="385px"
			scrolling="no">
		</iframe>

		<c:if test="${sessionMap.userFinished and sessionMap.reflectOn}">
			<div class="small-space-top">
				<h2>
					${sessionMap.reflectInstructions}
				</h2>

				<c:choose>
					<c:when test="${empty sessionMap.reflectEntry}">
						<p>
							<em> <fmt:message key="message.no.reflection.available" />
							</em>
						</p>
					</c:when>
					<c:otherwise>
						<p>
							<lams:out escapeHtml="true" value="${sessionMap.reflectEntry}" />
						</p>
					</c:otherwise>
				</c:choose>

				<c:if test="${mode != 'teacher'}">
					<html:button property="FinishButton" onclick="return continueReflect()" styleClass="button">
						<fmt:message key="label.edit" />
					</html:button>
				</c:if>
			</div>
		</c:if>

		<c:if test="${mode != 'teacher'}">
			<div class="space-bottom-top align-right">
				<c:choose>
					<c:when	test="${sessionMap.reflectOn && (not sessionMap.userFinished)}">
						<html:button property="FinishButton" onclick="return continueReflect()" styleClass="button">
							<fmt:message key="label.continue" />
						</html:button>
					</c:when>
					<c:otherwise>
						<html:button property="FinishButton" styleId="finishButton"	onclick="return finishSession()" styleClass="button">
							<fmt:message key="label.finished" />
						</html:button>
					</c:otherwise>
				</c:choose>
			</div>
		</c:if>

	</div>
	<!--closes content-->

	<div id="footer">
	</div>
	<!--closes footer-->

</body>
</lams:html>
