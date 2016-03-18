<%@ include file="/common/taglibs.jsp"%>

<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="comments" value="${sessionMap.item.groupComments}" />
<c:set var="mode" value="${sessionMap.mode}" />
<c:set var="item" value="${sessionMap.item}" />
<c:set var="finishedLock" value="${sessionMap.finishedLock}" />
<c:set var="isGroupMonitoring" value="${sessionMap.isGroupMonitoring}" />

<%@ include file="/common/messages.jsp"%>

<div class="table-responsive">
	<table id="comments-table" class="table table-hover table-condensed">
		<c:if test="${not empty comments}">
			<thead>
				<tr>
					<th>
						<fmt:message key="label.learning.comments" />
					</th>
				</tr>
			</thead>
		</c:if>
		
		<tbody>
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
						
						<c:if test="${isGroupMonitoring}">
							<div class="pull-right comment-hide-management">
								
								<c:choose>
									<c:when test="${comment.hidden}">
										<span class="comment-by-text">
											<fmt:message key="label.comment.is.hidden" />
										</span>
												
										<a href="#nogo" onclick="return hideComment(${comment.uid}, false);">
									       	<fmt:message key="label.unhide" />
								    	</a>
									</c:when>
									<c:otherwise>
										<a href="#nogo" onclick="return hideComment(${comment.uid}, true);">
									       	<fmt:message key="label.hide" />
									   	</a>
									</c:otherwise>
								</c:choose>
							
							</div>
						</c:if>
					</td>
				</tr>
			
			</c:forEach>
		</tbody>
	</table>
</div>

<c:if test="${isGroupMonitoring || (mode != 'teacher') && !finishedLock}">
	<div id="comment-textarea">
		<lams:STRUTS-textarea property="comment" rows="3" value="" styleClass="form-control"/>		
		
		<div class="pull-right">
			<html:button property="commentButton" onclick="javascript:addNewComment(document.getElementById('comment__lamshidden').value);" styleClass="btn btn-default" style="vertical-align:bottom;">
				<fmt:message key="label.learning.post.comment" />
			</html:button>
		</div>
	</div>
</c:if>
