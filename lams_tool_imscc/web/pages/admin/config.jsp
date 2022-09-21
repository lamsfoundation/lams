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
			
			<a href="<lams:LAMSURL/>/admin/appadminstart.do" class="btn btn-default"><fmt:message key="admin.return" /></a>

			<lams:errors/>
		
			<c:if test="${savedSuccess}">
				<lams:Alert type="info" id="admin-success" close="false">
					<fmt:message key="admin.success" />
				</lams:Alert>
			</c:if>
			
			<form:form action="saveContent.do" modelAttribute="commonCartridgeAdminForm" id="commonCartridgeAdminForm" method="post" enctype="multipart/form-data">
				<table class="table table-no-border voffset5">
					<tr>
						<td width="30%">
							<fmt:message key="admin.expose.user.name" />
						</td>
						
						<td width="70%">
							<form:checkbox path="allowExposeUserName"/>
						</td>
					</tr>
					
					<tr>
						<td width="30%">
							<fmt:message key="admin.expose.user.email" />
						</td>
						
						<td width="70%">
							<form:checkbox path="allowExposeUserEmail"/>
						</td>
					</tr>					
				</table>
				<button class="btn btn-primary pull-right"><fmt:message key="admin.button.save" /></button>
			</form:form>
			
		<div id="footer">
		</div>
		<!--closes footer-->
		
		</lams:Page>
	</body>
</head>