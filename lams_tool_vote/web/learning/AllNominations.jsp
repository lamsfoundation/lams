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
  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
  USA

  http://www.gnu.org/licenses/gpl.txt
--%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

<c:set scope="request" var="lams">
	<lams:LAMSURL />
</c:set>
<c:set scope="request" var="tool">
	<lams:WebAppURL />
</c:set>

<html:html>
<head>
	<html:base />
	<lams:headItems />
	<title><bean:message key="activity.title" /></title>
	
	<script language="JavaScript" type="text/JavaScript">
		function submitMethod(actionMethod) 
		{
			document.VoteLearningForm.dispatch.value=actionMethod; 
			document.VoteLearningForm.submit();
		}
	</script>
</head>

<body>
	<div id="page-learner">

<h1 class="no-tabs-below">
	<c:out value="${voteGeneralLearnerFlowDTO.activityTitle}" escapeXml="false" />
</h1>

<div id="header-no-tabs-learner"></div>

<div id="content-learner">

<html:form  action="/learning?validate=false" enctype="multipart/form-data" method="POST" target="_self">
	<html:hidden property="dispatch"/>
	<html:hidden property="toolSessionID"/>
	<html:hidden property="userID"/>	
	<html:hidden property="revisitingUser"/>	
	<html:hidden property="previewOnly"/>	
	<html:hidden property="maxNominationCount"/>	
	<html:hidden property="allowTextEntry"/>	
	<html:hidden property="voteChangable"/>	
	<html:hidden property="lockOnFinish"/>	
	<html:hidden property="reportViewOnly"/>		
	<html:hidden property="userEntry"/>		
	
		<table cellpadding="0">
		
			  <tr>
			  	<td NOWRAP valign=top align=center colspan=2> 
					<b> <bean:message key="label.progressiveResults"/>  </b>
			  	</td>
			  </tr>	

				  		<!--present  a mini summary table here -->
			  			<tr> 
						  	<td NOWRAP align=center  valign=top> 
							  	<table cellpadding="0" class="alternative-color">
						  				<tr>
									 		<td NOWRAP> <b>  <bean:message key="label.nomination"/> </b> </td>
											<td NOWRAP> <b>  <bean:message key="label.total.votes"/> </b> </td>
										</tr>
										
										<c:forEach var="currentNomination" items="${voteGeneralLearnerFlowDTO.mapStandardNominationsHTMLedContent}">
								  	 		<c:set var="currentNominationKey" scope="request" value="${currentNomination.key}"/>
								  	 		 <tr>
					  	 						<td NOWRAP valign=top align=left>
													<c:out value="${currentNomination.value}" escapeXml="false"/>
												 </td>
				
												<td NOWRAP valign=top align=left>				  	 		
										  	 		<c:forEach var="currentUserCount" items="${voteGeneralLearnerFlowDTO.mapStandardUserCount}">
											  	 		<c:set var="currentUserKey" scope="request" value="${currentUserCount.key}"/>
										  				<c:if test="${currentNominationKey == currentUserKey}"> 				
										  				
										  					<c:if test="${currentUserCount.value != '0' }"> 	
													  	 		<c:forEach var="currentQuestionUid" items="${voteGeneralLearnerFlowDTO.mapStandardQuestionUid}">
														  	 		<c:set var="currentQuestionUidKey" scope="request" value="${currentQuestionUid.key}"/>
													  				<c:if test="${currentQuestionUidKey == currentUserKey}"> 				
	
															  	 		<c:forEach var="currentSessionUid" items="${voteGeneralLearnerFlowDTO.mapStandardToolSessionUid}">
																  	 		<c:set var="currentSessionUidKey" scope="request" value="${currentSessionUid.key}"/>
															  				<c:if test="${currentSessionUidKey == currentQuestionUidKey}"> 				
			
																	  				<c:if test="${currentNomination.value != 'Open Vote'}"> 				
																						<c:set scope="request" var="viewURL">
																							<lams:WebAppURL/>monitoring.do?method=getVoteNomination&questionUid=${currentQuestionUid.value}&sessionUid=${currentSessionUid.value}
																						</c:set>
	
																						 <c:out value="${currentUserCount.value}"/>  
	
																					</c:if> 	    
																	  				<c:if test="${currentNomination.value == 'Open Vote'}"> 				
																							 <c:out value="${currentUserCount.value}"/>  
																					</c:if> 	    
																			</c:if> 	    
																		</c:forEach>		  
	
																	</c:if> 	    
																</c:forEach>		  
															</c:if> 	    								
											  				<c:if test="${currentUserCount.value == '0' }"> 		  				
																	<c:out value="${currentUserCount.value}"/>  
															</c:if> 	
															    																								
														</c:if> 	    
													</c:forEach>		  
				
										  	 		<c:forEach var="currentRate" items="${voteGeneralLearnerFlowDTO.mapStandardRatesContent}">
											  	 		<c:set var="currentRateKey" scope="request" value="${currentRate.key}"/>
										  				<c:if test="${currentNominationKey == currentRateKey}"> 				
																	 &nbsp(<c:out value="${currentRate.value}"/> <bean:message key="label.percent"/>) 
														</c:if> 	    
													</c:forEach>		  
												</td>						
											</tr>	
										</c:forEach>	
						  
						  			</table>
								</td> 

							  	<td NOWRAP align=center valign=top> 
									<c:set scope="request" var="viewURL">
										<html:rewrite page="/chartGenerator?type=pie"/>
									</c:set>
									<a title="<bean:message key='label.tip.displayPieChart'/>" href="javascript:;" onclick="javascript:launchInstructionsPopup('<c:out value='${viewURL}' escapeXml='false'/>')">
			                                    <img src="<c:out value="${tool}"/>images/piechart.gif" width=30 height=30 border="0">
									</a> 
									&nbsp
									<c:set scope="request" var="viewURL">
										<html:rewrite page="/chartGenerator?type=bar"/>
									</c:set>
		
									<a title="<bean:message key='label.tip.displayBarChart'/>" href="javascript:;" onclick="javascript:launchInstructionsPopup('<c:out value='${viewURL}' escapeXml='false'/>')">
			                                    <img src="<c:out value="${tool}"/>images/columnchart.gif" width=30 height=30 border="0">
									</a> 
								</td>

							</tr>					  

					  <tr>
					  	<td NOWRAP valign=top colspan=2> 
								&nbsp
					  	</td>
					  </tr>	

					  <tr>
					  	<td NOWRAP align=center  valign=top colspan=2> 
						  	 <b>  <bean:message key="label.learner.nominations"/> </b> 
					  	</td>
					  </tr>

			  		<c:forEach var="entry" items="${requestScope.listGeneralCheckedOptionsContent}">
						  <tr>
						  	<td NOWRAP align=center valign=top  colspan=2> 
								  <c:out value="${entry}" escapeXml="false" />									
						  	</td>
						  </tr>
					</c:forEach>

					<tr> 
						<td NOWRAP align=center  valign=top colspan=2> 
					 	  		<c:out value="${VoteLearningForm.userEntry}"/> 						 			
				 		</td>
				  	</tr>




						<tr> 
						<td NOWRAP align=center  valign=top colspan=2> 
								<table>
									<tr> 
					  	   		  		<td>
											<b> <bean:message key="label.notebook.entries"/> </b>						
										 </td>
									</tr>
									
									<tr> 
					  	   		  		<td>
											<c:out value="${voteGeneralLearnerFlowDTO.notebookEntry}" escapeXml="false"/>				  	   		  		
										 </td>
									</tr>
							</table>
	
						</td>
						</tr>




				<c:if test="${voteGeneralLearnerFlowDTO.reportViewOnly != 'true' }"> 			
					  <tr>
					  	<td NOWRAP valign=top> 
		                                <html:submit property="refreshVotes" 
		                                             styleClass="button" 
		                                             onclick="submitMethod('viewAllResults');">
		                                    <bean:message key="label.refresh"/>
		                                </html:submit>
		                                
							<c:if test="${VoteLearningForm.voteChangable == 'true'}"> 				   										  
										&nbsp&nbsp	
		                                <html:submit property="redoQuestionsOk" 
		                                             styleClass="button" 
		                                             onclick="submitMethod('redoQuestionsOk');">
		                                    <bean:message key="label.retake"/>
		                                </html:submit>
							</c:if> 		          					
		                                
		                                
						</td>					
						<td NOWRAP valign=top align=right> 			
	                                
									<c:if test="${voteGeneralLearnerFlowDTO.reflection != 'true'}"> 						  			  		
										<html:submit property="learnerFinished"  onclick="javascript:submitMethod('learnerFinished');" styleClass="button">
											<bean:message key="label.finished"/>
										</html:submit>	 				
								  	</c:if> 				    					
					
									<c:if test="${voteGeneralLearnerFlowDTO.reflection == 'true'}"> 						  			  		
										<html:submit property="forwardtoReflection" onclick="javascript:submitMethod('forwardtoReflection');" styleClass="button">
											<bean:message key="label.continue"/>
										</html:submit>	 				
								  	</c:if> 				    					

					  	 </td>
					  </tr>
				</c:if> 							  
		
		</table>
	
</html:form>

</div>

<div id="footer-learner"></div>

</div>
</body>
</html:html>







