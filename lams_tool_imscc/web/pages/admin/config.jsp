<!DOCTYPE html>
        

<%@ include file="/common/taglibs.jsp"%>

<html>
	<lams:head>
		<title><fmt:message key="admin.page.title" /></title>
		<lams:css/>
	</lams:head>
	
	<body class="stripes">
		<div id="content">

			<h1>
				<fmt:message key="admin.page.title" />
			</h1>
			
			<a href="<lams:LAMSURL/>/admin/sysadminstart.do"><fmt:message key="admin.return" /></a>

			<p>

			<%@ include file="/common/messages.jsp"%>
		
			<c:if test="${savedSuccess}">
				<p class="info">
					<fmt:message key="admin.success" />
				<p>
			</c:if>
			
			<html:form action="/laimsc11admin/saveContent" styleId="commonCartridgeAdminForm" method="post" enctype="multipart/form-data">
				<table class="alternative-color">
					<tr>
						<td width="30%">
							<fmt:message key="admin.expose.user.name" />
						</td>
						
						<td width="70%">
							<html:checkbox property="allowExposeUserName" />
						</td>
					</tr>
					
					<tr>
						<td width="30%">
							<fmt:message key="admin.expose.user.email" />
						</td>
						
						<td width="70%">
							<html:checkbox property="allowExposeUserEmail" />
						</td>
					</tr>					
				</table>
				<html:submit><fmt:message key="admin.button.save" /></html:submit>
			</html:form>
			
		</div>
		<!--closes content-->

		<div id="footer">
		</div>
		<!--closes footer-->
	</body>
</head>