<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>

<lams:html>
	<lams:head>
	
		<%@ include file="/common/header.jsp"%>
		
		<title><fmt:message key="label.vsa.allocate.button" /></title>
		
		<link href="<lams:WebAppURL />includes/css/vsaAllocate.css" rel="stylesheet" type="text/css">
		<style>
			body {
				padding: 10px;
			}
			
			#page-description, .question-description {
				margin-bottom: 20px;
			}
		</style>
		
	 	<script type="text/javascript" src="${lams}includes/javascript/portrait.js"></script>
	 	<script type="text/javascript" src="${lams}includes/javascript/Sortable.js"></script>
  	    <script>
			var VS_ANSWER_ALLOCATED_ALERT = "<fmt:message key="label.someone.allocated.this.answer" />",
				WEB_APP_URL = "<lams:WebAppURL />",
				csrfTokenName = "<csrf:tokenname/>",
				csrfTokenValue = "<csrf:tokenvalue/>";
  	    
	   		function refreshPage() { 
	       		location.reload();
	   		}
	
	   		function closePage() { 
	   			self.parent.tb_remove();
	   		}
  		</script>
  		<script src="<lams:WebAppURL />includes/javascript/vsaAllocate.js"></script>
	</lams:head>
	
<body>
	<a href="#nogo" onclick="javascript:closePage()" class="btn btn-default pull-right loffset20">
		<i class="fa fa-close"></i>
		<fmt:message key="label.close" /> 
	</a>
	<a href="#nogo" onclick="javascript:refreshPage()" class="btn btn-primary pull-right">
		<i class="fa fa-refresh"></i>
		<fmt:message key="label.refresh" /> 
	</a>
		
	<h4 id="page-description"><fmt:message key="label.vsa.allocate.description" /></h4>

	
	<c:forEach var="questionSummary" items="${questionSummaries}">
		<c:set var="questionDto" value="${questionSummary.questionDto}"/>
		<%@ include file="parts/vsaQuestionAllocate.jsp"%>
	</c:forEach>		
	
	<div id="footer">
	</div>
</body>
</lams:html>