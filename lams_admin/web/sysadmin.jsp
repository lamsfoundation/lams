<%@ page contentType="text/html; charset=utf-8" language="java" %>

<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<H1 class=no-tabs-below><fmt:message key="sysadmin.headline"/></H1>

<p><A HREF="cache.do"><fmt:message key="cache.title"/></A></p>
<p><fmt:message key="sysadmin.manage.config.file"/></p>

<H2><fmt:message key="sysadmin.batch.heading"/></H2>
<P><fmt:message key="sysadmin.batch.description"/>.</P>

<p><fmt:message key="sysadmin.batch.temp.file.delete"/></p>
<p><A HREF="<lams:LAMSURL/>/monitoring/monitoring.do?method=deleteOldPreviewLessons"><fmt:message key="sysadmin.batch.preview.delete"/></a></p>
