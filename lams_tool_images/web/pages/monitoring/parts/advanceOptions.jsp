<%@ include file="/common/taglibs.jsp"%>

<c:set var="adTitle"><fmt:message key="monitor.summary.th.advancedSettings" /></c:set>
<lams:AdvancedAccordian title="${adTitle}">
             
	<table class="table table-striped table-condensed">
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
</lams:AdvancedAccordian>
