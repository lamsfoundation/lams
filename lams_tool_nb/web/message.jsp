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
	
	<c:set var="errorKey" value="GLOBAL" />
        <c:if test="${not empty errorMap and not empty errorMap[errorKey]}">
            <lams:Alert id="error" type="danger" close="false">
                <c:forEach var="error" items="${errorMap[errorKey]}">
                    <c:out value="${error}" />
                </c:forEach>
            </lams:Alert>
        </c:if>
	
	<div class="align-right space-bottom-top">
		<form:form modelAttribute="messageForm" action="/learner.do" target="_self" onsubmit="disableFinishButton();" id="messageForm">
			<form:hidden path="toolSessionID" />
			<form:hidden path="mode" />
			<form:hidden path="method" value="finish"/>
			<c:choose>
				<c:when test="${reflectOnActivity}">
					<input type="submit" name="method" class="button">
						<fmt:message key="button.continue" />
					</input>
				</c:when>
				<c:otherwise>
					<a href="#nogo" class="button" id="finishButton" onclick="submitForm('finish')">
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
					</a>
				</c:otherwise>
			</c:choose>
		</form:form>
	</div>

</div>
