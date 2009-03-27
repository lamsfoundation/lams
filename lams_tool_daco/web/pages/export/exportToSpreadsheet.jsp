<%@ page language="java"  pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ include file="/common/taglibs.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<lams:html>
<lams:head>
	<title><fmt:message key="title.export.spreadsheet" /></title>
	<!-- ********************  CSS ********************** -->
	<lams:css />
	<script type="text/javascript">
		// if the download starts and finishes too quickly, then the page doesn't get a chance to finish rendering. So slow it down a tad.
		function goDownload() {
			var format = 1;
			if(document.getElementById("csv").checked)
				format = 2;
			document.getElementById("downloadFileDummyIframe").src="<c:url value='/monitoring/exportToSpreadsheet.do?sessionMapID=${param.sessionMapID}&format='/>" + format;
		}
	</script>
</lams:head>

<body class="stripes">
<div id="content">
	<h3>
		<fmt:message key="label.export.spreadsheet.instruction" />
	</h3>
	<table>
		<tr>
			<td style="padding-bottom: 5px;">
				<input type="radio" name="format" id="excel" value="1" checked="checked" class="noBorder"><fmt:message key="label.export.spreadsheet.choose.format.excel" /></input>
				<br /><br />
			</td>
		</tr>
		<tr>
			<td>
				<input type="radio" name="format" id="csv" value="2" class="noBorder"><fmt:message key="label.export.spreadsheet.choose.format.csv" /></input>
			</td>
		</tr>
	</table>
	<h3>
		<fmt:message key="label.export.spreadsheet.instruction.continue" />
	</h3>
	<div style="padding-top:20px;"></div>
	<div class="right-buttons">
		<a href="#" onclick="javascript:goDownload()" class="button"><span class="import"><fmt:message key="button.export" /></span></a>
		<a href="#" onclick="javascript:window.close()" class="button"><span class="close"><fmt:message key="label.monitoring.close" /></span></a>
	</div>
	<iframe id="downloadFileDummyIframe" style="display: none;"></iframe>
</div>
<div id="footer"></div>
</body>
</lams:html>