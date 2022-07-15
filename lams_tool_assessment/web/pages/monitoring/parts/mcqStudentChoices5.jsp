<%@ include file="/common/taglibs.jsp"%>

<script>
	$('#allocate-vsas-button').toggle(${vsaPresent});
</script>

<!-- Table -->
<c:if test="${not empty questions}">
<div class="card">
<div class="table-responsive card-body pb-0" style="margin:0">
	<table id="questions-data" class="table table-bordered table-hover table-condensed">
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
					<td class="text-center">
						<a data-bs-toggle="modal" data-bs-target="#question${i.index}Modal" href="#"class="fs-5">
							${i.index+1}
						</a>
					</td>
					
					<c:forEach var="option" items="${question.optionDtos}">
						<td class="align-middle text-center <c:if test='${option.correct}'>bg-success text-white</c:if>">
							<fmt:formatNumber type="number" maxFractionDigits="2" value="${option.percentage}"/>%
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