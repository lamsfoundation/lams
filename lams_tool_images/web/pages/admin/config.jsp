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
			
			<html:form action="/laimag10admin/saveContent" styleId="imageGalleryAdminForm" method="post" enctype="multipart/form-data">
				<table class="table table-no-border voffset5">
					<tr>
						<td width="30%">
							<fmt:message key="admin.maximum.dimensions.medium.image" />
						</td>
						
						<td width="70%">
							<html:text property="mediumImageDimensions" styleClass="form-control" size="50" maxlength="255">
						</html:text></td>
					</tr>
					
					<tr>
						<td width="30%">
							<fmt:message key="admin.maximum.dimensions.thumbnail.image" />
						</td>
						
						<td width="70%">
							<html:text property="thumbnailImageDimensions" styleClass="form-control" size="50" maxlength="255">
						</html:text></td>
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