<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
        

<lams:html>
<lams:head>
	<%@ include file="/common/header.jsp"%>
	<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
	<c:set var="daco" value="${sessionMap.daco}" />
	
	<title>
		<fmt:message key="label.learning.title" />
	</title>
</lams:head>
<body class="stripes">

<script type="text/javascript">
         function submitForm(methodName){
                var f = document.getElementById('messageForm');
                f.submit();
        }
</script>

	<lams:Page type="learner" title="${daco.title}">

	<html:form action="/learning/submitReflection" method="post" onsubmit="javascript:document.getElementById('finishButton').disabled = true;" styleId="messageForm">
		<html:hidden property="userId" />
		<html:hidden property="sessionId" />
		<html:hidden property="sessionMapID" />
		
			<%@ include file="/common/messages.jsp"%>

			<p>
				<lams:out value="${daco.reflectInstructions}" escapeHtml="true" />
			</p>

			<html:textarea styleId="focused" rows="5" property="entryText" styleClass="form-control"></html:textarea>

			<div class="space-bottom-top align-right">
				<button type="submit" class="btn btn-primary voffset5 pull-right na" id="finishButton" onclick="submitForm('finish')">
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
				</button>
			</div>
		</div>
	</html:form>
	
	<div id="footer">
	</div>
	<!--closes footer-->

	</lams:Page>

</body>
</lams:html>

<script type="text/javascript">
	window.onload = function() {
		document.getElementById("focused").focus();
	}
</script>

