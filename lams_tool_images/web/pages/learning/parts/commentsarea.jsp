<%@ include file="/common/taglibs.jsp"%>

<c:if test="${not empty param.sessionMapID}">
	<c:set var="sessionMapID" value="${param.sessionMapID}" />
</c:if>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="mode" value="${sessionMap.mode}" />
<c:set var="imageGallery" value="${sessionMap.imageGallery}" />
<c:set var="finishedLock" value="${sessionMap.finishedLock}" />
<c:set var="isImageSelected" value="${not empty sessionMap.currentImage}" />

<script type="text/javascript" src="<html:rewrite page='/includes/javascript/thickbox.js'/>"></script>	
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
			
				<lams:Rating ratingDtos="${sessionMap.ratingDtos}" disabled="${finishedLock}"
						maxRates="${imageGallery.maximumRates}" minRates="${imageGallery.minimumRates}" 
						countRatedImages="${sessionMap.countRatedImages}" />
			<br><br>
		</c:if>

	<div id="extra-controls">
				
			<%--Voting area--------------%>
		
			<c:if test="${imageGallery.allowVote && isImageSelected}">
				<html:form action="learning/vote" method="post" styleId="voting-form">
					<input type="hidden" name="sessionMapID" value="${sessionMapID}"/>
					<input type="hidden" name="imageUid" value="${sessionMap.currentImage.uid}"/>
								
					<p class="small-space-top" style="margin-right: 0px">
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
			
				<a href="#nogo" onclick="return checkNew()" class="button" id="check-for-new-button"> 
					<fmt:message key="label.check.for.new" /> 
				</a>
							
				<c:if test="${not finishedLock}">
					<br><br>
					<a href="<html:rewrite page='/learning/newImageInit.do?sessionMapID='/>${sessionMapID}&KeepThis=true&TB_iframe=true&height=540&width=480&modal=true" class="button thickbox" id="add-new-image-button">  
						<fmt:message key="label.learning.add.new.image" />
					</a>
				</c:if>
				
				<c:if test="${sessionMap.isAuthor}">
					<br><br>
					<a href="#nogo" onclick="return deleteImage(${sessionMap.currentImage.uid});" class="button" id="delete-button"> 
						<fmt:message key="label.learning.delete.image" /> 
					</a>
				</c:if>
			</c:if>
		
	</div>
</c:if>
