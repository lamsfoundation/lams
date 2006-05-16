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
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="fck-editor" prefix="FCK" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="tool"><lams:WebAppURL/></c:set>

	<html:hidden property="responseId"/>	 
	
		<c:if test="${(userExceptionNoToolSessions == 'true')}"> 	
				<table class="forms" align="center">
					<tr> 
						<td NOWRAP valign=top align=center> 
							<b> <font size=2> <bean:message key="error.noLearnerActivity"/> </font></b>
						</td> 
					<tr>
				</table>
		</c:if>			

		<c:if test="${(userExceptionNoToolSessions != 'true') }"> 	
		
			<html:hidden property="selectedToolSessionId"/>							
			<input type="hidden" name="isToolSessionChanged"/>
				<table>

					<c:if test="${(requestLearningReport != 'true')}"> 	
						<c:if test="${(isPortfolioExport != 'true') }"> 	
							<c:if test="${sessionScope.selectionCase != 2}"> 			
								<tr> 
									<td NOWRAP valign=top align=left>
										<c:set var="viewURL">
											<html:rewrite page="/chartGenerator?type=pie"/>
										</c:set>
										<a href="javascript:launchInstructionsPopup('<c:out value='${viewURL}' escapeXml='false'/>')">
											 <font size=2>	<bean:message key="label.view.chart"/>  </font>
										</a>
									</td>
								</tr>
							</c:if>			
								<tr> 
									<td NOWRAP class="formlabel" valign=top>
									&nbsp&nbsp&nbsp
									</td>
								</tr>
							
			
								<tr> 
									<td NOWRAP  valign=top align=right><font size=2> <b> <bean:message key="label.selectGroup"/> </b>
											<select name="monitoredToolSessionId" onchange="javascript:submitSession(this.value,'submitSession');">
											<c:forEach var="toolSessionName" items="${sessionScope.summaryToolSessions}">
												<c:forEach var="toolSessionId" items="${sessionScope.summaryToolSessionsId}">
													<c:out value="${toolSessionName.key}"/> -<c:out value="${toolSessionId.value}"/>
												
														<c:if test="${toolSessionName.key == toolSessionId.key}"> 			
													
															<c:set var="SELECTED_SESSION" scope="request" value=""/>
															<c:if test="${sessionScope.selectionCase == 2}"> 			
																<c:set var="currentMonitoredToolSession" scope="session" value="All"/>
															</c:if>						
															
															<c:if test="${toolSessionId.value == sessionScope.currentMonitoredToolSession}"> 			
																	<c:set var="SELECTED_SESSION" scope="request" value="SELECTED"/>
															</c:if>						
															
															<c:if test="${toolSessionId.value != 'All'}"> 		
																<option value="<c:out value="${toolSessionId.value}"/>"  <c:out value="${requestScope.SELECTED_SESSION}"/>> <c:out value="${toolSessionName.value}"/>  </option>						
															</c:if>						
															
															<c:if test="${toolSessionId.value == 'All'}"> 	
																<option value="<c:out value="${toolSessionId.value}"/>"  <c:out value="${requestScope.SELECTED_SESSION}"/>>  All  </option>						
															</c:if>						
													</c:if>							
												</c:forEach>		  	
											</c:forEach>		  	
											</select>
										</font>
									</td> 
								<tr>
	
						</c:if>								
					</c:if>			
					
					
	  				<c:if test="${currentMonitoredToolSession == 'All'}"> 				
						<tr>
					 		<td NOWRAP colspan=2> <b> <font size=2> <bean:message key="label.select.session"/> </b> </td>
						</tr>
					</c:if> 	    

	  				<c:if test="${currentMonitoredToolSession != 'All'}"> 				
	  					<tr>
					 		<td NOWRAP> <b> <font size=2> <bean:message key="label.total.students"/> </b> </td>
					 		<td> 1 </td>
						</tr>

	  					<tr>
					 		<td NOWRAP> <b> <font size=2> <bean:message key="label.total.completed.students"/> </b> </td> 
					 		<td> 2 </td>
						</tr>
	  				
						<tr>
					 		<td NOWRAP colspan=2> 
		                            <c:out value="${activityInstructions}" escapeXml="false"/>
							</td>
						</tr>
						

						<tr>
					 		<td NOWRAP> &nbsp&nbsp </td>
						</tr>
						
	
						<tr>
					 		<td NOWRAP> <b> <font size=2> <bean:message key="label.nomination"/> </b> </td>
							<td NOWRAP> <b> <font size=2> <bean:message key="label.total.votes"/> </b> </td>
						</tr>
						
						<c:forEach var="currentNomination" items="${mapStandardNominationsContent}">
				  	 		<c:set var="currentNominationKey" scope="request" value="${currentNomination.key}"/>
				  	 		
				  	 		<c:forEach var="currentRate" items="${mapStandardRatesContent}">
					  	 		<c:set var="currentRateKey" scope="request" value="${currentRate.key}"/>
		
				  				<c:if test="${currentNominationKey == currentRateKey}"> 				
									<tr>			
										<td NOWRAP valign=top align=left>
											<c:out value="${currentNomination.value}" escapeXml="false"/>
										</font> </td>
										
										<td NOWRAP valign=top align=left>
											<c:out value="${currentRate.value}"/>
										</font> </td>
									</tr>	
								</c:if> 	    
								
							</c:forEach>		  
						</c:forEach>	
					
					</c:if> 	    	  

				</table>
		</c:if>						


	