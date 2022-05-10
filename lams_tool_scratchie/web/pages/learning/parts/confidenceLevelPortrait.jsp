<%@ include file="/common/taglibs.jsp"%>

<c:set var="confidenceLevelString">
<c:choose>
	<c:when test="${confidenceLevelDto.type <= 1 && confidenceLevelDto.level == -1}">
	</c:when>
	
	<c:when test="${confidenceLevelDto.type == 1}">
		${confidenceLevelDto.level}<c:if test="${confidenceLevelDto.level != 0}">0</c:if>% 
	</c:when>
	
	<c:when test="${confidenceLevelDto.type == 2}">
		<c:choose>
			<c:when test="${confidenceLevelDto.level == 0}">
				<fmt:message key="label.not.confident" /> 
			</c:when>
			
			<c:when test="${confidenceLevelDto.level == 5}">
				<fmt:message key="label.confident" />
			</c:when>
			
			<c:when test="${confidenceLevelDto.level == 10}">
				<fmt:message key="label.very.confident" />
			</c:when>
		</c:choose>
	</c:when>
	
	<c:when test="${confidenceLevelDto.type == 3}">
		<c:choose>
			<c:when test="${confidenceLevelDto.level == 0}">
				<fmt:message key="label.not.sure" /> 
			</c:when>
			
			<c:when test="${confidenceLevelDto.level == 5}">
				<fmt:message key="label.sure" />
			</c:when>
			
			<c:when test="${confidenceLevelDto.level == 10}">
				<fmt:message key="label.very.sure" />
			</c:when>
		</c:choose>
	</c:when>
	
	<c:otherwise>
		no type set 
	</c:otherwise>
</c:choose>
</c:set>

<div class="c100 p${confidenceLevelDto.level}0 small" data-toggle="tooltip" data-placement="top" title="${confidenceLevelsAnonymous ? '' : confidenceLevelDto.userName}">
	<span>
		<c:choose>
			<c:when test="${confidenceLevelsAnonymous}">
				<div class="portrait-generic-sm"></div>
			</c:when>
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
		${confidenceLevelString}
	</div>
</div>