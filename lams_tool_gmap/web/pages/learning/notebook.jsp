<%@ include file="/common/taglibs.jsp"%>
<%
	//String EMPTYMARKERS = "&lt;?xml version=\"1.0\"?&gt;&lt;markers&gt;&lt;/markers&gt;";
	String EMPTYMARKERS = "<?xml version=\"1.0\"?><markers></markers>";
	
%>
<script type="text/javascript">
	function disableFinishButton() {
		document.getElementById("finishButton").disabled = true;
	}
</script>

<div id="content">
	<h1>
		${gmapDTO.title}
	</h1>

	<html:form action="/learning" method="post" onsubmit="disableFinishButton();">
		<html:hidden property="toolSessionID" styleId="toolSessionID"/>
		<html:hidden property="markersXML" />
		<html:hidden property="mode" value="${mode}" />
		
		<p class="small-space-top">
			<lams:out value="${gmapDTO.reflectInstructions}" />
		</p>

		<html:textarea cols="60" rows="8" property="entryText"
			styleClass="text-area"></html:textarea>

		<div class="space-bottom-top align-right">
			<html:hidden property="dispatch" value="submitReflection" />
			<html:submit styleClass="button" styleId="finishButton">
				<fmt:message>button.finish</fmt:message>
			</html:submit>
		</div>
	</html:form>
</div>

