<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>

<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" />
	</title>
	<%@ include file="/common/header.jsp"%>
	
	<script type="text/javascript">
		function disableFinishButton() {
			document.getElementById("finishButton").disabled = true;
		}
		
		function submitForm(methodName){
			var f = document.getElementById('reflectionForm');
			f.submit();
		}
	</script>
</lams:head>
<body class="stripes">

	<c:set var="sessionMapID" value="${param.sessionMapID}" />
	<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
	
	<html:form action="/learning/submitReflection" method="post" onsubmit="disableFinishButton();" styleId="reflectionForm">
		<html:hidden property="userID" />
		<html:hidden property="sessionMapID" />

		<lams:Page type="learner" title="${sessionMap.title}">

			<%@ include file="/common/messages.jsp"%>

			<p>
				<lams:out value="${sessionMap.reflectInstructions}" escapeHtml="true"/>
			</p>
			
			<html:textarea styleId="focused" rows="5" property="entryText" styleClass="form-control"></html:textarea>

			<div class="space-bottom-top align-right">
				<html:link href="#nogo" styleClass="btn btn-primary voffset5 pull-right na" styleId="finishButton" onclick="submitForm('finish')">
					<fmt:message key="label.finished" />
				</html:link>
			</div>
		</lams:Page>
	</html:form>

	<div id="footer">
	</div>
	<!--closes footer-->

<script type="text/javascript">
	window.onload = function() {
		document.getElementById("focused").focus();
	}
</script>

</body>
</lams:html>


