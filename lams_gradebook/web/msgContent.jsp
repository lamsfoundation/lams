<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
<lams:head>
	<html:base/>
	<title><fmt:message><tiles:getAsString name="titleKey"/></fmt:message></title>
	<link rel="icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />

	<lams:css/>

	<script type="text/javascript" src="includes/javascript/getSysInfo.js"></script>
	<script type="text/javascript" src="includes/javascript/openUrls.js"></script>
</lams:head>
    
<body class="stripes">
<div id="page">
	<div id="content">
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
	</div>
</div>
</body>
</lams:html>
