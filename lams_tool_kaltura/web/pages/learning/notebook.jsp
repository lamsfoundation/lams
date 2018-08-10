<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<c:set var="lams">
		<lams:LAMSURL />
	</c:set>
	<c:set var="tool">
		<lams:WebAppURL />
	</c:set>
	
	<lams:head>  
		<title>
			<fmt:message key="activity.title" />
		</title>
		<lams:css/>
		
		<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
	</lams:head>
	<body class="stripes">
		<c:set var="sessionMapID" value="${param.sessionMapID}" />
		<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
		<c:set var="kaltura" value="${sessionMap.kaltura}" />
		
		<script type="text/javascript">
			function disableFinishButton() {
				document.getElementById("finishButton").disabled = true;
			}
			function submitForm(methodName) {
				var f = document.getElementById('messageForm');
				f.submit();
			}
			$(document).ready(function() {
				document.getElementById("focused").focus();
			});
		</script>
			
		<form:form action="submitReflection.do" modelAttribute="messageForm" method="post" onsubmit="disableFinishButton();" id="messageForm">
			<form:hidden path="userID" />
			<form:hidden path="sessionMapID" />
		
			<lams:Page type="learner" title="${kaltura.title}">
		
				<%@ include file="/common/messages.jsp"%>
		
				<div class="panel">
					<lams:out value="${kaltura.reflectInstructions}" escapeHtml="true"/>
				</div>
		
				<form:textarea id="focused" rows="5" path="entryText" cssClass="form-control" />
		
				<a href="#nogo" cssClass="btn btn-primary voffset10 pull-right" id="finishButton" onclick="submitForm('finish')">
					<span class="na">
						<c:choose>
			 				<c:when test="${sessionMap.activityPosition.last}">
			 					<fmt:message key="button.submit" />
			 				</c:when>
			 				<c:otherwise>
			 	 				<fmt:message key="button.finish" />
			 				</c:otherwise>
			 			</c:choose>
			 		</span>
				</a>
				
			</lams:Page>
		</form:form>		
	</body>
</lams:html>


	
