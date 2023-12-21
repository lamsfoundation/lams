<!DOCTYPE html>
		

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
<lams:head>
	<%@ include file="/common/header.jsp"%>
	<lams:css/>
	<!-- To use in external script files. -->
	<script type="text/javascript">
	   var msgShowAdditionalOptions = "<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.authoring.basic.additionaloptions.show' /></spring:escapeBody>";
       var msgHideAdditionalOptions = "<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.authoring.basic.additionaloptions.hide' /></spring:escapeBody>";
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
<lams:errors/>

<form:form action="saveOrUpdateQuestion.do" modelAttribute="questionForm" method="post" id="dacoQuestionForm">
	<form:hidden path="sessionMapID" />
	<input type="hidden" id="questionType" name="questionType" value="1" />
	<form:hidden path="questionIndex" />

	<p><fmt:message key="label.authoring.basic.textfield.help" /></p>

	<%@ include file="description.jsp"%>
  	
  	<!--  Options -->  
	<a href="javascript:toggleAdditionalOptionsArea()" class="fa-xs"><i id="faIcon" class="fa fa-plus-square-o"></i> <span id="toggleAdditionalOptionsAreaLink"><fmt:message key="label.authoring.basic.additionaloptions.show" /></span></a>
	<div id="additionalOptionsArea" style="display: none;" class="panel-body">
		<div class="form-group form-inline">
			<div class="checkbox">
		    <label>
	 	      <form:checkbox path="questionRequired" id="questionRequired"/>&nbsp;<fmt:message key="label.authoring.basic.required" />
		    </label>
	  	</div>
	  </div>
		<div class="form-group form-inline">
	   	<label for=max><fmt:message key="label.authoring.basic.max.char" />: </label>
			<form:input id="max" path="max" size="10"  cssClass="form-control input-sm"/>
		</div>
	</div>
 	<!--  end options -->
  			
</form:form>

<c:set var="addButtonMessageKey" value="label.authoring.basic.textfield.add" />
<br/>
<%@ include file="buttons.jsp"%>

</div>
</div>
</body>
</lams:html>
