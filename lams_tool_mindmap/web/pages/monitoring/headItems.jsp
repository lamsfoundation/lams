<%@ include file="/common/taglibs.jsp"%>

<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<script type="text/javascript">
	var initialTabId = "${mindmapDTO.currentTab}";
</script>
<script type="text/javascript" src="${tool}includes/javascript/monitoring.js"></script>
