<%@ include file="/taglibs.jsp"%>

<p><a href="<lams:LAMSURL/>/admin/sysadminstart.do" class="btn btn-default"><fmt:message key="sysadmin.maintain" /></a></p>

<html:errors />

<html:form action="/loginsave.do?method=save" enctype="multipart/form-data" method="post">
<c:set var="language"><lams:user property="localeLanguage"/></c:set>
<p class="help-block"><fmt:message key="sysadmin.login.text"/></p>

<c:set var="existingContent"><bean:write name='LoginMaintainForm' property='news' filter='false' /></c:set>

<lams:CKEditor id="news" value="${existingContent}"	contentFolderID="../public" height="600px"></lams:CKEditor>

<div class="pull-right voffset5">
	<html:cancel styleId="cancelButton" styleClass="btn btn-default"><fmt:message key="admin.cancel" /></html:cancel>
	<html:submit styleId="saveButton" styleClass="btn btn-primary loffset5"><fmt:message key="admin.save" /></html:submit>
</div>
</html:form>
