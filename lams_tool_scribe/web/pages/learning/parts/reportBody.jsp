
<%@ include file="/common/taglibs.jsp"%>

<div class="panel">
	<c:out value="${scribeDTO.instructions}" escapeXml="false" />
</div>

<%@include file="/pages/parts/voteDisplay.jsp"%>

<h4>
	<fmt:message key="heading.report" />
</h4>

<c:forEach var="reportDTO" items="${scribeSessionDTO.reportDTOs}">

	<div class="row">
		<div class="col-12">
			<div class="panel panel-default">
				<div class="panel-heading panel-title">
					<c:out value="${reportDTO.headingDTO.headingText}" escapeXml="false" />
				</div>
				<div class="panel-body">
					<c:if test="${not empty reportDTO.entryText}">
						<c:set var="entry">
							<lams:out value="${reportDTO.entryText}" escapeHtml="true" />
						</c:set>
						<c:out value="${entry}" escapeXml="false" />
					</c:if>
				</div>
			</div>
		</div>
	</div>
</c:forEach>

<c:if test="${scribeUserDTO.finishedActivity and scribeDTO.reflectOnActivity}">
	<div class="row">
		<div class="col-12">
			<div class="panel panel-default">
				<div class="panel-heading panel-title">
					<fmt:message key="heading.reflection" />
				</div>
				<div class="panel-body">
					<div class="panel">
						<lams:out value="${scribeDTO.reflectInstructions}" escapeHtml="true" />
					</div>

					<div class="panel-body bg-warning mt-2">
						<lams:out value="${scribeUserDTO.notebookEntry}" escapeHtml="true" />
					</div>
				</div>
			</div>
		</div>
	</div>
</c:if>


<c:if test="${not empty otherScribeSessions}">
	<h4>
		<fmt:message key="heading.other.group.reports" />
	</h4>

	<c:set var="sessNumber" value="0"/>
	<c:forEach var="scribeSessionDTO" items="${otherScribeSessions}">
		<c:set var="sessNumber" value="${sessNumber +1 }"/>
		<div class="row">
			<div class="col-12">
				<div class="panel panel-default">
					<div class="panel-heading panel-title">
						<button type="button" class="btn btn-secondary collapsed" data-bs-toggle="collapse"
							 data-bs-target="#panel-${sessNumber}"
						>
							${scribeSessionDTO.sessionName}
						</button>
					</div>
					
					<div id="panel-${sessNumber}" class="panel-body panel-collapse collapse in">

						<c:forEach var="reportDTO" items="${scribeSessionDTO.reportDTOs}">
							<div class="panel panel-info">
								<div class="panel-heading panel-title">
									<c:out value="${reportDTO.headingDTO.headingText}" escapeXml="false" />
								</div>
								<div class="panel-body">
									<c:if test="${not empty reportDTO.entryText}">
										<c:set var="entry">
											<lams:out value="${reportDTO.entryText}" escapeHtml="true" />
										</c:set>
										<c:out value="${entry}" escapeXml="false" />
									</c:if>
								</div>
							</div>

						</c:forEach>


					</div>
				</div>
			</div>
		</div>


	</c:forEach>
</c:if>

