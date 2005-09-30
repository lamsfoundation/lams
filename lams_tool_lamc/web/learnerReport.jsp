<%@ taglib uri="/WEB-INF/struts-html-el.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic-el.tld" prefix="logic-el" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>

	<!-- this form is exists temporarily to represent tool icon, remove this form once the tool is deployed into authoring environment -->
	<html:form  action="/learning?method=displayQ&validate=false" method="POST" target="_self">
	<br>
	<center>

	<table align=center>
		<tr><td>
			<c:out value="${sessionScope.reportTitleLearner}"/>
		</td></tr>

		<tr><td> &nbsp </td></tr>

		<jsp:include page="singleLearnerReport.jsp" />

		<tr> <td> 
			<c:out value="${sessionScope.endLearningMessage}"/>
		</td> </tr>
		
		<tr> <td> 
			&nbsp&nbsp&nbsp&nbsp
		</td> </tr>

		<tr>
			 <td> 
			 	 <html:submit property="endLearning" styleClass="linkbutton" onmouseover="pviiClassNew(this,'linkbutton')" onmouseout="pviiClassNew(this,'linkbutton')">
					<bean:message key="button.endLearning"/>
				</html:submit>
			</td> 
		</tr>
	</table>

</html:form>
	
	
	
	
	
	