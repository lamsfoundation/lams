<%@ include file="/common/taglibs.jsp"%>

<c:set var="adTitle"><spring:escapeBody javaScriptEscape="true"><fmt:message key="monitor.summary.th.advancedSettings" /></spring:escapeBody></c:set>
<lams:AdvancedAccordian title="${adTitle}">
	<div class="ltable table-striped table-sm no-header mb-0">
		<div class="row">
			<div class="col">
				<fmt:message key="label.authoring.advance.lock.on.finished" />
			</div>
			
			<div class="col">
				<c:choose>
					<c:when test="${sessionMap.peerreview.lockWhenFinished}">
						<fmt:message key="label.on" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.off" />
					</c:otherwise>
				</c:choose>	
			</div>
		</div>
		
		<div class="row">
			<div class="col">
				<fmt:message key="label.show.ratings.left.for.user" />
			</div>
			
			<div class="col">
				<c:choose>
					<c:when test="${sessionMap.peerreview.showRatingsLeftForUser}">
						<fmt:message key="label.on" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.off" />
					</c:otherwise>
				</c:choose>	
			</div>
		</div>
		
		<div class="row">
			<div class="col">
				<fmt:message key="label.self.review" />
			</div>
			
			<div class="col">
				<c:choose>
					<c:when test="${sessionMap.peerreview.selfReview}">
						<fmt:message key="label.on" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.off" />
					</c:otherwise>
				</c:choose>	
			</div>
		</div>
	</div>
</lams:AdvancedAccordian>
