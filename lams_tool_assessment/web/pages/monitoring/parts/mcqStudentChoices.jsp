<%@ include file="/common/taglibs.jsp"%>

<!-- Table -->
<div class="row no-gutter">
<div class="col-xs-12 col-md-12 col-lg-12">
<div class="panel">
<div class="panel-body">
<div class="table-responsive" style="margin:0">
	<table id="questions-data" class="table table-striped table-bordered table-hover table-condensed">
		<thead>
			<tr role="row">
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
					<td class="normal">
						<a data-toggle="modal" href="#question${i.index}Modal">
							${i.index+1}
						</a>
					</td>
					
					<c:forEach var="option" items="${question.options}">
						<td class="text-right normal <c:if test='${option.grade == 1}'>success</c:if>">
							<fmt:formatNumber type="number" maxFractionDigits="2" value="${option.percentage}"/>%
						</td>
					</c:forEach>
					
					<c:forEach begin="1" end="${maxOptionsInQuestion - fn:length(question.options)}" var="j">
						<td class="normal"></td>
					</c:forEach>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
</div>
</div>          
</div>
</div>
<!-- End table -->

<!-- Question detail modal -->
<c:forEach var="question" items="${questions}" varStatus="i">
	<div class="modal fade" id="question${i.index}Modal">
	<div class="modal-dialog">
	<div class="modal-content">
	<div class="modal-body">
	
		<div class="panel panel-default">
			<div class="panel-heading">
				<h4 class="panel-title"><span class="float-left space-right">Q${i.index+1})</span> ${question.question}</h4>
			</div>
			<div class="panel-body">
				<div class="table-responsive">
					<table class="table table-striped table-hover">
						<tbody>
						
							<c:forEach var="option" items="${question.options}" varStatus="j">
								<c:set var="cssClass"><c:if test='${option.grade == 1}'>bg-success</c:if></c:set>
								<tr>
									<td width="5px" class="${cssClass}">
										${ALPHABET[j.index]}.
									</td>
									<td class="${cssClass}">
										<c:out value="${option.optionString}" escapeXml="false"/>
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
		</div>
	            
		<div class="modal-footer">	
			<a href="#" data-dismiss="modal" class="btn btn-default">
				<fmt:message key="label.ok"/>
			</a>
		</div>
	
	</div>
	</div>
	</div>
	</div>
</c:forEach>
<!-- End question detail modal -->
