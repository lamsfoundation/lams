<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>

<html>
<tiles:insert page="template.jsp" flush="true">
	<tiles:put name="title" value="LAMS :: Error"/>
	<tiles:put name="pageHeader" value="Error"/>
	<tiles:put name="header" value="header.jsp"/>
	<tiles:put name="content" value="errorContent.jsp" />	
	<tiles:put name="footer" value="footer.jsp"/>	
</tiles:insert>
</html>