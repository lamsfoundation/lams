<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<lams:html>
<lams:head>
	<%@ include file="/common/header.jsp"%>
	<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
	<c:set var="daco" value="${sessionMap.daco}" />
	
	<title>
		<fmt:message key="label.learning.title" />
	</title>
</lams:head>
<body class="stripes">

<script type="text/javascript">
         function submitForm(methodName){
                var f = document.getElementById('messageForm');
                f.submit();
        }
</script>
	<html:form action="/learning/submitReflection" method="post" onsubmit="javascript:document.getElementById('finishButton').disabled = true;" styleId="messageForm">
		<html:hidden property="userId" />
		<html:hidden property="sessionId" />
		<html:hidden property="sessionMapID" />
		
		<div id="content">
			<h1>
				<c:out value="${daco.title}" escapeXml="true"/>
			</h1>

			<%@ include file="/common/messages.jsp"%>

			<p>
				<lams:out value="${daco.reflectInstructions}" escapeHtml="true" />
			</p>

			<html:textarea cols="60" rows="8" property="entryText"
				styleClass="text-area" />

			<div class="space-bottom-top align-right">
				<html:link href="#nogo" styleClass="button" styleId="finishButton" onclick="submitForm('finish')">
					<span class="nextActivity">
						<c:choose>
		 					<c:when test="${sessionMap.activityPosition.last}">
		 						<fmt:message key="label.learning.submit" />
		 					</c:when>
		 					<c:otherwise>
		 		 				<fmt:message key="label.learning.finished" />
		 					</c:otherwise>
		 				</c:choose>
					</span>
				</html:link>
			</div>
		</div>
	</html:form>

	<div id="footer">
	</div>
	<!--closes footer-->

</body>
</lams:html>
