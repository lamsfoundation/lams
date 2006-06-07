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
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-bean" prefix="bean"%> 
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="fck-editor" prefix="FCK" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="tool"><lams:WebAppURL/></c:set>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
<head>
	<jsp:include page="/learning/learningHeader.jsp" />
	
	<script language="JavaScript" type="text/JavaScript">
		function submitLearningMethod(actionMethod) 
		{
			document.QaLearningForm.method.value=actionMethod; 
			document.QaLearningForm.submit();
		}
		
		function submitMethod(actionMethod) 
		{
			submitLearningMethod(actionMethod);
		}
	</script>	
</head>
<body>
	  <html:form  action="/learning?validate=false" enctype="multipart/form-data" method="POST" target="_self">		
  		<html:hidden property="method"/>	 

				<table width="80%" cellspacing="8" align="CENTER" class="forms">
				  <tr><th scope="col">
					  	 <bean:message key="label.learning.reportMessage"/> 
		    	  	</th>
				  </tr>

					<tr>
						<td NOWRAP align=right class="input" valign=top colspan=2> 
							<hr>
						</td> 
					</tr>

			
					<tr> <td align=left>		
						<table align="left">
							<c:forEach var="questionEntry" items="${sessionScope.mapQuestionContentLearner}">
									  <tr>
									  	<td colspan=2 NOWRAP valign=top> <font size=2> <b> <bean:message key="label.question"/> <c:out value="${questionEntry.key}" escapeXml="false"/>:  </b>  </font> 
									  		<c:out value="${questionEntry.value}" escapeXml="false"/> 
									  	 </td>
									  </tr>
									  
				  			  		<c:forEach var="answerEntry" items="${sessionScope.mapAnswers}">
										<c:if test="${answerEntry.key == questionEntry.key}"> 						  			  		
										  <tr>
  										  	<td colspan=2 NOWRAP align=left  valign=top> <font size=2> <b> <bean:message key="label.learning.yourAnswer"/>  </b>  </font> 
												  <c:out value="${answerEntry.value}" escapeXml="false" />						  																	
										  	</td>
										  </tr>
									  	</c:if> 				    
									</c:forEach>
									
									  <tr>
									  	<td colspan=2 NOWRAP valign=top> 
											&nbsp&nbsp
									  	 </td>
									  </tr>
									
							</c:forEach>
						</table>
					</td></tr>
					
			  	   	<tr> 
				 		<td NOWRAP colspan=2 class="input" valign=top> 
				 		&nbsp
				 		</td>
			  	   </tr>

			  	   
		  	   		  <tr>
					  	<td NOWRAP colspan=2 align=center class="input" valign=top> 
						  	<font size=2>

                                <html:submit property="viewAllResults" 
                                             styleClass="linkbutton" 
                                             onclick="submitMethod('viewAllResults');">
                                    <bean:message key="label.allResponses"/>
                                </html:submit>
							</font>
					  	 </td>
					  </tr>
					
				</table>
	</html:form>
	
</body>
</html:html>

	
					