<%--
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
USA

http://www.gnu.org/licenses/gpl.txt
--%>

<%@ taglib uri="tags-bean" prefix="bean"%> 
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="fck-editor" prefix="FCK" %>
<%@ taglib uri="tags-lams" prefix="lams" %>


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
	
	
	
	
	
	