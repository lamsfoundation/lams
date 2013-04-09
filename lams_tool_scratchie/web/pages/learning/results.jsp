<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>
	<%-- param has higher level for request attribute --%>
	<c:if test="${not empty param.sessionMapID}">
		<c:set var="sessionMapID" value="${param.sessionMapID}" />
	</c:if>
	<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

	<c:set var="mode" value="${sessionMap.mode}" />
	<c:set var="toolSessionID" value="${sessionMap.toolSessionID}" />
	<c:set var="scratchie" value="${sessionMap.scratchie}" />

<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" /></title>
	<%@ include file="/common/header.jsp"%>

	<script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript">
	<!--

		function finishSession(){
			document.getElementById("finishButton").disabled = true;
			document.location.href ='<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}"/>';
			return false;
		}
		function continueReflect(){
			document.location.href='<c:url value="/learning/newReflection.do?sessionMapID=${sessionMapID}"/>';
		}
	-->        
    </script>
</lams:head>
<body class="stripes">

	<div id="content">
		<h1>
			${scratchie.title}
		</h1>

		<%@ include file="/common/messages.jsp"%>

		<c:forEach var="item" items="${itemList}" varStatus="status">
			<h3>${item.title}</h3>
		
			<table>
				<tr>
					<td style="width: 120px;">
						<fmt:message key="label.correct.answer" />
					</td>
					<td>
						 ${item.correctAnswer}
					</td>
				</tr>
				<tr>
					<td style="width: 80px;">
						<fmt:message key="label.mark" />
					</td>
					<td>
						  ${item.userMark}
					</td>
				</tr>
				<tr>
					<td style="width: 80px;">
						<fmt:message key="label.attempts" />
					</td>
					<td>
						${item.userAttempts}
					</td>
				</tr>
			</table>
		
		</c:forEach>


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
					<html:button property="finishButton" onclick="return continueReflect()" styleClass="button">
						<fmt:message key="label.edit" />
					</html:button>
				</c:if>
			</div>
		</c:if>

		<c:if test="${mode != 'teacher'}">
			<div class="space-bottom-top align-right">
				<c:choose>
					<c:when test="${sessionMap.reflectOn && (not sessionMap.userFinished)}">
						<html:button property="finishButton" styleId="finishButton" onclick="return continueReflect()" styleClass="button">
							<fmt:message key="label.continue" />
						</html:button>
					</c:when>
					<c:otherwise>
						<html:link href="#nogo" property="finishButton" styleId="finishButton" onclick="return finishSession()" styleClass="button">
							<span class="nextActivity">
								<c:choose>
									<c:when test="${sessionMap.activityPosition.last}">
										<fmt:message key="label.submit" />
									</c:when>
									<c:otherwise>
										<fmt:message key="label.finished" />
									</c:otherwise>
								</c:choose>
							</span>
						</html:link>
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
