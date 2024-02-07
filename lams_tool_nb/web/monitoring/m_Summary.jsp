<%@ include file="/includes/taglibs.jsp"%>
<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/monitorToolSummaryAdvanced.js" ></script>
<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/portrait5.js" ></script>
	
<script type="text/javascript">
	$(document).ready(function(){
		initializePortraitPopover("<lams:LAMSURL />");
	});
</script>

<div class="instructions">
	<div class="fs-4">
		<c:out value="${monitoringDTO.title}" escapeXml="true" />
	</div>
	<div class="mt-2">
		<c:out value="${monitoringDTO.basicContent}" escapeXml="false" />
	</div>
</div>

<%@ include file="m_Statistics.jsp"%>
	
<c:set var="adTitle"><fmt:message key="monitor.summary.th.advancedSettings" /></c:set>
<lams:AdvancedAccordian title="${adTitle}">
<div class="ltable table-striped table-sm no-header mb-0">
	<div class="row">
		<div class="col">
			<fmt:message key="advanced.allow.comments" />
		</div>
		
		<div class="col text-end">
			<c:choose>
				<c:when test="${allowComments}">
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
