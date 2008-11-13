<%@ include file="/common/taglibs.jsp"%>
	
<c:set var="sessionMapID" value="${param.sessionMapID}" />
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="imageGallery" value="${sessionMap.imageGallery}" />
<c:set var="image" value="${sessionMap.currentImage}" />

<input type="hidden" name="imageUid" id="commentsArea_imageUid" value="${image.uid}"/>
<input type="hidden" name="imageDescription" id="commentsArea_description" value="${image.description}"/>
<input type="hidden" name="averageRating" id="commentsArea_averageRating" value="${image.averageRating}"/>
<input type="hidden" name="numberRatings" id="commentsArea_numberRatings" value="${image.numberRatings}"/>
<input type="hidden" name="currentRating" id="commentsArea_currentRating" value="${sessionMap.currentRating}"/>
<input type="hidden" name="votedImageUid" id="commentsArea_votedImageUid" value="${sessionMap.votedImageUid}"/>

<c:if test="${imageGallery.allowCommentImages}">

	<%@ include file="/common/messages.jsp"%>

	<%@ include file="commentlist.jsp"%>

	<c:if test="${mode != 'teacher'}">
		<div class="field-name">
			<fmt:message key="label.learning.add.comment" />
		</div>

		<div >
			<html:form action="learning/addNewComment" method="post">
				<lams:STRUTS-textarea property="comment" rows="3" cols="75" styleId="comment_textarea" style="margin-right:10px;"/>		
		
				<html:button property="commentButton" onclick="javascript:addNewComment(${sessionMap.currentImage.uid}, document.getElementById('comment__lamshidden').value);" styleClass="button" style="vertical-align:bottom;">
					<fmt:message key="label.learning.post" />
				</html:button>
			</html:form>					
		</div>
		<br>
	</c:if>
				
</c:if>


