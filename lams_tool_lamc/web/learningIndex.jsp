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
	
	String toolUrlTeacher="/learningStarter?toolSessionID=" + toolSessionID + "&mode=teacher";
	
	String strCreateToolSession="/learningStarter?toolSessionID=" + toolSessionID2 +  "&mode=learner" + "&createToolSession=1";
	String strRemoveToolSession="/learningStarter?toolSessionID=" + toolSessionID2 +  "&mode=learner" + "&removeToolSession=1";
	String strLeaveToolSession="/learningStarter?toolSessionID=" + toolSessionID2 +  "&mode=learner" + "&leaveToolSession=1" +  "&learnerId=4";
	
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

<html:form action="<%=toolUrlTeacher%>" method="post">
      <table border=1>
      </table><br/><BR>
      <html:submit value="Learning Starter, mode:teacher"/>
</html:form>

<html:form action="<%=strCreateToolSession%>" method="post">
      <table border=1>
      </table><br/><BR>
      <html:submit value="Create Tool Session"/>
</html:form>

<html:form action="<%=strRemoveToolSession%>" method="post">
      <table border=1>
      </table><br/><BR>
      <html:submit value="Remove Tool Session"/>
</html:form>

<html:form action="<%=strLeaveToolSession%>" method="post">
      <table border=1>
      </table><br/><BR>
      <html:submit value="Leave Tool Session"/>
</html:form>
