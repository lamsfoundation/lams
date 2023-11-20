<style>
	.rubrics-table .row:not(.rubrics-table-header) .col:not(:first-child) {
		cursor: pointer;
		text-align: center;
	}
	
	.rubrics-table .row .col:first-child {
		font-weight: bold;
	}

	.rubrics-table-header {
		font-style: normal;
		text-align: center;
	}
	
	.rubrics-table .col.text-bg-success {
		font-weight: bold;
		line-height: 40px;
		margin: -0.5em 0;
	}
	
	.column-header-span {
		font-style: italic;
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
</style>

<script>
	var rubricsRequireRatings = ${peerreview.rubricsRequireRatings};
	
	$(document).ready(function(){
		$('#rubrics-row-cards').closest('.container').addClass('container-fluid').removeClass('container');
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
        			alert('<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.rating.rubrics.error.rate" /></spring:escapeBody>');
    				return;
    			}
    			let row = $(cell).parent(),
    				columnIndex = row.children().index(cell);
    			$(cell).closest('.ltable').children('.row').each(function(){
        			$(this).children('.col').eq(columnIndex).removeClass('text-bg-success');
        		});
        		$(cell).addClass('text-bg-success');

        		if (rubricsRequireRatings) {
					$('#finishButton').prop('disabled', isRatingMissing());
            	}
    		},
			onError : function(){
    			alert('<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.rating.rubrics.error.rate" /></spring:escapeBody>');
 			}
    	});
	}

	function expandAllRubricsCards(){
		var isCollapsing = $("#expand-all-button").hasClass("collapsed") ? "show" : "hide";
		$("#expand-all-button").toggleClass("collapsed");
		$('.collapse').collapse(isCollapsing);
	}

	function isRatingMissing() {
		let criterionTables = $('.rubrics-table');
		return $('.text-bg-success', criterionTables).length !== $('.rubrics-table-header .col', criterionTables).length 
			- criterionTables.length;
	}
</script>

<c:if test="${peerreview.rubricsRequireRatings}">
	<lams:Alert5 type="info" id="info-assign-all-rubrics-pivot" close="true">
		<fmt:message key="label.rating.rubrics.require.ratings.tooltip.learner" />
	</lams:Alert5>
</c:if>

<div class="collapsable-icon-left">
	<button id="expand-all-button" class="btn btn-secondary collapsed" data-bs-toggle="collapse" onClick="javascript:expandAllRubricsCards()">
		<fmt:message key="label.rating.rubrics.expand.all" />
	</button>
</div>
<div class="clearfix"></div>

<div id="rubrics-row-cards">
	<%-- It is sufficient to take user names and columns from the first row/criterion --%>
	<c:set var="exampleRatings" value="${criteriaRatings.ratingDtos}" />
	<c:set var="columnHeaders" value="${criteriaRatings.ratingCriteria.rubricsColumnHeaders}" />
	<c:set var="columnHeaderCount" value="${fn:length(columnHeaders)}" />
	
	<c:forEach var="criteriaDto" items="${criteriaRatings.criteriaGroup}">
		<c:set var="criteria" value="${criteriaDto.ratingCriteria}" />
		
	    <div class="lcard card rubrics-row-card">
	       <div class="card-header collapsable-icon-left" id="heading${criteria.ratingCriteriaId}">
	       		<button type="button" class="btn btn-secondary-darker no-shadow collapsed" data-bs-toggle="collapse" data-bs-target="#collapse${criteria.ratingCriteriaId}" 
						aria-expanded="false" aria-controls="collapse${criteria.ratingCriteriaId}" data-parent="#rubrics-rows-cards"
				>
					<%-- Criterion "row" --%>
					<c:out value="${criteria.title}" escapeXml="false" />
				</button>
	       </div>
	       
	       <div id="collapse${criteria.ratingCriteriaId}" class="collapse" aria-labelledby="heading${criteria.ratingCriteriaId}">
				<div class="ltable rubrics-table">
					<div class="row rubrics-table-header m-0">
						<%-- Learner profile pictures and names --%>
						<div class="col"></div>
						<c:forEach var="ratingDto" items="${exampleRatings}" varStatus="learnerOrderId">
							<div class="col">
								<lams:Portrait userId="${ratingDto.itemId}" hover="false" /><br>
								<strong><c:out value="${ratingDto.itemDescription}" escapeXml="false"/></strong>
							</div>
						</c:forEach>
					</div>
						
							<c:forEach items="${columnHeaders}" varStatus="columnStatus">
								<c:set var="columnHeader" value="${columnHeaders[columnHeaderCount - columnStatus.count]}" />
								<div class="row align-items-center m-0">
									<div class="col">
										<%-- Criterion "column" --%>
										<c:if test="${not empty columnHeader}">
										<span class="column-header-span"><c:out value="${columnHeader}" escapeXml="false"/></span><br>
										</c:if>
										<%-- Criterion "cell" --%>
										<c:out value="${criteria.rubricsColumns[columnHeaderCount - columnStatus.count].name}" escapeXml="false" />	
									</div>
									<c:forEach var="ratingDto" items="${exampleRatings}" varStatus="learnerOrderId">
										
										<button type="button" onClick="javascript:rateRubricsCriteria(this, ${criteria.ratingCriteriaId}, ${ratingDto.itemId}, ${columnHeaderCount - columnStatus.index})"
											<%-- Columns are ordered from 1 to 5, so rate value is also the order ID of the column --%>
											 class='col <c:if test="${criteriaDto.ratingDtos[learnerOrderId.index].userRating == (columnHeaderCount - columnStatus.index)}">text-bg-success</c:if>'
										>
											${columnHeaderCount - columnStatus.index}
										</button>
									</c:forEach>
								</div>
								
								<c:if test="${not columnStatus.last and peerreview.rubricsInBetweenColumns}">
									<div class="row align-items-center m-0">
										<div class="col">
											<i><fmt:message key="label.rating.rubrics.in.between" /></i>
										</div>
										<c:forEach var="ratingDto" items="${exampleRatings}" varStatus="learnerOrderId">
											<button type="button" onClick="javascript:rateRubricsCriteria(this, ${criteria.ratingCriteriaId}, ${ratingDto.itemId}, ${columnHeaderCount - columnStatus.index - 0.5})"
												 class='col <c:if test="${criteriaDto.ratingDtos[learnerOrderId.index].userRating == (columnHeaderCount - columnStatus.index - 0.5)}">text-bg-success</c:if>'
											>
												${columnHeaderCount - columnStatus.index - 0.5}
											</button>
										</c:forEach>
									</div>
								</c:if>
							</c:forEach>
				</div>
			</div>
		</div>
		
	</c:forEach>
</div>
