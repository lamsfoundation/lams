<%@ page contentType="text/html; charset=utf-8" language="java" %>

<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<H1>System Administration</H1>

<UL>
<LI><A HREF="cache.do"><fmt:message key="cache.title"/></A></LI>
<LI><fmt:message key="sysadmin.manage.config.file"/></LI>
</UL>

<H2><fmt:message key="sysadmin.batch.heading"/></H2>
<P><fmt:message key="sysadmin.batch.description"/>.</P>

<UL>
<LI><fmt:message key="sysadmin.batch.temp.file.delete"/></LI>
<LI><A HREF="<lams:LAMSURL/>/monitoring/monitoring.do?method=deleteOldPreviewLessons"><fmt:message key="sysadmin.batch.preview.delete"/></a></LI>
</UL>

