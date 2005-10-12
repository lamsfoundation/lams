<%@ taglib uri="/WEB-INF/struts-html-el.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic-el.tld" prefix="logic-el" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>

	<!-- this form  exists temporarily to represent tool icon, remove this form once the tool is deployed into authoring environment -->
<%
	String userContentId="1234";
	String userId="1111";
	String toolUrl="/authoringStarter?userId=" + userId + "&toolContentId=" + userContentId;
%>


<html:form action="<%=toolUrl%>" method="post">
      <table border=1>
      </table><br/>
      <html:submit value="Tool Icon"/>
</html:form>




