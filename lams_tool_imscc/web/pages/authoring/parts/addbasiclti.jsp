<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
		"http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
		<lams:css style="tabbed" />
		<script type="text/javascript" src="<html:rewrite page='/includes/javascript/commonCartridgeItem.js'/>"></script>
	</lams:head>
	<body class="tabpart">

		<!-- Basic Info Form-->
		<%@ include file="/common/messages.jsp"%>
		<html:form action="/authoring/saveOrUpdateItem" method="post" styleId="commonCartridgeItemForm">
			<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
			<html:hidden property="sessionMapID" />
			<input type="hidden" name="itemType" id="itemType" value="1" />
			<html:hidden property="itemIndex" />

			<h2 class="no-space-left">
				<fmt:message key="label.authoring.basic.add.basiclti.tool" />
			</h2>

			<div class="field-name">
				<fmt:message key="label.authoring.basic.resource.title.input" />
			</div>

			<html:text property="title" size="55" />

			<%--  Remove description in as LDEV-617
							<tr>
								<td>
									<fmt:message key="label.authoring.basic.resource.description.input" />
								</td>
								<td>
									<lams:STRUTS-textarea rows="5" cols="55" property="description" />
								</td>
							</tr>
						 --%>

			<div class="field-name space-top">
				<fmt:message key="label.authoring.basic.bascilti.url" />
			</div>
			<html:text property="url" size="55" />
			
			<div class="field-name space-top">
				<fmt:message key="label.authoring.basic.bascilti.key" />
			</div>
			<html:text property="key" size="55" />
			
			<div class="field-name space-top">
				<fmt:message key="label.authoring.basic.bascilti.secret" />
			</div>
			<html:text property="secret" size="55" />
			
			<div class="field-name space-top">
				<fmt:message key="label.authoring.basic.bascilti.button.text" />
			</div>
			<html:text property="buttonText" size="55" />

			<div class="space-top">
				<html:checkbox property="openUrlNewWindow" styleId="openUrlNewWindow" styleClass="noBorder"/>
				<label for="openUrlNewWindow">
					<fmt:message key="open.in.new.window" />
				</label>
			</div>
			
			<div class="field-name space-top">
				<fmt:message key="label.authoring.basic.bascilti.iframe.height" />
			</div>
			<html:text property="frameHeight" size="55" />
			
			<c:if test="${not empty formBean.customStr}">
				<div class="field-name space-top">
					<fmt:message key="label.authoring.basic.bascilti.custom.parameters" />
				</div>
				<lams:STRUTS-textarea rows="5" cols="55" property="customStr"/>
			</c:if>

		</html:form>

		<lams:ImgButtonWrapper>
			<a href="#" onclick="submitCommonCartridgeItem()" class="button-add-item">
				<fmt:message key="label.authoring.basic.add.url" />
			</a>
			<a href="javascript:;" onclick="cancelCommonCartridgeItem()" class="button space-left">
				<fmt:message key="label.cancel" /> 
			</a>
		</lams:ImgButtonWrapper>
		<br />
	</body>
</lams:html>
