<%@ include file="/taglibs.jsp"%>

<h4>
	<a href="sysadminstart.do"><fmt:message key="sysadmin.maintain" /></a>
</h4>
<h1><fmt:message key="cache.title"/></h1>
<br/>
<p><fmt:message key="cache.explanation4"/></p>
<p><fmt:message key="cache.explanation2"/></p>

<c:if test="${not empty node}">
	<div class="info" >
		<c:choose>
			<c:when test="${node eq 'ALL'}">
				<fmt:message key="cache.removed.all" />
			</c:when>
			<c:otherwise>
				<fmt:message key="cache.removed">
					<fmt:param>${node}</fmt:param>
				</fmt:message>
			</c:otherwise>
		</c:choose>
	</div>
</c:if>

<!-- cache is a list, where each item is a cached class -->

<p><fmt:message key="cache.entries.title2"/></p>
<p>
	<a href="cache.do?method=remove&node=ALL"><fmt:message key="cache.button.remove.all"/></a>
</p>
<ul>
	<c:forEach var="node" items="${cache}">
		<li><c:out value="${node}"/>
			<a href="cache.do?method=remove&node=<c:out value="${node}"/>"><fmt:message key="cache.button.remove"/></a>
			<br/>
		</li>
	</c:forEach>
</ul>

