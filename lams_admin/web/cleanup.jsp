<!DOCTYPE html>

<%@ include file="/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.FileUtil" %>
<%@ page import="org.lamsfoundation.lams.util.TempDirectoryFilter" %>

<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="sysadmin.cleanup"/></c:set>
	<title>${title}</title>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />

	<lams:css/>
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme.css" type="text/css" media="screen">
</lams:head>
    
<body class="stripes">
	
	<lams:Page type="admin" formID="cleanupForm">
	
		<p><a href="<lams:LAMSURL/>admin/sysadminstart.do" class="btn btn-default"><fmt:message key="sysadmin.maintain" /></a></p>
		
		<div class="panel panel-default" >
			<div class="panel-heading">
				<span class="panel-title">
					<fmt:message key="sysadmin.batch.temp.file.delete" />
				</span>
			</div>
			
			<div class="panel-body">
				<lams:errors path="numDays"/>

				<lams:Alert type="warn" id="cleanup-warning" close="false">
					<fmt:message key="msg.cleanup.warning" /><br />
				</lams:Alert>
				
				<c:set var="filesDeleted" value="${filesDeleted}"/>
				<c:if test="${not empty filesDeleted}">
					<p><c:out value="${filesDeleted}" /></p>
				</c:if>
					<input class="btn btn-default" type="submit" value="Calculate" onClick="javascript:document.location='../cleanup/refresh.do'" />
					<span class="loffset10"><c:out value="<%= FileUtil.getTempDir() %>" /></span>
					
					<ul class="list-group voffset5">
						<li class="list-group-item"><c:out value="<%= TempDirectoryFilter.zip_prefix %>" />* : <c:out value="${zipTotal}" /><c:if test="${empty zipTotal}"><i><fmt:message key="label.unknown"/></i></c:if> KB
						<li class="list-group-item"><c:out value="<%= TempDirectoryFilter.tmp_prefix %>" />* : <c:out value="${tmpTotal}" /><c:if test="${empty tmpTotal}"><i><fmt:message key="label.unknown"/></i></c:if> KB
					</ul>
					<fmt:message key="msg.cleanup.actual.space" />
				</p>
				
				<p>
				<fmt:message key="msg.cleanup.recommended" />
				</p>
				
				<form:form action="files.do" modelAttribute="cleanupForm" id="cleanupForm" method="post">
				
					<p><fmt:message key="label.cleanup.delete" />:
					<form:input path="numDays" maxlength="4" size="4" cssClass="form-control form-control-inline" /></p>
					
					<div class="pull-right">
						<input type="submit" class="btn btn-primary loffset5" value="<fmt:message key="admin.delete"/>" />
					</div>
					
				</form:form>
			</div>
		</div>
		
		<div class="panel panel-default" >
			<div class="panel-heading">
				<span class="panel-title">
					<fmt:message key="sysadmin.cache.clear" />
				</span>
			</div>
			
			<div class="panel-body">
				<form:form action="cache.do" method="post">
					<input type="submit" class="btn btn-primary" value="<fmt:message key="sysadmin.clear"/>" />
					<c:if test="${param.cacheCleared eq 'true'}">
						<span class="loffset10"><fmt:message key="sysadmin.cache.cleared" /></span>
					</c:if>
				</form:form>
			</div>
		</div>
	
	</lams:Page>

</body>
</lams:html>



