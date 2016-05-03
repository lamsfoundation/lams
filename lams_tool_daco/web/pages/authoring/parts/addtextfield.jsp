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
	<div class="panel-title"><fmt:message key="label.authoring.basic.textfield" /></div>
</div>

<div class="panel-body">

<!-- Add question form-->
<%@ include file="/common/messages.jsp"%>

<html:form action="/authoring/saveOrUpdateQuestion" method="post" styleId="dacoQuestionForm">
	<html:hidden property="sessionMapID" />
	<input type="hidden" id="questionType" name="questionType" value="1" />
	<html:hidden property="questionIndex" />

	<p><fmt:message key="label.authoring.basic.textfield.help" /></p>

	<%@ include file="description.jsp"%>
  	
  	<!--  Options -->  
	<a id="toggleAdditionalOptionsAreaLink" href="javascript:toggleAdditionalOptionsArea()" class="btn btn-default btn-xs pull-right"><fmt:message key="label.authoring.basic.additionaloptions.show" /> </a>
	<div id="additionalOptionsArea" style="display: none;">
 		<div class="form-inline">
 		<div class="form-group">
    	<label for=max><fmt:message key="label.authoring.basic.max.char" /></label>
		<html:text styleId="max" property="max" size="10"  styleClass="form-control"/>
		</div>
		<div class="checkbox">
		    <label>
	 	      <html:checkbox property="questionRequired" styleId="questionRequired"/>&nbsp;<fmt:message key="label.authoring.basic.required" />
		    </label>
	  	</div>
	  	</div>
	</div>
 	<!--  end options -->
  			
</html:form>

<c:set var="addButtonMessageKey" value="label.authoring.basic.textfield.add" />
<%@ include file="buttons.jsp"%>

</div>
</div>
</body>
</lams:html>
