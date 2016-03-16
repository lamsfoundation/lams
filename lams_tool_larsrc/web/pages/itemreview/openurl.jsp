<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
	</lams:head>
	
	<body>
		<div class="container">
			<H4><c:out value="${title}" escapeXml="true"/></H4>

			<c:if test="${!isUrlItemType}">
				<c:set var="urlPrefix"><lams:WebAppURL /></c:set>
			</c:if>
				
			<a href="#nogo" onclick="javascipt:launchPopup('${urlPrefix}${popupUrl}','popupUrl');" class="btn btn-sm btn-default">
				<c:choose>
					<c:when test="${isUrlItemType}">
						<fmt:message key="open.in.new.window" />
					</c:when>
					<c:otherwise>
						<fmt:message key="open.file.in.new.window" />
					</c:otherwise>
				</c:choose>
			</a>
		</div>
	</body>
	
</lams:html>
