<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-core" prefix="c" %>
<c:set var="lams"><lams:LAMSURL/></c:set>

<c:redirect url="${lams}errorpages/403.jsp"/>
