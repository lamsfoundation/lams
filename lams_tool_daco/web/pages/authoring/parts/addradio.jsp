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
	<div class="panel-title"><fmt:message key="label.authoring.basic.radio" /></div>
</div>

<div class="panel-body">

<!-- Add question form-->
<%@ include file="/common/messages.jsp"%>
<html:form action="/authoring/saveOrUpdateQuestion" method="post" styleId="dacoQuestionForm">
	<html:hidden property="sessionMapID" />
	<input type="hidden" id="questionType" name="questionType" value="7" />
	<html:hidden property="questionIndex" />
	<input type="hidden" id="answerOptionList" name="answerOptionList" />

	<p><fmt:message key="label.authoring.basic.radio.help" /></p>

	<%@ include file="description.jsp"%>

  	<!--  Options -->  
	<a href="javascript:toggleAdditionalOptionsArea()" class="fa-xs"><i id="faIcon" class="fa fa-plus-square-o"></i> <span id="toggleAdditionalOptionsAreaLink"><fmt:message key="label.authoring.basic.additionaloptions.show" /></span></a>
	<div id="additionalOptionsArea" style="display: none;" class="panel-body">
		<div class="form-group">
			<div class="checkbox">
			    <label>
		 	      <html:checkbox property="questionRequired" styleId="questionRequired"/>&nbsp;<fmt:message key="label.authoring.basic.required" />
			    </label>
		 	</div>
		</div>
		<div class="form-group">
    	<label for="summary"><fmt:message key="label.common.summary" /></label>: 
			<html:select property="summary"  styleClass="form-control-inline input-sm">
				<html:option value="0" styleId="noSummaryOption"><fmt:message key="label.common.summary.none" /></html:option>
				<html:option value="2" ><fmt:message key="label.common.summary.average" /></html:option>
				<html:option value="3" ><fmt:message key="label.common.summary.count" /></html:option>
			</html:select>
		</div>
 	</div>

 	<!--  end options -->

 </html:form>

<!-- Answer options -->

<%@ include file="answeroptions.jsp"%>

<c:set var="addButtonMessageKey" value="label.authoring.basic.radio.add" />
<%@ include file="buttons.jsp"%>

</div>
</div>

</body>
</lams:html>
