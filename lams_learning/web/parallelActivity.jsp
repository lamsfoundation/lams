 
<%@ page language="java"%>
<%@ taglib uri="http://jakarta.apache.org/struts/struts-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/struts-html" prefix="html"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>


<%-- TODO: use type --%>
<frameset rows="*,*" bordercolor="1" id="lamsDynamicFrameSet">
	<c:forEach items="${activityForm.activityURLs}" var="activityURL" varStatus="loop">
		<%--c:out value="${activityURL}" /--%>
		<frame src="<c:out value="${activityURL.url}" />" 
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

