<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="summaryList" value="${sessionMap.summaryList}"/>

<h1>
  <c:out value="${sessionMap.imageGallery.title}" escapeXml="true"/>
</h1>

<div class="instructions small-space-top small-space-bottom">
  <c:out value="${sessionMap.imageGallery.instructions}" escapeXml="false"/>
</div>

<c:if test="${empty summaryList}">
	<div align="center">
		<b> <fmt:message key="message.monitoring.summary.no.session" /> </b>
	</div>
</c:if>

<c:forEach var="group" items="${summaryList}">

	<c:choose>
		 <c:when test="${sessionMap.isGroupedActivity}">
			<h1>
				<fmt:message key="monitoring.label.group" /> ${group[0].sessionName}	
			</h1>
		</c:when>
		
		<c:otherwise>
			<h1>
				<fmt:message key="label.monitoring.summary.overall.summary" />	
		 	</h1>
         </c:otherwise>
	</c:choose>

	<table class="tablesorter">
		<thead>
			<tr>
				<th width="4%" align="center">
					<!--thumbnail-->
				</th>
				<th>
					<fmt:message key="monitoring.label.title" />
				</th>
				<c:choose>
					<c:when test="${sessionMap.imageGallery.allowRank}">
						<th width="70px" style="padding-left:0px; text-align:center;">
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
				<th width="75px" >
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
						   	<html:rewrite page='/download/?uuid='/>${image.thumbnailFileUuid}&preferDownload=false
						</c:set>
						<c:set var="url" >
							<c:url value='/monitoring/imageSummary.do'/>?sessionMapID=${sessionMapID}&imageUid=${image.uid}&resizeIframe=true&TB_iframe=true&height=640&width=740
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
							<c:if test="${!summary.itemCreateByAuthor}">
								[ <fmt:message key="label.monitoring.by"/> <c:out value="${summary.username}" escapeXml="true"/> ]
							</c:if>						
						</c:set>
						<a href="${url}" class="thickbox">
							<c:out value="${title}" escapeXml="false"/>
						</a>
					</td>
					
					<c:choose>
						<c:when test="${sessionMap.imageGallery.allowRank}">
							<td style="vertical-align:middle; padding-left:0px; text-align:center;">
								<lams:Rating itemRatingDto="${summary.itemRatingDto}" disabled="true" showComments="false"
										isItemAuthoredByUser="true"
										maxRates="0" 
										countRatedItems="0" />
							</td>
							<c:if test="${sessionMap.isCommentsEnabled}">
								<td style="vertical-align:middle; padding-left:0px; text-align:center;">
									<c:forEach var="commentDto" items="${summary.itemRatingDto.commentDtos}">
										<div class="rating-comment">
											${commentDto.comment}
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
						<c:choose>
							<c:when test="${summary.itemHide}">
								<a href="<c:url value='/monitoring/showitem.do'/>?sessionMapID=${sessionMapID}&imageUid=${summary.itemUid}" class="button"> <fmt:message key="monitoring.label.show" /> </a>
							</c:when>
							
							<c:otherwise>
								<a href="<c:url value='/monitoring/hideitem.do'/>?sessionMapID=${sessionMapID}&imageUid=${summary.itemUid}" class="button"> <fmt:message key="monitoring.label.hide" /> </a>
							</c:otherwise>
						</c:choose>
					</td>
					
				</tr>
			</c:if>
		</c:forEach>
		<tbody>
	</table>
	
	<br>	

	<%-- Reflection list  --%>
	<c:if test="${sessionMap.imageGallery.reflectOnActivity && not (empty sessionId)}">	
	
		<h2 style="color:black; margin-left: 20px; " >
			<fmt:message key="label.monitoring.summary.title.reflection"/>
		</h2>
	
		<table cellpadding="0" class="alternative-color">			
		
			<tr>
				<th>
					<fmt:message key="monitoring.user.fullname"/>
				</th>
				<th>
					<fmt:message key="monitoring.user.reflection"/>
				</th>
			</tr>		
		
			<c:set var="userList" value="${sessionMap.reflectList[sessionId]}"/>
			<c:forEach var="user" items="${userList}" varStatus="refStatus">
				<tr>
					<td>
						<c:out value="${user.fullName}" escapeXml="true"/>
					</td>
					<td>
						<c:set var="viewReflection">
							<c:url value="/monitoring/viewReflection.do?toolSessionID=${sessionId}&userUid=${user.userUid}"/>
						</c:set>
						<html:link href="javascript:launchPopup('${viewReflection}')">
							<fmt:message key="label.view" />
						</html:link>
					</td>
				</tr>
			</c:forEach>
		</table>
	</c:if>

</c:forEach>

<div class="space-top space-left">
	<a href="<html:rewrite page='/monitoring/newImageInit.do?sessionMapID='/>${sessionMapID}&KeepThis=true&TB_iframe=true&height=540&width=480&modal=true" class="button add_new_image thickbox">  
		<fmt:message key="label.monitoring.summary.add.new.image" />
	</a>
</div>

<%@ include file="parts/advanceOptions.jsp"%>
