	<%@ include file="/common/taglibs.jsp"%>
	
	<h1>
		<c:out value="${scribeDTO.title}" escapeXml="false" />
	</h1>

	<div class="space-left space-right">

		<p>
			${scribeDTO.instructions}
		</p>
	
		<%@include file="/pages/parts/voteDisplay.jsp"%>
	
		<div class="field-name">
			<fmt:message key="heading.report" />
		</div>
		<c:forEach var="reportDTO" items="${scribeSessionDTO.reportDTOs}">
	
			<div class="shading-bg">
				<p>
					${reportDTO.headingDTO.headingText}
				</p>
	
				<c:if test="${not empty reportDTO.entryText}">
					<ul>
						<li>
							<p>
								<lams:out value="${reportDTO.entryText}" />
							</p>
						</li>
					</ul>
				</c:if>
			</div>
		</c:forEach>
	
		<c:if
			test="${scribeUserDTO.finishedActivity and scribeDTO.reflectOnActivity}">
			<div>
				<h4>
					${scribeDTO.reflectInstructions}
				</h4>
				<p>
					${scribeUserDTO.notebookEntry}
				</p>
			</div>
		</c:if>
	
	
		<c:if test="${not empty otherScribeSessions}">
			<h2>
				<fmt:message key="heading.other.group.reports" />
			</h2>
		
			<c:forEach var="scribeSessionDTO" items="${otherScribeSessions}">
				<div class="field-name">
					${scribeSessionDTO.sessionName}
				</div>
				<c:forEach var="reportDTO" items="${scribeSessionDTO.reportDTOs}">
					<div class="shading-bg">
						<p>
							${reportDTO.headingDTO.headingText}
						</p>
			
						<c:if test="${not empty reportDTO.entryText}">
							<ul>
								<li>
									<p>
										<lams:out value="${reportDTO.entryText}" />
									</p>
								</li>
							</ul>
						</c:if>
					</div>
				</c:forEach>
			</c:forEach>
		</c:if>
	</div>