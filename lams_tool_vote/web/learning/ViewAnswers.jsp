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
<html:html locale="true">
<head>
	<jsp:include page="/learning/learningHeader.jsp" />
</head>
<body>

<html:form  action="/learning?validate=false" enctype="multipart/form-data"method="POST" target="_self">
	<html:hidden property="dispatch"/>
	<html:hidden property="toolContentID"/>

				<table align=center bgcolor="#FFFFFF">
					  <tr>
					  	<td NOWRAP align=center class="input" valign=top bgColor="#333366" colspan=2> 
						  	<font size=2 color="#FFFFFF"> <b>  <bean:message key="label.viewNominations"/> </b> </font>
					  	</td>
					  </tr>
				
					  <tr>
					  	<td NOWRAP align=right class="input" valign=top colspan=2> 
							&nbsp
					  	</td>
					  </tr>	

			  		<c:forEach var="entry" items="${sessionScope.mapGeneralCheckedOptionsContent}">
						  <tr>
						  	<td NOWRAP align=left class="input" valign=top  colspan=2> 
									<c:out value="${entry.value}"/> 
						  	</td>
						  </tr>
					</c:forEach>


				  <tr>
				  	<td NOWRAP colspan=2 align=center class="input" valign=top> 
					  	<font size=2>
					  	
								<c:if test="${VoteLearningForm.voteChangable == 'true' && VoteLearningForm.lockOnFinish != 'true'}"> 				   						
			                                <html:submit property="redoQuestionsOk" 
			                                             styleClass="linkbutton" 
			                                             onclick="submitMethod('redoQuestionsOk');">
			                                    <bean:message key="label.retake"/>
			                                </html:submit>
								</c:if> 		          
								<c:if test="${VoteLearningForm.voteChangable == 'true' && VoteLearningForm.lockOnFinish == 'true' && VoteLearningForm.revisitingUser == 'false' }"> 				   						
				                                <html:submit property="redoQuestionsOk" 
				                                             styleClass="linkbutton" 
				                                             onclick="submitMethod('redoQuestionsOk');">
				                                    <bean:message key="label.retake"/>
				                                </html:submit>
									</c:if> 		          								                      
	   						&nbsp&nbsp&nbsp&nbsp&nbsp                                
                                <html:submit property="learnerFinished" 
                                             styleClass="linkbutton" 
                                             onclick="submitMethod('learnerFinished');">
                                    <bean:message key="label.finished"/>
                                </html:submit>
						</font>
				  	 </td>
				  </tr>
</html:form>

</body>
</html:html>



