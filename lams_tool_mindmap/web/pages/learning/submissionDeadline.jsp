<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">
	function disableFinishButton() {
		document.getElementById("finishButton").disabled = true;
	}
	function submitForm(methodName){
		var f = document.getElementById('Form');
		f.submit();
	}
</script>

<lams:Page type="learner" title="${mindmapDTO.title}">

	<lams:Alert id="deadline" type="danger" close="false">
		<fmt:message key="authoring.info.teacher.set.restriction" >
			<fmt:param><lams:Date value="${submissionDeadline}" /></fmt:param>
		</fmt:message>
	</lams:Alert>

	<c:if test="${mode == 'learner' || mode == 'author'}">
		<html:form action="/learning" method="post" onsubmit="disableFinishButton();" styleId="Form">
			<html:hidden property="dispatch" value="finishActivity" />
			<html:hidden property="toolSessionID" />

			<div class="voffset10 pull-right">
				<html:link href="#nogo" styleClass="btn btn-primary na" styleId="finishButton" onclick="submitForm('finish')">
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
			</div>
		</html:form>
	</c:if>
	
</lams:Page>

