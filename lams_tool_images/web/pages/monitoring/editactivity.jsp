<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="imageGallery" value="${sessionMap.imageGallery}"/>

<c:set var="adTitle"><fmt:message key="monitoring.tab.edit.activity" /></c:set>
<lams:AdvancedAccordian title="${adTitle}">
	<c:if test="${sessionMap.isPageEditable}">
		<lams:Alert5 type="warn" id="no-edit">
			<fmt:message key="message.alertContentEdit" />
		</lams:Alert5>
	</c:if>
	
	<form id='define-later-form' method='post' action='../authoring/definelater.do' target='definelater'>
		<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
		<input type="hidden" name="toolContentID" value="${sessionMap.toolContentID}" />
		<input type="hidden" name="contentFolderID" value="${sessionMap.contentFolderID}" />
	</form>
	
	<div id="manage-image-buttons" class="clearfix m-3">	
		<button type="button" onclick="launchDefineLaterPopup()" class="btn btn-secondary btn-icon-pen float-end ms-2">
			<fmt:message key="label.monitoring.edit.activity.edit" />
		</button>
		
		<button type="button" onclick="newImageInit('<c:url value="/authoring/newImageInit.do?sessionMapID=${sessionMapID}&saveUsingLearningAction=true&bootstrap5=true"/>')"
				class="btn btn-secondary float-end">  
			<i class="fa fa-upload"></i> 
			<fmt:message key="label.learning.add.new.image" />
		</button>
	</div>
             
	<table class="table table-striped table-condensed">
		<tr>
			<td>
				<fmt:message key="label.authoring.basic.title" />
			</td>
			
			<td>
				<c:out value="${imageGallery.title}" escapeXml="true" />
			</td>
		</tr>
	
		<tr>
			<td>
				<fmt:message key="label.authoring.basic.instruction" />
			</td>
			
			<td>
				<c:out value="${imageGallery.instructions}" escapeXml="false" />
			</td>
		</tr>
	
		<tr>
			<td>
				<fmt:message key="label.authoring.advance.lock.on.finished" />
			</td>
			
			<td>
				<c:choose>
					<c:when test="${sessionMap.imageGallery.lockWhenFinished}">
						<fmt:message key="label.on" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.off" />
					</c:otherwise>
				</c:choose>	
			</td>
		</tr>
		
		<tr>
			<td>
				<fmt:message key="label.authoring.advance.allow.learner.share.images" />
			</td>
			
			<td>
				<c:choose>
					<c:when test="${sessionMap.imageGallery.allowShareImages}">
						<fmt:message key="label.on" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.off" />
					</c:otherwise>
				</c:choose>	
			</td>
		</tr>
		
		<tr>
			<td>
				<fmt:message key="label.authoring.advance.notify.monitoring.teachers" />
			</td>
			
			<td>
				<c:choose>
					<c:when test="${sessionMap.imageGallery.notifyTeachersOnImageSumbit}">
						<fmt:message key="label.on" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.off" />
					</c:otherwise>
				</c:choose>	
			</td>
		</tr>
		
		<tr>
			<td>
				<fmt:message key="label.authoring.advance.allow.learner.comment.images" />
			</td>
			
			<td>
				<c:choose>
					<c:when test="${sessionMap.isCommentsEnabled}">
						<fmt:message key="label.on" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.off" />
					</c:otherwise>
				</c:choose>	
			</td>
		</tr>
		
		<tr>
			<td>
				<fmt:message key="label.authoring.advance.allow.learner.vote" />
			</td>
			
			<td>
				<c:choose>
					<c:when test="${sessionMap.imageGallery.allowVote}">
						<fmt:message key="label.on" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.off" />
					</c:otherwise>
				</c:choose>	
			</td>
		</tr>
		
		<tr>
			<td>
				<fmt:message key="label.authoring.advance.allow.learner.rank" />
			</td>
			
			<td>
				<c:choose>
					<c:when test="${sessionMap.imageGallery.allowRank}">
						<fmt:message key="label.on" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.off" />
					</c:otherwise>
				</c:choose>	
			</td>
		</tr>
		
		<tr>
			<td>
				<fmt:message key="label.minimum" />
			</td>
			
			<td>
				<c:choose>
					<c:when test="${sessionMap.imageGallery.minimumRates == 0}">
						<fmt:message key="label.no.minimum" />
					</c:when>
					<c:otherwise>
						${sessionMap.imageGallery.minimumRates}
					</c:otherwise>
				</c:choose>	
			</td>
		</tr>
		
		<tr>
			<td>
				<fmt:message key="label.maximum" />
			</td>
			
			<td>
				<c:choose>
					<c:when test="${sessionMap.imageGallery.maximumRates == 0}">
						<fmt:message key="label.no.maximum" />
					</c:when>
					<c:otherwise>
						${sessionMap.imageGallery.maximumRates}
					</c:otherwise>
				</c:choose>	
			</td>
		</tr>
		
		<tr>
			<td>
				<fmt:message key="label.authoring.advance.allow.learner.comment.images" />
			</td>
			
			<td>
				<c:choose>
					<c:when test="${sessionMap.isCommentsEnabled}">
						<fmt:message key="label.on" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.off" />
					</c:otherwise>
				</c:choose>	
			</td>
		</tr>
		
		<tr>
			<td>
				<fmt:message key="label.minimum" />
			</td>
			
			<td>
				<c:choose>
					<c:when test="${sessionMap.imageGallery.minimumRates == 0}">
						<fmt:message key="label.no.minimum" />
					</c:when>
					<c:otherwise>
						${sessionMap.imageGallery.minimumRates}
					</c:otherwise>
				</c:choose>	
			</td>
		</tr>
		
		<tr>
			<td>
				<fmt:message key="label.maximum" />
			</td>
			
			<td>
				<c:choose>
					<c:when test="${sessionMap.imageGallery.maximumRates == 0}">
						<fmt:message key="label.no.maximum" />
					</c:when>
					<c:otherwise>
						${sessionMap.imageGallery.maximumRates}
					</c:otherwise>
				</c:choose>	
			</td>
		</tr>
	</table>

	<div id="new-image-input-area" class="mt-3"></div>
</lams:AdvancedAccordian>
