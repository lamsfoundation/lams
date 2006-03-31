<%@ include file="/common/taglibs.jsp" %>
<html>
<head>
	<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request"/>
	<!-- depending on user / site preference this will get changed probbably use passed in variable from flash to select which one to use-->
    <link href="<html:rewrite page='/includes/css/aqua.css'/>" rel="stylesheet" type="text/css">
	<!-- this is the custom CSS for hte tool -->
	<link href="<html:rewrite page='/includes/css/tool_custom.css'/>" rel="stylesheet" type="text/css">
	<link href="<html:rewrite page='/includes/css/rsrc.css'/>" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="<html:rewrite page='/includes/javascript/prototype.js'/>"></script>
    <%-- user for  rsrcresourceitem.js --%>
	<script type="text/javascript">
	   var removeInstructionUrl = "<c:url value='/authoring/removeInstruction.do'/>";
       var addInstructionUrl = "<c:url value='/authoring/newInstruction.do'/>";
	</script>
	<script type="text/javascript" src="<html:rewrite page='/includes/javascript/rsrcresourceitem.js'/>"></script>

</head>
<body class="tabpart">
	<table class="forms">
		<!-- Basic Info Form-->
		<tr>
			<td>
			<%@ include file="/common/messages.jsp" %>
			<html:form action="/authoring/saveOrUpdateItem" method="post" styleId="resourceItemForm">
				<input type="hidden" name="instructionList" id="instructionList"/>
				<input type="hidden" name="itemType" id="itemType" value="1"/>
				<html:hidden property="itemIndex"/>
				<table class="innerforms">
					<tr>
						<td colspan="2"><h2><fmt:message key="label.authoring.basic.add.url"/></h2></td>
					</tr>
					<tr>
						<td width="130px"><fmt:message key="label.authoring.basic.resource.title.input"/></td>
						<td><html:text property="title" size="55"/></td>
					</tr>
					<tr>
						<td width="130px"><fmt:message key="label.authoring.basic.resource.url.input"/></td>
						<td><html:text property="url" size="55"/></td>
					</tr>	
				</table>
			</html:form>
			</td>
		</tr>
		<tr>
			<td><hr></td>
		</tr>
		<tr>
			
			<!-- Instructions -->
			<td>
				<table class="innerforms">
					<tr>
						<td>
							<%@ include file="instructions.jsp" %>
						</td>
						<td width="100%" align="right" valign="bottom">
							<input onclick="submitResourceItem()" type="button" name="add" value="<fmt:message key="label.authoring.basic.add.url"/>" class="buttonStyle">
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>

</body>
</html>