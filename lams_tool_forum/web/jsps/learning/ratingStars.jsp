<%@ include file="/common/taglibs.jsp"%>
<c:set var="messageId" value="${msgDto.message.uid}"/>
<c:choose>
	<c:when test='${(sessionMap.mode == "teacher") || msgDto.isAuthor || sessionMap.finishedLock || sessionMap.noMoreRatings}'>
		<c:set var="ratingStarsClass" value="rating-stars-disabled"/>
	</c:when>
	<c:otherwise>
		<c:set var="ratingStarsClass" value="rating-stars"/>
	</c:otherwise>
</c:choose>

<c:if test="${sessionMap.allowRateMessages}">
	<div style="display: inline-block">
		<div class="${ratingStarsClass} rating-stars-new" data-average="${msgDto.averageRating}" data-id="${messageId}"></div>
		
		<div class="rating-stars-caption">
			<fmt:message key="label.learning.number.of.votes" >
				<fmt:param>
					<span id="averageRating${messageId}">
						<fmt:formatNumber value="${msgDto.averageRating}" type="number" maxFractionDigits="1" />
					</span>
				</fmt:param>
				<fmt:param>
					<span id="numberOfVotes${messageId}">${msgDto.numberOfVotes}</span>
				</fmt:param>
			</fmt:message>
		</div>
	</div>
</c:if>