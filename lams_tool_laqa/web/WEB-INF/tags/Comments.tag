<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-function" prefix="fn" %>

<%@ attribute name="toolSessionId" required="true" rtexprvalue="true"%>
<%@ attribute name="toolItemId" required="false" rtexprvalue="true"%>
<%@ attribute name="toolSignature" required="true" rtexprvalue="true"%>
<%@ attribute name="height" required="false" rtexprvalue="true"%>
<%@ attribute name="width" required="false" rtexprvalue="true"%>
<%@ attribute name="mode" required="false" rtexprvalue="true"%>
<%@ attribute name="likeAndDislike" required="false" rtexprvalue="true"%>
<%@ attribute name="anonymous" required="false" rtexprvalue="true"%>
<%@ attribute name="readOnly" required="false" rtexprvalue="true"%>
<%@ attribute name="pageSize" required="false" rtexprvalue="true"%>
<%@ attribute name="sortBy" required="false" rtexprvalue="true"%>
<%@ attribute name="embedInAccordian" required="false" rtexprvalue="true"%>
<%@ attribute name="accordionTitle" required="false" rtexprvalue="true"%>


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

<c:if test="${empty anonymous}">
	<c:set var="anonymous" value="false" />
</c:if>

<c:if test="${empty readOnly}">
	<c:set var="readOnly" value="false" />
</c:if>

<c:if test="${empty sortBy}">
	<c:set var="sortBy" value="0" /> <!-- 0: date, 1: likes -->
</c:if>

<c:if test="${empty embedInAccordian}">
	<c:set var="embedInAccordian" value="false" />
</c:if>	

<c:if test="${embedInAccordian}">
<div class="panel-group" id="accordionComments" role="tablist" aria-multiselectable="true"> 
    <div class="panel panel-default">
        <div class="panel-heading collapsable-icon-left" id="headingComments">
        	<span class="panel-title">
	    	<a class="collapsed" role="button" data-toggle="collapse" href="#collapseComments" aria-expanded="false" aria-controls="collapseComments">
          	${not empty accordionTitle?accordionTitle:"Comments"}</a>
      		</span>
        </div>
        <div id="collapseComments" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingComments" aria-expanded="false" style="">
</c:if>

<div id="commentFrame" class="commentFrame"  style="width: ${width}; height: ${height};"></div>

<c:if test="${embedInAccordian}">
		</div>
	</div>
</div>
</c:if>

<script>
$(document).ready(function(){
	var url='<lams:LAMSURL/>comments/init.do?externalID=${toolSessionId}&externalSecondaryID=${toolItemId}&externalSig=${toolSignature}&externalType=1${modeStr}&likeAndDislike=${likeAndDislike}&readOnly=${readOnly}&pageSize=${pageSize}&sortBy=${sortBy}&anonymous=${anonymous}';
	$.ajaxSetup({ cache: true });
	$('#commentFrame').load(url);
});
</script>