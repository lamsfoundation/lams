<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<c:set var="lams"><lams:LAMSURL/></c:set>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<lams:html locale="true">
  	<head>    
	    <title><c:out value="${NbExportForm.title}"/></title>
	  	
		<lams:css localLinkPath="../"/>
	</head>  
  	<body>
		<div id="page-learner"/>

			<h1 class="no-tabs-below">
				<c:out value="${NbExportForm.title}" escapeXml="false" />
			</h1>

			<div id="header-no-tabs-learner">
			</div>

			<div id="content-learner">

				<p>
					<c:out value="${NbExportForm.content}" escapeXml="false" />
				</p>
			</div>

			<div id="footer-learner">
			</div>

		</div>

	</body>
</lams:html>