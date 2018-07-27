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

<lams:Page type="learner" title="${scribeDTO.title}">

	<html:form action="/learning" method="post" onsubmit="disableFinishButton();" styleId="messageForm">
		<html:hidden property="dispatch" value="submitReflection" />
		<html:hidden property="scribeUserUID" />

		<div class="panel">
			<lams:out value="${scribeDTO.reflectInstructions}" escapeHtml="true"/>
		</div>

		<html:textarea rows="5" styleId="focused" property="entryText"
			styleClass="form-control"></html:textarea>

		
			<html:link href="#nogo" styleClass="btn btn-primary voffset10 pull-right na" styleId="finishButton" onclick="submitForm('finish')">
				<span class="nextActivity">
					<c:choose>
	 					<c:when test="${activityPosition.last}">
	 						<fmt:message key="button.submitActivity" />
	 					</c:when>
	 					<c:otherwise>
	 		 				<fmt:message key="button.finish" />
	 					</c:otherwise>
	 				</c:choose>
	 			</span>
			</html:link>

	</html:form>
</lams:Page>


<script type="text/javascript">
	window.onload = function() {
		document.getElementById("focused").focus();
	}
</script>

