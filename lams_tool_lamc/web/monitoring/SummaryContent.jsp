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

	<h2><font size=2> <bean:message key="button.summary"/> </font> </h2>
	<div id="datatablecontainer">
		<c:if test="${sessionScope.userExceptionNoStudentActivity == 'true'}"> 	
				<table class="forms" align="center">
					<tr> 
						<td NOWRAP valign=top align=center> 
							<b> <font size=2> <bean:message key="error.noLearnerActivity"/> </font></b>
						</td> 
					<tr>
				</table>
		</c:if>						
		
		<c:if test="${sessionScope.userExceptionNoStudentActivity != 'true'}"> 	
			<html:hidden property="selectedToolSessionId"/>							
			<input type="hidden" name="isToolSessionChanged"/>
				<table class="forms">
					<tr> 
						<td NOWRAP class="formlabel" valign=top align=center><font size=2> <b> <bean:message key="label.selectGroup"/> </b>
								<select name="monitoredToolSessionId" onchange="javascript:submitSession(this.value,'submitSession');">
								<c:forEach var="toolSessionId" items="${sessionScope.summaryToolSessions}">
										<c:set var="SELECTED_SESSION" scope="request" value=""/>
										<c:if test="${sessionScope.selectionCase == 2}"> 			
											<c:set var="currentMonitoredToolSession" scope="session" value="All"/>
										</c:if>						
										
										<c:if test="${toolSessionId.value == sessionScope.currentMonitoredToolSession}"> 			
												<c:set var="SELECTED_SESSION" scope="request" value="SELECTED"/>
										</c:if>						
										
										<c:if test="${toolSessionId.value != 'All'}"> 		
											<option value="<c:out value="${toolSessionId.value}"/>"  <c:out value="${requestScope.SELECTED_SESSION}"/>> Group <c:out value="${toolSessionId.key}"/>  </option>						
										</c:if>						
										
										<c:if test="${toolSessionId.value == 'All'}"> 	
											<option value="<c:out value="${toolSessionId.value}"/>"  <c:out value="${requestScope.SELECTED_SESSION}"/>>  All  </option>						
										</c:if>						
								</c:forEach>		  	
								</select>
							</font>
						</td> 
					<tr>
				
					<c:forEach var="currentDto" items="${sessionScope.listMonitoredAnswersContainerDto}">
			  	 		<c:set var="currentQuestionId" scope="request" value="${currentDto.questionUid}"/>
			  	 		<tr>
			  	 			<td> &nbsp&nbsp&nbsp</td>
			  	 		</tr>
						<tr>			
							<td NOWRAP valign=top align=left><b> <font size=2> <bean:message key="label.question.col"/> </b>
								<c:out value="${currentDto.question}"/>
							</font> </td>
						</tr>	
						<tr>					
							<td NOWRAP valign=top align=left> <font size=2> <b> <bean:message key="label.mc.options.col"/> </font> </b> 
								<table align=left>
									<c:forEach var="answersData" items="${currentDto.candidateAnswers}">
										<tr>			
											<td NOWRAP valign=top align=left>
												<font size=2>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
												<c:out value="${answersData}"/> </font>
											</td>	
										</tr>
									</c:forEach>		  	
								</table>
							</td>  
						</tr>			
						
						<tr> 
							<td NOWRAP class="formlabel" valign=top>
								<table align=center>
									<tr> 
										 <td NOWRAP valign=top> <b> <font size=2> <bean:message key="label.user"/> </font> 			</b> </td>  
				  						 <td NOWRAP valign=top> <b> <font size=2> <bean:message key="label.attemptTime"/> </font> 	</b></td>
				  						 <td NOWRAP valign=top> <b> <font size=2> <bean:message key="label.timezone"/> </font>		</b></td>
				  						 <td NOWRAP valign=top> <b> <font size=2> <bean:message key="label.response"/> </font> 		</b></td>
						  			</tr>				 
	
			  							<c:forEach var="questionAttemptData" items="${currentDto.questionAttempts}">
											<c:forEach var="sData" items="${questionAttemptData.value}">
									  	 		<c:set var="userData" scope="request" value="${sData.value}"/>
		  	 									<c:if test="${currentQuestionId == userData.questionUid}"> 			
			  	 									<c:if test="${sessionScope.currentMonitoredToolSession == 'All'}"> 			
														<tr> 
															 <td NOWRAP valign=top>  <font size=2> <c:out value="${userData.userName}"/> </font>  </td>  
									  						 <td NOWRAP valign=top>  <font size=2> <c:out value="${userData.attemptTime}"/> </font> </td>
									  						 <td NOWRAP valign=top>  <font size=2> <c:out value="${userData.timeZone}"/> </font> </td>
									  						 <td NOWRAP valign=top>  <font size=2> <c:out value="${userData.response}"/> </font> </td>
											  			</tr>		
													</c:if>														  					 									  			
													
			  	 									<c:if test="${sessionScope.currentMonitoredToolSession != 'All'}"> 			
			  	 										<c:if test="${sessionScope.currentMonitoredToolSession == userData.sessionId}"> 			
															<tr>  
																 <td NOWRAP valign=top>  <font size=2> <c:out value="${userData.userName}"/> </font>  </td>  
										  						 <td NOWRAP valign=top>  <font size=2> <c:out value="${userData.attemptTime}"/> </font> </td>
										  						 <td NOWRAP valign=top>  <font size=2> <c:out value="${userData.timeZone}"/> </font> </td>
										  						 <td NOWRAP valign=top>  <font size=2> <c:out value="${userData.response}"/> </font> </td>
												  			</tr>														
														</c:if>														  					 									  													  			
													</c:if>														  					 									  													  			
												</c:if>														  					 
		 									</c:forEach>		  	
										</c:forEach>		  	
								</table>
							</td>  
			  			</tr>
						
					</c:forEach>		  	
				</table>
		</c:if>						
	</div>

	