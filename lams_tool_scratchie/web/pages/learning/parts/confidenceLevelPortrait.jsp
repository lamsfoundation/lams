<%@ include file="/common/taglibs.jsp"%>

<div class="c100 p${confidenceLevelDto.level}0 small" data-toggle="tooltip" data-placement="top" title="${confidenceLevelDto.userName}">
	<span>
		<c:choose>
			<c:when test="${confidenceLevelDto.portraitUuid == null}">
				<div class="portrait-generic-sm portrait-color-${confidenceLevelDto.userId % 7}"></div>
			</c:when>
			<c:otherwise>
				<img class="portrait-sm portrait-round" src="${lams}download/?uuid=${confidenceLevelDto.portraitUuid}&preferDownload=false&version=4" alt="">
			</c:otherwise>
		</c:choose>
	</span>
									
	<div class="slice">
		<div class="bar"></div>
		<div class="fill"></div>
	</div>
							
	<div class="confidence-level-percentage">
		<c:if test="${confidenceLevelDto.level != -1}">
			${confidenceLevelDto.level}<c:if test="${confidenceLevelDto.level != 0}">0</c:if>%
		</c:if>
	</div>
</div>