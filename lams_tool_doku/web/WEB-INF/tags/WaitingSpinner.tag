<%@ taglib uri="tags-core" prefix="c"%>

<%@ attribute name="id" required="false" rtexprvalue="true"%>
<%@ attribute name="showInline" required="false" rtexprvalue="true"%>
			
<c:if test="${not empty id}">
	<c:set var="idString">id="${id}"</c:set>
</c:if>

<c:choose>
<c:when test="${showInline}">
<i class="fa fa-refresh fa-spin fa-fw text-primary" style="display:none" ${idString} /></i>
</c:when>
<c:otherwise>
<div class="text-center" style="display:none" ${idString} ><i class="fa fa-refresh fa-spin fa-2x fa-fw text-primary" /></i></div>
</c:otherwise>
</c:choose>