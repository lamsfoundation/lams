<%@ include file="/common/taglibs.jsp"%>
	
<c:set var="sessionMapID" value="${param.sessionMapID}" />
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="mode" value="${sessionMap.mode}" />
<c:set var="imageGallery" value="${sessionMap.imageGallery}" />
<c:set var="finishedLock" value="${sessionMap.finishedLock}" />

<script type="text/javascript" src="<html:rewrite page='/includes/javascript/thickbox.js'/>"></script>	
<script type="text/javascript">
	$(document).ready(function(){
		$('#voting-form-checkbox').click(function() {
			$('#voting-form').ajaxSubmit( {
				success: afterRatingSubmit  // post-submit callback
			});
		});
	});

	// post-submit callback 
	function afterRatingSubmit(responseText, statusText)  {
		var votingFormLabel;
		if ($('#voting-form-checkbox').is(':checked')) {
			votingFormLabel = "<fmt:message key='label.learning.unvote'/>";					
				 
		} else {
			votingFormLabel = "<fmt:message key='label.learning.vote.here'/>";
		}
		$('#voting-form-label').text(votingFormLabel);
	}
</script>

<%--Comments area---------------------------------------%>
<div id="comments-area">
	<c:if test="${imageGallery.allowCommentImages}">

		<%@ include file="/common/messages.jsp"%>
	
		<div class="field-name">
			<fmt:message key="label.learning.comments" />
		</div>
		
		<c:forEach var="comment" items="${sessionMap.comments}">
		
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
	
		<c:if test="${mode != 'teacher' && (not finishedLock)}">
			<div >
				<html:form action="learning/addNewComment" method="post">
					<lams:STRUTS-textarea property="comment" rows="3" styleId="comment-textarea"/>		
			
					<html:button property="commentButton" onclick="javascript:addNewComment(${sessionMap.currentImage.uid}, document.getElementById('comment__lamshidden').value);" styleClass="button" styleId="comment-button">
						<fmt:message key="label.learning.add.comment" />
					</html:button>
				</html:form>					
			</div>
		</c:if>
				
	</c:if>
</div>

<c:if test="${(mode != 'teacher') && (imageGallery.allowRank || imageGallery.allowVote || imageGallery.allowShareImages)}">	
	<div id="extra-controls">
				
		<%--Ranking area---------------------------------------%>
	
		<c:if test="${imageGallery.allowRank}">
			<div class="extra-controls-inner">
				<lams:Rating ratingDtos="${sessionMap.ratingDtos}" disabled="${finishedLock}"/>
			</div>
			<br><br>
		</c:if>
		
		<div class="extra-controls-inner2">
				
			<%--Voting area--------------%>
		
			<c:if test="${imageGallery.allowVote}">
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
	</div>
</c:if>
