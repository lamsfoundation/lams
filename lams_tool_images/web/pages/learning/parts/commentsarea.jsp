<%@ include file="/common/taglibs.jsp"%>
	
<c:set var="sessionMapID" value="${param.sessionMapID}" />
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="imageGallery" value="${sessionMap.imageGallery}" />

<c:if test="${imageGallery.allowCommentImages}">

	<%@ include file="commentlist.jsp"%>

	<c:if test="${mode != 'teacher'}">
		<div class="field-name">
			<fmt:message key="label.learning.add.comment" />
		</div>
		

		<div >
			<html:form action="learning/addNewComment" method="post">
			<lams:STRUTS-textarea property="comment" rows="3" cols="75" styleId="comment_textarea" style="margin-right:10px;"/>		
		
				<html:button property="commentButton" onclick="javascript:addNewComment(${sessionMap.currentImage.uid}, document.getElementById('comment_textarea').value);" styleClass="button" style="vertical-align:bottom;">
					<fmt:message key="label.learning.post" />
				</html:button>
			</html:form>					
		</div>
		<br>
	</c:if>
				
</c:if>


