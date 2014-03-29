<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">
	function disableFinishButton() {
		document.getElementById("finishButton").disabled = true;
	}
	function submitForm(methodName){
 		var f = document.getElementById('messageForm');
		f.submit();
 	}
</script>

<div id="content">
	<h1>
		<c:out value="${reflectTitle}" escapeXml="true" />
	</h1>
	<html:form action="/learning" method="post" onsubmit="disableFinishButton();" styleId="messageForm">
		<p>
			<lams:out value="${reflectInstructions}" escapeHtml="true"/>
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
			<html:link  href="#" styleClass="button" styleId="finishButton" onclick="submitForm('finish')">
				<span class="nextActivity">
					<c:choose>
						<c:when test="${activityPosition.last}">
							<fmt:message key="button.submit" />
						</c:when>
						<c:otherwise>
							<fmt:message key="button.finish" />
						</c:otherwise>
					</c:choose>
				</span>
			</html:link>
		</div>

	</html:form>
</div>

