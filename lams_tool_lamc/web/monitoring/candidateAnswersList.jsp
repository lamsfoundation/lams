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
  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA

  http://www.gnu.org/licenses/gpl.txt
--%>

<%@ include file="/common/taglibs.jsp" %>

<html:hidden property="candidateIndex"/>			
<html:hidden property="questionIndex"/>			



	<table class="innerforms">
		<td>
			<div class="field-name" style="text-align: left;">
				<fmt:message key="label.add.candidates"></fmt:message>
			</div>
		</td>
	</table>					

<%@ include file="/common/messages.jsp"%>


<table id="caTable" style="align:left;width:600px" >

    <c:set var="queIndex" scope="request" value="0"/>
    
    <c:forEach items="${listQuestionContentDTO}" var="currentDTO" varStatus="status">
	<c:set var="queIndex" scope="request" value="${queIndex +1}"/>
	
	<c:set var="caCount" scope="request" value="${currentDTO.caCount}"/>
	
	<c:if test="${currentEditableQuestionIndex == queIndex}"> 		
	
		<c:set var="listCandidateAnswersDTO" scope="request" value="${currentDTO.listCandidateAnswersDTO}"/>	
	
		    <c:set var="caIndex" scope="request" value="0"/>
		    
		   <c:forEach items="${listCandidateAnswersDTO}" var="currentCandidateDTO" varStatus="status">
		   	<c:set var="caIndex" scope="request" value="${caIndex +1}"/>
		   	
			<tr>
				<td width="4%" align="right">
					<c:out value="${caIndex}" /> 
				</td>		
			
				<td align="left">
					<input type="text" name="ca${caIndex}"  value="${currentCandidateDTO.candidateAnswer}" size="50">
				</td>		
				
				<td width="16%" align="left">				


					<select name="correct${caIndex}">
						<c:forEach var="correctEntry" items="${mcGeneralAuthoringDTO.correctMap}">
							<c:set var="SELECTED_ANSWER" scope="request" value=""/>
							<c:if test="${correctEntry.value == currentCandidateDTO.correct}"> 			
										<c:set var="SELECTED_ANSWER" scope="request" value="SELECTED"/>
							</c:if>						
	
							<option value="<c:out value="${correctEntry.value}"/>"  <c:out value="${SELECTED_ANSWER}"/>> <c:out value="${correctEntry.value}"/>  </option>																				
								
						</c:forEach>		  	
					</select>
				
				</td>
		
				<td width="22%" align="left">
			 		<c:if test="${caCount != 1}"> 		
			 		
				 		<c:if test="${caIndex == 1}"> 		
							<a title="<bean:message key='label.tip.moveCandidateDown'/>" href="javascript:;" onclick="javascript:submitModifyMonitoringCandidate('<c:out value="${queIndex}"/>', '<c:out value="${caIndex}"/>', 'moveCandidateDown');">
			                            <img src="<c:out value="${tool}"/>images/down.gif" border="0">
							</a> 
						</c:if> 							
		
										
				 		<c:if test="${caIndex == caCount}"> 		
							<a title="<bean:message key='label.tip.moveCandidateUp'/>" href="javascript:;" onclick="javascript:submitModifyMonitoringCandidate('<c:out value="${queIndex}"/>', '<c:out value="${caIndex}"/>', 'moveCandidateUp');">
			                            <img src="<c:out value="${tool}"/>images/up.gif" border="0">
							</a> 
						</c:if> 							
						
						<c:if test="${(caIndex != 1)  && (caIndex != caCount)}"> 		
							<a title="<bean:message key='label.tip.moveCandidateDown'/>" href="javascript:;" onclick="javascript:submitModifyMonitoringCandidate('<c:out value="${queIndex}"/>', '<c:out value="${caIndex}"/>', 'moveCandidateDown');">
			                            <img src="<c:out value="${tool}"/>images/down.gif" border="0">
							</a> 
		
							<a title="<bean:message key='label.tip.moveCandidateUp'/>" href="javascript:;" onclick="javascript:submitModifyMonitoringCandidate('<c:out value="${queIndex}"/>', '<c:out value="${caIndex}"/>', 'moveCandidateUp');">
			                            <img src="<c:out value="${tool}"/>images/up.gif" border="0">
							</a> 
						</c:if> 							
					
					</c:if> 			
				</td>
				
				<td width="10%" align="left">				
						<a title="<bean:message key='label.tip.removeCandidate'/>" href="javascript:;" onclick="removeCandidate(${queIndex}, ${caIndex});">
			                    <img src="<c:out value="${tool}"/>images/delete.gif" border="0">
						</a> 				
				</td>						
		
			</tr>
			</c:forEach>	
			
		</c:if> 					
</c:forEach>

</table>
