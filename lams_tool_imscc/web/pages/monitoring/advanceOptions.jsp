<%@ include file="/common/taglibs.jsp"%>

<c:set var="adTitle"><fmt:message key="monitor.summary.th.advancedSettings" /></c:set>
<lams:AdvancedAccordian title="${adTitle}">          
	<table class="table table-striped table-condensed">
		
		<tr>
			<td>
				<fmt:message key="label.authoring.advance.run.content.auto" />
			</td>
			
			<td>
				<c:choose>
					<c:when test="${sessionMap.commonCartridge.runAuto}">
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
				<fmt:message key="label.authoring.advance.mini.number.resources.view" />
			</td>
			
			<td>
				${sessionMap.commonCartridge.miniViewCommonCartridgeNumber}
			</td>
		</tr>
		
	</table>
</lams:AdvancedAccordian>
