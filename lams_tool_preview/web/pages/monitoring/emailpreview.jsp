<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="emailPreviewDTO" value="${sessionMap.emailPreviewDTO}"/>

<div class="lcard mt-4">
	<div class="card-header">
		<fmt:message key="label.email.preview"/>
	</div>
	
	<div class="card-body">
		<div id="emailHTML">${emailPreviewDTO.emailHTML}</div>
		
		<div class="float-end mb-3">
			<button type="button" onclick="closeResultsForLearner(${emailPreviewDTO.toolSessionId})" 
					class="btn btn-secondary btn-sm btn-disable-on-submit">
				<i class="fa-regular fa-eye-slash me-1"></i>
				<fmt:message key="label.hide"/>
			</button>
			<button type="button" id="sendEmailNowButton" onclick="sendResultsForLearner(${emailPreviewDTO.toolSessionId}, ${emailPreviewDTO.learnerUserId}, ${emailPreviewDTO.dateTimeStamp})" 
					class="btn btn-secondary btn-sm ms-2 btn-disable-on-submit">
				<i class="fa-regular fa-sm fa-envelope me-1"></i>
				<fmt:message key="button.email.results"/>
			</button>
		</div>
	</div>
</div>
