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
								<csrf:form style="display: inline-block;" id="disable_${userId}" method="post" action="/lams/admin/user/disable.do"><input type="hidden" name="userId" value="${userId}"/><input type="hidden" name="orgId" value="<c:out value="${orgId}"/>"/><input type="submit" class="btn btn-primary" value="<fmt:message key="admin.disable" />"/></csrf:form>
								&nbsp;

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
								<csrf:form style="display: inline-block;" id="delete_${userId}" method="post" action="delete.do"><input type="hidden" name="userId" value="${userId}"/><input type="hidden" name="orgId" value="${orgId}"/><button type="submit" class="btn btn-danger"><fmt:message key="admin.delete" /></button></csrf:form>

								<button class="btn btn-default" type="button" onClick="javascript:document.location='<c:out value="${cancel}"/>'">
									Cancel
								</button>
							</div>
						</div>
					</div>
				</c:if>
				
	<div id="footer"/>
	</lams:Page>
</body>
</lams:html>


