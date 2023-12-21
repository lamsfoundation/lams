<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL/></c:set>

<lams:html>
<lams:head>
	<title><fmt:message key ="heading.general.error" /></title>
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
				<p><fmt:message key="${messageKey}"/>
				</p>
			</div>
		</td>
	</tr>
</table>
</lams:Page>
</body>
</lams:html>