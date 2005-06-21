
<%@ page import="org.lamsfoundation.lams.tool.qa.QaUtils" %>
<%@ taglib uri="/WEB-INF/struts-html-el.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic-el.tld" prefix="logic-el" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>

	<!-- this form temporarily represents tool icon in monitoring environment, 
	remove this form once the tool is deployed into monitoring environment -->
<%
	String fromToolContentId="6666666";
	String toToolContentId="4071202367667828685";
	String toolSessionId="11237955877394927";
	String userId="1068924863";
	String toolUrl="/monitoringStarter?toolSessionId=" + toolSessionId + 
											"&fromToolContentId=" + fromToolContentId + 
											"&toToolContentId=" + toToolContentId +
											"&userId=" + userId;


	userId="222";
	String toolContentId="6666";
	String toolSessionId1="1234567";
	String toolSessionId2="";
	String toolSessionId3="";

	String multipleSessionstoolUrl="/monitoringStarter?userId=" + userId+ "&toolContentId=" + toolContentId + "&toolSessionId1=" + toolSessionId1 + 
															"&toolSessionId2=" + toolSessionId2 +
															"&toolSessionId3=" + toolSessionId3; 

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

<!--Dave, starting off the real monitoring jsp page -->
<html:form action="<%=multipleSessionstoolUrl%>" method="post">
	<table align=center> <!-- Dave to take off-->
		<tr>
			<td>
				<html:submit property="summary" styleClass="linkbutton" onmouseover="pviiClassNew(this,'linkbutton')" onmouseout="pviiClassNew(this,'linkbutton')">
					<bean:message key="button.summary"/>
				</html:submit> 
			</td>
		</tr>
	</table>
</html:form>






