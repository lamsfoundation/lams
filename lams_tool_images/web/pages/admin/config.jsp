<!DOCTYPE html>
        

<%@ include file="/common/taglibs.jsp"%>

<html>
	<lams:head>
		<title><fmt:message key="admin.page.title" /></title>
		<lams:css/>
	</lams:head>
	
	<body class="stripes">

		<c:set var="title"><fmt:message key="admin.page.title" /></c:set>
		<lams:Page type="admin" title="${title}" formID="imageGalleryAdminForm">
		
			<a href="<lams:LAMSURL/>/admin/appadminstart.do" class="btn btn-default"><fmt:message key="admin.return" /></a>

			<lams:errors/>
		
			<c:if test="${savedSuccess}">
				<lams:Alert type="info" id="admin-success" close="false">
					<fmt:message key="admin.success" />
				</lams:Alert>
			</c:if>
			
			<form:form action="saveContent.do" modelAttribute="imageGalleryAdminForm" id="imageGalleryAdminForm" method="post" enctype="multipart/form-data">
				<table class="table table-no-border voffset5">
					<tr>
						<td width="30%">
							<fmt:message key="admin.maximum.dimensions.medium.image" />
						</td>
						
						<td width="70%">
							<input type="text" path="mediumImageDimensions" class="form-control" size="50" maxlength="255"/>
						</td>
					</tr>
					
					<tr>
						<td width="30%">
							<fmt:message key="admin.maximum.dimensions.thumbnail.image" />
						</td>
						
						<td width="70%">
							<input type="text" path="thumbnailImageDimensions" class="form-control" size="50" maxlength="255"/>
						</td>
					</tr>					
				</table>
				<input type="submit" value="<fmt:message key="admin.button.save" />" class="btn btn-primary pull-right"/>
			</form:form>
			
		<div id="footer">
		</div>
		<!--closes footer-->
		
		</lams:Page>
	</body>
</head>