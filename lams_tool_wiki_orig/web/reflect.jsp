<%@ include file="/includes/taglibs.jsp"%>

<script type="text/javascript">
	function disableFinishButton() {
		document.getElementById("finishButton").disabled = true;
	}
</script>

<div id="content">
<h1>
	<c:out value="${title}" escapeXml="false" />
</h1>

	<html:form action="/learner" method="post" onsubmit="disableFinishButton();">
	 
		<p><lams:out value="${reflectInstructions}" /></p>				
			 
		<html:textarea cols="60" rows="8" property="reflectionText" value="${reflectEntry}" styleClass="text-area"></html:textarea>
		 
		<div align="right" class="space-bottom-top">
			<html:hidden property="toolSessionID" />
			<html:hidden property="mode" />
			<html:hidden property="method" value="finish"/>
			<html:submit styleClass="button" styleId="finishButton">
				<fmt:message key="button.finish" />
			</html:submit>
		</div>
			 
	</html:form>
</div>

