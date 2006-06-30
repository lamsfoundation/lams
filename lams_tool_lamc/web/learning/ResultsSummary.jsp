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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

<html:html locale="true">
<head>
	<title> <bean:message key="label.learning"/> </title>
	<%@ include file="/common/header.jsp"%>
	<%@ include file="/common/fckeditorheader.jsp"%>
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
							  	<b>  <bean:message key="label.withRetries.results.summary"/> </b> 
						  	</td>
						  </tr>
  					</c:if> 			

					<c:if test="${sessionScope.isRetries != 'true'}"> 							  
					  <tr>
					  	<td NOWRAP align=center valign=top colspan=2> 
						  	<b>  <bean:message key="label.withoutRetries.results.summary"/> </b> 
					  	</td>
					  </tr>
				</c:if> 			


				  <tr>
				  	<td NOWRAP align=left valign=top colspan=2> 
					  	  <c:out value="${sessionScope.countSessionComplete}"/> <bean:message key="label.learnersFinished.simple"/> 
				  	</td>
				  </tr>	

				<tr>
					<td NOWRAP align=right valign=top colspan=2> 
						<hr>
					</td> 
				</tr>


				 <tr>
				 <td colspan=2> 
	 				<table align=left>
					  <tr>
					  	<td NOWRAP align=left valign=top> 
						  	<b>  <bean:message key="label.topMark"/> </b> 
						 </td> 
						 <td NOWRAP align=left>	
							  	 <c:out value="${sessionScope.topMark}"/>
					  	</td>
					  </tr>	

					  <tr>
					  	<td NOWRAP align=left valign=top> 
						  	 <b>  <bean:message key="label.avMark"/> </b>  
					  	</td>
					  	<td NOWRAP align=left>
							  	<c:out value="${sessionScope.averageMark}"/>
					  	</td>
					  </tr>	

					  <tr>
					  	<td NOWRAP align=left valign=top> 
						  	 <b>  <bean:message key="label.loMark"/> </b> 
					  	</td>
					  	<td NOWRAP align=left>
							  	<c:out value="${sessionScope.lowestMark}"/>
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
				  	<td NOWRAP colspan=2 align=left valign=top> 
				  			<html:submit property="redoQuestions" styleClass="button">
								<bean:message key="label.redo.questions"/>
							</html:submit>	 		
		       
							<c:if test="${((McLearningForm.passMarkApplicable == 'true') && (McLearningForm.userOverPassMark == 'true'))}">
						  	   <html:submit property="learnerFinished" styleClass="button">
									<bean:message key="label.finished"/>
							   </html:submit>
					  	   </c:if>
				  	 </td>
				  </tr>
				</c:if> 																		

				<c:if test="${sessionScope.isRetries != 'true'}"> 							  
	  	   		  <tr>
	  	   		    <td NOWRAP colspan=2 align=left valign=top>
					  	   <html:submit property="learnerFinished" styleClass="button">
										<bean:message key="label.finished"/>
						   </html:submit>
				  	 </td>
				  </tr>
				</c:if> 																		
			</table>
</html:form>

</body>
</html:html>


