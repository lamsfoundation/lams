<%@ include file="/common/taglibs.jsp"%>

			<html:hidden property="questionIndex"/>
			<table cellpadding="0">

						<tr>
							<td colspan="2">
								<div class="field-name">
									<fmt:message key="label.authoring.title.col"></fmt:message>
								</div>
						  			<c:out value="${mcGeneralAuthoringDTO.activityTitle}" escapeXml="false"/> 								
							</td>
						</tr>
						

						<tr>
							<td colspan="2">
								<div class="field-name">
									<fmt:message key="label.authoring.instructions.col"></fmt:message>
								</div>
						  			<c:out value="${mcGeneralAuthoringDTO.activityInstructions}" escapeXml="false"/> 																	
							</td>
						</tr>
	
				 		<tr>
						<td colspan="2">
							<div id="resourceListArea">
									<%@ include file="/authoring/itemlistViewOnly.jsp"%>									
							</div>
						</td>
						</tr>

						<tr>
							<td colspan=2 class="align-right">			
				   				<html:submit onclick="javascript:submitMethod('editActivityQuestions');" styleClass="button">
									<fmt:message key="label.edit"/>
								</html:submit>	 				 		  					
							</td>
						</tr>
			 </table>
			 