<%@ tag body-content="scriptless"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %> 

<%@ attribute name="toolSessionID" required="true" rtexprvalue="true"%>
<%@ attribute name="title" required="true" rtexprvalue="true"%>
<%@ attribute name="instructions" required="true" rtexprvalue="true"%>
<%@ attribute name="formActionUrl" required="true" rtexprvalue="true"%>
<%@ attribute name="formModelAttribute" required="false" rtexprvalue="true"%>
<c:if test="${empty formModelAttribute}">
	<c:set var="formModelAttribute" value="reflectionForm" />
</c:if>
<%@ attribute name="isNextActivityButtonSupported" required="true" rtexprvalue="true"%>
<%@ attribute name="isLastActivity" required="false" rtexprvalue="true"%>
<%@ attribute name="hiddenInputs" required="false" rtexprvalue="true"%>
<c:if test="${empty hiddenInputs}">
	<c:set var="hiddenInputs" value="userID,sessionMapID" />
</c:if>
<%@ attribute name="isNbTool" required="false" rtexprvalue="true"%>
<c:if test="${empty isNbTool}">
	<c:set var="isNbTool" value="false" />
</c:if>

<%@ attribute name="notebookLabelKey" required="false" rtexprvalue="true"%>
<c:if test="${empty notebookLabelKey}">
	<c:set var="notebookLabelKey" value="label.notebook" />
</c:if>
<%@ attribute name="finishButtonLabelKey" required="false" rtexprvalue="true"%>
<c:if test="${empty finishButtonLabelKey}">
	<c:set var="finishButtonLabelKey" value="label.submit" />
</c:if>
<%@ attribute name="nextActivityButtonLabelKey" required="false" rtexprvalue="true"%>
<c:if test="${empty nextActivityButtonLabelKey}">
	<c:set var="nextActivityButtonLabelKey" value="label.finished" />
</c:if>

<lams:PageLearner title="${title}" toolSessionID="${toolSessionID}" >
	<script type="text/javascript">
		checkNextGateActivity('finish-button', '${toolSessionID}', '', submitForm);
		
		function disableFinishButton() {
			document.getElementById("finish-button").disabled = true;
		}
		function submitForm() {
			var f = document.getElementById('reflectionForm');
			f.submit();
		}
		window.onload = function() {
			document.getElementById("focused").focus();
		}
	</script>
	
	<div id="instructions" class="instructions">
		<lams:out value="${instructions}" escapeHtml="true" />
	</div>

	<form:form action="${formActionUrl}" modelAttribute="${formModelAttribute}" method="post" onsubmit="disableFinishButton();" id="reflectionForm">
		<c:forTokens items="${hiddenInputs}" delims="," var="hiddenInput"> 
			<form:hidden path="${hiddenInput}" />
		</c:forTokens>	
			
		<lams:errors5/>
		
		<div class="container-lg">
			<div class="card lcard">
				<div class="card-header lcard-header-button-border">
					<fmt:message key="${notebookLabelKey}" />
				</div>
				<div class="card-body mb-3">
					<fmt:message key="${notebookLabelKey}" var="notebookLabel"/>
					<form:textarea aria-label="${notebookLabel}" aria-multiline="true"
						aria-required="true" required="true" path="entryText"
						cssClass="form-control" id="focused" rows="5"></form:textarea>
				</div>
			</div>
	
			<div class="activity-bottom-buttons">
				<button name="finishButton" id="finish-button" class="btn btn-primary na">
					<c:choose>
						<c:when test="${!isNextActivityButtonSupported || isLastActivity}">
							<fmt:message key="${finishButtonLabelKey}" />
						</c:when>
						<c:otherwise>
							<fmt:message key="${nextActivityButtonLabelKey}" />
						</c:otherwise>
					</c:choose>
				</buttun>
			</div>
		</div>
	</form:form>
	
	<c:if test="${isNbTool}">
		<!-- Comments: the extra div counteracts the float -->
		<c:if test="${allowComments}">
			<div class="row g-0"><div class="col-12"></div></div>
			<lams:Comments toolSessionId="${toolSessionID}"
						   toolSignature="lanb11" likeAndDislike="${likeAndDislike}" readOnly="true"
						   pageSize="10" sortBy="1" />
		</c:if>
		<!-- End comments -->
	</c:if>

</lams:PageLearner>