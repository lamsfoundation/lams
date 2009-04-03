<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">
	function disableFinishButton() {
		document.getElementById("finishButton").disabled = true;
	}
</script>

<div id="content">
	<h1>
		<c:out value="${reflectTitle}" escapeXml="false" />
	</h1>
	<html:form action="/learning" method="post" onsubmit="disableFinishButton();">
		<p>
			<lams:out value="${reflectInstructions}" />
		</p>
		
		<c:choose>
			<c:when test="${contentEditable}">
				<html:textarea cols="60" rows="8" property="entryText" value="${reflectEntry}" styleClass="text-area">
				</html:textarea>
			</c:when>
		
			<c:otherwise>
				<p>
					<c:out value="${reflectEntry}" escapeXml="false" />
				</p>
			</c:otherwise>
		</c:choose>
		
		<div align="right" class="space-bottom-top">
			<html:hidden property="toolSessionID" />
			<html:hidden property="mindmapContent" styleId="mindmapContent" />
			<html:hidden property="mode" />
			<html:hidden property="dispatch" value="finishActivity"/>
			<html:submit styleClass="button" styleId="finishButton">
				<fmt:message key="button.finish" />
			</html:submit>
		</div>

	</html:form>
</div>

