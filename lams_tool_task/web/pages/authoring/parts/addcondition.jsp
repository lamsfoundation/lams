<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
		"http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
		<lams:css style="tabbed" />
		<script type="text/javascript"> 
			function callHideConditionMessage() {
				if (window.parent && window.parent.hideConditionMessage) {
					window.parent.hideConditionMessage();
				} else if (window.parent && window.parent.hideConditionMessage) {
					window.top.hideConditionMessage();
				}
			}
		</script>
	</lams:head>
	<body class="tabpart">
		<!-- Basic Info Form-->
		<%@ include file="/common/messages.jsp"%>
		<html:form action="/authoring/saveOrUpdateCondition" method="post" styleId="taskListConditionForm" focus="name" >

			<html:hidden property="sessionMapID" />
			<html:hidden property="sequenceId" />
			<h2 class="no-space-left">
				<fmt:message key="label.authoring.conditions.add.condition" />
			</h2>

			<div class="field-name">
            	<fmt:message key="label.authoring.conditions.condition.name" />
			</div>

			<div class="small-space-bottom">
         		<html:text property="name" size="55"/>
			</div>

			<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />					
	        <c:set var="sessionMapID" value="${formBean.sessionMapID}" />				
		    <c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />


			<logic:iterate name="taskListConditionForm" id="itemE" property="possibleItems">
			  	<html:multibox property="selectedItems">
			    	<bean:write name="itemE" property="value" />
			  	</html:multibox>
			    <bean:write name="itemE" property="label" />
			    <br />
			</logic:iterate>

		</html:form>

		<lams:ImgButtonWrapper>
			<a href="#" onclick="taskListConditionForm.submit();" class="button-add-item"><fmt:message
					key="button.add" /> </a> 
			<a href="javascript:;" onclick="javascript:callHideConditionMessage()"
				class="button space-left"><fmt:message key="label.cancel" /> </a>
		</lams:ImgButtonWrapper>
	</body>
</lams:html>
