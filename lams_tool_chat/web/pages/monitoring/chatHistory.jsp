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
	document.getElementById(paneID).style.display = "block";
	currentPaneID = paneID;
}

function closeEditPane() {
	document.getElementById(currentPaneID).style.display = "none";
	currentPaneID=null;
}

</script>

<c:set var="session" value="${requestScope.sessionDTO}" />

<c:set var="title"><fmt:message key="pageTitle.monitoring.chatHistory"/></c:set>
<lams:Page title="${title}" type="learner" hideProgressBar="true">

<c:choose>
	<c:when test="${empty session.messageDTOs}">
		<fmt:message key="message.noChatMessages" />
	</c:when>
	<c:otherwise>
		<div class="space-left space-right">
			<c:forEach var="message" items="${session.messageDTOs}">
				<c:choose>
				<c:when test="${message.hidden}">
					<c:set var="hiddenStyle" value="color: graytext; text-decoration:line-through" />
				</c:when>
				<c:otherwise>
					<c:set var="hiddenStyle" value="" />
				</c:otherwise>
				</c:choose>

				<div class="message"
					onclick="javascript:toggleEditPane('u${message.uid}')" style="">
					<div class="messageFrom">
						${message.from}
					</div>
					<span style="${hiddenStyle}"><lams:out escapeHtml="true" value="${message.body}"></lams:out></span>
					<i class="fa fa-pencil"	title="<fmt:message key='button.edit'/>"></i>
				</div>

				<div id="u${message.uid}" class="edit-pane">
					<html:form action="/monitoring" method="post">
						<html:hidden property="dispatch" value="editMessage" />
						<html:hidden property="toolSessionID" value="${session.sessionID}" />
						<html:hidden property="messageUID" value="${message.uid}" />

						<c:choose>
							<c:when test="${message.hidden}">
								<input id="cb${message.uid}" name="messageHidden"
									type="checkbox" checked="checked"/>
							</c:when>
							<c:otherwise>
								<input id="cb${message.uid}" name="messageHidden"
									type="checkbox"/>
							</c:otherwise>
						</c:choose>

						<label for="cb${message.uid}">
							<fmt:message>chatHistory.hideMessage</fmt:message>
						</label>
						<br />

						<html:textarea property="messageBody" value="${message.body}"
							style="width:95%" rows="3" />
						<br />

						<html:submit styleClass="btn btn-primary btn-sm">
							<fmt:message>button.save</fmt:message>
						</html:submit>&nbsp;
						<html:button property="dispatch"
							onclick="javascript:toggleEditPane('u${message.uid}')"
							styleClass="btn btn-default btn-sm">
							<fmt:message>button.cancel</fmt:message>
						</html:button>
					</html:form>
				</div>
				<br />

			</c:forEach>
		</div>
	</c:otherwise>
</c:choose>
</lams:Page>