<h1>
	<img src="<lams:LAMSURL/>/images/tree_closed.gif" id="treeIcon3" onclick="javascript:toggleAdvancedOptionsVisibility(document.getElementById('advanced-questions'), document.getElementById('treeIcon3'), '<lams:LAMSURL/>');" />

	<a href="javascript:toggleAdvancedOptionsVisibility(document.getElementById('advanced-questions'), document.getElementById('treeIcon3'),'<lams:LAMSURL/>');" >
		<fmt:message key="label.Questions" />
	</a>
</h1>

<div class="monitoring-advanced" id="advanced-questions" style="display:none">

		<c:if test="${mcGeneralMonitoringDTO.userExceptionNoToolSessions != 'true'}"> 	
				
			<table class="forms">
					
				<c:forEach var="question" items="${content.mcQueContents}" varStatus="i">

						<tr>			
							<td NOWRAP valign=top class="align-left"><b>  <fmt:message key="label.question.only"/> ${i.index + 1}:</b>
								<c:out value="${question.question}" escapeXml="false"/> &nbsp (<fmt:message key="label.mark"/> <c:out value="${question.mark}"/> )
							 </td>
						</tr>	
						<tr>					
							<td NOWRAP valign=top class="align-left">  <b> <fmt:message key="label.mc.options.col"/>  </b> 
								<table class="align-left">
									<c:forEach var="option" items="${question.mcOptionsContents}">
										<tr>			
											<td NOWRAP valign=top class="align-left">
												&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
												<c:out value="${option.mcQueOptionText}" escapeXml="false"/> 
												
												<c:if test="${option.correctOption}"> 		
													&nbsp (<fmt:message key="label.correct"/>)
												</c:if>																		
											</td>	
										</tr>
									</c:forEach>		  	
								</table>
							</td>  
						</tr>			

				</c:forEach>		  	

		  	 	<tr>
		  	 		<td NOWRAP valign=top class="align-left"> <b> 
		  	 			<fmt:message key="label.passingMark"/>: </b> <c:out value="${passMark}"/> 
		  	 		</td>
		  	 	</tr>
			</table>
		</c:if>

</div>
		