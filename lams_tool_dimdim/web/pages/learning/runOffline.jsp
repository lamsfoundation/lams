<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">
	function disableFinishButton() {
		document.getElementById("finishButton").disabled = true;
	}
</script>

<div id="content">
	<h1>
		${contentDTO.title}
	</h1>

	<p>
		<fmt:message key="message.runOfflineSet" />
	</p>

	<c:if test="${mode == 'learner' || mode == 'author'}">
		<html:form action="/learning" method="post"
			onsubmit="disableFinishButton();">
			<html:hidden property="dispatch" value="finishActivity" />
			<html:hidden property="toolSessionID" />

			<div align="right" class="space-bottom-top">
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
	</c:if>
</div>

