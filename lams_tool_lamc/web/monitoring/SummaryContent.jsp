<%@ page language="java" import="java.lang.*,java.util.*" %>
<%@ taglib uri="tags-html-el" prefix="html" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-logic-el" prefix="logic-el" %>
<%@ taglib uri="tags-c" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-fck-editor" prefix="FCK" %>
<%@ taglib uri="tags-lams" prefix="lams" %>


		<table class="forms" align=left>
		<tr> 
			<td NOWRAP class="formlabel" valign=top align=right> 
					<select name="toolSessionId1" onchange="selectToolSession();">
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
			</td> 
		<tr>

		<input type="hidden" name="isToolSessionChanged"/>
		<tr> <td> &nbsp&nbsp&nbsp </td> </tr>
		<tr> <td> &nbsp&nbsp&nbsp </td> </tr>
		
		<tr> <td NOWRAP class="formlabel" valign=top align=left> 
			<table align=left>
			
				<c:forEach var="currentDto" items="${sessionScope.listMonitoredAnswersContainerDto}">
		  	 		<c:set var="currentQuestionId" scope="request" value="${currentDto.questionUid}"/>
					<tr>			
						<td NOWRAP valign=top align=left><b> <font size=2> <bean:message key="label.question.col"/> </b></font> </td>
						<td NOWRAP valign=top align=left> <font size=2> <c:out value="${currentDto.question}"/> </font> </td>
					</tr>	
					<tr>					
						<td NOWRAP valign=top align=left> <font size=2> <b> <bean:message key="label.mc.options.col"/> </b> </font></td>  
						<td NOWRAP valign=top align=left>
							<table align=left>
								<c:forEach var="answersData" items="${currentDto.candidateAnswers}">
									<tr>			
										<td NOWRAP valign=top align=left>
											<font size=2> <c:out value="${answersData}"/> </font>
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
												<tr> 
													 <td NOWRAP valign=top>  <font size=2> <c:out value="${userData.userName}"/> </font>  </td>  
							  						 <td NOWRAP valign=top>  <font size=2> <c:out value="${userData.attemptTime}"/> </font> </td>
							  						 <td NOWRAP valign=top>  <font size=2> <c:out value="${userData.timeZone}"/> </font> </td>
							  						 <td NOWRAP valign=top>  <font size=2> <c:out value="${userData.response}"/> </font> </td>
									  			</tr>		
										</c:if>														  					 
	 									</c:forEach>		  	
									</c:forEach>		  	
							</table>
						</td>  
		  			</tr>
					
				</c:forEach>		  	
			</table>
		</td> </tr>
	</table>

	
<SCRIPT language="JavaScript"> 
	function selectToolSession()
	{
		document.forms[0].isToolSessionChanged.value='1';
		document.forms[0].submit();
	}
</SCRIPT>	
	