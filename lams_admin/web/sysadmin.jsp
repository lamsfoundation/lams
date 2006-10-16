<%@ page contentType="text/html; charset=utf-8" language="java" %>

<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<h1 class=no-tabs-below><fmt:message key="sysadmin.headline"/></h1>
<br />
<p><a href="cache.do"><fmt:message key="cache.title"/></a></p>

<p><a href="<lams:LAMSURL/>admin/loginmaintain.do"><fmt:message key="sysadmin.maintain.loginpage"/></a></p>

<p><a href="<lams:LAMSURL/>admin/serverlist.do"><fmt:message key="sysadmin.maintain.external.servers"/></a></p>

<p><fmt:message key="sysadmin.manage.config.file"/></p>

<p><fmt:message key="sysadmin.batch.temp.file.delete"/></p>

