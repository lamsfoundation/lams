<%@ include file="/common/taglibs.jsp"%>

<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="comments" value="${sessionMap.item.groupComments}" />
<c:set var="mode" value="${sessionMap.mode}" />
<c:set var="item" value="${sessionMap.item}" />
<c:set var="finishedLock" value="${sessionMap.finishedLock}" />

<%@ include file="/common/messages.jsp"%>

<table cellspacing="0" class="alternative-color" id="comments-table">
	<c:forEach var="comment" items="${comments}">
		
			<tr>
				<td id="comment${comment.uid}" <c:if test="${comment.hidden}"> class="item-hidden" </c:if>>
					<c:out value="${comment.comment}" escapeXml="false" />
					
					<div class="comment-by">
						<fmt:message key="label.learning.by" />
						<c:out value="${comment.createBy.firstName} ${comment.createBy.lastName}" escapeXml="true"/> 
								-				
						<lams:Date value="${comment.createDate}" />
					
					</div>
				</td>
			</tr>
		
	</c:forEach>
</table>

<c:if test="${(mode != 'teacher') && !finishedLock}">
	<div >
		<lams:STRUTS-textarea property="comment" rows="3" cols="53" styleId="comment-textarea" style="margin-right:10px;" value=""/>		
		
		<div>
			<html:button property="commentButton" onclick="javascript:addNewComment(document.getElementById('comment__lamshidden').value);">
				<fmt:message key="label.learning.post.comment" />
			</html:button>
		</div>
	</div>
</c:if>
