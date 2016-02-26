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
			
			<html:form action="/laimag10admin/saveContent" styleId="imageGalleryAdminForm" method="post" enctype="multipart/form-data">
				<table class="alternative-color">
					<tr>
						<td width="30%">
							<fmt:message key="admin.maximum.dimensions.medium.image" />
						</td>
						
						<td width="70%">
							<html:text property="mediumImageDimensions" size="50" maxlength="255">
						</html:text></td>
					</tr>
					
					<tr>
						<td width="30%">
							<fmt:message key="admin.maximum.dimensions.thumbnail.image" />
						</td>
						
						<td width="70%">
							<html:text property="thumbnailImageDimensions" size="50" maxlength="255">
						</html:text></td>
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