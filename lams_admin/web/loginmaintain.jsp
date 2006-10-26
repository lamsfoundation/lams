<%@ include file="/taglibs.jsp"%>
<%@ taglib uri="fck-editor" prefix="fck"%>

<h2>
	<a href="sysadminstart.do"><fmt:message key="sysadmin.maintain" /></a> :
	<fmt:message key="sysadmin.maintain.loginpage"/>
</h2>
<br/>
<html:errors />
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
	> 
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