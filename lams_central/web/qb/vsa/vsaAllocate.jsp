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
			body {
				padding: 10px;
			}
			
			#page-description, .question-description {
				margin-bottom: 20px;
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
	
	   		function closePage() { 
	   			self.parent.tb_remove();
	   		}
  		</script>
  		<lams:JSImport src="includes/javascript/vsaAllocate.js" />
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

	
	<c:forEach var="questionDto" items="${questions}">
		<c:set var="question" value="${questionDto.key}" />
		<c:set var="notAllocatedAnswers" value="${questionDto.value}" />
		<%@ include file="vsaQuestionAllocate.jsp"%>
	</c:forEach>		
	
	<div id="footer">
	</div>
</body>
</lams:html>