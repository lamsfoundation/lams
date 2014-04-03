	<%@ include file="/common/taglibs.jsp"%>
	
	<h1>
		<c:out value="${scribeDTO.title}" escapeXml="true" />
	</h1>

	<div class="space-left space-right">

		<p>
			<c:out value="${scribeDTO.instructions}" escapeXml="false"/>
		</p>
	
		<%@include file="/pages/parts/voteDisplay.jsp"%>
	
		<div class="field-name">
			<fmt:message key="heading.report" />
		</div>
		<c:forEach var="reportDTO" items="${scribeSessionDTO.reportDTOs}">
	
			<div class="shading-bg">
				<p>
					<c:out value="${reportDTO.headingDTO.headingText}" escapeXml="false"/>
				</p>
	
				<c:if test="${not empty reportDTO.entryText}">
					<ul>
						<li>
							<p>
								<c:set var="entry">
									<lams:out value="${reportDTO.entryText}" escapeHtml="true"/>
								</c:set>
								<c:out value="${entry}" escapeXml="false"/>
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
				  <fmt:message key="heading.reflection" />
				</h4>
				
				<strong><lams:out value="${scribeDTO.reflectInstructions}" escapeHtml="true"/></strong>
				<p>
					<lams:out value="${scribeUserDTO.notebookEntry}" escapeHtml="true"/>
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
							<c:out value="${reportDTO.headingDTO.headingText}" escapeXml="false"/>
						</p>
			
						<c:if test="${not empty reportDTO.entryText}">
							<ul>
								<li>
									<p>
										<c:set var="entry">
											<lams:out value="${reportDTO.entryText}" escapeHtml="true"/> 
										</c:set>
										<c:out value="${entry}" escapeXml="false"/>
									</p>
								</li>
							</ul>
						</c:if>
					</div>
				</c:forEach>
			</c:forEach>
		</c:if>
	</div>
