<%@ include file="/common/taglibs.jsp"%>

<form:form action="openNotebook.do" method="post" modelAttribute="learningForm" id="learningForm">
	<form:hidden path="chatUserUID" value="${chatUserDTO.uid}" />

	<c:if
		test="${chatUserDTO.finishedActivity and chatDTO.reflectOnActivity}">
		<div class="space-top">
			<h3><fmt:message key="button.reflect" /> </h3>
			<p>
				<strong><lams:out value="${chatDTO.reflectInstructions}" escapeHtml="true"/></strong>
			</p>

			<p>
				<c:choose>
					<c:when test="${not empty chatUserDTO.notebookEntry}">
						<lams:out escapeHtml="true" value="${chatUserDTO.notebookEntry}" />
					</c:when>

					<c:otherwise>
						<em><fmt:message key="message.no.reflection.available" /> </em>
					</c:otherwise>
				</c:choose>
			</p>

			<input type="submit" class="button" value="<fmt:message key="button.edit" />">
		</div>
	</c:if>
</form:form>

<script type="text/javascript">
	function disableFinishButton() {
		var finishButton = document.getElementById("finishButton");
		if (finishButton != null) {
			finishButton.disabled = true;
		}
	}
         function submitForm(methodName){
                var f = document.getElementById('learningForm');
                f.submit();
        }
</script>

<form:form action="${!chatUserDTO.finishedActivity and chatDTO.reflectOnActivity ? 'openNotebook.do' : 'finishActivity.do'}" method="post"
	onsubmit="disableFinishButton();"  modelAttribute="learningForm" id="learningForm">
	<form:hidden path="chatUserUID" value="${chatUserDTO.uid}" />

		<c:choose>
			<c:when
				test="${!chatUserDTO.finishedActivity and chatDTO.reflectOnActivity}">

				<input type="submit" value="<fmt:message key="button.continue" />" class="btn btn-responsive btn-primary pull-right voffset10"/>
			</c:when>
			<c:otherwise>
				<a href="#nogo" class="btn btn-primary pull-right voffset10 na btn-autoresize" id="finishButton"  onclick="submitForm('finish')">
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
	</div>
</form:form>
