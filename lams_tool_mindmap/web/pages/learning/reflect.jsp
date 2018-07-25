<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">
	function disableFinishButton() {
		document.getElementById("finishButton").disabled = true;
	}
	function submitForm(methodName){
 		var f = document.getElementById('messageForm');
		f.submit();
 	}

	$(document).ready(function() {
		document.getElementById("focused").focus();
	});
</script>

<lams:Page type="learner" title="${reflectTitle}">
	<html:form action="/learning" method="post" onsubmit="disableFinishButton();" styleId="messageForm">
		<html:hidden property="toolSessionID" />
		<html:hidden property="mindmapContent" styleId="mindmapContent" />
		<html:hidden property="mode" />
		<html:hidden property="dispatch" value="finishActivity"/>
	
		<div class="panel">
			<lams:out value="${reflectInstructions}" escapeHtml="true"/>
		</div>
		
		<c:choose>
			<c:when test="${contentEditable}">
				<html:textarea rows="5" property="entryText" value="${reflectEntry}" styleClass="form-control" styleId="focused"/>
			</c:when>
		
			<c:otherwise>
				<p>
					<c:out value="${reflectEntry}" escapeXml="false" />
				</p>
			</c:otherwise>
		</c:choose>
		
		<html:link  href="#nogo" styleClass="btn btn-primary voffset5 pull-right" styleId="finishButton" onclick="submitForm('finish')">
			<span class="na">
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

	</html:form>
</lams:Page>
