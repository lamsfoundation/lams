<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
		
		<c:set var="lams"> <lams:LAMSURL /> </c:set>
		<c:set var="tool"> <lams:WebAppURL /> </c:set>
	
	<lams:head>
		<title>
			<fmt:message key="activity.title" />
		</title>
		<link href="${tool}includes/css/chat.css" rel="stylesheet" type="text/css">
		<link href="<lams:LAMSURL/>css/defaultHTML_learner.css" rel="stylesheet" type="text/css">
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap.min.js"></script>
		
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
		
	</lams:head>
	
	<body class="stripes">
		

		<c:set var="session" value="${requestScope.sessionDTO}" />

		<lams:Page title="${pageTitle.monitoring.chatHistory}" type="learner" hideProgressBar="true" formID="">
		
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
							<form:form action="editMessage.do" id="monitoringForm" modelAttribute="monitoringForm" method="post">
								<form:hidden path="toolSessionID" value="${session.sessionID}" />
								<form:hidden path="messageUID" value="${message.uid}" />
		
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
		
								<textarea name="messageBody" value="${messageBody}"
									style="width:95%" rows="3" ></textarea>
								<br />
		
								<input type="submit" value="<fmt:message>button.save</fmt:message>" class="btn btn-primary btn-sm">
								&nbsp;
								<button name="dispatch"
									onclick="javascript:toggleEditPane('u${message.uid}')"
									class="btn btn-default btn-sm">
									<fmt:message>button.cancel</fmt:message>
								</button>
							</form:form>
						</div>
						<br />
		
					</c:forEach>
				</div>
			</c:otherwise>
		</c:choose>
		</lams:Page>
	</body>
</lams:html>
