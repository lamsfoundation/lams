<%@ include file="/common/taglibs.jsp"%>
<html>
	<head>
		<%@ include file="/common/header.jsp"%>
		<%-- user for  rsrcresourceitem.js --%>
		<script type="text/javascript">
	   var removeInstructionUrl = "<c:url value='/authoring/removeInstruction.do'/>";
       var addInstructionUrl = "<c:url value='/authoring/newInstruction.do'/>";
	</script>
		<script type="text/javascript" src="<html:rewrite page='/includes/javascript/rsrcresourceitem.js'/>"></script>
		<style type="text/css">
	<!--
	table { 
		 width:650px;
		 margin-left:0px; 
		 text-align:left; 
		 }
	
	td { 
		padding:4px; 
		font-size:12px;
	}
	hr {
		border: none 0;
		border-top: 1px solid #ccc;
		width: 650px;
		height: 1px;
		margin: 0px 10px 10px 0px;
	}
		
	-->
	</style>
	</head>
	<body class="tabpart">
		<table class="forms" border="0">
			<!-- Basic Info Form-->
			<tr>
				<td>
					<%@ include file="/common/messages.jsp"%>
					<html:form action="/authoring/saveOrUpdateItem" method="post" styleId="resourceItemForm">
						<html:hidden property="sessionMapID" />
						<input type="hidden" name="instructionList" id="instructionList" />
						<input type="hidden" name="itemType" id="itemType" value="1" />
						<html:hidden property="itemIndex" />
						<table class="innerforms">
							<tr>
								<td colspan="2">
									<h2>
										<fmt:message key="label.authoring.basic.add.url" />
									</h2>
								</td>
							</tr>
							<tr>
								<td width="75px">
									<fmt:message key="label.authoring.basic.resource.title.input" />
								</td>
								<td>
									<html:text property="title" size="55" />
								</td>
							</tr>
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
							<tr>
								<td valign="top">
									<fmt:message key="label.authoring.basic.resource.url.input" />
								</td>
								<td>
									<html:text property="url" size="55" /><BR><BR>
									<html:checkbox property="openUrlNewWindow">
										<fmt:message key="open.in.new.window" />
									</html:checkbox>
								</td>
							</tr>
						</table>
					</html:form>
				</td>
			</tr>
			<tr>

				<!-- Instructions -->
				<td>
					<%@ include file="instructions.jsp"%>
				</td>
			</tr>
			<tr>
				<td align="center" valign="bottom">
					 <a href="#" onclick="submitResourceItem()" class="button-add-item"><fmt:message key="label.authoring.basic.add.url" /></a>  <a href="javascript:;" onclick="cancelResourceItem()" class="button"><fmt:message key="label.cancel" /></a>
				</td>
			</tr>
			<tr>
				<td>
					&nbsp;
				</td>
			</tr>
		</table>
	</body>
</html>
