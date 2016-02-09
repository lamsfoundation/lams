<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
	<lams:head>
		<%@ include file="/common/mobileheader.jsp"%>
	</lams:head>
	
	<body>
		<div data-role="page" data-cache="false">
		
			<div data-role="content">
				<h1>
					<c:out value="${title}" escapeXml="true"/>
				</h1>
			
				<p>
					<c:if test="${!isUrlItemType}">
						<c:set var="urlPrefix"><lams:WebAppURL /></c:set>
					</c:if>

					<a href="#nogo" onclick="javascipt:launchPopup('${urlPrefix}${popupUrl}','popupUrl');"
							style="width:200px;float:none;" class="button">
						<c:choose>
							<c:when test="${isUrlItemType}">
								<fmt:message key="open.in.new.window" />
							</c:when>
							<c:otherwise>
								<fmt:message key="open.file.in.new.window" />
							</c:otherwise>
						</c:choose>
					</a>
				</p>
			</div>

		<div>
	</body>
</lams:html>
