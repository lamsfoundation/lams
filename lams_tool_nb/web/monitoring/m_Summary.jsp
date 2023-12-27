<%@ include file="/includes/taglibs.jsp"%>
<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/monitorToolSummaryAdvanced.js" ></script>
<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/portrait.js" ></script>
	
<script type="text/javascript">
	$(document).ready(function(){
		initializePortraitPopover("<lams:LAMSURL />");
	});
</script>

<div class="panel">
	<h4><c:out value="${monitoringDTO.title}" escapeXml="true" /></h4>
	<div class="voffset5"><c:out value="${monitoringDTO.basicContent}" escapeXml="false" /></div>
</div>

<H4><fmt:message key="titleHeading.statistics"/></H2>
<%@ include file="m_Statistics.jsp"%>
	
<c:set var="adTitle"><fmt:message key="monitor.summary.th.advancedSettings" /></c:set>
<lams:AdvancedAccordian title="${adTitle}">      	
	<table class="table table-striped table-condensed">
		<tr>
			<td>
				<fmt:message key="advanced.allow.comments" />
			</td>
			
			<td>
				<c:choose>
					<c:when test="${allowComments}">
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