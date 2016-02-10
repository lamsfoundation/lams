<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
		"http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
<lams:head>
	<%@ include file="/common/header.jsp"%>
	<lams:css style="main" />
	<!-- To use in external script files. -->
	<script type="text/javascript">
	   var msgShowAdditionalOptions = "<fmt:message key='label.authoring.basic.additionaloptions.show' />";
       var msgHideAdditionalOptions = "<fmt:message key='label.authoring.basic.additionaloptions.hide' />";
	</script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<html:rewrite page='/includes/javascript/dacoAuthoring.js'/>"></script>
</lams:head>
<body class="tabpart">

<!-- Add question form-->
<%@ include file="/common/messages.jsp"%>
<html:form action="/authoring/saveOrUpdateQuestion" method="post" styleId="dacoQuestionForm">
	<html:hidden property="sessionMapID" />
	<input type="hidden" id="questionType" name="questionType" value="3" />
	<html:hidden property="questionIndex" />

	<h2 class="no-space-left"><fmt:message key="label.authoring.basic.number" /></h2>
	<div><fmt:message key="label.authoring.basic.number.help" /></div>
	<div class="field-name space-top"><fmt:message key="label.authoring.basic.description" /></div>

	<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
	<c:set var="sessionMap" value="${sessionScope[formBean.sessionMapID]}" />
	<lams:CKEditor id="description" value="${formBean.description}" 
		contentFolderID="${sessionMap.contentFolderID}"
                width="100%"
                resizeParentFrameName="questionInputArea">
	</lams:CKEditor>
	<div class="space-bottom-top"><a id="toggleAdditionalOptionsAreaLink" href="javascript:toggleAdditionalOptionsArea()"><fmt:message
		key="label.authoring.basic.additionaloptions.show" /> </a><br />
	</div>

	<div id="additionalOptionsArea" style="display: none;">

	<div class="field-name"><fmt:message key="label.common.min" /></div>
	<html:text styleId="min" property="min" size="10" />
	<div class="field-name space-top"><fmt:message key="label.common.max" /></div>
	<html:text styleId="max" property="max" size="10" />
	<div class="field-name space-top"><fmt:message key="label.authoring.basic.number.digitsdecimal" /></div>
	<html:text styleId="digitsDecimal" property="digitsDecimal" size="10" />
	<div class="space-top"><html:checkbox property="questionRequired" styleId="questionRequired" styleClass="noBorder">
		<fmt:message key="label.authoring.basic.required" />
	</html:checkbox></div>
	<div class="field-name space-top"><fmt:message key="label.common.summary" /></div>
	<html:select property="summary" styleClass="noBorder">
	<html:option value="0" styleId="noSummaryOption"><fmt:message key="label.common.summary.none" /></html:option>
		<html:option value="1"><fmt:message key="label.common.summary.sum" /></html:option>
		<html:option value="2"><fmt:message key="label.common.summary.average" /></html:option>
		<html:option value="3"><fmt:message key="label.common.summary.count" /></html:option>
	</html:select></div>
</html:form>
<lams:ImgButtonWrapper>
	<a href="#" onclick="javascript:submitDacoQuestion()" class="button-add-item"><fmt:message
		key="label.authoring.basic.number.add" /> </a>
	<a href="#" onclick="javascript:cancelDacoQuestion()" class="button space-left"><fmt:message key="label.common.cancel" /> </a>
</lams:ImgButtonWrapper>

</body>
</lams:html>
