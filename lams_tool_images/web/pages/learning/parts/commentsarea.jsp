<%@ include file="/common/taglibs.jsp"%>
	
<c:set var="commentsSessionMapID" value="${param.sessionMapID}" />
<c:set var="commentsSessionMap" value="${sessionScope[commentsSessionMapID]}" />
<c:set var="commentsMode" value="${commentsSessionMap.mode}" />
<c:set var="commentsImageGallery" value="${commentsSessionMap.imageGallery}" />
<c:set var="commentsImage" value="${commentsSessionMap.currentImage}" />
<c:set var="commentsFinishedLock" value="${commentsSessionMap.finishedLock}" />
<c:if test="${!commentsImage.createByAuthor && (commentsImage.createBy != null)}">
	<c:set var="commentsAddedBy" >
		<fmt:message key="label.learning.added.by" /> 
		<br>
			<c:out value="${commentsImage.createBy.firstName} ${commentsImage.createBy.lastName}" escapeXml="true"/>
		<br>
	</c:set>
</c:if>

<input type="hidden" name="imageUid" id="commentsArea_imageUid" value="${commentsImage.uid}"/>
<input type="hidden" name="title" id="commentsArea_title" value="<c:out value='${commentsImage.title}' />"/>
<input type="hidden" name="originalImageWidth" id="commentsArea_originalImageWidth" value="${commentsImage.originalImageWidth}"/>
<input type="hidden" name="originalImageHeight" id="commentsArea_originalImageHeight" value="${commentsImage.originalImageHeight}"/>
<input type="hidden" name="originalFileUuid" id="commentsArea_originalFileUuid" value="${commentsImage.originalFileUuid}"/>
<input type="hidden" name="imageDescription" id="commentsArea_description" value="<c:out value='${commentsImage.description}' />"/>
<input type="hidden" name="averageRating" id="commentsArea_averageRating" value="${commentsSessionMap.averageRating}"/>
<input type="hidden" name="numberRatings" id="commentsArea_numberRatings" value="${commentsSessionMap.numberRatings}"/>
<input type="hidden" name="currentRating" id="commentsArea_currentRating" value="${commentsSessionMap.currentRating}"/>
<input type="hidden" name="isVoted" id="commentsArea_isVoted" value="${commentsSessionMap.isVoted}"/>
<input type="hidden" name="isAuthor" id="commentsArea_isAuthor" value="${commentsSessionMap.isAuthor}"/>
<input type="hidden" name="addedBy" id="commentsArea_addedBy" value="${commentsAddedBy}"/>


<c:if test="${commentsImageGallery.allowCommentImages && (not empty commentsSessionMap.imageGalleryList)}">

	<%@ include file="/common/messages.jsp"%>

	<%@ include file="commentlist.jsp"%>

	<c:if test="${commentsMode != 'teacher' && (not commentsFinishedLock)}">
		<div class="field-name">
			<fmt:message key="label.learning.add.comment" />
		</div>

		<div >
			<html:form action="learning/addNewComment" method="post">
				<lams:STRUTS-textarea property="comment" rows="3" cols="75" styleId="comment_textarea" style="margin-right:10px;"/>		
		
				<html:button property="commentButton" onclick="javascript:addNewComment(${commentsSessionMap.currentImage.uid}, document.getElementById('comment__lamshidden').value);" styleClass="button" style="vertical-align:bottom;">
					<fmt:message key="label.learning.post" />
				</html:button>
			</html:form>					
		</div>
		<br>
	</c:if>
				
</c:if>


