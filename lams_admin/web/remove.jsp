<!DOCTYPE html>

<%@ include file="/taglibs.jsp"%>

<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="admin.user.management"/></c:set>
	<title>${title}</title>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />

	<link rel="stylesheet" href="<lams:LAMSURL/>css/bootstrap5.custom.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>includes/font-awesome6/css/all.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/components.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">

	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap5.bundle.min.js"></script>
</lams:head>
    
<body class="component">
	<%-- Build breadcrumb --%>
	<c:set var="breadcrumbItems">
		<lams:LAMSURL />admin/appadminstart.do | <fmt:message
			key="appadmin.maintain" />
	</c:set>
	<c:set var="breadcrumbItems">${breadcrumbItems}, <lams:LAMSURL />admin/usersearch.do | <fmt:message
			key="admin.user.management" />
	</c:set>
	<c:if test="${method == 'disable'}">
		<c:set var="breadcrumbItems">${breadcrumbItems}, . | <fmt:message
				key="admin.user.disable" />
		</c:set>
	</c:if>
	<c:if test="${method == 'delete'}">
		<c:set var="breadcrumbItems">${breadcrumbItems}, . | <fmt:message
				key="admin.user.delete" />
		</c:set>
	</c:if>
	
	<lams:Page5 type="admin" title="${title}" breadcrumbItems="${breadcrumbItems}">
				<c:if test="${empty orgId}">
					<c:url var="cancel" value="/usersearch.do" />
				</c:if>
				<c:if test="${not empty orgId}">
					<c:url var="cancel" value="/usermanage.do">
						<c:param name="org" value="${orgId}" />
					</c:url>
				</c:if>
				
				<c:if test="${method == 'disable'}">
					<div class="card" >
						<div class="card-header">
							<fmt:message key="admin.user.disable"/>
						</div>
						<div class="card-body">     
							<p>
								<fmt:message key="msg.disable.user.1"/>&nbsp;&nbsp;
								<fmt:message key="msg.disable.user.2"/>&nbsp;&nbsp;
								<fmt:message key="msg.disable.user.3"/><br />
								<fmt:message key="msg.disable.user.4"/>
							</p>
							<c:url var="disableaction" value="/user/disable.do">
								<c:param name="userId" value="${userId}" />
								<c:param name="orgId" value="${orgId}" />
							</c:url>
							<div class="text-end">
								<button class="btn btn-secondary" type="button" onClick="javascript:document.location='<c:out value="${cancel}"/>'">
									<fmt:message key="admin.cancel" />
								</button>
								<csrf:form style="display: inline-block;" id="disable_${userId}" method="post" action="/lams/admin/user/disable.do">
									<input type="hidden" name="userId" value="${userId}"/>
									<input type="hidden" name="orgId" value="<c:out value="${orgId}"/>"/>
									<input type="submit" class="btn btn-primary" value="<fmt:message key="admin.disable" />"/>
								</csrf:form>
							</div>
						</div>
					</div>	
				</c:if>
				
				<c:if test="${method == 'delete'}">
					<div class="card" >
						<div class="card-header">
							<fmt:message key="admin.user.delete"/>
						</div>
						<div class="card-body">     
							<p><fmt:message key="msg.delete.user.1"/>&nbsp;&nbsp;<fmt:message key="msg.delete.user.2"/></p>
							<c:url var="deleteaction" value="/user/delete.do">
								<c:param name="userId" value="${userId}" />
								<c:param name="orgId" value="${orgId}" />
							</c:url>
							<div class="float-end">
								<button class="btn btn-default" type="button" onClick="javascript:document.location='<c:out value="${cancel}"/>'">
									<fmt:message key="admin.cancel" />
								</button>
							
								<csrf:form style="display: inline-block;" id="delete_${userId}" method="post" action="delete.do">
									<input type="hidden" name="userId" value="${userId}"/>
									<input type="hidden" name="orgId" value="${orgId}"/>
									<button type="submit" class="btn btn-danger">
										<fmt:message key="admin.delete" />
									</button>
								</csrf:form>
							</div>
						</div>
					</div>
				</c:if>
				
	<div id="footer"/>
	</lams:Page5>
</body>
</lams:html>


