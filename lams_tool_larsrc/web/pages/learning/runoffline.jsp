<%@include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
<head>
	<%@ include file="/common/header.jsp"%>

	<script type="text/javascript">
	<!--
		function finishSession(){
			document.location.href ='<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}&runOffline=true"/>';
			return false;
		}
		function continueReflect(){
			document.location.href='<c:url value="/learning/newReflection.do?sessionMapID=${sessionMapID}"/>';
		}
		
	-->        
    </script>
</head>

<body>
	<h1>
		${sessionMap.title}
	</h1>
	<div align="center">
		<h2>
			<fmt:message key="run.offline.message" />
		</h2>
		<p>
		<c:choose>
			<c:when test="${sessionMap.reflectOn}">
				<html:button property="FinishButton"  onclick="return continueReflect()" disabled="${sessionMap.finishedLock}"  styleClass="button">
					<fmt:message key="label.continue" />
				</html:button>
			</c:when>
			<c:otherwise>
				<html:button property="FinishButton"  onclick="return finishSession()" disabled="${sessionMap.finishedLock}"  styleClass="button">
					<fmt:message key="label.finished" />
				</html:button>
			</c:otherwise>
		</c:choose>		
	</div>


</body>
</html:html>
