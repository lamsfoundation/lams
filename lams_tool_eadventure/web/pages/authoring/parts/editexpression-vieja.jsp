<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
		"http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
		<lams:css style="tabbed" />
		
		
	</lams:head>
	<body class="tabpart">
		<!-- Basic Info Form-->
		<%@ include file="/common/messages.jsp"%>
		<html:form action="/authoring/saveOrUpdateExpression" method="post" styleId="eadventureExpressionForm" >
				<html:hidden property="sessionMapID" />
			<!--<html:hidden property="sessionMapID" />
			<h2 class="no-space-left">
				<fmt:message key="label.authoring.conditions.add.expression" />
			</h2>
			<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
			
			<!--<div id="editExpression">-->
	
			<!--<table>
				<tr>
					<td>
						<fmt:message key="label.authoring.conditions.condition.var1" />
						<c:set var="url">
						<html:rewrite
							page="/authoring/prepareOperatorAndSecondVar.do?sessionMapID=${sessionMapID}" />
						</c:set>
						
					</td>
					<!--<td>
					<fmt:message key="label.authoring.conditions.condition.operator" />
						<c:if test="${empty eadventureExpressionForm.possibleOperator }">
						<html:select property="selectedOperator" >
							<logic:iterate name="eadventureExpressionForm" id="varOp" property="possibleOperator"> 
							<html:option value="varOp" >
								<bean:write name="varOp" />
							</html:option>
							</logic:iterate>
						</html:select>
						</c:if>
					</td>
					<td>
					<fmt:message key="label.authoring.conditions.condition.var2" />
					<c:if test="${empty eadventureExpressionForm.possibleVarOp2 }">
						<div id="selectVar">
						<html:select property="selectedVarOp2" >
							<logic:iterate name="eadventureExpressionForm" id="varOp2" property="possibleVarsOp2"> 
							<html:option value="varOp2" >
								<bean:write name="varOp2" />
							</html:option>
							</logic:iterate>
						</html:select>
						</div>
					</c:if>
						<div id="introduceVar">
							<html:text property="introducedValue" style="width: 99%;"></html:text>
						</div>
						<input type="checkbox" name="check" onclick="javascript:changeSecondOp()"/>
					</td>
					<td>
					<fmt:message key="label.authoring.conditions.empty.condition.var1" />
					<c:if test="${empty formBean.selectedVarOp1 }">
					
					</c:if>
					</td>
				</tr>
			</table>
			<!--</div>-->
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
			<a href="javascript:;" onclick="window.top.hideExpressionMessage();"
				class="button space-left"><fmt:message key="label.cancel" /> </a>
		</lams:ImgButtonWrapper>
		
	</body>
</lams:html>

