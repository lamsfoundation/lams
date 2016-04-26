<!DOCTYPE html>
		

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
<lams:head>
	<%@ include file="/common/header.jsp"%>
	<lams:css/>
	<!-- To use in external script files. -->
	<script type="text/javascript">
	   var removeAnswerOptionUrl = "<c:url value='/authoring/removeAnswerOption.do'/>";
       var addAnswerOptionUrl = "<c:url value='/authoring/newAnswerOption.do'/>";
	   var msgShowAdditionalOptions = "<fmt:message key='label.authoring.basic.additionaloptions.show' />";
       var msgHideAdditionalOptions = "<fmt:message key='label.authoring.basic.additionaloptions.hide' />";
	</script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<html:rewrite page='/includes/javascript/dacoAuthoring.js'/>"></script>
</lams:head>
<body class="tabpart">

<div class="panel panel-default">
<div class="panel-heading">
	<div class="panel-title"><fmt:message key="label.authoring.basic.checkbox" /></div>
</div>

<div class="panel-body">

<!-- Add question form-->
<%@ include file="/common/messages.jsp"%>
<html:form action="/authoring/saveOrUpdateQuestion" method="post" styleId="dacoQuestionForm">
	<html:hidden property="sessionMapID" />
	<input type="hidden" id="questionType" name="questionType" value="9" />
	<html:hidden property="questionIndex" />
	<input type="hidden" id="answerOptionList" name="answerOptionList" />

	<p><fmt:message key="label.authoring.basic.radio.help" /></p>

	<%@ include file="description.jsp"%>

	<!--  Options -->  
	<a id="toggleAdditionalOptionsAreaLink" href="javascript:toggleAdditionalOptionsArea()" class="visible-xs-block visible-lg-block btn btn-default btn-xs pull-right"><fmt:message key="label.authoring.basic.additionaloptions.show" /> </a>
	<div id="additionalOptionsArea" style="display: none;">
 		<div class="form-inline">
		<div class="form-group">
    	<label for="min"><fmt:message key="label.authoring.basic.min.select" /></label>
		<html:text styleId="min" property="min" size="10" styleClass="form-control"/>
		</div>
		<div class="form-group">
    	<label for="max"><fmt:message key="label.authoring.basic.max.select" /></label>
		<html:text styleId="max" property="max" size="10" styleClass="form-control"/>
		</div>
		<div class="form-group">
    	<label for="summary"><fmt:message key="label.common.summary" /></label>
		<html:select property="summary" styleClass="form-control">
			<html:option value="0" styleId="noSummaryOption"><fmt:message key="label.common.summary.none" /></html:option>
			<html:option value="2"><fmt:message key="label.common.summary.average" /></html:option>
			<html:option value="3"><fmt:message key="label.common.summary.count" /></html:option>
		</html:select>
		</div>
		<div class="checkbox">
		    <label>
	 	      <html:checkbox property="questionRequired" styleId="questionRequired"/>&nbsp;<fmt:message key="label.authoring.basic.required" />
		    </label>
	  	</div>
	  	</div>
	</div>
	<a id="toggleAdditionalOptionsAreaLink" href="javascript:toggleAdditionalOptionsArea()" class="visible-sm-block visible-md-block btn btn-default btn-xs pull-right"><fmt:message key="label.authoring.basic.additionaloptions.show" /> </a>
 	<!--  end options -->
  
</html:form>

<!-- Answer options -->

<%@ include file="answeroptions.jsp"%>

<c:set var="addButtonMessageKey" value="label.authoring.basic.checkbox.add" />
<%@ include file="buttons.jsp"%>

</div>
</div>

</body>
</lams:html>
