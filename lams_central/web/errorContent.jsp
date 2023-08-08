<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
<lams:head>
	<base href="<lams:LAMSURL/>"/>
	<title><fmt:message key="heading.general.error" /></title>
	<link rel="icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
	<lams:css/>

	<lams:JSImport src="includes/javascript/getSysInfo.js" />
	<lams:JSImport src="includes/javascript/openUrls.js" />
</lams:head>
    
<body class="stripes">
<lams:Page type="learner" title="">
<table width="90%" border="0" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF">
	<tr> 
		<td> 
			<div align="center" class="heading">
				<p><fmt:message key="error.general.1"/></p>
				<p class="body"><fmt:message key="error.general.2"/></p>
				<p class="body"><fmt:message key="error.general.3"/></p>
			</div>
		</td>
	</tr>
</table>
<div id="footer"></div>
</lams:Page>
</body>
</lams:html>