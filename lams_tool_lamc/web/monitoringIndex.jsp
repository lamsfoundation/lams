
<%@ page import="org.lamsfoundation.lams.tool.qa.QaUtils" %>
<%@ taglib uri="/WEB-INF/struts-html-el.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic-el.tld" prefix="logic-el" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>

	<!-- this form temporarily represents tool icon in monitoring environment, 
	remove this form once the tool is deployed into monitoring environment -->
<%
	String fromToolContentId="1234";
	String toToolContentId="4321";
	String toolSessionId="999888";
	String userId="123123";
	String toolUrl="/monitoringStarter?toolSessionId=" + toolSessionId + 
											"&fromToolContentId=" + fromToolContentId + 
											"&toToolContentId=" + toToolContentId +
											"&userId=" + userId;



%>

<!--Dave, this is a temporary form/table, should be removed -->
<html:form action="<%=toolUrl%>" method="post">
	<table align=center> <!-- Dave to take off-->
	<tr><td>
		<html:submit property="startLesson" styleClass="linkbutton" onmouseover="pviiClassNew(this,'linkbutton')" onmouseout="pviiClassNew(this,'linkbutton')">
			<bean:message key="button.startLesson"/>
		</html:submit>
	</td><td>
		<html:submit property="deleteLesson" styleClass="linkbutton" onmouseover="pviiClassNew(this,'linkbutton')" onmouseout="pviiClassNew(this,'linkbutton')">
			<bean:message key="button.deleteLesson"/>
		</html:submit>
	</td><td>
		<html:submit property="forceComplete" styleClass="linkbutton" onmouseover="pviiClassNew(this,'linkbutton')" onmouseout="pviiClassNew(this,'linkbutton')">
			<bean:message key="button.forceComplete"/>
		</html:submit>
	<td></tr>
	</table>
</html:form>

<br>






