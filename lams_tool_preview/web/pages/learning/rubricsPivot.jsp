<style>	
	.rubrics-row-panel .collapsable-icon-left a {
		text-decoration: none;
	}
		
	.rubrics-table td:not(:first-child) {
		cursor: pointer;
		text-align: center;
	}

	.rubrics-table th {
		font-style: normal;
		text-align: center;
	}
	
	.column-header-span {
		font-style: italic;
	}

	.expand-all-button {
		margin-bottom: 10px;
	}
</style>

<script>

	$(document).ready(function(){
		$('#rubrics-row-panels').closest('.container').addClass('container-fluid').removeClass('container');
	});

	function submitEntry(next) {
		// answer saved when clicked so don't use next button to submit
		nextprev(next);
	}

    // it communicates with the same servlet as Star rating style, so it uses the same data format
	function rateRubricsCriteria(cell, ratingCriteriaId, targetUserId, rating) {
		$.ajax({
    		type: "POST",
    		url: '<lams:LAMSURL/>servlet/rateItem',
    		data: {
    			idBox: ratingCriteriaId + '-' + targetUserId,
    			rate : rating,
    			toolSessionId: '${toolSessionId}'
    		},
    		success: function(data, textStatus) {
    			if (data.error) {
        			alert('<fmt:message key="label.rating.rubrics.error.rate" />');
    				return;
    			}
    			let row = $(cell).parent(),
    				columnIndex = row.children().index(cell);
    			$(cell).closest('tbody').children('tr').each(function(){
        			$(this).children('td').eq(columnIndex).removeClass('bg-success');
        		});
        		$(cell).addClass('bg-success');
    		},
			onError : function(){
    			alert('<fmt:message key="label.rating.rubrics.error.rate" />');
 			}
    	});
	}

	function expandAllRubricsPanels(){
		$('.collapse').collapse('show');
	}
</script>
<button class="btn btn-default expand-all-button" onClick="javascript:expandAllRubricsPanels()">
	<fmt:message key="label.rating.rubrics.expand.all" />
</button>
<div id="rubrics-row-panels" class="panel-group" role="tablist" aria-multiselectable="true">
	<%-- It is sufficient to take user names and columns from the first row/criterion --%>
	<c:set var="exampleRatings" value="${criteriaRatings.ratingDtos}" />
	<c:set var="columnHeaders" value="${criteriaRatings.ratingCriteria.rubricsColumnHeaders}" />
	<c:set var="columnHeaderCount" value="${fn:length(columnHeaders)}" />
	
	<c:forEach var="criteriaDto" items="${criteriaRatings.criteriaGroup}">
		<c:set var="criteria" value="${criteriaDto.ratingCriteria}" />
		
	    <div class="panel panel-default rubrics-row-panel">
	       <div class="panel-heading" role="tab" id="heading${criteria.ratingCriteriaId}">
	       	<span class="panel-title collapsable-icon-left" style="font-size:larger">
	       		<a class="collapsed" role="button" data-toggle="collapse" href="#collapse${criteria.ratingCriteriaId}" 
						aria-expanded="false" aria-controls="collapse${criteria.ratingCriteriaId}" data-parent="#rubrics-rows-panels">
					<%-- Criterion "row" --%>
					<c:out value="${criteria.title}" escapeXml="false" />
				</a>
			</span>
	       </div>
	       
	       <div id="collapse${criteria.ratingCriteriaId}" class="panel-collapse collapse" 
	       	    role="tabpanel" aria-labelledby="heading${criteria.ratingCriteriaId}">
					<table class="table table-hover table-bordered rubrics-table">
						<thead>
							<tr>
								<%-- Learner profile pictures and names --%>
								<th></th>
								<c:forEach var="ratingDto" items="${exampleRatings}" varStatus="learnerOrderId">
									<th>
										<lams:Portrait userId="${ratingDto.itemId}" hover="false" /><br>
										<strong><c:out value="${ratingDto.itemDescription}" escapeXml="false"/></strong>
									</th>
								</c:forEach>
							</tr>
						</thead>
						
						<tbody>
							<c:forEach items="${columnHeaders}" varStatus="columnStatus">
								<c:set var="columnHeader" value="${columnHeaders[columnHeaderCount - columnStatus.count]}" />
								<tr>
									<td>
										<%-- Criterion "column" --%>
										<c:if test="${not empty columnHeader}">
										<span class="column-header-span"><c:out value="${columnHeader}" escapeXml="false"/></span><br>
										</c:if>
										<%-- Criterion "cell" --%>
										<c:out value="${criteria.rubricsColumns[columnHeaderCount - columnStatus.count].name}" escapeXml="false" />	
									</td>
									<c:forEach var="ratingDto" items="${exampleRatings}" varStatus="learnerOrderId">
										
										<td onClick="javascript:rateRubricsCriteria(this, ${criteria.ratingCriteriaId}, ${ratingDto.itemId}, ${columnHeaderCount - columnStatus.index})"
											<%-- Columns are ordered from 1 to 5, so rate value is also the order ID of the column --%>
											<c:if test="${criteriaDto.ratingDtos[learnerOrderId.index].userRating == (columnHeaderCount - columnStatus.index)}">
												class="bg-success"
											</c:if>
										>
											${columnHeaderCount - columnStatus.index}
										</td>
									</c:forEach>
								</tr>
								<c:if test="${not columnStatus.last and peerreview.rubricsInBetweenColumns}">
									<tr>
										<td>
											<i><fmt:message key="label.rating.rubrics.in.between" /></i>
										</td>
										<c:forEach var="ratingDto" items="${exampleRatings}" varStatus="learnerOrderId">
											<td onClick="javascript:rateRubricsCriteria(this, ${criteria.ratingCriteriaId}, ${ratingDto.itemId}, ${columnHeaderCount - columnStatus.index - 0.5})"
												<c:if test="${criteriaDto.ratingDtos[learnerOrderId.index].userRating == (columnHeaderCount - columnStatus.index - 0.5)}">
													class="bg-success"
												</c:if>
											>
												${columnHeaderCount - columnStatus.index - 0.5}
											</td>
										</c:forEach>
									</tr>
								</c:if>
							</c:forEach>
						</tbody>
					</table>
			</div>
		</div>
		
	</c:forEach>
</div>
