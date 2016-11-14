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

<script type="text/javascript" src="${lams}includes/javascript/thickbox.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
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
		WARN_MIN_NUMBER_WORDS_LABEL = '<fmt:message key="warning.minimum.number.words"><fmt:param value="${sessionMap.commentsMinWordsLimit}"/></fmt:message>';
	</script>
	<script type="text/javascript" src="${lams}includes/javascript/rating.js"></script>
</c:if>
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
				<html:form action="learning/vote" method="post" styleId="voting-form">
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
				</html:form>							
			</c:if>
			
			<%--"Check for new", "Add new image" and "Delete" buttons---------------%>
				
			<c:if test="${imageGallery.allowShareImages}">
			
				<a href="#nogo" onclick="return checkNew()" class="btn btn-default" id="check-for-new-button"> 
					<fmt:message key="label.check.for.new" /> 
				</a>
							
				<c:if test="${not finishedLock}">
					<br>
					<a href="<html:rewrite page='/learning/newImageInit.do?sessionMapID='/>${sessionMapID}&KeepThis=true&TB_iframe=true&modal=true" 
							class="btn btn-default voffset10 thickbox" id="add-new-image-button">  
						<fmt:message key="label.learning.add.new.image" />
					</a>
				</c:if>
				
				<c:if test="${sessionMap.isAuthor}">
					<br>
					<a href="#nogo" onclick="return deleteImage(${sessionMap.currentImage.uid});" class="btn btn-default voffset10" id="delete-button"> 
						<fmt:message key="label.learning.delete.image" /> 
					</a>
				</c:if>
			</c:if>
		
	</div>
</c:if>
