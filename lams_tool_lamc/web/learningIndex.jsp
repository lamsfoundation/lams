<%@ taglib uri="tags-html-el" prefix="html" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-logic-el" prefix="logic-el" %>
<%@ taglib uri="tags-c" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-fck-editor" prefix="FCK" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

	<!-- this form temporarily represents tool icon in monitoring environment, 
	remove this form once the tool is deployed into monitoring environment -->

<%
	String toolSessionID="88888888";
	String toolUrl="/learningStarter?toolSessionID=" + toolSessionID;
%>


<html:form action="<%=toolUrl%>" method="post">
      <table border=1>
      </table><br/><BR>
      <html:submit value="Learning Starter"/>
</html:form>




