<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
            "http://www.w3.org/TR/html4/loose.dtd">
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

<noframes>
	<body>
		<fmt:message key="message.activity.parallel.noFrames" />
	</body>
</noframes>
</c:if>

</lams:html>