<%@ include file="/common/taglibs.jsp"%>

<c:set var="sessionMapID" value="${param.sessionMapID}" />
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="kaltura" value="${sessionMap.kaltura}" />

<script type="text/javascript">
	function disableFinishButton() {
		document.getElementById("finishButton").disabled = true;
	}
	function submitForm(methodName){
		var f = document.getElementById('messageForm');
		f.submit();
	}
</script>
	
<html:form action="/learning.do?dispatch=submitReflection" method="post" onsubmit="disableFinishButton();" styleId="messageForm">
	<html:hidden property="userID" />
	<html:hidden property="sessionMapID" />

	<div id="content">
		<h1>
			<c:out value="${kaltura.title}" escapeXml="true"/>
		</h1>

		<%@ include file="/common/messages.jsp"%>

		<p>
			<lams:out value="${kaltura.reflectInstructions}" escapeHtml="true"/>
		</p>

		<html:textarea cols="60" rows="8" property="entryText" styleClass="text-area" />

		<div class="space-bottom-top align-right">
			<html:link href="#nogo" styleClass="button" styleId="finishButton" onclick="submitForm('finish')">
				<span class="nextActivity">
					<c:choose>
	 					<c:when test="${sessionMap.activityPosition.last}">
	 						<fmt:message key="button.submit" />
	 					</c:when>
	 					<c:otherwise>
	 		 				<fmt:message key="button.finish" />
	 					</c:otherwise>
	 				</c:choose>
	 			</span>
			</html:link>
		</div>
	</div>
</html:form>
	
