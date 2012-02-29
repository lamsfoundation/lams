<%@ include file="/includes/taglibs.jsp"%>

<script type="text/javascript">
	function disableFinishButton() {
		document.getElementById("finishButton").disabled = true;
	}
	function submitForm(methodName){
		var f = document.getElementById('learnerForm');
		f.submit();
	}

</script>

<div data-role="header" data-theme="b" data-nobackbtn="true">
	<h1>
		<c:out value="${title}" escapeXml="false" />
	</h1>
</div>

<html:form action="/learner" method="post" onsubmit="disableFinishButton();" styleId="learnerForm">
	<div data-role="content">
	
		<div style="padding-bottom: 20px;"><lams:out value="${reflectInstructions}" /></div>				

		<html:textarea cols="60" rows="8" property="reflectionText" value="${reflectEntry}" styleClass="text-area"></html:textarea>
	
	</div>

	<div data-role="footer" data-theme="b" class="ui-bar">
		<span class="ui-finishbtn-right">
			<html:hidden property="toolSessionID" />
			<html:hidden property="mode" />
			<html:hidden property="method" value="finish"/>
			<a href="#nogo" id="finishButton" onclick="submitForm('finish')" data-role="button" data-icon="arrow-r" data-theme="b">
				<span class="nextActivity"><fmt:message key="button.finish" /></span>
			</a>
		</span>
	</div>
</html:form>
