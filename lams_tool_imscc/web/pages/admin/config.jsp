<!DOCTYPE html>
        

<%@ include file="/common/taglibs.jsp"%>

<html>
	<lams:head>
		<title><fmt:message key="admin.page.title" /></title>
		<lams:css/>
	</lams:head>
	
	<body class="stripes">

		<c:set var="title"><fmt:message key="admin.page.title" /></c:set>
		<lams:Page type="admin" title="${title}">
			
			<a href="<lams:LAMSURL/>/admin/sysadminstart.do" class="btn btn-default"><fmt:message key="admin.return" /></a>

			<%@ include file="/common/messages.jsp"%>
		
			<c:if test="${savedSuccess}">
				<lams:Alert type="info" id="admin-success" close="false">
					<fmt:message key="admin.success" />
				</lams:Alert>
			</c:if>
			
			<html:form action="/laimsc11admin/saveContent" styleId="commonCartridgeAdminForm" method="post" enctype="multipart/form-data">
				<table class="table table-no-border voffset5">
					<tr>
						<td width="30%">
							<fmt:message key="admin.expose.user.name" />
						</td>
						
						<td width="70%">
							<html:checkbox property="allowExposeUserName"/>
						</td>
					</tr>
					
					<tr>
						<td width="30%">
							<fmt:message key="admin.expose.user.email" />
						</td>
						
						<td width="70%">
							<html:checkbox property="allowExposeUserEmail"/>
						</td>
					</tr>					
				</table>
				<html:submit styleClass="btn btn-primary pull-right"><fmt:message key="admin.button.save" /></html:submit>
			</html:form>
			
		<div id="footer">
		</div>
		<!--closes footer-->
		
		</lams:Page>
	</body>
</head>