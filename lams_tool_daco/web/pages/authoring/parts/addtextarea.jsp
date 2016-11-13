<!DOCTYPE html>
		

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
<lams:head>
	<%@ include file="/common/header.jsp"%>
	<lams:css/>
	<!-- To use in external script files. -->
	<script type="text/javascript">
	   var msgShowAdditionalOptions = "<fmt:message key='label.authoring.basic.additionaloptions.show' />";
       var msgHideAdditionalOptions = "<fmt:message key='label.authoring.basic.additionaloptions.hide' />";
	   	//Initial behavior
	   	$(document).ready(function() {
	   		defaultShowAdditionaOptionsArea();
	   	});
	</script>
</lams:head>
<body class="tabpart">

<div class="panel panel-default">
<div class="panel-heading">
	<div class="panel-title"><fmt:message key="label.authoring.basic.textarea" /></div>
</div>

<div class="panel-body">

<!-- Add question form-->
<%@ include file="/common/messages.jsp"%>
<html:form action="/authoring/saveOrUpdateQuestion" method="post" styleId="dacoQuestionForm">
	<html:hidden property="sessionMapID" />
	<input type="hidden" id="questionType" name="questionType" value="2" />
	<html:hidden property="questionIndex" />
	
	<p><fmt:message key="label.authoring.basic.textarea.help" /></p>
	
	<%@ include file="description.jsp"%>
 
   	<!--  Options -->  
	<a href="javascript:toggleAdditionalOptionsArea()" class="fa-xs"><i id="faIcon" class="fa fa-plus-square-o"></i> <span id="toggleAdditionalOptionsAreaLink"><fmt:message key="label.authoring.basic.additionaloptions.show" /></span></a>
	<div id="additionalOptionsArea" style="display: none;" class="panel-body">
 		<div class="form-group form-inline">
			<div class="checkbox">
			    <label>
		 	      <html:checkbox property="questionRequired" styleId="questionRequired"/>&nbsp;<fmt:message key="label.authoring.basic.required" />
			    </label>
	  	</div>
		</div>
 		<div class="form-group form-inline">
    	<label for="max"><fmt:message key="label.authoring.basic.max.word" />:</label>
	 		<html:text styleId="max" property="max" size="10"  styleClass="form-control"/>
		</div>	  	
 </div>
 	<!--  end options -->
  	
</html:form>

<c:set var="addButtonMessageKey" value="label.authoring.basic.textarea.add" />
<%@ include file="buttons.jsp"%>

</div>
</div>

</body>
</lams:html>
