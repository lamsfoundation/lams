<style>	
	.rubrics-user-panel .collapsable-icon-left a {
		text-decoration: none;
	}
	
	.rubrics-user-panel .collapsable-icon-left a:after {
		margin-top: 6px;
	}
	
	.rubrics-table tr:first-child {
		background-color: initial !important;
	}
	
	.rubrics-table th {
		font-style: normal;
		font-weight: bold;
		}

	.rubrics-table td:first-child {
		font-weight: bold;
	}
	
	.rubrics-table td:not(:first-child) {
		cursor: pointer;
	}
	
	.rubrics-table td.bg-success {
		font-weight: bold;
		font-size: larger;
	}
	
	.expand-all-button {
		margin-bottom: 15px;
		float: right;
	}
</style>

<script>
	var rubricsRequireRatings = ${peerreview.rubricsRequireRatings};
	
	$(document).ready(function(){
		if (rubricsRequireRatings){
			$('#finishButton').prop('disabled', isRatingMissing());
		}
	});
	
	function submitEntry(next) {
		if (next && !rubricsRequireRatings && isRatingMissing() 
				&& !confirm('<spring:escapeBody javaScriptEscape="true"><fmt:message key="message.rating.rubrics.selection.missing" /></spring:escapeBody>')){
			return;
		}
		// answer saved when clicked so don't use next button to submit
		nextprev(next);
	}

    // it communicates with the same servlet as Star rating style, so it uses the same data format
	function rateRubricsCriteria(cell, ratingCriteriaId, targetUserId, columnOrderId) {
		$.ajax({
    		type: "POST",
    		url: '<lams:LAMSURL/>servlet/rateItem',
    		data: {
    			idBox: ratingCriteriaId + '-' + targetUserId,
    			rate : columnOrderId,
    			toolSessionId: '${toolSessionId}'
    		},
    		success: function(data, textStatus) {
    			if (data.error) {
        			alert('<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.rating.rubrics.error.rate" /></spring:escapeBody>');
    				return;
    			}
    			$(cell).addClass('bg-success').siblings('td').removeClass('bg-success');
    			
        		if (rubricsRequireRatings) {
					$('#finishButton').prop('disabled', isRatingMissing());
            	}
    		},
			onError : function(){
    			alert('<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.rating.rubrics.error.rate" /></spring:escapeBody>');
 			}
    	});
	}

	function expandAllRubricsUserPanels(){
		$('.collapse').collapse('show');
	}

	function isRatingMissing() {
		let criterionTables = $('.rubrics-table');
		return $('tbody td.bg-success', criterionTables).length !== $('tbody tr', criterionTables).length
			- criterionTables.length;
	}
</script>
<button class="btn btn-secondary expand-all-button" onClick="javascript:expandAllRubricsUserPanels()">
	<fmt:message key="label.rating.rubrics.expand.all" />
</button>
<div class="clearfix"></div>

<div id="rubrics-user-panels" class="panel-group" role="tablist" aria-multiselectable="true">
	<%-- It is sufficient to take user names and columns from the first row/criterion --%>
	<c:set var="exampleRatings" value="${criteriaRatings.ratingDtos}" />

	<c:forEach var="ratingDto" items="${exampleRatings}" varStatus="learnerOrderId">
			
	    <div class="panel panel-default rubrics-user-panel">
	       <div class="panel-heading" role="tab" id="heading${ratingDto.itemId}">
	       	<span class="panel-title collapsable-icon-left">
	       		<button type="button" class="btn btn-secondary collapsed" data-bs-toggle="collapse" data-bs-target="#collapse${ratingDto.itemId}" 
						aria-expanded="false" aria-controls="collapse${ratingDto.itemId}" data-parent="#rubrics-users-panels"
				>
					<lams:Portrait userId="${ratingDto.itemId}" hover="false" />
					&nbsp;
					<strong>
						<c:out value="${ratingDto.itemDescription}" escapeXml="false"/>
					</strong>
				</button>
			</span>
	       </div>
	       
	       <div id="collapse${ratingDto.itemId}" class="panel-collapse collapse" 
	       	    role="tabpanel" aria-labelledby="heading${ratingDto.itemId}">
					<table class="table table-hover table-bordered rubrics-table">
						<tr>
							<%-- Each answer column has the same length, all remaining space is take by the question column --%>
							<th></th>
							<c:forEach var="columnHeader" items="${criteriaRatings.ratingCriteria.rubricsColumnHeaders}" varStatus="columnStatus">
								<th>
									(${columnStatus.count})&nbsp;<c:out value="${columnHeader}" escapeXml="false"/>
								</th>
								<c:if test="${not columnStatus.last and peerreview.rubricsInBetweenColumns}">
									<th>
										<i>(${columnStatus.count + 0.5})&nbsp;<fmt:message key="label.rating.rubrics.in.between" /></i>
									</th>
								</c:if>
							</c:forEach>
						</tr>
						<c:forEach var="criteriaDto" items="${criteriaRatings.criteriaGroup}">
							<c:set var="criteria" value="${criteriaDto.ratingCriteria}" />
							<tr>
								<td>
									<c:out value="${criteria.title}" escapeXml="false" />
								</td>
								<c:forEach var="column" items="${criteria.rubricsColumns}" varStatus="columnOrderId">
									<td onClick="javascript:rateRubricsCriteria(this, ${criteria.ratingCriteriaId}, ${ratingDto.itemId}, ${columnOrderId.count})"
										<%-- Columns are ordered from 1 to 5, so rate value is also the order ID of the column --%>
										<c:if test="${criteriaDto.ratingDtos[learnerOrderId.index].userRating == columnOrderId.count}">
											class="bg-success"
										</c:if>
									>
										<c:out value="${column.name}" escapeXml="false" />	
									</td>
									<c:if test="${not columnOrderId.last and peerreview.rubricsInBetweenColumns}">
										<td onClick="javascript:rateRubricsCriteria(this, ${criteria.ratingCriteriaId}, ${ratingDto.itemId}, ${columnOrderId.count + 0.5})"
											<c:if test="${criteriaDto.ratingDtos[learnerOrderId.index].userRating == (columnOrderId.count + 0.5)}">
												class="bg-success"
											</c:if>
										>
											<i><fmt:message key="label.rating.rubrics.in.between" /></i>
										</td>
									</c:if>
								</c:forEach>
							</tr>
						</c:forEach>
					</table>
			</div>
		</div>
		
	</c:forEach>
</div>
