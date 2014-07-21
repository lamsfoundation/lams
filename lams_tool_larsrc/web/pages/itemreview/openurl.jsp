<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
	</lams:head>
	
	<body class="stripes">
		<div id="content">
			<p>
				<c:out value="${title}" escapeXml="true"/>
			</p>
			<br>
			
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
		<div>
	</body>
	
</lams:html>
