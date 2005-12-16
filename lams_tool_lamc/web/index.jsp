<%@ taglib uri="tags-html-el" prefix="html" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-logic-el" prefix="logic-el" %>
<%@ taglib uri="tags-c" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="fck-editor" prefix="FCK" %>

	<!-- this form  exists temporarily to represent tool icon, remove this form once the tool is deployed into authoring environment -->
<%
	String userContentId="1234";
	String userId="1111";
	String toolUrl="/authoringStarter?userId=" + userId + "&toolContentID=" + userContentId;
%>


<html:form action="<%=toolUrl%>" method="post">
      <table border=1>
      </table><br/>
      <html:submit value="Tool Icon"/>
</html:form>




