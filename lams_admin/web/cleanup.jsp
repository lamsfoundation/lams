<%@ include file="/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.FileUtil" %>
<%@ page import="org.lamsfoundation.lams.util.TempDirectoryFilter" %>

<p><a href="<lams:LAMSURL/>/admin/sysadminstart.do" class="btn btn-default"><fmt:message key="sysadmin.maintain" /></a></p>

<html:errors />

<lams:Alert type="warn" id="cleanup-warning" close="false">
	<fmt:message key="msg.cleanup.warning" /><br />
</lams:Alert>

<logic:notEmpty name="filesDeleted">
	<p><c:out value="${filesDeleted}" /></p>
</logic:notEmpty>

	<input class="btn btn-default" type="submit" value="Calculate" onClick="javascript:document.location='cleanup.do?action=refresh'" />
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

<html:form action="/cleanup" method="post">

	<p><fmt:message key="label.cleanup.delete" />:
	<html:text property="numDays" maxlength="4" size="4" styleClass="form-control form-control-inline"/></p>
	
	<div class="pull-right">
		<html:cancel styleClass="btn btn-default"><fmt:message key="admin.cancel"/></html:cancel>	
		<html:submit styleClass="btn btn-primary loffset5"><fmt:message key="admin.delete"/></html:submit>
	</div>
	
</html:form>
