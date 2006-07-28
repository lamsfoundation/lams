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

<%@ include file="/common/taglibs.jsp" %>

	<tr> 
		<c:if test="${editResponse != 'true'}">	  	 									 			
			 <td NOWRAP valign=top>   <c:out value="${userData.userName}"/>   </td>  
			 <td NOWRAP valign=top>   <c:out value="${userData.attemptTime}"/> </td>
			 <td NOWRAP valign=top>   <c:out value="${userData.response}"/> 

			<c:if test="${userData.visible != 'true' }"> 			
                         <i><bean:message key="label.response.hidden"/> </i> 
			</c:if> 								
			 </td>
	
			
				 <td NOWRAP valign=top> <img src="<c:out value="${tool}"/>images/edit.gif" align=left onclick="javascript:submitEditResponse('<c:out value="${userData.uid}"/>','editResponse');">	</td>	  	 
				 <td NOWRAP valign=top> 				 
				<c:if test="${userData.visible == 'true' }"> 			
                            <html:submit property="hideResponse" 
                                         styleClass="linkbutton" 
                                         onclick="submitResponse(${responseUid}, 'hideResponse');">						                                             
                                <bean:message key="label.hide"/>
                            </html:submit>
				</c:if> 													

				<c:if test="${userData.visible != 'true' }"> 			
                            <html:submit property="showResponse" 
                                         styleClass="linkbutton" 
                                         onclick="submitResponse(${responseUid}, 'showResponse');">						                                             
                                <bean:message key="label.show"/>
                            </html:submit>
				</c:if> 						
			 	</td>	  	 
		</c:if>
		
		<c:if test="${editResponse == 'true'}">	  	
			<c:if test="${editableResponseId == responseUid}">	  	 									 			
				 <td NOWRAP valign=top>  <c:out value="${userData.userName}"/>   </td>  
				 <td NOWRAP valign=top>  <c:out value="${userData.attemptTime}"/>  </td>
				 <td NOWRAP valign=top>  <input type="text" name="updatedResponse" value='<c:out value="${userData.response}"/>'> 

				<c:if test="${userData.visible != 'true' }"> 			
	                         <i><bean:message key="label.response.hidden"/> </i> 
				</c:if> 								
				 </td>
		
	
					 <td NOWRAP valign=top> <img src="<c:out value="${tool}"/>images/tick.gif" align=left onclick="javascript:submitEditResponse('<c:out value="${userData.uid}"/>','updateResponse');">	</td>	  	 
					 <td NOWRAP valign=top> 
						<c:if test="${userData.visible == 'true' }"> 			
		                            <html:submit property="hideResponse" 
		                                         styleClass="linkbutton" 
		                                         onclick="submitResponse(${responseUid}, 'hideResponse');">						                                             
		                                <bean:message key="label.hide"/>
		                            </html:submit>
						</c:if> 													
		
						<c:if test="${userData.visible != 'true' }"> 			
		                            <html:submit property="showResponse" 
		                                         styleClass="linkbutton" 
		                                         onclick="submitResponse(${responseUid}, 'showResponse');">						                                             
		                                <bean:message key="label.show"/>
		                            </html:submit>
						</c:if> 						
				 	</td>	  	 
				</c:if>
		
		<c:if test="${editableResponseId != responseUid}">	  	 									 			
				 <td NOWRAP valign=top>   <c:out value="${userData.userName}"/>   </td>  
				 <td NOWRAP valign=top>   <c:out value="${userData.attemptTime}"/>  </td>
				 <td NOWRAP valign=top>   <c:out value="${userData.response}"/> 
					<c:if test="${userData.visible != 'true' }"> 			
		                         <i><bean:message key="label.response.hidden"/> </i> 
					</c:if> 								
				 </td>
		
		
					 <td NOWRAP valign=top> <img src="<c:out value="${tool}"/>images/edit.gif" align=left onclick="javascript:submitEditResponse('<c:out value="${userData.uid}"/>','editResponse');">	</td>	  	 
					 <td NOWRAP valign=top> 
						<c:if test="${userData.visible == 'true' }"> 			
		                            <html:submit property="hideResponse" 
		                                         styleClass="linkbutton" 
		                                         onclick="submitResponse(${responseUid}, 'hideResponse');">						                                             
		                                <bean:message key="label.hide"/>
		                            </html:submit>
						</c:if> 													
		
						<c:if test="${userData.visible != 'true' }"> 			
		                            <html:submit property="showResponse" 
		                                         styleClass="linkbutton" 
		                                         onclick="submitResponse(${responseUid}, 'showResponse');">						                                             
		                                <bean:message key="label.show"/>
		                            </html:submit>
						</c:if> 						
					 	</td>	  	 
			</c:if>														  					 									  													  			
		</c:if>														  					 									  													  			
	</tr>		


