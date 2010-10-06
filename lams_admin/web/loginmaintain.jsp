<%@ include file="/taglibs.jsp"%>
<%@ taglib uri="tags-lams" prefix="lams" %>

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

<lams:CKEditor id="news" value="<bean:write name="LoginMaintainForm" property="news" filter="false" />"
	contentFolderID="-1" width="95%">
</lams:CKEditor>

</div>
<br />
<p align="center">
	<html:submit styleClass="button"><fmt:message key="admin.save" /></html:submit>
	<html:reset styleClass="button"><fmt:message key="admin.reset" /></html:reset>
	<html:cancel styleClass="button"><fmt:message key="admin.cancel" /></html:cancel>
</p>
</html:form>