<!DOCTYPE html>
<%@ include file="/taglibs.jsp"%>

<lams:html>   
<lams:head></lams:head>

<c:if test="${not empty parallelUrls}">
<frameset rows="*,*" bordercolor="1" id="lamsDynamicFrameSet">
	<c:forEach items="${parallelUrls}" var="url" varStatus="loop">
		<frame src="<lams:LAMSURL />/<c:out value="${url}" />" 
			name="TaskFrame<c:out value="${loop.index}" />"
			frameborder="" bordercolor="#E0E7EB"
			id="lamsDynamicFrame<c:out value="${loop.index}" />">
	</c:forEach>
</frameset>

</c:if>

</lams:html>
