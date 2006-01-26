<%@ taglib uri="tags-html-el" prefix="html" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-logic-el" prefix="logic-el" %>
<%@ taglib uri="tags-c" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-fck-editor" prefix="FCK" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<%
	String toolSessionID="88888888";
	String toolUrl="/learningStarter?toolSessionID=" + toolSessionID + "&mode=learner";
	
	
	String toolSessionID2="55555555";
	String toolUrl2="/learningStarter?toolSessionID=" + toolSessionID2 + "&mode=learner";
%>

<html:form action="<%=toolUrl%>" method="post">
      <table border=1>
      </table><br/><BR>
      <html:submit value="Session 1 - Learning Starter, mode:learner"/>
</html:form>

<html:form action="<%=toolUrl2%>" method="post">
      <table border=1>
      </table><br/><BR>
      <html:submit value="Session 2 - Learning Starter, mode:learner"/>
</html:form>



