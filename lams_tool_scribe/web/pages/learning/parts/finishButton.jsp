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

<div class="voffset10">
	<html:form action="/learning" method="post" onsubmit="disableFinishButton()" styleId="messageForm">
		<html:hidden property="scribeUserUID" value="${scribeUserDTO.uid}" />
		<c:choose>
			<c:when
				test="${!scribeUserDTO.finishedActivity and scribeDTO.reflectOnActivity}">
				<html:hidden property="dispatch" value="openNotebook" />

				<html:submit styleClass="btn btn-primary pull-right">
					<fmt:message key="button.continue" />
				</html:submit>

			</c:when>
			<c:otherwise>
				<html:hidden property="dispatch" value="finishActivity" />
				<html:link href="#nogo" styleClass="btn btn-primary pull-right na" styleId="finishButton" onclick="submitForm('finish')">
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
			</c:otherwise>
		</c:choose>
	</html:form>
</div>
