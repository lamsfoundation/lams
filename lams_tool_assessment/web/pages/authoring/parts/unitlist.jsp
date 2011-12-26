<%@ include file="/common/taglibs.jsp"%>
<div id="unitArea">
	<input type="hidden" name="unitCount" id="unitCount" value="${fn:length(unitList)}">
	<input type="hidden" name="questionType" id="questionType" value="${questionType}" />
	
	<table class="alternative-color" cellspacing="0">
		<c:forEach var="unit" items="${unitList}" varStatus="status">
			<tr>
				<td width="3px" style="vertical-align:middle;">
					${status.index+1}
				</td>			
				<td>
					<table>
						<tr>
							<td style="padding-left:0px; border-bottom:0px; vertical-align:middle; background:none;" width="50 px">
								<span class="field-name">
									<fmt:message key="label.authoring.basic.unit.unit"></fmt:message>
								</span>
							</td>
							<td style="padding-left:0px; border-bottom:0px; background:none;">	
								<input type="hidden" name="unitSequenceId${status.index}" id="unitSequenceId${status.index}" value="${unit.sequenceId}">									
								<input type="text" name="unitUnit${status.index}"
									id="unitUnit${status.index}" value="${unit.unit}">
							</td>									
						</tr>
						<tr>
							<td style="padding-left:0px; border-bottom:0px; vertical-align:middle; background:none;">
								<span class="field-name">
									<fmt:message key="label.authoring.basic.unit.multiplier"></fmt:message>
								</span>
							</td>
							<td style="padding-left:0px; border-bottom:0px; background:none;">	
							<c:choose>
								<c:when test="${status.index == 0}">
									<input type="hidden" name="unitMultiplier0" id="unitMultiplier0" value="${unit.multiplier}">
									${unit.multiplier}
								</c:when>
								<c:otherwise>
									<input type="text" name="unitMultiplier${status.index}"
										id="unitMultiplier${status.index}" value="${unit.multiplier}" class="number shortInputText" title="<fmt:message key='label.authoring.choice.enter.float'/>">
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