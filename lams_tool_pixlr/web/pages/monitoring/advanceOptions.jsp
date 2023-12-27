<%@ include file="/common/taglibs.jsp"%>

<c:set var="adTitle"><fmt:message key="monitor.summary.th.advancedSettings" /></c:set>
<lams:AdvancedAccordian title="${adTitle}">

	<table class="table table-condensed">
	
		<tr>
			<td>
				<fmt:message key="advanced.lockOnFinished" />
			</td>
			
			<td>
				<c:choose>
					<c:when test="${dto.lockOnFinish}">
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
				<fmt:message key="advanced.allowViewOthersImages" />
			</td>
			
			<td>
				<c:choose>
					<c:when test="${dto.allowViewOthersImages}">
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
