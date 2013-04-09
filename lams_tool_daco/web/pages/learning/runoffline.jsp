<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@include file="/common/taglibs.jsp"%>

<lams:html>
<lams:head>
	<%@ include file="/common/header.jsp"%>
	<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
	<c:set var="daco" value="${sessionMap.daco}" />
	
	<script type="text/javascript">
	<!--
		function finishSession(){
			document.getElementById("finishButton").disabled = true;
			document.location.href ='<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}&runOffline=true"/>';
			return false;
		}
		function continueReflect(){
			document.location.href='<c:url value="/learning/startReflection.do?sessionMapID=${sessionMapID}"/>';
		}
		
	-->        
    </script>
</lams:head>

<body class="stripes">
	<div id="content">
		<h1>
			${daco.title}
		</h1>
		<%-- Just a message and reflection entry (if exists) --%>
		<p>
			<fmt:message key="message.learning.offline" />
		</p>

		<c:if test="${sessionMap.userFinished and daco.reflectOnActivity}">
			<div class="small-space-top">
				<h2>
					${daco.reflectInstructions}
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

				<html:button property="FinishButton"
					onclick="javascript:continueReflect()" styleClass="button">
					<fmt:message key="label.common.edit" />
				</html:button>
			</div>
		</c:if>

		<div class="space-bottom-top align-right">
			<c:choose>
				<c:when
					test="${daco.reflectOnActivity && (not sessionMap.userFinished)}">
					<html:button property="FinishButton"
						onclick="javascript:continueReflect()" styleClass="button">
						<fmt:message key="label.learning.continue" />
					</html:button>
				</c:when>
				<c:otherwise>
					<html:link href="#nogo" property="FinishButton" styleId="finishButton"
						onclick="return finishSession()" styleClass="button">
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
				</c:otherwise>
			</c:choose>
		</div>
	</div>
	<div id="footer">
	</div>
	<!--closes footer-->

</body>
</lams:html>
