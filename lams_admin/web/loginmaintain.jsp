<%@ include file="/taglibs.jsp"%>
<%@ taglib uri="tags-lams" prefix="lams" %>
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
<c:set var="basePath"><lams:LAMSURL/>/fckeditor/</c:set>
<fck:editor id="news" basePath="${basePath}"
    width = "95%"
	imageBrowserURL="${basePath}editor/filemanager/browser/default/browser.html?Type=Image&Connector=connectors/jsp/connector&CurrentFolder=/-1/"
	linkBrowserURL="${basePath}editor/filemanager/browser/default/browser.html?Connector=connectors/jsp/connector&CurrentFolder=/-1/"
	flashBrowserURL="${basePath}editor/filemanager/browser/default/browser.html?Type=Flash&Connector=connectors/jsp/connector&CurrentFolder=/-1/"
	imageUploadURL="${basePath}editor/filemanager/upload/simpleuploader?Type=Image&CurrentFolder=/-1/"
	linkUploadURL="${basePath}editor/filemanager/upload/simpleuploader?Type=File&CurrentFolder=/-1/"
	flashUploadURL="${basePath}editor/filemanager/upload/simpleuploader?Type=Flash&CurrentFolder=/-1/"
	defaultLanguage="${language}" 
	autoDetectLanguage="false"
	> 
	<bean:write name="LoginMaintainForm" property="news" filter="false" />
</fck:editor>
</div>
<br />
<p align="center">
	<html:submit styleClass="button"><fmt:message key="admin.save" /></html:submit>
	<html:reset styleClass="button"><fmt:message key="admin.reset" /></html:reset>
	<html:cancel styleClass="button"><fmt:message key="admin.cancel" /></html:cancel>
</p>
</html:form>