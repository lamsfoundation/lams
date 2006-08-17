<%@ page contentType="text/html; charset=utf-8" language="java" %>

<%@ taglib uri="fck-editor" prefix="fck"%>
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-bean" prefix="bean"%>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-core" prefix="c" %>

<h1 class=no-tabs-below><fmt:message key="sysadmin.maintain.loginpage"/></h1>
<br />
<p style="font-color:red"><html:errors /></p>
<br />
<html:form action="/loginsave.do?method=save" enctype="multipart/form-data" method="post">
<p><fmt:message key="sysadmin.login.logo"/></p>
<p><html:file property="logo" size="40" styleClass="button" /></p>
<br />
<c:set var="language"><lams:user property="localeLanguage"/></c:set>
<p><fmt:message key="sysadmin.login.text"/></p>
<div align="center">
<fck:editor id="news" basePath="/lams/fckeditor/"
    width = "95%"
	imageBrowserURL="/FCKeditor/editor/filemanager/browser/default/browser.html?Type=Image&amp;Connector=connectors/jsp/connector"
	linkBrowserURL="/FCKeditor/editor/filemanager/browser/default/browser.html?Connector=connectors/jsp/connector"
	flashBrowserURL="/FCKeditor/editor/filemanager/browser/default/browser.html?Type=Flash&amp;Connector=connectors/jsp/connector"
	imageUploadURL="/FCKeditor/editor/filemanager/upload/simpleuploader?Type=Image"
	linkUploadURL="/FCKeditor/editor/filemanager/upload/simpleuploader?Type=File"
	flashUploadURL="/FCKeditor/editor/filemanager/upload/simpleuploader?Type=Flash"
	defaultLanguage="${language}" 
	autoDetectLanguage="false"
	toolbarSet="Default-Learner"> 
	<bean:write name="LoginMaintainForm" property="news" filter="false" />
</fck:editor>
</div>
<br />
<p align="center">
	<html:submit><fmt:message key="admin.save" /></html:submit>
	<html:reset><fmt:message key="admin.reset" /></html:reset>
	<html:cancel><fmt:message key="admin.cancel" /></html:cancel>
</p>
</html:form>