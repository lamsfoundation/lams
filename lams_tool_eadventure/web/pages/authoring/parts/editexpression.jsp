<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
		"http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/common/taglibs.jsp"%>
<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
		<lams:css style="main" />


		<script lang="javascript">
		
		
			var messageAdvance = "<fmt:message key='label.authoring.conditions.change.expression.advance.edition' />";
			var messageSimple = "<fmt:message key='label.authoring.conditions.change.expression.simple.edition' />";
			var id = -1;
			var showVars=false;
			var showAdvance = true;
		function changeFirstOp(option,sessionMapID,position ){
		var url = "<c:url value="/authoring/prepareOperatorAndSecondVar.do?sessionMapID="/>" + sessionMapID +"&paramName="+option + "&positionExpression="+position;;
		var area=parent.document.getElementById("expressionInputArea");
			if(area != null && option!="no-option" ){
				area.src=url;
			}
			var win = window.top ? window.top : window;
			win.document.location.hash = "expressionInputArea";
		}
		
		function changeSecondOp(){
		
		if(document.eadventureExpressionForm.group[0].checked){
			document.getElementById("div1").style.display="none";
			document.getElementById("div2").style.display="block";
		}else if (document.eadventureExpressionForm.group[1].checked){
			document.getElementById("div1").style.display="block";
			document.getElementById("div2").style.display="none";	
			}
		
		}
		
		function submitExpression(){
		var eadventureExpressionForm = document.getElementById("eadventureExpressionForm");
			var win = window.top ? window.top : window;
			win.top.document.location.hash = "conditionInputArea";
			if (eadventureExpressionForm.group!= null){
			if(eadventureExpressionForm.group[0].checked){
				eadventureExpressionForm.secondVarSelected.value = false;
			}else if (document.eadventureExpressionForm.group[1].checked){
				eadventureExpressionForm.secondVarSelected.value = true;
			}
			} else {
				eadventureExpressionForm.secondVarSelected = false;
			}
			
			eadventureExpressionForm.submit();
			
		}
		
		function spinnerHandler(introducedValue, up){
			id = setInterval(function(){
				if (up){
					introducedValue.value++;
				} else if (!up){
					introducedValue.value--;
				}
			}, 80)
		}
		
		function stopCounting(){
			setTimeout("clearInterval("+id+")");
		}
		
		function showNextOpColum() {
			var nextOp =  document.getElementById("nextOp");
			var nextOpTH =  document.getElementById("nextOpTH");
			var link = document.getElementById("changeLink");
			if (showAdvance) {
				nextOp.style.display = "block"; 
				nextOpTH.style.display = "block"; 
				link.textContent = messageSimple;
				showAdvance = false;
			} else {
				nextOp.style.display = "none"; 
				nextOpTH.style.display = "none";
				link.textContent = messageAdvance;
				showAdvance = true;
			}
}
		
		</script>
	</lams:head>
	<body class="tabpart">
		<!-- Basic Info Form-->
		<%@ include file="/common/messages.jsp"%>
	
		<html:form action="/authoring/saveOrUpdateExpression" method="post" styleId="eadventureExpressionForm" >
				<html:hidden property="sessionMapID" />
				<html:hidden property="position" />
				<html:hidden property="secondVarSelected" />
				
				
					<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />					
					<c:set var="sessionMapID" value="${formBean.sessionMapID}" />
					<c:set var="position" value="${formBean.position}" />
					<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />	
						<h2 class="no-space-left">
							<fmt:message key="label.authoring.conditions.add.expression" />
						</h2>
						<table class="alternative-color" id="expressionsTable" cellspacing="0">
							<tr>
								<th align="left" colspan="4">
									<fmt:message key="label.authoring.conditions.condition.var1" />
								</th>
								<th align="left" colspan="4">
										<fmt:message key="label.authoring.conditions.condition.operator" />
								</th>
								<th align="left" colspan="4">
									<fmt:message key="label.authoring.conditions.condition.var2" />
								</th>
								<th id="nextOpTH" align="left" colspan="4" style="display:none">
									<fmt:message key="label.authoring.conditions.condition.nextOp" />
								</th>
							</tr>
							
							<tr>
								<td colspan="4" >
								<html:select property="selectedVarOp1" 
										onchange="changeFirstOp(this.options[this.selectedIndex].value,'${sessionMapID}','${position}');" >
									<html:option value="no-option">				
										<fmt:message key="label.authoring.conditions.select.var" />
									</html:option>
									<logic:iterate name="eadventureExpressionForm" id="var" property="possibleVarsOp1" type="java.lang.String"> 
									<html:option value="<%= var %>" >
										<bean:write name="var" />
									</html:option>
									</logic:iterate>
								</html:select>
								</td>	
								<td colspan="4" >
								
								<c:if test="${not empty eadventureExpressionForm.possibleOperator }">
									<html:select property="selectedOperator" >
									<html:option value="no-option">
										<fmt:message key="label.authoring.conditions.select.operator" />
									</html:option>
									<logic:iterate name="eadventureExpressionForm" id="varOp" property="possibleOperator" type="java.lang.String">  
										<html:option value="<%= varOp %>"  >
											<bean:write name="varOp" />
										</html:option>
									</logic:iterate>
									</html:select>
								</c:if>
								
								</td>
								<td colspan="4" >
								<c:if test="${not empty eadventureExpressionForm.possibleVarsOp2 }">
								
									<div id="div1">
										<html:select property="selectedVarOp2" styleId="secondVar">
											<html:option value="no-option">
												<fmt:message key="label.authoring.conditions.select.var" />
											</html:option>
											<logic:iterate name="eadventureExpressionForm" id="varOption" property="possibleVarsOp2" type="java.lang.String">
												<html:option value="<%= varOption %>" >
													<bean:write name="varOption" />
												</html:option>
											</logic:iterate>
										</html:select>
										</br>
										
										
									</div>
								</c:if>
								
								<c:if test="${not empty eadventureExpressionForm.possibleOperator }">
									<div id="div2">
									<!-- if integer -->
									<c:if test="${sessionMap.type == 0}">
									
										<table cellpadding="0" cellspacing="0" border="0">
											<tr>
												<td style="width:10%;border-bottom: none;">
													<html:text readonly="true" property="introducedValue" style="width:400%">
														<fmt:message key="label.authoring.conditions.introduce.value" />
													</html:text>
												</td>
												<td style="border-bottom: none;">
												<!-- idea from http://www.webcodingtech.com/javascript/spinner.php -->
													<table cellpadding="0" cellspacing="0" border="0">
														<tr>
															<td style="margin: 0pt; padding: 0pt; border-bottom: medium none;">
																<input type="button" value=" /\ " onmouseup="javascript:stopCounting();" 
																onmousedown="javascript:spinnerHandler(form.introducedValue,true);" 
																style="width:14px;height:14px;font-size:7px;" >
															</td>
														</tr>
														<tr>
															<td style="margin: 0pt; padding: 0pt; background: transparent;border-bottom: none;">
																<input type="button" value=" \/ " onmouseup="javascript:stopCounting();" 
																onmousedown="javascript:spinnerHandler(form.introducedValue,false);" 
																style="width:14px;height:14px;font-size:7px;" >
															</td>
														</tr>
														</table>
												</td>
											</tr>
											</table>
									
									</c:if>	
									<!-- /if integer-->
									<!-- if string -->
									<c:if test="${ sessionMap.type == 1}">
									
										<td style="width:10%;border-bottom: none;">
											<html:text property="introducedValue" style="width:400%">
												<fmt:message key="label.authoring.conditions.introduce.value" />
											</html:text>
										</td>										
									
									</c:if>	
									<!-- /if string -->
									<!-- if boolean -->
									<c:if test="${ sessionMap.type == 2}">
										<html:select property="introducedValue">
											<html:option value="true">
												<fmt:message key="label.authoring.conditions.select.boolean.value.true" />
											</html:option>
											<html:option value="false">
												<fmt:message key="label.authoring.conditions.select.boolean.value.false" />
											</html:option>
										</html:select>
									</c:if>	
									<!-- /if boolean -->
									</div>
								</c:if>		
							
								<c:if test="${not empty eadventureExpressionForm.possibleVarsOp2 }">
										
										<input type="radio" name="group" value="value" onclick="javascript:changeSecondOp();" >
											<fmt:message key='label.authoring.conditions.change.introduce.value' />
										</input>
										<br> 
										<input type="radio" name="group" value="var" onclick="javascript:changeSecondOp();">
											<fmt:message key="label.authoring.conditions.change.introduce.var" />
										</input>
										<%-- <c:if test="${not empty eadventureExpressionForm.introducedValue || (empty eadventureExpressionForm.introducedValue && empty eadventureExpressionForm.selectedVarOp2)}"> --%>
										<c:if test="${not eadventureExpressionForm.secondVarSelected}">
											<script lang="javascript">
											
												document.eadventureExpressionForm.group[0].checked = true;
											</script>
										</c:if>
										<%-- <c:if test="${not empty eadventureExpressionForm.selectedVarOp2}"> --%>
										<c:if test="${eadventureExpressionForm.secondVarSelected}">
											<script lang="javascript">
											
												document.eadventureExpressionForm.group[1].checked = true;
											</script>
										</c:if>
										<!-- ver si lo puedo inicializar de otra manera-->
											<script lang="javascript">
												changeSecondOp();
											</script>
									</c:if>
							
							
							
								</td>
								<td colspan="4" id="nextOp" style="display:none">
								<html:select property="nextOp">
											<html:option value="and">
												<fmt:message key="label.authoring.conditions.select.boolean.value.and" />
											</html:option>
											<html:option value="or">
												<fmt:message key="label.authoring.conditions.select.boolean.value.or" />
											</html:option>
								</html:select>
								</td>
							</tr>
						</table>
						
						<a id="changeLink" href="javascript:;" onclick="javascript:showNextOpColum();">
						<fmt:message key="label.authoring.conditions.change.expression.advance.edition" /> </a>
					
		</html:form>
		<lams:ImgButtonWrapper>
			<a href="#" onclick="submitExpression();" class="button-add-item"><fmt:message
					key="button.add" /> </a>
			<a href="javascript:;" onclick="parent.hideExpressionMessage();"
				class="button space-left"><fmt:message key="label.cancel" /> </a>
		</lams:ImgButtonWrapper>
		
		
	</body>
</lams:html>

