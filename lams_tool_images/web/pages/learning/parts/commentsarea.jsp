<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL/></c:set>

<c:if test="${not empty param.sessionMapID}">
	<c:set var="sessionMapID" value="${param.sessionMapID}" />
</c:if>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="mode" value="${sessionMap.mode}" />
<c:set var="imageGallery" value="${sessionMap.imageGallery}" />
<c:set var="finishedLock" value="${sessionMap.finishedLock}" />
<c:set var="isImageSelected" value="${not empty sessionMap.currentImage}" />
<c:set var="toolSessionID" value="${sessionMap.toolSessionID}" />

<lams:JSImport src="includes/javascript/common.js" />
<c:if test="${isImageSelected}">
	<script type="text/javascript">
		//var for jquery.jRating.js
		var pathToImageFolder = "${lams}images/css/";
		
		//vars for rating.js
		var AVG_RATING_LABEL = '<fmt:message key="label.average.rating"><fmt:param>@1@</fmt:param><fmt:param>@2@</fmt:param></fmt:message>',
		YOUR_RATING_LABEL = '<fmt:message key="label.your.rating"><fmt:param>@1@</fmt:param><fmt:param>@2@</fmt:param><fmt:param>@3@</fmt:param></fmt:message>',
		COMMENTS_MIN_WORDS_LIMIT = ${sessionMap.commentsMinWordsLimit},
		MAX_RATES = ${imageGallery.maximumRates},
		MIN_RATES = ${imageGallery.minimumRates},
		LAMS_URL = '${lams}',
		COUNT_RATED_ITEMS = ${sessionMap.countRatedItems},
		COMMENT_TEXTAREA_TIP_LABEL = '<fmt:message key="label.comment.textarea.tip"/>',
		WARN_COMMENTS_IS_BLANK_LABEL = '<fmt:message key="error.resource.image.comment.blank"/>',
		WARN_MIN_NUMBER_WORDS_LABEL = '<fmt:message key="warning.minimum.number.words"><fmt:param value="${sessionMap.commentsMinWordsLimit}"/></fmt:message>',
		ALLOW_RERATE = false,
		SESSION_ID = ${toolSessionID};
	</script>
	<lams:JSImport src="includes/javascript/rating.js" />
</c:if>
<lams:JSImport src="includes/javascript/uploadImageLearning.js" relative="true" />
<script type="text/javascript" src="${lams}includes/javascript/jquery.jRating.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		$('#voting-form-checkbox').click(function() {
			$('#voting-form').ajaxSubmit( {
				success: afterVotingSubmit  // post-submit callback
			});
		});
	});

	// post-submit callback 
	function afterVotingSubmit(responseText, statusText)  {
		var votingFormLabel;
		if ($('#voting-form-checkbox').is(':checked')) {
			votingFormLabel = "<fmt:message key='label.learning.unvote'/>";					
				 
		} else {
			votingFormLabel = "<fmt:message key='label.learning.vote.here'/>";
		}
		$('#voting-form-label').text(votingFormLabel);
	}
</script>

<c:if test="${(mode != 'teacher') && (imageGallery.allowRank || imageGallery.allowVote || imageGallery.allowShareImages)}">

	<%--Ranking area---------------------------------------%>
	
	<c:if test="${imageGallery.allowRank && isImageSelected}">
		
		<lams:Rating itemRatingDto="${sessionMap.itemRatingDto}" disabled="${finishedLock}" isItemAuthoredByUser="${sessionMap.isAuthor}"
				maxRates="${imageGallery.maximumRates}" countRatedItems="${sessionMap.countRatedItems}"
				minNumberWordsLabel="label.minimum.number.words" />
		<br><br>
	</c:if>

	<div id="extra-controls">
				
			<%--Voting area--------------%>
		
			<c:if test="${imageGallery.allowVote && isImageSelected}">
				<form:form action="vote.do" method="post" modelAttribute="imageRatingForm" id="voting-form">
					<input type="hidden" name="sessionMapID" value="${sessionMapID}"/>
					<input type="hidden" name="imageUid" value="${sessionMap.currentImage.uid}"/>
								
					<p class="voffset5" style="margin-right: 0px">
						<input type="checkbox" name="vote" class="noBorder" id="voting-form-checkbox" 
								<c:if test="${finishedLock}">disabled="disabled"</c:if>	
								<c:if test="${sessionMap.isVoted}">checked="checked"</c:if>	
						/>
						<label for="voting-form-checkbox" id="voting-form-label">
							<c:choose>
								<c:when test="${sessionMap.isVoted}">
									<fmt:message key='label.learning.unvote'/>
								</c:when>
								<c:otherwise>
									<fmt:message key='label.learning.vote.here'/>
								</c:otherwise>
							</c:choose>
						</label>
					</p>
				</form:form>							
			</c:if>
			
			<%--"Check for new", "Add new image" and "Delete" buttons---------------%>
				
			<div id="manage-image-buttons" class="btn-group" role="group">	
				<c:if test="${imageGallery.allowShareImages}">
					<button onclick="return checkNew()" class="btn btn-sm btn-default" id="check-for-new-button"> 
						<i class="fa fa-refresh"></i> <fmt:message key="label.check.for.new" /> 
					</button>
								
					<c:if test="${not finishedLock}">
						<button onclick="javascript:newImageInit('<lams:WebAppURL />authoring/newImageInit.do?sessionMapID=${sessionMapID}&saveUsingLearningAction=true');"
								class="btn btn-default btn-sm" id="add-new-image-button">  
							<i class="fa fa-upload"></i> <fmt:message key="label.learning.add.new.image" />
						</button>
					</c:if>
					
					<c:if test="${sessionMap.isAuthor}">
						<button href="#nogo" onclick="return deleteImage(${sessionMap.currentImage.uid});" class="btn btn-default btn-sm" id="delete-button"> 
							<i class="fa fa-trash"></i> <fmt:message key="label.learning.delete.image" /> 
						</button>
					</c:if>
				</c:if>
			</div>		
	</div>
							
	<c:if test="${imageGallery.allowShareImages && !finishedLock}">
		<div id="new-image-input-area" class="voffset20"></div>
	</c:if>	
</c:if>