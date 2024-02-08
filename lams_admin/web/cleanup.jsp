<!DOCTYPE html>
<%@ include file="/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.FileUtil" %>
<%@ page import="org.lamsfoundation.lams.util.TempDirectoryFilter" %>

<%-- Build breadcrumb --%>
<c:set var="breadcrumbItems"><lams:LAMSURL/>admin/appadminstart.do | <fmt:message key="appadmin.maintain" /></c:set>
<c:set var="breadcrumbItems">${breadcrumbItems}, . | <fmt:message key="sysadmin.cleanup"/></c:set>
	
<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="sysadmin.cleanup"/></c:set>
	<title>${title}</title>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />

	<link rel="stylesheet" href="<lams:LAMSURL/>css/components.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>includes/font-awesome6/css/all.css">
	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap5.bundle.min.js"></script>
</lams:head>

	<lams:PageAdmin title="${title}" formID="cleanupForm" breadcrumbItems="${breadcrumbItems}">
		<div class="card" >
			<div class="card-header">
				<fmt:message key="appadmin.batch.temp.file.delete" />
			</div>
				
			<div class="card-body">
				<lams:errors path="numDays"/>
		
				<lams:Alert5 type="warn" id="cleanup-warning" close="false">
					<fmt:message key="msg.cleanup.warning" /><br />
				</lams:Alert5>
				
				<c:set var="filesDeleted" value="${filesDeleted}"/>
				<c:if test="${not empty filesDeleted}">
					<p><c:out value="${filesDeleted}" /></p>
				</c:if>
				<input class="btn btn-secondary mb-2" type="submit" value="Calculate" onClick="javascript:document.location='refresh.do'" />
				<span class="loffset10"><c:out value="<%= FileUtil.getTempDir() %>" /></span>
				
				<ul class="list-group voffset5">
					<li class="list-group-item"><c:out value="<%= TempDirectoryFilter.zip_prefix %>" />* : <c:out value="${zipTotal}" /><c:if test="${empty zipTotal}"><i><fmt:message key="label.unknown"/></i></c:if> KB
					<li class="list-group-item"><c:out value="<%= TempDirectoryFilter.tmp_prefix %>" />* : <c:out value="${tmpTotal}" /><c:if test="${empty tmpTotal}"><i><fmt:message key="label.unknown"/></i></c:if> KB
				</ul>
				<fmt:message key="msg.cleanup.actual.space" />
			
				<p>
					<fmt:message key="msg.cleanup.recommended" />
				</p>
			
				<form:form action="files.do" modelAttribute="cleanupForm" id="cleanupForm" method="post">
					<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
			
					<p><fmt:message key="label.cleanup.delete" />:
					<form:input path="numDays" maxlength="4" size="4" cssClass="form-control d-inline ms-2" cssStyle="width: 100px" title="Number of days" /></p>
					
					<div class="text-end">
						<input type="submit" class="btn btn-primary" value="<fmt:message key="admin.delete"/>" />
					</div>
				</form:form>
			</div>
		</div>
			
		<div class="card my-3" >
			<div class="card-header">
				<fmt:message key="sysadmin.cache.clear" />
			</div>
			
			<div class="card-body text-end">
				<form:form action="cache.do" method="post">
					<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
					
					<c:if test="${param.cacheCleared eq 'true'}">
						<span class="me-2"><fmt:message key="sysadmin.cache.cleared" /></span>
					</c:if>
					<input type="submit" class="btn btn-primary" value="<fmt:message key="sysadmin.clear"/>" />

				</form:form>
			</div>
		</div>
			
		<div class="card" >
			<div class="card-header">
				<fmt:message key="sysadmin.garbage.clear" />
			</div>
			
			<div class="card-body text-end">
				<form:form action="garbage.do" method="post">
					<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
					
					<c:if test="${param.garbageCollectorRun eq 'true'}">
						<span class="me-2"><fmt:message key="sysadmin.garbage.cleared" /></span>
					</c:if>
					<input type="submit" class="btn btn-primary" value="<fmt:message key="sysadmin.clear"/>" />
				</form:form>
			</div>
		</div>
	
	</lams:PageAdmin>
</lams:html>
