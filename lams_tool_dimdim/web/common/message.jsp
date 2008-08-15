<%@ include file="/common/taglibs.jsp"%>

<h1>
	<%-- TODO check if message exists	--%>
	<fmt:message key="pageTitle.message"></fmt:message>
</h1>

<p>
	${requestScope.message};
</p>
