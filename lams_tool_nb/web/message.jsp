<%@ include file="/includes/taglibs.jsp"%>



<script type="text/javascript">
	
	function disableFinishButton() {
		var elem = document.getElementById("finishButton");
		elem.disabled = true;
	}
	 function submitForm(methodName){
                var f = document.getElementById('messageForm');
                f.submit();
        }
</script>


<div id="content">
	
	<h1>
		<fmt:message key="activity.title" />
	</h1>
	
	<lams:errors5/>
	
	<div class="activity-bottom-buttons">
		<form:form modelAttribute="messageForm" action="/learner.do" target="_self" onsubmit="disableFinishButton();" id="messageForm">
			<form:hidden path="toolSessionID" />
			<form:hidden path="mode" />
			<form:hidden path="method" value="finish"/>
			<c:choose>
				<c:when test="${reflectOnActivity}">
					<button type="submit" name="method" class="btn btn-primary">
						<fmt:message key="button.continue" />
					</button>
				</c:when>
				<c:otherwise>
					<button type="button" class="btn btn-primary" id="finishButton" onclick="submitForm('finish')">
						<c:choose>
							<c:when test="${isLastActivity}">
								<fmt:message key="button.submit" />
							</c:when>
							<c:otherwise>
								<fmt:message key="button.finish" />
							</c:otherwise>
						</c:choose>
					</button>
				</c:otherwise>
			</c:choose>
		</form:form>
	</div>

</div>
