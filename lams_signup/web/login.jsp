<%@ include file="/taglibs.jsp"%>

<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<script type="text/javascript">
	if (window.top != null) {
		window.top.location.href = "${lams}";
	} else
		location.href = "${lams}";
</script>


