<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
		"http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
<lams:head>
	<lams:css style="core" />
	<style type="text/css">
		a {
			margin: 10px 2px 0px 0px;
			float: right;
		}
		
		textarea {
			margin: 5px 0px 0px 10px;
		}
	</style>
	
	<script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-latest.pack.js"></script>
  	<script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.form.js"></script>
  	<script type="text/javascript">
  		function createQuestion(){
  			$('#pedagogicalPlannerForm').ajaxSubmit({
  				url: "<c:url value='/pedagogicalPlanner.do?dispatch=createPedagogicalPlannerQuestion' />",
  				success: function(responseText){
  					$('#body').html(responseText.substring(responseText.search(/<body/i)));
  				}
  			});
  		}
  	</script>
</lams:head>
<body id="body">
	<%@ include file="/common/messages.jsp"%>
	<h4 class="space-left"><fmt:message key="label.questions"/></h4>
	<html:form action="/pedagogicalPlanner.do?dispatch=saveOrUpdatePedagogicalPlannerForm" styleId="pedagogicalPlannerForm" method="post">
		<html:hidden property="toolContentID" />
		<html:hidden property="valid" styleId="valid" />
		<html:hidden property="callID" styleId="callID" />
		<html:hidden property="activityOrderNumber" styleId="activityOrderNumber" />
		<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
		
		<c:forEach var="questionIndex"  begin="1" end="${formBean.questionCount}" >
			<html:textarea property="question[${questionIndex-1}]" cols="65" rows="4" />
		</c:forEach>
	</html:form>
	<a class="button" href="javascript:createQuestion();"><fmt:message key="label.add.new.question" /></a>
</body>
</lams:html>