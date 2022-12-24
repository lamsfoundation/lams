<%@ include file="/common/taglibs.jsp"%>

<script>
	$('#allocate-vsas-button').toggle(${vsaPresent});
</script>

<!-- Table -->
<c:if test="${not empty questions}">
<div class="card">
<div class="table-responsive card-body pb-0" style="margin:0">
	<table id="questions-data" class="table table-bordered table-condensed">
		<thead>
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
							<c:set var="highlightClass" value="bg-success" />
							<c:set var="textClass" value="text-white" />
						</c:when>
						<c:when test="${correctOptionPercentage >= 0 and correctOptionPercentage < 40}">
							<c:set var="highlightClass" value="bg-danger" />
							<c:set var="textClass" value="text-white" />
						</c:when>
						<c:when test="${correctOptionPercentage >= 0 and correctOptionPercentage < 75}">
							<c:set var="highlightClass" value="bg-warning" />
							<c:set var="textClass" value="" />
						</c:when>
						<c:otherwise>
							<c:set var="highlightClass" value="" />
							<c:set var="textClass" value="" />
						</c:otherwise>
					</c:choose>
					
					<td class="text-center ${highlightClass}">
						<a data-bs-toggle="modal" data-bs-target="#question${i.index}Modal" href="#" class="fs-5 ${textClass}">
							${i.index+1}
						</a>
					</td>
					
					<c:forEach var="option" items="${question.optionDtos}">
						<td class="align-middle text-center
							<c:if test="${option.correct}">
								fw-bolder fs-5" title="<fmt:message key="label.authoring.true.false.correct.answer"/>
							</c:if>
							">
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
</div>

<!-- Question detail modal -->
<c:forEach var="question" items="${questions}" varStatus="i">
	<div class="modal fade" id="question${i.index}Modal">
	<div class="modal-dialog modal-dialog-centered">
	<div class="modal-content">
		<div class="modal-header align-items-start">
			<div class="modal-title">
				<span>Q${i.index+1})</span>
				<c:if test="${not empty question.title}">
					<p><c:out value="${question.title}"  escapeXml="false" /></p>
				</c:if>
				${question.question}
			</div>
			<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
		</div>
		<div class="modal-body">
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
									<fmt:formatNumber type="number" maxFractionDigits="2" value="${option.percentage}"/>%
								</td>
							</tr>
						</c:forEach>
						
					</tbody>
				</table>
			</div>
		</div>
	            
		<div class="modal-footer">	
			<a href="#" data-bs-dismiss="modal" class="btn btn-secondary">
				<fmt:message key="label.ok"/>
			</a>
		</div>
	</div>
	</div>
	</div>
</c:forEach>
<!-- End question detail modal -->
</c:if>