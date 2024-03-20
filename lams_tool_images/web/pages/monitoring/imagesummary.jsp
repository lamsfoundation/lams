<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMapID" value="${param.sessionMapID}" />
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="imageGallery" value="${sessionMap.imageGallery}" />	

<c:set var="title"><fmt:message key="label.monitoring.imagesummary.image" /></c:set>
<lams:PageMonitor title="${title}" hideHeader="true">
	<link href="${lams}css/rating.css" rel="stylesheet" type="text/css">
		
	<script type="text/javascript">
		//vars for rating.js
		var AVG_RATING_LABEL = '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.average.rating"><fmt:param>@1@</fmt:param><fmt:param>@2@</fmt:param></fmt:message></spring:escapeBody>',
		YOUR_RATING_LABEL = '',
		IS_DISABLED =  true,
		COMMENTS_MIN_WORDS_LIMIT = 0,
		MAX_RATES = 0,
		MIN_RATES = 0,
		LAMS_URL = '${lams}',
		COUNT_RATED_ITEMS = 0,
		COMMENT_TEXTAREA_TIP_LABEL = '',
		WARN_COMMENTS_IS_BLANK_LABEL = '',
		WARN_MIN_NUMBER_WORDS_LABEL = '';
	</script>
	<lams:JSImport src="includes/javascript/rating.js" />
	<lams:JSImport src="includes/javascript/imageGallerycommon.js" relative="true" />
	<lams:JSImport src="includes/javascript/imageGalleryitem.js" relative="true" />
	<lams:JSImport src="includes/javascript/uploadImageLearning.js" relative="true" />
	
	<h1 class="fs-3 mb-3">
		${title}
	</h1>

	<c:set var="csrfToken"><csrf:token/></c:set>
	<form:form action="updateImage.do?${csrfToken}" method="post" modelAttribute="imageGalleryItemForm" id="imageGalleryItemForm">
		<form:hidden path="imageUid" />		
		<form:hidden path="sessionMapID" value="${sessionMapID}"/>
				
		<c:set var="mediumImagePath">
	   		<lams:WebAppURL />download/?uuid=${image.mediumFileDisplayUuid}&preferDownload=false
		</c:set>	
		<c:set var="title">
			<c:out value="${image.title}" escapeXml="true"/>
		</c:set>
		<img src="${mediumImagePath}" alt="${title}" title="${title}"/>
					
		<div class="my-3">
			<label for="file-title">
			   	<fmt:message key="label.authoring.basic.resource.title.input"/>
			</label>
			<input type="text" name="title" value="${imageGalleryItemForm.title}" class="form-control" id="file-title" tabindex="1"/>
		</div>
				
		<div class="mb-3">
			<label for="description">
				<fmt:message key="label.authoring.basic.resource.description.input" />
			</label>
		            	
			<lams:CKEditor id="description" value="${imageGalleryItemForm.description}" width="99%" 
					contentFolderID="${sessionMap.imageGalleryForm.contentFolderID}" />
		</div>
	</form:form>
			
	<c:if test="${imageGallery.allowRank}">
		<lams:Rating itemRatingDto="${itemRatingDto}" disabled="true"
				isDisplayOnly="true"
				maxRates="0" 
				countRatedItems="0" />
	</c:if>
			
	<c:if test="${imageGallery.allowVote}">
		<c:forEach var="groupSummary" items="${imageSummary}">
			<c:if test="${sessionMap.isGroupedActivity}">
				<h4>
					<fmt:message key="monitoring.label.group" />&nbsp;${groupSummary[0].sessionName}	
				</h4>
			</c:if>
				
			<div class="badge text-bg-warning mb-2">
				<fmt:message key="label.monitoring.number.votes" />: ${groupSummary[0].numberOfVotesForImage}
			</div>
				
			<table class="table table-striped">
				<tr>
					<th width="150px">
						<fmt:message key="label.monitoring.imagesummary.user" />
					</th>			
					<th style="padding-left:0px; text-align:center; width: 100px;">
						<fmt:message key="label.monitoring.imagesummary.voted.for.this.image" />
					</th>
				</tr>
				
				<c:forEach var="userImageContribution" items="${groupSummary}">
					<tr>
						<td>
							<c:out value="${userImageContribution.user.firstName} ${userImageContribution.user.lastName}" escapeXml="true"/>
						</td>
													
						<td style="padding-left:0px; text-align:center;">
							<c:choose>
								<c:when test="${userImageContribution.votedForThisImage}">
									<i class="fa fa-check text-success"></i>
								</c:when>
											
								<c:otherwise>
									<i class="fa fa-minus"></i>
								</c:otherwise>
							</c:choose>
						</td>						
					</tr>
				</c:forEach>
			</table>
		</c:forEach>
	</c:if>
			
	<div class="activity-bottom-buttons" id="uploadButtons">
		<button type="button" onclick="submitImageGalleryItem()" class="btn btn-primary">
			<i class="fa-regular fa-circle-check me-1"></i>
			<fmt:message key="label.monitoring.imagesummary.save" /> 
		</button>
		
		<button type="button" onclick="self.parent.tb_remove()" class="btn btn-secondary btn-icon-cancel me-2">
			<fmt:message key="label.cancel" /> 
		</button>
	</div>
</lams:PageMonitor>
