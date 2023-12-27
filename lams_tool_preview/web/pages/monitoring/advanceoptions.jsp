<%@ include file="/common/taglibs.jsp"%>

<c:set var="adTitle"><spring:escapeBody javaScriptEscape="true"><fmt:message key="monitor.summary.th.advancedSettings" /></spring:escapeBody></c:set>
<lams:AdvancedAccordian title="${adTitle}">
             
<table class="table table-striped table-condensed">
	<tr>
		<td>
			<fmt:message key="label.authoring.advance.lock.on.finished" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${sessionMap.peerreview.lockWhenFinished}">
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
			<fmt:message key="label.show.ratings.left.for.user" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${sessionMap.peerreview.showRatingsLeftForUser}">
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
			<fmt:message key="label.self.review" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${sessionMap.peerreview.selfReview}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
</table>

</lams:AdvancedAccordian>
