<%@ include file="/common/taglibs.jsp"%>

<c:if test="${generalLearnerFlowDTO.allowRateAnswers == 'true'}">
	<div class="ratingStarsDiv">
		<div class="ratingStars" data="${userData.averageRating}_${responseUid}"></div>
		<div class="ratingStarsCaption">
		
			<fmt:message key="label.learning.number.of.votes" >
				<fmt:param>
					<span id="averageRating${responseUid}">
						<fmt:formatNumber value="${userData.averageRating}" type="number" maxFractionDigits="1" />
					</span>
				</fmt:param>
				<fmt:param>
					<span id="numberOfVotes${responseUid}">${userData.numberOfVotes}</span>
				</fmt:param>
			</fmt:message>
		</div>
	</div>
	
	<div class="afterRatingStarsDiv"></div>
</c:if>