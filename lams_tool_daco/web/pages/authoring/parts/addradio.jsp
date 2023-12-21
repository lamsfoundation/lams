<!DOCTYPE html>
		

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
<lams:head>
	<%@ include file="/common/header.jsp"%>
	<lams:css />
	<!-- To use in external script files. -->
	<script type="text/javascript">
	   var removeAnswerOptionUrl = "<c:url value='/authoring/removeAnswerOption.do'/>";
       var addAnswerOptionUrl = "<c:url value='/authoring/newAnswerOption.do'/>";
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
	<div class="panel-title"><fmt:message key="label.authoring.basic.radio" /></div>
</div>

<div class="panel-body">

<!-- Add question form-->
<lams:errors/>
<form:form action="saveOrUpdateQuestion.do" modelAttribute="questionForm" method="post" id="dacoQuestionForm">
	<form:hidden path="sessionMapID" />
	<input type="hidden" id="questionType" name="questionType" value="7" />
	<form:hidden path="questionIndex" />
	<input type="hidden" id="answerOptionList" name="answerOptionList" />

	<p><fmt:message key="label.authoring.basic.radio.help" /></p>

	<%@ include file="description.jsp"%>

  	<!--  Options -->  
	<a href="javascript:toggleAdditionalOptionsArea()" class="fa-xs"><i id="faIcon" class="fa fa-plus-square-o"></i> <span id="toggleAdditionalOptionsAreaLink"><fmt:message key="label.authoring.basic.additionaloptions.show" /></span></a>
	<div id="additionalOptionsArea" style="display: none;" class="panel-body">
		<div class="form-group">
			<div class="checkbox">
			    <label>
		 	      <form:checkbox path="questionRequired" id="questionRequired"/>&nbsp;<fmt:message key="label.authoring.basic.required" />
			    </label>
		 	</div>
		</div>
		<div class="form-group">
    	<label for="summary"><fmt:message key="label.common.summary" /></label>: 
			<form:select path="summary"  cssClass="form-control-inline input-sm">
				<form:option value="0" id="noSummaryOption"><fmt:message key="label.common.summary.none" /></form:option>
				<form:option value="2" ><fmt:message key="label.common.summary.average" /></form:option>
				<form:option value="3" ><fmt:message key="label.common.summary.count" /></form:option>
			</form:select>
		</div>
 	</div>

 	<!--  end options -->

 </form:form>

<!-- Answer options -->

<%@ include file="answeroptions.jsp"%>

<c:set var="addButtonMessageKey" value="label.authoring.basic.radio.add" />
<%@ include file="buttons.jsp"%>

</div>
</div>

</body>
</lams:html>
