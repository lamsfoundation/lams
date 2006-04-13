<%@ include file="/common/taglibs.jsp" %>
<html>
<head>
    <%@ include file="/common/header.jsp" %>
    	
	<script type="text/javascript">
	   <%-- user for  rsrcresourceitem.js --%>
	   var removeInstructionUrl = "<c:url value='/authoring/removeInstruction.do'/>";
       var addInstructionUrl = "<c:url value='/authoring/newInstruction.do'/>";
       var removeItemAttachmentUrl = "<c:url value='/authoring/removeItemAttachment.do'/>";
	</script>
	<script type="text/javascript" src="<html:rewrite page='/includes/javascript/rsrcresourceitem.js'/>"></script>

</head>
<body class="tabpart">
	<table class="forms">
		<!-- Basic Info Form-->
		<tr>
			<td>
			<%@ include file="/common/messages.jsp" %>
			<html:form action="/authoring/saveOrUpdateItem" method="post" styleId="resourceItemForm" enctype="multipart/form-data">
				<input type="hidden" name="instructionList" id="instructionList"/>
				<input type="hidden" name="itemType" id="itemType" value="4"/>
				<html:hidden property="itemIndex"/>
				<table class="innerforms">
					<tr>
						<td colspan="2"><h2><fmt:message key="label.authoring.basic.add.learning.object"/></h2></td>
					</tr>
					<tr>
						<td width="130px"><fmt:message key="label.authoring.basic.resource.title.input"/></td>
						<td><html:text property="title" size="55" tabindex="1" /></td>
					</tr>
					<tr>
						<td width="130px"><fmt:message key="label.authoring.basic.resource.description.input"/></td>
						<td><lams:STRUTS-textarea rows="5" cols="55" tabindex="2" property="description"/> </td>
					</tr>	
					<tr>
						<td width="130px"><fmt:message key="label.authoring.basic.resource.zip.file.input"/></td>
						<td>
						<c:set var="itemAttachment" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
						<div id="itemAttachmentArea">
							<%@ include file="/pages/authoring/parts/itemattachment.jsp"%>
						</div>
						</td>
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
						<td width="100px" align="right" valign="bottom">
							<input onclick="cancelResourceItem()" type="button" name="cancel" value="<fmt:message key="label.cancel"/>" class="buttonStyle">
						</td>
						<td width="150px" align="right" valign="bottom">
							<input onclick="submitResourceItem()" type="button" name="add" value="<fmt:message key="label.authoring.basic.add.learning.object"/>" class="buttonStyle">
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>

</body>
</html>