<%@ include file="/common/taglibs.jsp"%>
<c:set var="adTitle"><fmt:message key="monitor.summary.th.advancedSettings" /></c:set>

<lams:AdvancedAccordian title="${adTitle}">
	<table class="table table-striped table-condensed">
			<tr>
				<td>
					<fmt:message key="advanced.lockOnFinished" />
					<lams:Popover>
						<fmt:message key="label.advanced.lockOnFinished.tip.1" /><br>
					</lams:Popover>
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
					<fmt:message key="advanced.allowRichEditor" />
				</td>
				
				<td>
					<c:choose>
						<c:when test="${dto.allowRichEditor}">
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