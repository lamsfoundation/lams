<%@ include file="/common/taglibs.jsp"%>

<c:if test="${(generalLearnerFlowDTO.allowRateAnswers == 'true') || content.allowRateAnswers}">
	<div class="rating-stars-div">
		<div class="rating-stars-disabled" data-average="${userData.averageRating}" data-id="${responseUid}"></div>
		<div class="rating-stars-caption">
		
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
	
	<div style="clear: both;"></div>
</c:if>