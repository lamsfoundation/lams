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
	<c:set var="isUserLeader" value="${sessionMap.isUserLeader}" />

<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" /></title>
	<%@ include file="/common/header.jsp"%>

	<script type="text/javascript">
		function finishSession(){
			document.getElementById("finishButton").disabled = true;
			document.location.href ='<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}"/>';
			return false;
		}
		function continueReflect(){
			document.location.href='<c:url value="/learning/newReflection.do?sessionMapID=${sessionMapID}"/>';
		}
    </script>
</lams:head>

<body class="stripes">

	<div id="content">
		<h1>
			${scratchie.title}
		</h1>
		
		<c:if test="${not empty sessionMap.submissionDeadline}">
			<div class="info">
				<fmt:message key="authoring.info.teacher.set.restriction" >
					<fmt:param><lams:Date value="${sessionMap.submissionDeadline}" /></fmt:param>
				</fmt:message>
			</div>
		</c:if>

		<%@ include file="/common/messages.jsp"%>

		<h3>
			<fmt:message key="label.score" />
		</h3>
		
		<h3>
			<fmt:message key="label.you.ve.got" >
				<fmt:param>${score}%</fmt:param>
			</fmt:message>
		</h3>

		<c:if test="${sessionMap.reflectOn}">
			<div class="small-space-top">
				<h3>
					${sessionMap.reflectInstructions}
				</h3>

				<c:choose>
					<c:when test="${empty sessionMap.reflectEntry}">
						<p>
							<em> <fmt:message key="message.no.reflection.available" />
							</em>
						</p>
					</c:when>
					<c:otherwise>
						<p>
							<fmt:message key="label.your.answer" />
						</p>
						<p>
							<lams:out escapeHtml="true" value="${sessionMap.reflectEntry}" />
						</p>
					</c:otherwise>
				</c:choose>

				<c:if test="${(mode != 'teacher') && isUserLeader}">
					<html:button property="finishButton" onclick="return continueReflect()" styleClass="button">
						<fmt:message key="label.edit" />
					</html:button>
				</c:if>
				
				<c:if test="${fn:length(reflections) > 0}">
					<h2>
						<fmt:message key="label.other.groups" />
					</h2>
					
				    <c:forEach var="reflectDTO" items="${reflections}">
				    	<div>
				    		${reflectDTO.groupName}: <c:out value="${reflectDTO.reflection}" escapeXml="true"></c:out> 
				    	</div>
			        </c:forEach>
		        </c:if>
			</div>
		</c:if>

		<c:if test="${mode != 'teacher'}">
			<div class="space-bottom-top align-right">
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
			</div>
		</c:if>

	</div>
	<!--closes content-->

	<div id="footer">
	</div>
	<!--closes footer-->

</body>
</lams:html>
