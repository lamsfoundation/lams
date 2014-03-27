<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">
	function disableFinishButton() {
		document.getElementById("finishButton").disabled = true;
	}
</script>

<div id="content">
	<h1>
		<c:out value="${contentDTO.title}" escapeXml="true"/>
	</h1>

	<html:form action="/learning" method="post"
		onsubmit="disableFinishButton();">

		<p class="small-space-top">
			<lams:out value="${contentDTO.reflectInstructions}" escapeHmtl="true"/>
		</p>

		<html:textarea cols="60" rows="8" property="entryText"
			styleClass="text-area"></html:textarea>

		<div class="space-bottom-top align-right">
			<html:hidden property="dispatch" value="submitReflection" />
			<html:hidden property="toolSessionID" />
			<html:submit styleClass="button" styleId="finishButton">
			    <c:choose>
 					<c:when test="${activityPosition.last}">
 						<fmt:message key="button.submit" />
 					</c:when>
 					<c:otherwise>
 		 				<fmt:message key="button.finish" />
 					</c:otherwise>
	 			</c:choose>
			</html:submit>
		</div>
	</html:form>
</div>

