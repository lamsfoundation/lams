<%@ include file="/common/taglibs.jsp"%>
	
<c:set var="commentsSessionMapID" value="${param.sessionMapID}" />
<c:set var="commentsSessionMap" value="${sessionScope[commentsSessionMapID]}" />
<c:set var="commentsMode" value="${commentsSessionMap.mode}" />
<c:set var="commentsImageGallery" value="${commentsSessionMap.imageGallery}" />
<c:set var="commentsImage" value="${commentsSessionMap.currentImage}" />
<c:set var="commentsFinishedLock" value="${commentsSessionMap.finishedLock}" />

<input type="hidden" name="imageUid" id="commentsArea_imageUid" value="${commentsImage.uid}"/>
<input type="hidden" name="title" id="commentsArea_title" value="<c:out value='${commentsImage.title}' />"/>
<input type="hidden" name="originalImageWidth" id="commentsArea_originalImageWidth" value="${commentsImage.originalImageWidth}"/>
<input type="hidden" name="originalImageHeight" id="commentsArea_originalImageHeight" value="${commentsImage.originalImageHeight}"/>
<input type="hidden" name="originalFileUuid" id="commentsArea_originalFileUuid" value="${commentsImage.originalFileUuid}"/>
<input type="hidden" name="averageRating" id="commentsArea_averageRating" value="${commentsSessionMap.averageRating}"/>
<input type="hidden" name="numberRatings" id="commentsArea_numberRatings" value="${commentsSessionMap.numberRatings}"/>
<input type="hidden" name="currentRating" id="commentsArea_currentRating" value="${commentsSessionMap.currentRating}"/>
<input type="hidden" name="isVoted" id="commentsArea_isVoted" value="${commentsSessionMap.isVoted}"/>
<input type="hidden" name="isAuthor" id="commentsArea_isAuthor" value="${commentsSessionMap.isAuthor}"/>

<c:if test="${commentsImageGallery.allowCommentImages && (not empty commentsSessionMap.imageGalleryList)}">

	<%@ include file="/common/messages.jsp"%>

			<div class="field-name">
				<fmt:message key="label.learning.comments" />
			</div>
	
	<c:forEach var="comment" items="${commentsSessionMap.comments}">
	
		<div>
			<table cellspacing="0" class="forum">
				<tr >
					<th >
						<fmt:message key="label.learning.by" />
						<c:set var="author" value="${comment.createBy.firstName} ${comment.createBy.lastName}" />
						<c:if test="${empty author}">
							<c:set var="author">
								<fmt:message key="label.default.user.name" />
							</c:set>
						</c:if>
						<c:out value="${author}" escapeXml="true" /> - <lams:Date value="${comment.createDate}" />
					</th>
				</tr>
					
				<tr>
					<td class="posted-by">
					</td>
				</tr>
		
				<tr>
					<td>
						<c:out value="${comment.comment}" escapeXml="false" />
					</td>
				</tr>
					
			</table>
		</div>
			
	</c:forEach>

	<c:if test="${commentsMode != 'teacher' && (not commentsFinishedLock)}">
		<div >
			<html:form action="learning/addNewComment" method="post">
				<lams:STRUTS-textarea property="comment" rows="3" styleId="comment-textarea"/>		
		
				<html:button property="commentButton" onclick="javascript:addNewComment(${commentsSessionMap.currentImage.uid}, document.getElementById('comment__lamshidden').value);" styleClass="button" styleId="comment-button">
					<fmt:message key="label.learning.add.comment" />
				</html:button>
			</html:form>					
		</div>
	</c:if>
				
</c:if>
