<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
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

<body class="stripes">
			<h1>
				${sessionMap.title}
			</h1>
			
			<div id="content">
				<table>
					<tr>
						<td>
						<h2>
							<fmt:message key="run.offline.message" />
						</h2>
						</td>
					</tr>
				</table>						
				<div class="right-buttons">
					<c:choose>
						<c:when test="${sessionMap.reflectOn && (not sessionMap.finishedLock)}">
							<html:button property="FinishButton"  onclick="return continueReflect()"  styleClass="button">
								<fmt:message key="label.continue" />
							</html:button>
						</c:when>
						<c:otherwise>
							<html:button property="FinishButton"  onclick="return finishSession()"  styleClass="button">
								<fmt:message key="label.finished" />
							</html:button>
						</c:otherwise>
					</c:choose>		
				</div>
			</div>
	<div id="footer">
		</div>
		<!--closes footer-->

</body>
</html:html>
