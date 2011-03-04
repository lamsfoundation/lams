<%@ include file="/common/taglibs.jsp"%>
<c:set var="messageId" value="${msgDto.message.uid}"/>

<c:if test="${sessionMap.allowRateMessages}">
	<div class="ratingStarsDiv">
		<div class="ratingStarsDisabled" data="${msgDto.averageRating}_${messageId}"></div>
		<div class="ratingStarsCaption">
		
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