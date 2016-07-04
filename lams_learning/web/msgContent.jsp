<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-lams" prefix="lams" %>


<c:set var="title"><fmt:message key="error.title"/></c:set>

<lams:Page type="admin" title="${title}">

	<span class="text-center">
		 ${messageKey}
	</span>

</lams:Page>