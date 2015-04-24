<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
		"http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
		<script type="text/javascript" src="${lams}includes/javascript/prototype.js"></script>
		<lams:css style="tabbed" />
		<script type="text/javascript"> 
			function callHideConditionMessage() {
				try {
					if (window.parent && window.parent.hideConditionMessage) {
						window.parent.hideConditionMessage();
					} else if (window.top && window.top.hideConditionMessage) {
						window.top.hideConditionMessage();
					}
				} catch(err) {
					// mute cross-domain iframe access errors
				}
			}
		</script>
	</lams:head>
	<body class="tabpart">
		<!-- Basic Info Form-->
		<%@ include file="/common/messages.jsp"%>
		<html:form action="/authoring/saveOrUpdateCondition" method="post" styleId="surveyConditionForm" focus="displayName" >
			<html:hidden property="orderId" />
			<h2 class="no-space-left">
				<fmt:message key="label.authoring.conditions.add.condition" />
			</h2>

			<div class="field-name">
            	<fmt:message key="label.authoring.conditions.condition.name" />
			</div>

			<div class="small-space-bottom">
         		<html:text property="displayName" size="51"/>
			</div>
			<%-- Text search form fields are being included --%>
			<lams:TextSearch wrapInFormTag="false" sessionMapID="${sessionMapID}"  />
			<h4 class="no-space-left"><fmt:message key="textsearch.questions" /></h4>
			<logic:iterate name="surveyConditionForm" id="itemE" property="possibleItems">
			  	<html:multibox property="selectedItems">
			    	<bean:write name="itemE" property="value" />
			  	</html:multibox>
			    <bean:write name="itemE" property="label" />
			    <br />
			</logic:iterate>

		</html:form>

		<lams:ImgButtonWrapper>
			<a href="javascript:;" onclick="javascript:surveyConditionForm.submit();" class="button-add-item"><fmt:message
					key="label.save" /> </a>
			<a href="javascript:;" onclick="javascript:callHideConditionMessage()"
				class="button space-left"><fmt:message key="label.cancel" /> </a>
		</lams:ImgButtonWrapper>
	</body>
</lams:html>
