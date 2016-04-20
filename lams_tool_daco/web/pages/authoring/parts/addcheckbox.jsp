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

	<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
	<c:set var="sessionMap" value="${sessionScope[formBean.sessionMapID]}" />

	<div class="form-group">
    	<label for="description"><fmt:message key="label.authoring.basic.description" /></label>
		<lams:CKEditor id="description" value="${formBean.description}" 
			contentFolderID="${sessionMap.contentFolderID}"
	                width="100%"
	                resizeParentFrameName="questionInputArea">
		</lams:CKEditor>
	</div>
  
	<a id="toggleAdditionalOptionsAreaLink" href="javascript:toggleAdditionalOptionsArea()" class="btn btn-default btn-xs pull-right"><fmt:message
		key="label.authoring.basic.additionaloptions.show" /> </a>

	<div id="additionalOptionsArea" class="voffset5 form-inline" style="display: none;">
		<div class="form-group">
	    	<label for=min><fmt:message key="label.authoring.basic.min.select" />&nbsp;
		<html:text styleId="min" property="min" size="10" />
		</div>
		
		<div class="form-group">
	    	<label for="max"><fmt:message key="label.authoring.basic.max.select" />&nbsp;
		<html:text styleId="max" property="max" size="10" />
		</div>
		
		<div class="form-group">
	    	<label for="questionRequired"><html:checkbox property="questionRequired" styleId="questionRequired" styleClass="noBorder">&nbsp;
			<fmt:message key="label.authoring.basic.required" />
		</html:checkbox>
		</div>
	</div>
	
	<div class="form-group voffset5">
    	<label for="summary"><fmt:message key="label.common.summary" />
	<html:select property="summary" styleClass="noBorder">
		<html:option value="0" styleId="noSummaryOption"><fmt:message key="label.common.summary.none" /></html:option>
		<html:option value="2"><fmt:message key="label.common.summary.average" /></html:option>
		<html:option value="3"><fmt:message key="label.common.summary.count" /></html:option>
	</html:select>
	</div>
</html:form>

<!-- Answer options -->

<%@ include file="answeroptions.jsp"%>

<div class="voffset5">
	<a href="#" onclick="javascript:submitDacoQuestion()" class="btn btn-default btn-sm"> <fmt:message
		key="label.authoring.basic.checkbox.add" /> </a>
	<a href="#" onclick="javascript:cancelDacoQuestion()" class="btn btn-default btn-sm loffset5"> <fmt:message key="label.common.cancel" /> </a>
</div>

</div>
</div>

</body>
</lams:html>
