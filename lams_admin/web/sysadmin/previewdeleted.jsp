<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<H1><fmt:message key="preview.deleted.title"/></H1>

<p><fmt:message key="preview.deleted.message">
 <fmt:param value="${numDeleted}"/>
 </fmt:message></p>
