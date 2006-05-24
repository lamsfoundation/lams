<%-- 
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
License Information: http://lamsfoundation.org/licensing/lams/2.0/

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License version 2 as 
  published by the Free Software Foundation.

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

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>

<html:html locale="true">
<head>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<jsp:include page="/learning/learningHeader.jsp" />
</head>
<body>

		<html:form  action="/learning?method=displayMc&validate=false" method="POST" target="_self">
				<table width="80%" cellspacing="8" align="CENTER" class="forms">
					  <tr>
					  	<th scope="col" valign=top colspan=2> 
						  	  <bean:message key="label.assessment"/> 
					  	</th>
					  </tr>
				
			 		<c:if test="${sessionScope.isRetries == 'true'}"> 		
						  <tr>
						  	<td NOWRAP align=center valign=top colspan=2> 
							  	<font size=3> <b>  <bean:message key="label.withRetries.results.summary"/> </b> </font>
						  	</td>
						  </tr>
  					</c:if> 			

					<c:if test="${sessionScope.isRetries != 'true'}"> 							  
						  <tr>
						  	<td NOWRAP align=center valign=top colspan=2> 
							  	<font size=3> <b>  <bean:message key="label.withoutRetries.results.summary"/> </b> </font>
						  	</td>
						  </tr>
					</c:if> 			


					  <tr>
					  	<td NOWRAP align=left valign=top colspan=2> 
						  	<font size=2>  <c:out value="${sessionScope.countSessionComplete}"/> <bean:message key="label.learnersFinished.simple"/> 
						  	</font>
					  	</td>
					  </tr>	

					<tr>
						<td NOWRAP align=right valign=top colspan=2> 
							<hr>
						</td> 
					</tr>


					 <tr>
					 <td> 
		 				<table align=center>
						  <tr>
						  	<td NOWRAP align=left valign=top colspan=2> 
							  	<font size=2> <b>  <bean:message key="label.topMark"/> </b> </font> 
							 </td> 
							 <td NOWRAP align=right>	
								 <font size=2>
								  	 <c:out value="${sessionScope.topMark}"/>
							  	 </font>
						  	</td>
						  </tr>	
	
						  <tr>
						  	<td NOWRAP align=left valign=top colspan=2> 
							  	<font size=2> <b>  <bean:message key="label.avMark"/> </b>  </font>
						  	</td>
						  	<td NOWRAP align=right>
							  	<font size=2>
								  	<c:out value="${sessionScope.averageMark}"/>
							  	</font>
						  	</td>
						  </tr>	
	
						  <tr>
						  	<td NOWRAP align=left valign=top colspan=2> 
							  	<font size=2> <b>  <bean:message key="label.loMark"/> </b> </font>
						  	</td>
						  	<td NOWRAP align=right>
							  	<font size=2>
								  	<c:out value="${sessionScope.lowestMark}"/>
							  	</font>
						  	</td>
						  </tr>	
						  
						 </table>
					</td>
					</tr>

			  	   	<tr> 
				 		<td NOWRAP colspan=2 valign=top> 
				 		&nbsp
				 		</td>
			  	   </tr>

			 		<c:if test="${sessionScope.isRetries == 'true'}"> 					  	   
		  	   		  <tr>
					  	<td NOWRAP colspan=2 align=center valign=top> 
						  	<font size=2>
					  			<html:submit property="redoQuestions" styleClass="button">
									<bean:message key="label.redo.questions"/>
								</html:submit>	 		
			       
						  	   <html:submit property="learnerFinished" styleClass="button">
									<bean:message key="label.finished"/>
							   </html:submit>
							</font>
					  	 </td>
					  </tr>
					</c:if> 																		

					<c:if test="${sessionScope.isRetries != 'true'}"> 							  
		  	   		  <tr>
		  	   		    <td NOWRAP colspan=2 align=right valign=top>
			  	   		    <font size=2>
						  	   <html:submit property="learnerFinished" styleClass="button">
											<bean:message key="label.finished"/>
							   </html:submit>
							 </font>
					  	 </td>
					  </tr>
					</c:if> 																		
				</table>
	</html:form>

</body>
</html:html>


