<%@ include file="/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.FileUtil" %>
<%@ page import="org.lamsfoundation.lams.util.TempDirectoryFilter" %>

<h4><a href="sysadminstart.do"><fmt:message key="sysadmin.maintain" /></a></h4>
<h1><fmt:message key="sysadmin.batch.temp.file.delete"/></h1>

<p><html:errors /></p>

<logic:notEmpty name="filesDeleted">
	<p><c:out value="${filesDeleted}" /></p>
</logic:notEmpty>

<p>
	<fmt:message key="msg.cleanup.warning" /><br />
	<input class="button" type="submit" value="Calculate" onClick="javascript:document.location='cleanup.do?action=refresh'" />
	<ul>
		<li><c:out value="<%= FileUtil.getTempDir() %>" />
		<ul>
			<li><%= TempDirectoryFilter.zip_prefix %>* : <c:out value="${zipTotal}" /><logic:empty name="zipTotal"><i><fmt:message key="label.unknown"/></i></logic:empty> KB
			<li><%= TempDirectoryFilter.tmp_prefix %>* : <c:out value="${tmpTotal}" /><logic:empty name="tmpTotal"><i><fmt:message key="label.unknown"/></i></logic:empty> KB
		</ul>
	</ul>
	<br />
	<fmt:message key="msg.cleanup.actual.space" />
</p>

<p>
<fmt:message key="msg.cleanup.recommended" />
</p>

<html:form action="/cleanup" method="post">
	<p><fmt:message key="label.cleanup.delete" />: <br />
	<html:text property="numDays" maxlength="4" size="4" /></p>

	<html:submit styleClass="button"><fmt:message key="admin.delete"/></html:submit>
	<html:cancel styleClass="button"><fmt:message key="admin.cancel"/></html:cancel>					

</html:form>