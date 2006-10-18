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

	<table class="innerforms">
		<td>
			<div class="field-name">
				<fmt:message key="label.add.candidates"></fmt:message>
			</div>
		</td>
	</table>					

<%@ include file="/common/messages.jsp"%>


<table id="caTable" style="align:left;width:600px" >

    
    <c:forEach items="${newAddableQuestionContentList}" var="currentDTO" varStatus="status">
	
	<c:set var="caCount" scope="request" value="${currentDTO.caCount}"/>
	
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
							<a title="<fmt:message key='label.tip.moveCandidateDown'/>" href="javascript:;" onclick="javascript:submitModifyAddedCandidate('<c:out value="${caIndex}"/>', 'moveAddedCandidateDown');">
			                            <img src="<c:out value="${tool}"/>images/down.gif" border="0">
							</a> 
						</c:if> 							
		
										
				 		<c:if test="${caIndex == caCount}"> 		
							<a title="<fmt:message key='label.tip.moveCandidateUp'/>" href="javascript:;" onclick="javascript:submitModifyAddedCandidate('<c:out value="${caIndex}"/>', 'moveAddedCandidateUp');">
			                            <img src="<c:out value="${tool}"/>images/up.gif" border="0">
							</a> 
						</c:if> 							
						
						<c:if test="${(caIndex != 1)  && (caIndex != caCount)}"> 		
							<a title="<fmt:message key='label.tip.moveCandidateDown'/>" href="javascript:;" onclick="javascript:submitModifyAddedCandidate('<c:out value="${caIndex}"/>', 'moveAddedCandidateDown');">
			                            <img src="<c:out value="${tool}"/>images/down.gif" border="0">
							</a> 
		
							<a title="<fmt:message key='label.tip.moveCandidateUp'/>" href="javascript:;" onclick="javascript:submitModifyAddedCandidate('<c:out value="${caIndex}"/>', 'moveAddedCandidateUp');">
			                            <img src="<c:out value="${tool}"/>images/up.gif" border="0">
							</a> 
						</c:if> 							
					
					</c:if> 			
				</td>
				
				<td width="10%" align="left">				
						<a title="<fmt:message key='label.tip.removeCandidate'/>" href="javascript:;" onclick="removeAddedCandidate(${caIndex});">
			                    <img src="<c:out value="${tool}"/>images/delete.gif" border="0">
						</a> 				
				</td>						
		
			</tr>
			</c:forEach>	

</c:forEach>

</table>