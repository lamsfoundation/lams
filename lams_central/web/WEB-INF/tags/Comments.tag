<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-function" prefix="fn" %>

<%@ attribute name="toolSessionId" required="true" rtexprvalue="true"%>
<%@ attribute name="toolSignature" required="true" rtexprvalue="true"%>
<%@ attribute name="height" required="false" rtexprvalue="true"%>
<%@ attribute name="width" required="false" rtexprvalue="true"%>
<%@ attribute name="mode" required="false" rtexprvalue="true"%>
<%@ attribute name="likeAndDislike" required="false" rtexprvalue="true"%>
<%@ attribute name="readOnly" required="false" rtexprvalue="true"%>
<%@ attribute name="pageSize" required="false" rtexprvalue="true"%>
<%@ attribute name="sortBy" required="false" rtexprvalue="true"%>


<c:if test="${empty width}">
	<c:set var="width" value="100%" />
</c:if>

<c:if test="${empty height}">
	<c:set var="height" value="auto" />
</c:if>

<c:set var="modeStr" value=""/>
<c:if test="${!empty mode}">
	<c:set var="modeStr">&mode=${mode}</c:set>
</c:if>

<c:if test="${empty likeAndDislike}">
	<c:set var="likeAndDislike" value="false" />
</c:if>

<c:if test="${empty readOnly}">
	<c:set var="readOnly" value="false" />
</c:if>

<c:if test="${empty sortBy}">
	<c:set var="sortBy" value="0" /> <!-- 0: date, 1: likes -->
</c:if>

<div id="commentFrame" class="commentFrame"  style="width: ${width}; height: ${height};"></div>
<script>
$(document).ready(function(){
	var url='<lams:LAMSURL/>comments/comments.do?externalID=${toolSessionId}&externalSig=${toolSignature}&externalType=1${modeStr}&likeAndDislike=${likeAndDislike}&readOnly=${readOnly}&pageSize=${pageSize}&sortBy=${sortBy}';
	$.ajaxSetup({ cache: true });
	$('#commentFrame').load(url);
});
</script>