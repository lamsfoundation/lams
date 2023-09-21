<script type="text/javascript">	
		var currentMark = 0;
		
		$(document).ready(function(){
			updateMark();
			$(".mark-hedging-select").on('change keydown keypress keyup paste', updateMark);
		});

		function updateMark(){
			var mark = 0;
			$(".mark-hedging-select option:selected").each(function() {
				mark = mark + +$(this).val();
			});
			currentMark = mark;
			$("#totalMark").html(currentMark);
			
			if ( currentMark == ${criteriaRatings.ratingCriteria.maxRating} ) {
				showButtons();
			} else {
				hideButtons();
			}

		} 		
				
		function submitEntry(next){
			hideButtons();
			updateMark();

			if (  currentMark == ${criteriaRatings.ratingCriteria.maxRating} ) {
				$("#next").val(next);

				var finishedLock = '${finishedLock}';
				<c:if test="${criteriaRatings.ratingCriteria.commentsEnabled && ! finishedLock}">
				if ( validateJustification() )
				</c:if>
				{				
				$("#editForm").submit();
				}
			} else {
				alert('<spring:escapeBody javaScriptEscape="true"><fmt:message key="error.assign.marks"><fmt:param>${criteriaRatings.ratingCriteria.maxRating}</fmt:param></fmt:message></spring:escapeBody>');
			}
		}
		
		<c:if test="${criteriaRatings.ratingCriteria.commentsEnabled && ! finishedLock}">
		function validateJustification() {
			//word count limit
			var wordLimit = ${criteriaRatings.ratingCriteria.commentsMinWordsLimit};
			if (wordLimit != 0) { 
				var justify = document.getElementById("justify"),
					value =  $("#justify").val().trim(),
					wordCount = value ? (value.replace(/['";:,.?\-!]+/g, '').match(/\S+/g) || []).length : 0;
				    
			    if(wordCount < wordLimit){
			    	var alertMessage = '<spring:escapeBody javaScriptEscape="true"><fmt:message key="warning.minimum.number.words"><fmt:param value="${criteriaRatings.ratingCriteria.commentsMinWordsLimit}"/></fmt:message></spring:escapeBody>';
					alert( alertMessage.replace("{1}", wordCount));
					return false;
				}
			}
			
			return true;
		} 
		</c:if>
		
		function cancel() {
			document.location.href='<c:url value="/learning/refresh.do?sessionMapID=${sessionMapID}"/>';
		}
</script>

<c:if test="${notcomplete}">
	<lams:Alert5 type="danger" id="warn-assign-more" close="true">
		<fmt:message key="error.assign.marks">
			<fmt:param>${criteriaRatings.ratingCriteria.maxRating}</fmt:param>
		</fmt:message>
	</lams:Alert5>
</c:if>

<lams:Alert5 type="info" id="assign-mark-info" close="false">
	<fmt:message key="label.assign.marks">
		<fmt:param>${criteriaRatings.ratingCriteria.maxRating}</fmt:param>
		<fmt:param>
			<span id="totalMark">0</span>
		</fmt:param>
	</fmt:message>
</lams:Alert5>

<form action="<c:url value="/learning/submitRankingHedging.do?"/>" method="post" id="editForm">
	<input type="hidden" name="sessionMapID" value="${sessionMapID}"/>
	<input type="hidden" name="toolContentId" value="${toolContentId}"/>
	<input type="hidden" name="criteriaId" value="${criteriaRatings.ratingCriteria.ratingCriteriaId}"/>
	<input type="hidden" name="next" id="next" value=""/>
		
	<div class="card lcard">
		<div class="card-header text-bg-secondary">
			<c:out value="${criteriaRatings.ratingCriteria.title}" escapeXml="true" />
		</div>

		<div class="table-responsive">
			<div class="div-hover">
				<c:forEach var="ratingDto" items="${criteriaRatings.ratingDtos}">
				<div class="row">
					<div class="col">
						<lams:Portrait userId="${ratingDto.itemId}"/>
						<span class="portrait-sm-lineheight ms-2">${ratingDto.itemDescription}</span>
					</div>
					
					<div style="width: 100px;">
						<c:choose>
						<c:when test="${finishedLock}">
							${ratingDto.userRating}
						</c:when>
						<c:otherwise>
							<select name="mark${ratingDto.itemId}" class="mark-hedging-select form-select">
								<c:if test="${isEditingDisabled || question.responseSubmitted}">disabled="disabled"</c:if>				
								<c:forEach var="i" begin="0" end="${criteriaRatings.ratingCriteria.maxRating}">
									<option
										<c:if test="${ratingDto.userRating == i}">selected="selected"</c:if>						
									>${i}</option>
								</c:forEach>
							</select>
						</c:otherwise>
						</c:choose>
					</div>
				</div>
				</c:forEach>
			</div>
		</div>

		<c:if test="${criteriaRatings.ratingCriteria.commentsEnabled}">
			<div class="m-3">
				<div>
					<label for="justify" class="fst-italic">
						<fmt:message key="label.justify.hedging.marks" />
					</label>
				</div>
				<c:choose>
					<c:when test="${finishedLock}">
						<span>${criteriaRatings.justificationComment}</span>
					</c:when>
					<c:otherwise>
						<textarea id="justify" name="justify" rows="4" cols="60"
							class="mark-hedging-select form-control" onblur="updateMark()">${criteriaRatings.justificationComment}</textarea>
					</c:otherwise>
				</c:choose>
			</div>
		</c:if>
	</div>
</form>
		
