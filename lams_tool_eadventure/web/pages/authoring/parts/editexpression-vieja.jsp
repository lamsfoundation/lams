<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
		"http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
		<lams:css style="tabbed" />
		
		<script type="text/javascript"> 
			function callHideExpressionMessage() {
				if (window.parent && window.parent.hideExpressionMessage) {
					window.parent.hideExpressionMessage();
				} else if (window.parent && window.parent.hideExpressionMessage) {
					window.top.hideExpressionMessage();
				}
			}
		</script>
	</lams:head>
	<body class="tabpart">
		<!-- Basic Info Form-->
		<%@ include file="/common/messages.jsp"%>
		<html:form action="/authoring/saveOrUpdateExpression" method="post" styleId="eadventureExpressionForm" >
			<html:hidden property="sessionMapID" />
	
			<div class="small-space-bottom">
         		<html:text property="name" size="55"/>
			</div>
			
			<div>
			<html:select property="selectedVarOp1" >
							<logic:iterate name="prueba" id="var" type="java.lang.String"> 
							<html:option value="<%= var %>" >
								<bean:write name="var" />
							</html:option>
							</logic:iterate>
						</html:select>
			</div>	
		</html:form>

		<lams:ImgButtonWrapper>
			<a href="#" onclick="eadventureExpressionForm.submit();" class="button-add-item"><fmt:message
					key="button.add" /> </a>
			<a href="javascript:;" onclick="javascript:callHideExpressionMessage()"
				class="button space-left"><fmt:message key="label.cancel" /> </a>
		</lams:ImgButtonWrapper>
		
	</body>
</lams:html>

