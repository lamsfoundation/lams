<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
	"http://www.w3.org/TR/html4/loose.dtd">
<c:set var="sessionMapID" value="${param.sessionMapID}"/>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="itemSummaryList" value="${sessionMap.itemSummaryList}"/>
<c:set var="mode" value="${sessionMap.mode}"/>
<c:set var="title" value="${sessionMap.title}"/>

<lams:html>
<lams:head>
	<title><fmt:message key="export.title" /></title>
	<c:set var="lams">
		<lams:LAMSURL />
	</c:set>

	<lams:css localLinkPath="../"/>
</lams:head>
<body class="stripes">

	<div id="content">

		<h1><c:out value="${title}" escapeXml="true"/></h1>

		<table border="0" cellspacing="3" width="98%">
			<c:forEach var="itemSummary" items="${itemSummaryList}">
				<tr>
					<td>
						<%@ include file="itemsummary.jsp"%>
					</td>
				</tr>
			</c:forEach>
		</table>

	</div>  <!--closes content-->


	<div id="footer">
	</div><!--closes footer-->

</body>
</lams:html>
