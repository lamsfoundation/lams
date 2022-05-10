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
<div class="panel-group" id="accordionComments-${toolSessionId}-${toolItemId}" role="tablist" aria-multiselectable="true"> 
    <div class="panel panel-default">
        <div class="panel-heading collapsable-icon-left" id="headingComments-${toolSessionId}-${toolItemId}">
        	<span class="panel-title">
	    	<a class="collapsed" role="button" data-toggle="collapse" href="#collapseComments-${toolSessionId}-${toolItemId}"
	    		aria-expanded="false" aria-controls="collapseComments-${toolSessionId}-${toolItemId}">
          	${not empty accordionTitle?accordionTitle:"Comments"}</a>
      		</span>
        </div>
        <div id="collapseComments-${toolSessionId}-${toolItemId}" class="panel-collapse collapse collapseComments"
        	 role="tabpanel" aria-labelledby="headingComments-${toolSessionId}-${toolItemId}" aria-expanded="false" style="">
</c:if>

<div id="commentFrame-${toolSessionId}-${toolItemId}" class="commentFrame"  style="width: ${width}; height: ${height};"></div>

<c:if test="${embedInAccordian}">
		</div>
	</div>
</div>
</c:if>

<script>
$(document).ready(function(){
	var url='<lams:LAMSURL/>comments/init.do?externalID=${toolSessionId}&externalSecondaryID=${toolItemId}&externalSig=${toolSignature}&externalType=1${modeStr}&likeAndDislike=${likeAndDislike}&readOnly=${readOnly}&pageSize=${pageSize}&sortBy=${sortBy}&anonymous=${anonymous}';
	
	<c:choose>
		<c:when test="${embedInAccordian}">
			$('#collapseComments-${toolSessionId}-${toolItemId}').on('show.bs.collapse', function(){
				$('.collapseComments').not('#collapseComments-${toolSessionId}-${toolItemId}').collapse('hide').find('.commentFrame').empty();
				$('.commentFrame', this).load(url);
			});
		</c:when>
		<c:otherwise>
			$('#commentFrame-${toolSessionId}-${toolItemId}').load(url);
		</c:otherwise>
	</c:choose>
});
</script>