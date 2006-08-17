<%@ page contentType="text/html; charset=utf-8" language="java" %>

<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<h1 class=no-tabs-below><fmt:message key="sysadmin.headline"/></h1>
<br />
<p><a href="cache.do"><fmt:message key="cache.title"/></a></p>

<p><a href="<lams:LAMSURL/>/admin/loginmaintain.do"><fmt:message key="sysadmin.maintain.loginpage"/></a>

<p><fmt:message key="sysadmin.manage.config.file"/></p>

<h2><fmt:message key="sysadmin.batch.heading"/></h2>
<p><fmt:message key="sysadmin.batch.description"/>.</p>

<p><fmt:message key="sysadmin.batch.temp.file.delete"/></p>
<p><a href="<lams:LAMSURL/>/monitoring/monitoring.do?method=deleteOldPreviewLessons"><fmt:message key="sysadmin.batch.preview.delete"/></a></p>

