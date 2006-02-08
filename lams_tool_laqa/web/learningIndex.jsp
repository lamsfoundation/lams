<%@ taglib uri="/WEB-INF/struts-html-el.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic-el.tld" prefix="logic-el" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>

	<!-- this form temporarily represents tool icon in monitoring environment, 
	remove this form once the tool is deployed into monitoring environment -->

<%
	String userId="222";
	String toolSessionID="88888888";
	String toolSessionID2="55555555";

	String toolUrl="/learningStarter?toolSessionID=" + toolSessionID;
	String toolUrl2="/learningStarter?&toolSessionID=" + toolSessionID2;
	
	String strCreateToolSession="/learningStarter?toolSessionID=" + toolSessionID +  "&createToolSession=1";
	String strCreateToolSession2="/learningStarter?toolSessionID=" + toolSessionID2 +  "&createToolSession=1";
%>

<html:form action="<%=strCreateToolSession%>" method="post">
      <table border=1>
      </table><br/><BR>
      <html:submit value="Create Tool Session: 88888888"/>
</html:form>

<html:form action="<%=strCreateToolSession2%>" method="post">
      <table border=1>
      </table><br/><BR>
      <html:submit value="Create Tool Session: 55555555"/>
</html:form>


<html:form action="<%=toolUrl%>" method="post">
      <table border=1>
      </table><br/><BR>
      <html:submit value="Learning Starter : 88888888"/>
</html:form>


<html:form action="<%=toolUrl2%>" method="post">
      <table border=1>
      </table><br/><BR>
      <html:submit value="Learning Starter : 55555555"/>
</html:form>



