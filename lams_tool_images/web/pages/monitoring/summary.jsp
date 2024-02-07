<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="summaryList" value="${sessionMap.summaryList}"/>
<c:set var="lams"><lams:LAMSURL /></c:set>
<script type="text/javascript" src="${lams}/includes/javascript/portrait5.js" ></script>

<script type="text/javascript">
$(document).ready(function(){
	initializePortraitPopover('${lams}');
});
</script>

<div class="panel">
	<h4>
	  <c:out value="${sessionMap.imageGallery.title}" escapeXml="true"/>
	</h4>
	
	<div class="instructions voffset5">
	  <c:out value="${sessionMap.imageGallery.instructions}" escapeXml="false"/>
	</div>
	
	<c:if test="${empty summaryList}">
		<lams:Alert type="info" id="no-session-summary" close="false">
			<fmt:message key="message.monitoring.summary.no.session" />
		</lams:Alert>
	</c:if>
</div>

<c:if test="${sessionMap.isGroupedActivity}">
	<div class="panel-group" id="accordionSessions" role="tablist" aria-multiselectable="true"> 
</c:if>

<c:forEach var="group" items="${summaryList}" varStatus="status">

	<c:if test="${sessionMap.isGroupedActivity}">	
	    <div class="panel panel-default" >
	        <div class="panel-heading" id="heading${group[0].sessionId}">
	        	<span class="panel-title collapsable-icon-left">
	        		<a class="${status.first ? '' : 'collapsed'}" role="button" data-toggle="collapse" href="#collapse${group[0].sessionId}" 
							aria-expanded="${status.first ? 'false' : 'true'}" aria-controls="collapse${group[0].sessionId}" >
						<fmt:message key="monitoring.label.group" />:	<c:out value="${group[0].sessionName}" />
					</a>
				</span>
	        </div>
        
        <div id="collapse${group[0].sessionId}" class="panel-collapse collapse ${status.first ? 'in' : ''}" 
        		role="tabpanel" aria-labelledby="heading${sessionSummary.sessionId}">
	</c:if>

	<table class="tablesorter">
		<thead>
			<tr>
				<th width="20%" align="center">
					<!--thumbnail-->
				</th>
				<th>
					<fmt:message key="monitoring.label.title" />
				</th>
				<c:choose>
					<c:when test="${sessionMap.imageGallery.allowRank}">
						<th width="170px" style="padding-left:0px; text-align:center;">
							<fmt:message key="label.monitoring.average.rating" />
						</th>
						<c:if test="${sessionMap.isCommentsEnabled}">
							<th width="200px" style="padding-left:0px; text-align:center;">
								<fmt:message key="label.monitoring.imagesummary.comments" />
							</th>								
						</c:if>
					</c:when>
					<c:when test="${sessionMap.imageGallery.allowVote}">
						<th width="70px" style="padding-left:0px; text-align:center;">
							<fmt:message key="label.monitoring.number.votes" />
						</th>
					</c:when>
				</c:choose>				
				<th width="60px" >
					<!--hide/show-->
				</th>
			</tr>
		</thead>
	
		<tbody>
		<c:forEach var="summary" items="${group}">
			<c:set var="sessionId" value="${summary.sessionId}" />		
			<c:set var="image" value="${summary.item}" />
			
			<c:if test="${summary.itemUid == -1}">
				<tr>
					<td colspan="5">
						<div class="align-left">
							<b> <fmt:message key="message.monitoring.summary.no.resource.for.group" /> </b>
						</div>
					</td>
				</tr>
			</c:if>
			
			<c:if test="${summary.itemUid != -1}">
				<tr>

					<td align="center">
						<c:set var="thumbnailPath">
						   	<lams:WebAppURL />download/?uuid=${image.thumbnailFileDisplayUuid}&preferDownload=false
						</c:set>
						<c:set var="url" >
							<c:url value='/monitoring/imageSummary.do'/>?sessionMapID=${sessionMapID}&imageUid=${image.uid}&toolSessionID=${sessionId}&KeepThis=true&TB_iframe=true&modal=true
						</c:set>				
						<a href="${url}" class="thickbox" title="<fmt:message key='label.monitoring.imagesummary.image.summary' />" style="border-style: none;"> 
							<c:set var="title">
								<c:out value="${image.title}" escapeXml="true"/>
							</c:set>
							<img src="${thumbnailPath}" alt="${title}" style="border-style: none;"/>
						</a>
					</td>
					
					<td style="vertical-align:middle;">
						<c:set var="title">
							<c:out value="${image.title}" escapeXml="true"/>
						</c:set>
						<a href="${url}" class="thickbox"><c:out value="${title}" escapeXml="false"/></a>

						<c:if test="${! summary.itemCreateByAuthor}">
							<c:set var="portraitURL"><lams:Portrait userId="${summary.userId}" hover="true"><c:out value="${summary.username}" escapeXml="true"/></lams:Portrait></c:set>
							<c:choose>
							<c:when test="${fn:containsIgnoreCase(portraitURL,'<a ')}">
								<c:set var="authorref">${fn:replace(portraitURL, "class=\"", "href=\"REPLACEMEURL\" class=\"thickbox ")}</c:set>
								<c:set var="authorref">${fn:replace(authorref, "REPLACEMEURL", url)}</c:set>
							</c:when>
							<c:otherwise>
								<c:set var="authorref"><c:out value="${summary.username}" escapeXml="true"/></c:set>
							</c:otherwise>
							</c:choose>
							[ <fmt:message key="label.monitoring.by"/>&nbsp;${authorref} ]
						</c:if>							
					</td>

					<c:choose>
						<c:when test="${sessionMap.imageGallery.allowRank}">
							<td style="vertical-align:middle; padding-left:0px; text-align:center;">
								<lams:Rating itemRatingDto="${summary.itemRatingDto}" disabled="true" showComments="false"
										isDisplayOnly="true"
										maxRates="0" 
										countRatedItems="0" />
							</td>
							
							<c:if test="${sessionMap.isCommentsEnabled}">
								<td style="min-width: 250px;vertical-align:middle; padding-left:0px;">
									<c:forEach var="commentDto" items="${summary.itemRatingDto.commentDtos}">
										<div class="rating-comment">
											${commentDto.comment}
											
											<div class="rating-comment-posted-by">
												<fmt:message key="label.posted.by">
													<fmt:param>${commentDto.userFullName}</fmt:param>
													<fmt:param><lams:Date value="${commentDto.postedDate}"/></fmt:param>
												</fmt:message>
											</div>
										</div>
									</c:forEach>								
								</td>
								
							</c:if>
						</c:when>
						
						<c:when test="${sessionMap.imageGallery.allowVote}">
							<td style="vertical-align:middle; padding-left:0px; text-align:center;">
								${summary.numberOfVotes}
							</td>
						</c:when>
					</c:choose>						
					
					<td style="vertical-align:middle; padding-left: 0px; text-align: center;">
						<a href="#nogo"	class="btn btn-default btn-xs loffset5 toggle-image-visibility" 
								data-image-uid="${summary.itemUid}" data-is-hidden="${summary.itemHide}">
							<c:choose>
								<c:when test="${summary.itemHide}">
									<fmt:message key="monitoring.label.show" />
								</c:when>
								<c:otherwise>
									<fmt:message key="monitoring.label.hide" />
								</c:otherwise>
							</c:choose>
						</a>
					</td>
					
				</tr>
			</c:if>
		</c:forEach>
		<tbody>
	</table>
	
	<c:if test="${sessionMap.isGroupedActivity}">
		</div> <!-- end collapse area  -->
		</div> <!-- end collapse panel  -->
	</c:if>
	${ !sessionMap.isGroupedActivity || ! status.last ? '<div class="voffset5">&nbsp;</div>' :  ''}

</c:forEach>

<c:if test="${sessionMap.isGroupedActivity}">
	</div> <!--  end panel group -->
</c:if>

<%@ include file="parts/advanceOptions.jsp"%>

<div id="manage-image-buttons">
	<div class="panel panel-default" >
		<div class="panel-heading">
			<button onclick="javascript:newImageInit('<c:url value="/authoring/newImageInit.do?sessionMapID=${sessionMapID}&saveUsingLearningAction=true"/>');"
					class="btn btn-default btn-xs loffset5  " id="">  
				<i class="fa fa-upload"></i> <fmt:message key="label.learning.add.new.image" />
			</button>
		</div>
	</div>
</div>

<div id="new-image-input-area" class="voffset20"></div>