<!DOCTYPE html>

<%@ include file="/taglibs.jsp"%>

<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="admin.user.management"/></c:set>
	<title>${title}</title>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />

	<lams:css/>
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme.css" type="text/css" media="screen">
</lams:head>
    
<body class="stripes">
	<lams:Page type="admin" title="${title}">
		<form>
				<c:if test="${empty orgId}">
					<c:url var="cancel" value="/usersearch.do" />
				</c:if>
				<c:if test="${not empty orgId}">
					<c:url var="cancel" value="/usermanage.do">
						<c:param name="org" value="${orgId}" />
					</c:url>
				</c:if>
				
				<c:if test="${method == 'disable'}">
					<div class="panel panel-default" >
						<div class="panel-heading">
							<span class="panel-title">
								<fmt:message key="admin.user.disable"/>
							</span>
						</div>
						<div class="panel-body">     
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
							<div class="pull-right">
								<button class="btn btn-primary" type="button" onClick="javascript:document.location='<c:out value="${disableaction}"/>'">
									Disable
								</button>
								<button class="btn btn-default" type="button" onClick="javascript:document.location='<c:out value="${cancel}"/>'">
									Cancel 
								</button>
							</div>
						</div>
					</div>	
				</c:if>
				
				<c:if test="${method == 'delete'}">
					<div class="panel panel-default" >
						<div class="panel-heading">
							<span class="panel-title">
								<fmt:message key="admin.user.delete"/>
							</span>
						</div>
						<div class="panel-body">     
							<p><fmt:message key="msg.delete.user.1"/>&nbsp;&nbsp;<fmt:message key="msg.delete.user.2"/></p>
							<c:url var="deleteaction" value="/user/delete.do">
								<c:param name="userId" value="${userId}" />
								<c:param name="orgId" value="${orgId}" />
							</c:url>
							<div class="pull-right">
								<button class="btn btn-default" type="button" onClick="javascript:document.location='<c:out value="${deleteaction}"/>'">
									Delete
								</button>
								<button class="btn btn-default" type="button" onClick="javascript:document.location='<c:out value="${cancel}"/>'">
									Cancel
								</button>
							</div>
						</div>
					</div>
				</c:if>
		</form>
				
	<div id="footer"/>
	</lams:Page>
</body>
</lams:html>


