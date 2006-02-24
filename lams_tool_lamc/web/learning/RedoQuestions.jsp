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
<%@ taglib uri="tags-logic-el" prefix="logic-el" %>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="fck-editor" prefix="FCK" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="tool"><lams:WebAppURL/></c:set>

		<html:form  action="/learning?method=displayMc&validate=false" method="POST" target="_self">
				<table align=center bgcolor="#FFFFFF">
					  <tr>
					  	<td NOWRAP align=left class="input" valign=top bgColor="#333366" colspan=2> 
						  	<font size=2 color="#FFFFFF"> <b>  <bean:message key="label.assessment"/> </b> </font>
					  	</td>
					  </tr>
				
			 		<c:if test="${sessionScope.isRetries == 'true'}"> 		
						  <tr>
						  	<td NOWRAP align=center class="input" valign=top colspan=2> 
							  	<font size=3> <b>  <bean:message key="label.individual.results.withRetries"/> </b> </font>
						  	</td>
						  </tr>
  					</c:if> 			

					<c:if test="${sessionScope.isRetries == 'false'}"> 							  
						  <tr>
						  	<td NOWRAP align=center class="input" valign=top colspan=2> 
							  	<font size=3> <b>  <bean:message key="label.individual.results.withoutRetries"/> </b> </font>
						  	</td>
						  </tr>
					</c:if> 			


					  <tr>
					  	<td NOWRAP align=center class="input" valign=top colspan=2> 
						  	<font size=3>  <bean:message key="label.learner.redo"/> </font>
					  	</td>
					  </tr>	

					  <tr>
					  	<td NOWRAP align=right class="input" valign=top colspan=2> 
							&nbsp
					  	</td>
					  </tr>	


					  <tr>
					  	<td NOWRAP align=center class="input" valign=top colspan=2> 
						  	<font size=2>  <bean:message key="label.learner.bestMark"/>
						  	<b>   <c:out value="${sessionScope.learnerBestMark}"/> </b> 
						  	<bean:message key="label.outof"/> 
						  	<b> <c:out value="${sessionScope.totalQuestionCount}"/> </b> </font>
					  	</td>
					  </tr>	
					  
					<tr> 
				 		<td NOWRAP colspan=2 class="input" valign=top> 
				 		&nbsp
				 		</td>
			  	   </tr>
					  
					  
				  <tr>
				  	<td NOWRAP colspan=2 align=center class="input" valign=top> 
					  	<font size=2>
				  			<html:submit property="viewAnswers" styleClass="button">
								<bean:message key="label.view.answers"/>
							</html:submit>	 		
	   						&nbsp&nbsp&nbsp&nbsp&nbsp
	   						<html:submit property="redoQuestionsOk" styleClass="button">
								<bean:message key="label.redo.questions"/>
							</html:submit>	 				 		  					
						</font>
				  	 </td>
				  </tr>
		</html:form>

