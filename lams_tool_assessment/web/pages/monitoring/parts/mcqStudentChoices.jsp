<%@ include file="/common/taglibs.jsp"%>
<link href="<lams:WebAppURL/>includes/css/assessment.css" rel="stylesheet" type="text/css">

<script>
	$('#allocate-vsas-button').toggle(${vsaPresent});
	$(function () {
		$('[data-bs-toggle="tooltip"]').tooltip();
	})	
</script>

<!-- Table -->
<c:if test="${not empty questions}">
	<div class="table-responsive shadow rounded-4">
	<table id="questions-data" class="table table-hover">
		<thead class="text-bg-secondary">
			<tr role="row" class="border-top-0">
				<th class="text-center">
					<fmt:message key="label.monitoring.question.summary.question"/>
				</th>
				<c:forEach begin="0" end="${maxOptionsInQuestion - 1}" var="i">
					<th class="text-center">
						${ALPHABET_CAPITAL_LETTERS[i]}
					</th>
				</c:forEach>
			</tr>
		</thead>
		
		<tbody>
			<c:forEach var="question" items="${questions}" varStatus="i">
				<tr>
					<c:set var="correctOptionPercentage" value="-1" />
					<c:forEach var="option" items="${question.optionDtos}">
						<c:if test="${option.correct}">
							<c:set var="correctOptionPercentage" value="${option.percentage}" />
						</c:if>
					</c:forEach>
					<c:choose>
						<c:when test="${correctOptionPercentage > 95}">
							<c:set var="highlightClass" value="bg-success border-success" />
							<c:set var="textClass" value="text-white" />
						</c:when>
						<c:when test="${correctOptionPercentage >= 0 and correctOptionPercentage < 40}">
							<c:set var="highlightClass" value="bg-danger border-danger" />
							<c:set var="textClass" value="text-white" />
						</c:when>
						<c:when test="${correctOptionPercentage >= 0 and correctOptionPercentage < 75}">
							<c:set var="highlightClass" value="bg-warning border-warning" />
							<c:set var="textClass" value="" />
						</c:when>
						<c:otherwise>
							<c:set var="highlightClass" value="" />
							<c:set var="textClass" value="" />
						</c:otherwise>
					</c:choose>
					
					<td class="text-center">
						<a data-bs-toggle="modal" data-bs-target="#question${i.index}Modal" href="#" class="${highlightClass} fs-5 ${textClass} aQuestionLink">
							${i.index+1}
						</a>
					</td>
					
					<c:forEach var="option" items="${question.optionDtos}">
						<td class="align-middle text-center
							<c:if test="${option.correct}">
								fw-bolder text-success fs-5" title="<fmt:message key="label.authoring.true.false.correct.answer"/>
							</c:if>
							"  data-bs-toggle="tooltip" data-bs-placement="top"
							>
							<c:choose>
								<c:when test="${option.percentage == -1}">-</c:when>
								<c:otherwise><fmt:formatNumber type="number" maxFractionDigits="2" value="${option.percentage}"/>%</c:otherwise>
							</c:choose>
						</td>
					</c:forEach>
					
					<c:forEach begin="1" end="${maxOptionsInQuestion - fn:length(question.optionDtos)}" var="j">
						<td class="normal"></td>
					</c:forEach>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	</div>

<!-- Question detail modal -->
<c:forEach var="question" items="${questions}" varStatus="i">
	<div class="modal fade iraQuestionModal" id="question${i.index}Modal">
	<div class="modal-dialog modal-dialog-centered">
	<div class="modal-content">
		<div class="modal-header align-items-start text-bg-warning">
			<div class="modal-title d-flex">
				<span>Q${i.index+1})</span>
				<c:if test="${not empty question.title}">
					<div>
						<c:out value="${question.title}"  escapeXml="false" />
					</div>
				</c:if>
			</div>
			<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
		</div>
		<div class="modal-body">
			${question.question}
		
			<div class="table-responsive">
				<table class="table table-hover">
					<tbody>
						<c:forEach var="option" items="${question.optionDtos}" varStatus="j">
							<c:set var="cssClass"><c:if test='${option.correct}'>bg-success</c:if></c:set>
							<tr>
								<td width="5px" class="${cssClass}">
									${ALPHABET[j.index]}.
								</td>
								<td class="${cssClass}">
									<c:out value="${option.name}" escapeXml="false"/>
								</td>
								<td class="${cssClass}">
									<c:choose>
										<c:when test="${option.percentage == -1}">-</c:when>
										<c:otherwise><fmt:formatNumber type="number" maxFractionDigits="2" value="${option.percentage}"/>%</c:otherwise>
									</c:choose>
								</td>
							</tr>
						</c:forEach>
						
					</tbody>
				</table>
			</div>
		</div>
	</div>
	</div>
	</div>
</c:forEach>
<!-- End question detail modal -->
</c:if>
