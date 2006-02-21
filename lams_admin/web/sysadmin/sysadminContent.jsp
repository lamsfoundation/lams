<%@ page contentType="text/html; charset=iso-8859-1" language="java" %>

<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<H1>System Administration<H1>

<H2><A HREF="cache.do"><fmt:message key="cache.title"/></A></H2>
<H2><fmt:message key="sysadmin.manage.config.file"/></H3>

<H2><fmt:message key="sysadmin.batch.heading"/></H2>
<P><fmt:message key="sysadmin.batch.description"/>.</P>

<H3><fmt:message key="sysadmin.batch.temp.file.delete"/></H3>
<H3><A HREF="sysadmin.do?method=deleteOldPreviewLessons"><fmt:message key="sysadmin.batch.preview.delete"/></a></H3>

