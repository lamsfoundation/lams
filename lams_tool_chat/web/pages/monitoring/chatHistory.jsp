<%@ include file="/common/taglibs.jsp"%>

<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<script type="text/javascript">
var currentPaneID = null;
function toggleEditPane(paneID) {
	if (currentPaneID == paneID) {
		document.getElementById(currentPaneID).style.display = "none";
		currentPaneID=null;
		return;
	}

	var display = document.getElementById(paneID).style.display;
	if (!(currentPaneID == null)) {
		closeEditPane();
	}
	document.getElementById(paneID).style.display = "";
	currentPaneID = paneID;
}

function closeEditPane() {
	document.getElementById(currentPaneID).style.display = "none";
	currentPaneID=null;
}

</script>

<c:set var="session" value="${requestScope.sessionDTO}" />

<c:choose>
	<c:when test="${empty session.chatMessages}">
		<fmt:message>No messages available</fmt:message>
	</c:when>
	<c:otherwise>
		<div>
			<c:forEach var="message" items="${session.chatMessages}">
				<c:set var="hiddenStyle" value="" />
				<c:if test="${message.hidden}" >
					<c:set var="hiddenStyle" value="color: graytext" />
				</c:if>			
				
				<div onclick="javascript:toggleEditPane('u${message.uid}')" style="${hiddenStyle}">
					<span style="font-weight: bold;"> <c:out value="${message.from}" /></span>
					<c:out value="${message.body}" />
					<img src="${tool}images/edit.gif" alt="edit" />
				</div>
				<div id="u${message.uid}" style="display: none;border: thin solid; background: infoBackground ;padding: 8px;">
					<html:form action="/monitoring" method="post">
						<html:hidden property="dispatch" value="editMessage" />
						<html:hidden property="toolSessionID" value="${session.sessionID}" />
						<html:hidden property="messageUID" value="${message.uid}" />
						
						<c:set var="checked" value=""/>
						<c:if test="${message.hidden}">
							<c:set var="checked" value="checked='checked'" />
						</c:if>
						<input id="cb${message.uid}" name="messageHidden" type="checkbox" <c:out value="${checked}"/> />
						<label for="cb${message.uid}">
							<fmt:message>chatHistory.hideMessage</fmt:message>
						</label>						
						<br />
						
						<html:textarea property="messageBody" value="${message.body}" cols="80" rows="3" />
						<br />
						
						<html:submit>
							<fmt:message>button.save</fmt:message>
						</html:submit>
						<html:button property="dispatch" onclick="javascript:toggleEditPane('u${message.uid}')">
							<fmt:message>button.cancel</fmt:message>
						</html:button>
					</html:form>
				</div>
				<br />

			</c:forEach>
		</div>
	</c:otherwise>
</c:choose>
