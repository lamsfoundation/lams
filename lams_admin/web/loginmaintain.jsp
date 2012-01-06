<%@ include file="/taglibs.jsp"%>

<h4><a href="sysadminstart.do"><fmt:message key="sysadmin.maintain" /></a>
</h4>
<h1><fmt:message key="sysadmin.maintain.loginpage"/></h1>

<html:errors />
<br />
<html:form action="/loginsave.do?method=save" enctype="multipart/form-data" method="post">
<p><fmt:message key="sysadmin.login.logo"/></p>
<p><html:file property="logo" size="40" styleClass="button" /></p>
<br />
<c:set var="language"><lams:user property="localeLanguage"/></c:set>
<p><fmt:message key="sysadmin.login.text"/></p>

<div align="center"> 

<c:set var="existingContent"><bean:write name='LoginMaintainForm' property='news' filter='false' /></c:set>

<lams:CKEditor id="news" value="${existingContent}"	contentFolderID="../public" width="95%" height="600px"></lams:CKEditor>

</div>
<br />
<p align="center">
	<html:submit styleClass="button"><fmt:message key="admin.save" /></html:submit>
	<html:reset styleClass="button"><fmt:message key="admin.reset" /></html:reset>
	<html:cancel styleClass="button"><fmt:message key="admin.cancel" /></html:cancel>
</p>
</html:form>
