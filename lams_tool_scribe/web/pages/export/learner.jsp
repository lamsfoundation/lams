<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
	"http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<lams:head>
		<title><c:out value="${scribeDTO.title}" escapeXml="false" />
		</title>
		<lams:css localLinkPath="../" />

		<style>
			<jsp:include flush="true" page="/includes/css/scribe.css"></jsp:include>			
		</style>
	</lams:head>

	<body class="stripes">

		<div id="content">
		<%@ include file="../learning/parts/reportBody.jsp"%>
		</div>
		<!--closes content-->

		<div id="footer">
		</div>
		<!--closes footer-->

	</body>
</lams:html>
