<%@ include file="/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.FileUtil" %>
<%@ page import="org.lamsfoundation.lams.util.TempDirectoryFilter" %>

<p><a href="<lams:LAMSURL/>/admin/sysadminstart.do" class="btn btn-default"><fmt:message key="sysadmin.maintain" /></a></p>

<%-- Error Messages --%>
 <c:set var="errorKey" value="GLOBAL" />
   <c:if test="${not empty errorMap and not empty errorMap[errorKey]}">
      <lams:Alert id="error" type="danger" close="false">
        <c:forEach var="error" items="${errorMap[errorKey]}">
           <c:out value="${error}" />
        </c:forEach>
      </lams:Alert>
  </c:if>

<lams:Alert type="warn" id="cleanup-warning" close="false">
	<fmt:message key="msg.cleanup.warning" /><br />
</lams:Alert>

<c:set var="filesDeleted" value="${filesDeleted}"/>
<c:if test="${not empty filesDeleted}">
	<p><c:out value="${filesDeleted}" /></p>
</c:if>

	<input class="btn btn-default" type="submit" value="Calculate" onClick="javascript:document.location='cleanup/refresh.do'" />
	<span class="loffset10"><c:out value="<%= FileUtil.getTempDir() %>" /></span>
	
	<ul class="list-group voffset5">
		<li class="list-group-item"><%= TempDirectoryFilter.zip_prefix %>* : <c:out value="${zipTotal}" /><logic:empty name="zipTotal"><i><fmt:message key="label.unknown"/></i></logic:empty> KB
		<li class="list-group-item"><%= TempDirectoryFilter.tmp_prefix %>* : <c:out value="${tmpTotal}" /><logic:empty name="tmpTotal"><i><fmt:message key="label.unknown"/></i></logic:empty> KB
	</ul>
	<fmt:message key="msg.cleanup.actual.space" />
</p>

<p>
<fmt:message key="msg.cleanup.recommended" />
</p>

<form:form action="start.do" modelAttribute="dynaForm" id="dynaForm" method="post">

	<p><fmt:message key="label.cleanup.delete" />:
	<input type="text" name="numDays" maxlength="4" size="4" class="form-control form-control-inline" /></p>
	
	<div class="pull-right">
		<html:cancel styleClass="btn btn-default"><fmt:message key="admin.cancel"/></html:cancel>	
		<input type="submit" class="btn btn-primary loffset5" value="<fmt:message key="admin.delete"/>" />
	</div>
	
</form:form>
