<%@ taglib uri="/WEB-INF/struts-html-el.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic-el.tld" prefix="logic-el" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>

	<!-- this form temporarily represents tool icon in monitoring environment, 
	remove this form once the tool is deployed into monitoring environment -->

<%
	String userId="222";
	String toolContentId="6666";
	String toolSessionId="88888888";
	String toolUrl="/learningStarter?userId=" + userId + "&toolSessionId=" + toolSessionId + "&toolContentId=" + toolContentId;
%>


<html:form action="<%=toolUrl%>" method="post">
      <table border=1>
      </table><br/><BR>
      <html:submit value="Learning Starter"/>
</html:form>




