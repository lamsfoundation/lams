<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-function" prefix="fn" %>

<%@ attribute name="toolSessionId" required="true" rtexprvalue="true"%>
<%@ attribute name="toolSignature" required="true" rtexprvalue="true"%>
<%@ attribute name="height" required="false" rtexprvalue="true"%>
<%@ attribute name="width" required="false" rtexprvalue="true"%>
<%@ attribute name="mode" required="false" rtexprvalue="true"%>

<c:if test="${empty width}">
	<c:set var="width" value="100%" />
</c:if>

<c:if test="${empty height}">
	<c:set var="height" value="470px" />
</c:if>

<c:set var="modeStr" value=""/>
<c:if test="${!empty mode}">
	<c:set var="modeStr">&mode=${mode}</c:set>
</c:if>

<iframe id="commentFrame" class="commentFrame" src="/lams/comments/comments.do?externalID=${toolSessionId}&externalSig=${toolSignature}&externalType=1${modeStr}" style="width: ${width}; height: ${height};"></iframe>

<script>
function resizeCommentFrame(pixels){
    var w = window.innerWidth;
    var h = window.innerHeight;
}
</script>