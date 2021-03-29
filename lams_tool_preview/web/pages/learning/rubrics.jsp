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
	
	.rubrics-table td:not(:first-child) {
		cursor: pointer;
	}
</style>

<script>

	function submitEntry(next) {
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
        			alert('<fmt:message key="label.rating.rubrics.error.rate" />');
    				return;
    			}
    			$(cell).addClass('bg-success').siblings('td').removeClass('bg-success');
	  			
    		},
			onError : function(){
    			alert('<fmt:message key="label.rating.rubrics.error.rate" />');
 			}
    	});
	}
</script>

<div id="rubrics-user-panels" class="panel-group" role="tablist" aria-multiselectable="true">
	<%-- It is sufficient to take user names and columns from the first row/criterion --%>
	<c:set var="exampleCriteria" value="${criteriaGroup[0]}" />
	<c:set var="exampleRatings" value="${exampleCriteria.ratingDtos}" />

	<c:forEach var="ratingDto" items="${exampleRatings}" varStatus="learnerOrderId">
			
	    <div class="panel panel-default rubrics-user-panel">
	       <div class="panel-heading" role="tab" id="heading${ratingDto.itemId}">
	       	<span class="panel-title collapsable-icon-left">
	       		<a class="collapsed" role="button" data-toggle="collapse" href="#collapse${ratingDto.itemId}" 
						aria-expanded="false" aria-controls="collapse${ratingDto.itemId}" data-parent="#rubrics-users-panels">
					<lams:Portrait userId="${ratingDto.itemId}" hover="false" />&nbsp;<strong><c:out value="${ratingDto.itemDescription}" escapeXml="true"/></strong>
				</a>
			</span>
	       </div>
	       
	       <div id="collapse${ratingDto.itemId}" class="panel-collapse collapse" 
	       	    role="tabpanel" aria-labelledby="heading${ratingDto.itemId}">
					<table class="table table-hover table-bordered rubrics-table">
						<tr>
							<%-- Each answer column has the same length, all remaining space is take by the question column --%>
							<th class="col-xs-${12 - fn:length(exampleCriteria.ratingCriteria.rubricsColumnHeaders) * 2}"></th>
							<c:forEach var="columnHeader" items="${exampleCriteria.ratingCriteria.rubricsColumnHeaders}">
								<th class="col-xs-2">
									<c:out value="${columnHeader}" escapeXml="false"/>
								</th>
							</c:forEach>
						</tr>
						<c:forEach var="criteriaDto" items="${criteriaGroup}">
							<c:set var="criteria" value="${criteriaDto.ratingCriteria}" />
							<tr>
								<td>
									<c:out value="${criteria.title}" escapeXml="false" />
								</td>
								<c:forEach var="column" items="${criteria.rubricsColumns}" varStatus="columnOrderId">
									<td onClick="javascript:rateRubricsCriteria(this, ${criteria.ratingCriteriaId}, ${ratingDto.itemId}, ${columnOrderId.count})"
										<%-- Columns are ordered from 1 to 5, so rate value is also the order ID of the column --%>
										<c:if test="${criteriaDto.ratingDtos[learnerOrderId.index].userRating eq columnOrderId.count}">
											class="bg-success"
										</c:if>
									>
										<c:out value="${column.name}" escapeXml="false" />	
									</td>
								</c:forEach>
							</tr>
						</c:forEach>
					</table>
			</div>
		</div>
		
	</c:forEach>
</div>