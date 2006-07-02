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

<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
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
	<c:out value="${sessionScope.activityTitle}" escapeXml="false" />
</h1>

<div id="header-no-tabs-learner"></div>

<div id="content-learner">
<html:form  action="/learning?validate=false" enctype="multipart/form-data"method="POST" target="_self">
	<html:hidden property="dispatch"/>
	<html:hidden property="toolContentID"/>

				<table  class="forms">
					  <tr>
					  	<td NOWRAP align=center  valign=top colspan=2> 
						  	 <b>  <bean:message key="label.progressiveResults"/> </b> 
					  	</td>
					  </tr>
					  
 					<c:if test="${VoteLearningForm.nominationsSubmited == 'true'}"> 			
						<tr> <td class="error" align=center>
								<img src="<c:out value="${tool}"/>images/success.gif" align="left" width=20 height=20>  
								 <bean:message key="sbmt.learner.nominations.successful"/>  </img>
						</td></tr>
					</c:if> 		
					  
					  <tr>
					  	<td NOWRAP align=center valign=top colspan=2> 
							<c:set var="viewURL">
								<html:rewrite page="/chartGenerator?type=pie"/>
							</c:set>
							<a href="javascript:launchInstructionsPopup('<c:out value='${viewURL}' escapeXml='false'/>')">
								 	<bean:message key="label.view.piechart"/>  
							</a>
								
					  	</td>
					  </tr>	
					  
					  
			  			<tr> 
						  	<td NOWRAP align=center  valign=top colspan=2> 
								<c:set var="viewURL">
									<html:rewrite page="/chartGenerator?type=bar"/>
								</c:set>
								<a href="javascript:launchInstructionsPopup('<c:out value='${viewURL}' escapeXml='false'/>')">
									 	<bean:message key="label.view.barchart"/>  
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
				  	<td NOWRAP valign=top> 
								<c:if test="${VoteLearningForm.voteChangable == 'true' && VoteLearningForm.lockOnFinish != 'true'}"> 				   						
			                                <html:submit property="redoQuestionsOk" 
			                                             styleClass="button" 
			                                             onclick="submitMethod('redoQuestionsOk');">
			                                    <bean:message key="label.retake"/>
			                                </html:submit>
								</c:if> 		          

								<c:if test="${VoteLearningForm.voteChangable == 'true' && VoteLearningForm.lockOnFinish == 'true' && VoteLearningForm.revisitingUser == 'false' }"> 				   						
				                                <html:submit property="redoQuestionsOk" 
				                                             styleClass="button" 
				                                             onclick="submitMethod('redoQuestionsOk');">
				                                    <bean:message key="label.retake"/>
				                                </html:submit>
								</c:if> 		          								                      
					<td>					
					<td NOWRAP valign=top> 			
						<div class="right-buttons">
                                <html:submit property="learnerFinished" 
                                             styleClass="button" 
                                             onclick="submitMethod('learnerFinished');">
                                    <bean:message key="label.finished"/>
                                </html:submit>
                         </div>
				  	 </td>
				  </tr>

	</html:form>
</div>

<div id="footer-learner"></div>

</div>
</body>
</html:html>

