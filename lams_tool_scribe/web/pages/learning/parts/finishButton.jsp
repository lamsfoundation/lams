<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">
	function disableFinishButton() {
		var finishButton = document.getElementById("finishButton");
		if (finishButton != null) {
			finishButton.disabled = true;
		}
	}
         function submitForm(methodName){
                var f = document.getElementById('messageForm');
                f.submit();
        }
</script>

<div class="space-bottom">
	<html:form action="/learning" method="post" onsubmit="disableFinishButton()" styleId="messageForm">
		<html:hidden property="scribeUserUID" value="${scribeUserDTO.uid}" />
		<c:choose>
			<c:when
				test="${!scribeUserDTO.finishedActivity and scribeDTO.reflectOnActivity}">
				<html:hidden property="dispatch" value="openNotebook" />

				<html:submit styleClass="button right-buttons">
					<fmt:message key="button.continue" />
				</html:submit>

			</c:when>
			<c:otherwise>
				<html:hidden property="dispatch" value="finishActivity" />
				<html:link href="javascript:;" styleClass="button right-buttons" styleId="finishButton" onclick="submitForm('finish')">
					<span class="nextActivity"><fmt:message key="button.finish" /></span>
				</html:link>
			</c:otherwise>
		</c:choose>
	</html:form>
</div>
