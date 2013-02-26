<%@ include file="/common/taglibs.jsp"%>
<c:choose>
	<c:when test='${(mode == "teacher") || sessionMap.isUserItemAuthor || finishedLock}'>
		<c:set var="ratingStarsClass" value="rating-stars-disabled"/>
	</c:when>
	<c:otherwise>
		<c:set var="ratingStarsClass" value="rating-stars"/>
	</c:otherwise>
</c:choose>

<div class="rating-stars-div">
	<div class="${ratingStarsClass}" data-average="${item.averageRatingDto.rating}" data-id="${item.uid}"></div>
	
	<div class="rating-stars-caption">
		
		<fmt:message key="label.learning.number.of.votes" >
			<fmt:param>
				<span id="averageRating${item.uid}">
					<fmt:formatNumber value="${item.averageRatingDto.rating}" type="number" maxFractionDigits="1" />
				</span>
			</fmt:param>
			<fmt:param>
				<span id="numberOfVotes${item.uid}">${item.averageRatingDto.numberOfVotes}</span>
			</fmt:param>
		</fmt:message>
			
	</div>
</div>