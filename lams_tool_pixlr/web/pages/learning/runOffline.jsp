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
		${pixlrDTO.title}
	</h1>

	<p>
		<fmt:message key="message.runOfflineSet" />
	</p>

	<c:if test="${mode == 'learner' || mode == 'author'}">
		<html:form action="/learning" method="post" onsubmit="disableFinishButton();" styleId="messageForm">
			<html:hidden property="dispatch" value="finishActivity" />
			<html:hidden property="toolSessionID" />

			<div align="right" class="space-bottom-top">
				<html:link href="javascript:;" styleClass="button" styleId="finishButton" onclick="submitForm('finish')">
					<span class="nextActivity"><fmt:message>button.finish</fmt:message></span>
				</html:link>
			</div>
		</html:form>
	</c:if>
</div>

