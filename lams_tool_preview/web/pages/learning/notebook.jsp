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
	
	<form:form action="submitReflection.do" method="post" onsubmit="disableFinishButton();" modelAttribute="reflectionForm">
		<form:hidden path="userID" />
		<form:hidden path="sessionMapID" />

		<lams:Page type="learner" title="${sessionMap.title}">

			<lams:errors/>

			<p>
				<lams:out value="${sessionMap.reflectInstructions}" escapeHtml="true"/>
			</p>
			
			<form:textarea id="focused" rows="5" path="entryText" cssClass="form-control"></form:textarea>

			<div class="space-bottom-top align-right">
				<a href="#nogo" class="btn btn-primary voffset5 pull-right na" id="finishButton" onclick="submitForm('finish')">
					<fmt:message key="label.finished" />
				</a>
			</div>
		</lams:Page>
	</form:form>

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


