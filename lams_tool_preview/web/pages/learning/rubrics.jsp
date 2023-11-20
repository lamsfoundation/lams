<style>
	.rubrics-user-card .collapsable-icon-left button:after {
		margin-top: 6px;
	}
	
	.rubrics-table .row:first-child {
		background-color: initial !important;
	}
	
	.rubrics-table-header {
		font-weight: bold;
		text-align: center;
	}

	.rubrics-table .col:first-child {
		font-weight: bold;
	}
	
	.rubrics-table .row:not(.rubrics-table-header) .col:not(:first-child) {
		cursor: pointer;
	}
	
	.rubrics-table .col.text-bg-success {
		font-weight: bold;
		line-height: 40px;
		margin: -0.5em 0;
	}
	
	#expand-all-button {
		margin-bottom: 15px;
		float: right;
	}
	
	.lcard {
		margin-bottom: 1.25rem;
	}
	.row>button.col {
		border: none;
		background: none;
	}
	.row>button.col:focus-visible {
    	box-shadow: 0 0 0 0.25rem rgba(130, 138, 145, .5);
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
    			$(cell).addClass('text-bg-success').siblings('button').removeClass('text-bg-success');
    			
        		if (rubricsRequireRatings) {
					$('#finishButton').prop('disabled', isRatingMissing());
            	}
    		},
			onError : function(){
    			alert('<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.rating.rubrics.error.rate" /></spring:escapeBody>');
 			}
    	});
	}

	function expandAllRubricsUserCards(){
		let isCollapsing = $("#expand-all-button").hasClass("collapsed") ? "show" : "hide";
		$("#expand-all-button").toggleClass("collapsed");
		$('.collapse').collapse(isCollapsing);
	}

	function isRatingMissing() {
		let criterionTables = $('.rubrics-table');
		return $('.col.text-bg-success', criterionTables).length !== $('.row:not(.rubrics-table-header)', criterionTables).length;
	}
</script>

<c:if test="${peerreview.rubricsRequireRatings}">
	<lams:Alert5 type="info" id="info-assign-all-rubrics" close="true">
		<fmt:message key="label.rating.rubrics.require.ratings.tooltip.learner" />
	</lams:Alert5>
</c:if>

<div class="collapsable-icon-left">
	<button id="expand-all-button" class="btn btn-secondary collapsed" data-bs-toggle="collapse" onClick="javascript:expandAllRubricsUserCards()">
		<fmt:message key="label.rating.rubrics.expand.all" />
	</button>
</div>
<div class="clearfix"></div>

<div id="rubrics-user-cards">
	<%-- It is sufficient to take user names and columns from the first row/criterion --%>
	<c:set var="exampleRatings" value="${criteriaRatings.ratingDtos}" />

	<c:forEach var="ratingDto" items="${exampleRatings}" varStatus="learnerOrderId">
			
	    <div class="card lcard rubrics-user-card">
	       <div class="card-header collapsable-icon-left" id="heading${ratingDto.itemId}">
	       		<button type="button" class="btn btn-secondary-darker no-shadow collapsed" data-bs-toggle="collapse" data-bs-target="#collapse${ratingDto.itemId}" 
						aria-expanded="false" aria-controls="collapse${ratingDto.itemId}" data-parent="#rubrics-users-cards"
				>
					<lams:Portrait userId="${ratingDto.itemId}" hover="false" />
					&nbsp;
					<c:out value="${ratingDto.itemDescription}" escapeXml="false"/>
				</button>
	       </div>
	       
	       <div id="collapse${ratingDto.itemId}" class="collapse" aria-labelledby="heading${ratingDto.itemId}">
				<div class="ltable rubrics-table mb-0">
						<div class="row rubrics-table-header m-0">
							<%-- Each answer column has the same length, all remaining space is taken by the question column --%>
							<div class="col"></div>
							<c:forEach var="columnHeader" items="${criteriaRatings.ratingCriteria.rubricsColumnHeaders}" varStatus="columnStatus">
								<div class="col">
									(${columnStatus.count})&nbsp;<c:out value="${columnHeader}" escapeXml="false"/>
								</div>
								<c:if test="${not columnStatus.last and peerreview.rubricsInBetweenColumns}">
									<div class="col">
										<i>(${columnStatus.count + 0.5})&nbsp;<fmt:message key="label.rating.rubrics.in.between" /></i>
									</div>
								</c:if>
							</c:forEach>
						</div>
						
						<c:forEach var="criteriaDto" items="${criteriaRatings.criteriaGroup}">
							<c:set var="criteria" value="${criteriaDto.ratingCriteria}" />
							<div class="row m-0">
								<div class="col">
									<c:out value="${criteria.title}" escapeXml="false" />
								</div>
								<c:forEach var="column" items="${criteria.rubricsColumns}" varStatus="columnOrderId">
									<button type="button" onClick="javascript:rateRubricsCriteria(this, ${criteria.ratingCriteriaId}, ${ratingDto.itemId}, ${columnOrderId.count})"
										<%-- Columns are ordered from 1 to 5, so rate value is also the order ID of the column --%>
										class='col <c:if test="${criteriaDto.ratingDtos[learnerOrderId.index].userRating == columnOrderId.count}">text-bg-success</c:if>'
									>
										<c:choose>
											<c:when test="${empty column.name}">
												...
											</c:when>
											<c:otherwise>
												<c:out value="${column.name}" escapeXml="false" />	
											</c:otherwise>
										</c:choose>
									</button>
									<c:if test="${not columnOrderId.last and peerreview.rubricsInBetweenColumns}">
										<button type="button" onClick="javascript:rateRubricsCriteria(this, ${criteria.ratingCriteriaId}, ${ratingDto.itemId}, ${columnOrderId.count + 0.5})"
											class='col <c:if test="${criteriaDto.ratingDtos[learnerOrderId.index].userRating == (columnOrderId.count + 0.5)}">text-bg-success</c:if>'
										>
											<i><fmt:message key="label.rating.rubrics.in.between" /></i>
										</button>
									</c:if>
								</c:forEach>
							</div>
						</c:forEach>
				</div>
			</div>
		</div>
		
	</c:forEach>
</div>