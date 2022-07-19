<%@ include file="/common/taglibs.jsp"%>

<c:forEach var="item" items="${scratchieItems}" varStatus="i">
	<div class="card mb-3">
			
		<div class="card-header">
			<span class="card-title fw-bold">
				<span class="float-start me-2">Q${i.index+1})</span> 
				<c:out value="${item.qbQuestion.name}" escapeXml="false"/>
			</span> 
		</div>
				
		<div class="card-body">
			<table class="table table-responsive">
				<tbody>
					<c:forEach var="optionDto" items="${item.optionDtos}" varStatus="j">
						<c:if test="${optionDto.scratched}">
								
							<tr>
								<td width="5px">
									${ALPHABET[j.index]}.
								</td>
								<td>
									<c:out value="${optionDto.answer}" escapeXml="${!optionDto.mcqType}"/>
									
									<c:choose>
										<c:when test="${optionDto.correct}">
											<i class="fa fa-check-square text-success"></i>
										</c:when>
										<c:otherwise>
											<i class="fa fa-minus-square text-danger"></i>
										</c:otherwise>
									</c:choose>
								</td>
							</tr>
									
						</c:if>
					</c:forEach>
				</tbody>
			</table>
		</div>
				
	</div> 
</c:forEach>
