<%@ include file="/taglibs.jsp"%>
					
<c:choose>
	<c:when test="${qbQuestion.type == 1}">
		<c:forEach var="qbOption" items="${qbQuestion.qbOptions}">
				
			<tr>
				<td>
					<c:out value="${qbOption.name}" escapeXml="false"/>
				</td>
			</tr>
									
		</c:forEach>						
	</c:when>
	
	<c:when test="${qbQuestion.type == 2}">
		
		<tr>
			<td>
	
				<div>
					<div style="float: left;">
						<c:forEach var="qbOption" items="${question.qbOptions}">
							<div>
								<c:out value="${qbOption.matchingPair}" escapeXml="false"/>
							</div>
						</c:forEach>
					</div>
					
					<div style=" float: right; width: 50%;">
						<c:forEach var="qbOption" items="${question.qbOptions}">
							<div>
								<c:out value="${qbOption.name}" escapeXml="false"/>
							</div>
						</c:forEach>							
					</div>
				</div>
					
			</td>
		</tr>								
		
	</c:when>
	
	<c:when test="${(qbQuestion.type == 3) || (qbQuestion.type == 4) || (qbQuestion.type == 6)}">
	</c:when>
						
	<c:when test="${qbQuestion.type == 5}">
		<tr>
			<td>
				true
			</td>
		</tr>
		<tr>
			<td>
				false
			</td>
		</tr>
	</c:when>
						
	<c:when test="${qbQuestion.type == 7}">
		<c:forEach var="qbOption" items="${question.qbOptions}">
						
			<tr>
				<td>
					<c:out value="${qbOption.name}" escapeXml="false"/>
				</td>
			</tr>
			
		</c:forEach>
	</c:when>						
	
</c:choose>