<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="lams"><lams:LAMSURL /></c:set>

<c:set var="title"><fmt:message key="label.vsa.allocate.button" /></c:set>
<lams:PageMonitor title="${title}" hideHeader="true">
	<link href="${lams}css/vsaAllocate.css" rel="stylesheet" type="text/css">
	<style>
		#questions-container {
			margin: auto;
		}
			
		.question-description {
			margin-bottom: 20px;
		}
	</style>

	<lams:JSImport src="includes/javascript/portrait5.js" />
	<lams:JSImport src="includes/javascript/Sortable.js" />
	<script>
		var VS_ANSWER_ALLOCATED_ALERT = "<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.someone.allocated.this.answer' /></spring:escapeBody>",
			VS_ANSWER_DEALLOCATE_CONFIRM = "<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.vsa.deallocate.confirm' /></spring:escapeBody>",
			LAMS_URL = "${lams}",
			csrfTokenName = "<csrf:tokenname/>",
			csrfTokenValue = "<csrf:tokenvalue/>";
  	    
	   	function refreshPage() { 
	    	location.reload();
	  	}
  	</script>
  	<lams:JSImport src="includes/javascript/vsaAllocate.js" />
  	
  	<div id="container-main">
		<h1 class="fs-4 mb-4">
			<fmt:message key="label.vsa.allocate.description" />
		</h1>
	
		<div id="questions-container">
			<c:forEach var="toolQuestionEntry" items="${toolQuestions}" varStatus="toolQuestionStatus">
				<c:set var="toolQuestion" value="${toolQuestionEntry.key}" />
				<c:set var="notAllocatedAnswers" value="${toolQuestionEntry.value}" />
				<%@ include file="vsaQuestionAllocate.jsp"%>
			</c:forEach>		
		</div>
		
		<div class="activity-bottom-buttons">
			<button type="button" onclick="refreshPage()" class="btn btn-primary">
				<i class="fa fa-refresh me-1"></i>
				<fmt:message key="label.refresh" /> 
			</button>
		</div>
	</div>
</lams:PageMonitor>
