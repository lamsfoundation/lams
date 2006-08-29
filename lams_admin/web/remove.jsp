<%@ page contentType="text/html; charset=utf-8" language="java" %>

<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>

<form>
<logic:equal name="method" value="disable">
<h2>Disable User</h2>
<p>&nbsp;</p>
<p>This user has lesson and/or sequence data associated with it and cannot be deleted.&nbsp;&nbsp;
User will be disabled instead, maintaining their data, but the account will
be treated as deleted.&nbsp;&nbsp;It will not appear in group/subgroup lists, nor
will the user be able to login.<br />
You can enable the user account again by editing the user's profile.</p>
<c:url var="disableaction" value="user.do">
		<c:param name="method" value="disable" />
		<c:param name="userId" value="${userId}" />
		<c:param name="orgId" value="${orgId}" />
	</c:url>
<div style="float:right">
<input type="button" value="Disable" onClick="javascript:document.location='<c:out value="${disableaction}"/>'" />
<input type="button" value="Cancel" onClick="javascript:document.location='usermanage.do?org=<c:out value="${orgId}" />'" />
</div>
</logic:equal>

<logic:equal name="method" value="delete">
<h2>Delete User</h2>
<p>&nbsp;</p>
<p>User has no associated data and can be safely removed.&nbsp;&nbsp;Are you sure you want to delete this account?</p>
<c:url var="deleteaction" value="user.do">
		<c:param name="method" value="delete" />
		<c:param name="userId" value="${userId}" />
		<c:param name="orgId" value="${orgId}" />
	</c:url>
<div style="float:right">
<input type="button" value="Delete" onClick="javascript:document.location='<c:out value="${deleteaction}"/>'" />
<input type="button" value="Cancel" onClick="javascript:document.location='usermanage.do?org=<c:out value="${orgId}" />'" />
</div>
</logic:equal>
</form>