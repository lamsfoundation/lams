<%@ taglib uri="/WEB-INF/struts-html-el.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic-el.tld" prefix="logic-el" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>

	<!-- this form  exists temporarily to represent tool icon, remove this form once the tool is deployed into authoring environment -->
<%
	String toolContentId="1234";
	String userId="1111";
	String toolUrl="/authoringStarter?userId=" + userId + "&toolContentID=" + toolContentId;
	
	String strCopyToolContent="/authoringStarter?toolContentID=" + toolContentId + "&copyToolContent=1";
%>


<html:form action="<%=toolUrl%>" method="post">
      <table border=1>
      </table><br/>
      <html:submit value="Tool Icon"/>
</html:form>


<html:form action="<%=strCopyToolContent%>" method="post">
      <table border=1>
      </table><br/>
      <html:submit value="Copy Original Tool Content"/>
</html:form>




