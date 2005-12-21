<%@ taglib uri="tags-html-el" prefix="html" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-logic-el" prefix="logic-el" %>
<%@ taglib uri="tags-c" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-fck-editor" prefix="FCK" %>

	<!-- this form  exists temporarily to represent tool icon, remove this form once the tool is deployed into authoring environment -->
<%
	String userContentId="1234";
	String userId="1111";
	String toolUrl="/authoringStarter?userId=" + userId + "&toolContentID=" + userContentId;
	
	String strCopyToolContent="/authoringStarter?userId=" + userId + "&toolContentID=" + userContentId + "&copyToolContent=1";
	String strRemoveToolContent="/authoringStarter?userId=" + userId + "&toolContentID=" + userContentId + "&removeToolContent=1";
	String strDefineLater="/defineLaterStarter?userId=" + userId + "&toolContentID=" + userContentId;
%>

Start authoring module
<html:form action="<%=toolUrl%>" method="post">
      <table border=1>
      </table><br/>
      <html:submit value="Tool Icon"/>
</html:form>

Copy Tool Content
<html:form action="<%=strCopyToolContent%>" method="post">
      <table border=1>
      </table><br/>
      <html:submit value="Copy Tool Content"/>
</html:form>

Remove Tool Content
<html:form action="<%=strRemoveToolContent%>" method="post">
      <table border=1>
      </table><br/>
      <html:submit value="Remove Tool Content"/>
</html:form>

Define Later Url
<html:form action="<%=strDefineLater%>" method="post">
      <table border=1>
      </table><br/>
      <html:submit value="Define Later URL"/>
</html:form>



