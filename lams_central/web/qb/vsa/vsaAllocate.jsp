<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="lams"><lams:LAMSURL /></c:set>

<lams:html>
	<lams:head>
	
		<title><fmt:message key="label.vsa.allocate.button" /></title>
		
		<lams:css />
		<link href="${lams}css/vsaAllocate.css" rel="stylesheet" type="text/css">
		<style>
			#questions-container {
				max-width: 1550px;
				margin: auto;
			}
			
			#page-description {
				max-width: 1550px;
				text-align: center;
				background-color: white;
				padding: 20px;
				margin: 5px auto;
			}
			
			.question-description {
				margin-bottom: 20px;
			}
			
			#questions-refresh-button {
				position: fixed;
				top: 10px;
				right: 10px;
				z-index: 2;
			}
		</style>
		
		<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/bootstrap.tabcontroller.js"></script>
	 	<script type="text/javascript" src="${lams}includes/javascript/portrait.js"></script>
	 	<script type="text/javascript" src="${lams}includes/javascript/Sortable.js"></script>
  	    <script>
			var VS_ANSWER_ALLOCATED_ALERT = "<fmt:message key="label.someone.allocated.this.answer" />",
				VS_ANSWER_DEALLOCATE_CONFIRM = "<fmt:message key="label.vsa.deallocate.confirm" />",
				LAMS_URL = "${lams}",
				csrfTokenName = "<csrf:tokenname/>",
				csrfTokenValue = "<csrf:tokenvalue/>";
  	    
	   		function refreshPage() { 
	       		location.reload();
	   		}
  		</script>
  		<lams:JSImport src="includes/javascript/vsaAllocate.js" />
	</lams:head>
	
<body class="stripes">
	<a href="#nogo" onclick="javascript:refreshPage()" class="btn btn-primary" id="questions-refresh-button">
		<i class="fa fa-refresh"></i>
		<fmt:message key="label.refresh" /> 
	</a>
		
		
	<h4 id="page-description"><fmt:message key="label.vsa.allocate.description" /></h4>

	<div id="questions-container">
		<c:forEach var="toolQuestionEntry" items="${toolQuestions}">
			<c:set var="toolQuestion" value="${toolQuestionEntry.key}" />
			<c:set var="notAllocatedAnswers" value="${toolQuestionEntry.value}" />
			<%@ include file="vsaQuestionAllocate.jsp"%>
		</c:forEach>		
	</div>
	
	<div id="footer">
	</div>
</body>
</lams:html>