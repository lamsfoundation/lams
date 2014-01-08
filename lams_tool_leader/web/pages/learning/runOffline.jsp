<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">
    function finishActivity(){
    	document.getElementById("finishButton").disabled = true;
		location.href = '<c:url value="/learning.do"/>?dispatch=finishActivity&toolSessionID=${toolSessionID}';
    }
</script>

<div id="content">
	<h1>
		${content.title}
	</h1>
	
	<p>
		<fmt:message key="message.runOfflineSet" />
	</p>
	
	<c:if test="${mode == 'learner' || mode == 'author'}">

			<div align="right" class="space-bottom-top">
				<html:link href="#nogo" styleClass="button" styleId="finishButton" onclick="finishActivity()">
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
	</c:if>
</div>

