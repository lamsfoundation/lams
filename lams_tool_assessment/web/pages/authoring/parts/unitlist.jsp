<%@ include file="/common/taglibs.jsp"%>
<div id="unitArea">
	<input type="hidden" name="unitCount" id="unitCount" value="${fn:length(unitList)}">
	<input type="hidden" name="questionType" id="questionType" value="${questionType}" />
	
	<table class="table">
		<c:forEach var="unit" items="${unitList}" varStatus="status">
			<tr>
				<td width="10px" style="vertical-align:middle;">
					${status.index+1}
				</td>
				<td>
					<table class="table-row-spacing">
						<tr>
							<td width="80px">
								<fmt:message key="label.authoring.basic.unit.unit"/>
							</td>
							<td>	
								<input type="hidden" name="unitSequenceId${status.index}" value="${unit.sequenceId}" 
										id="unitSequenceId${status.index}">						
								<input type="text" name="unitUnit${status.index}" value="${unit.unit}"
										id="unitUnit${status.index}" class="number form-control input-sm">
							</td>									
						</tr>
						<tr>
							<td>
								<fmt:message key="label.authoring.basic.unit.multiplier"/>
							</td>
							<td>	
								<c:choose>
									<c:when test="${status.index == 0}">
										<input type="hidden" name="unitMultiplier0" id="unitMultiplier0" value="${unit.multiplier}">
										${unit.multiplier}
									</c:when>
									<c:otherwise>
										<input type="text" name="unitMultiplier${status.index}" value="${unit.multiplier}"
												id="unitMultiplier${status.index}" class="number form-control short-input-text input-sm" 
												title="<fmt:message key='label.authoring.choice.enter.float'/>">
									</c:otherwise>
								</c:choose>							
							</td>									
						</tr>	
					</table>
	
				</td>
			</tr>
		</c:forEach>
	</table>
</div>