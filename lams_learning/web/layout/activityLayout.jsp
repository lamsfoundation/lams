<%@ page language="java"%>
<%@ taglib uri="http://jakarta.apache.org/struts/struts-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/struts-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/struts-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<html:html locale="true" xhtml="true">

	<tiles:useAttribute name="title" ignore="false" />
	
	<head>
		<title>LAMS</title>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<link href="css/learner.css" rel="stylesheet" type="text/css" />
		<script language="javascript" src="javascript/learning.js"></script>
	</head>

	<body bgcolor="#9DC5EC">
		<div align="center">
		
			<table width="95%" border="0" cellpadding="0" cellspacing="0" summary="This table is being used for layout purposes">
				<tr>
					<td valign="top"> 
					
						<table width="100%" border="0" cellpadding="0" cellspacing="0" summary="This table is being used for layout purposes">
							<tr> 
								<td width="136" height="10"></td>
								<td width="92%" height="10"></td>
							</tr>
							<tr bgcolor="#282871"> 
								<td width="50%" height="18" align="left">
									<html:img height="8" width="8" page="/images/spacer.gif" />
									<font color="#FFFFFF" size="1" face="Verdana, Arial, Helvetica, sans-serif"><c:out value="${title}" /></font>
								</td>
								<td width="50%" height="18" align="right" > 
									<html:link page="/doc/LAMS_Learner_Guide_b60.pdf">
										<font color="#FFFFFF" size="1" face="Verdana, Arial, Helvetica, sans-serif">[HELP]</font>
									</html:link>
									<html:img height="8" width="8" page="/images/spacer.gif" />
								</td>
							</tr>
						</table>
						
					</td>
				</tr>
				<tr>
					<td valign="top">
						<table width="100%" border="0" align="center" cellpadding="5" cellspacing="0" bgcolor="#FFFFFF" summary="This table is being used for layout purposes">
							<tr> 
								<td valign="top">
	
									<tiles:insert attribute="body" />
	
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td valign="top">
						<table width="100%" border="0" cellspacing="0" cellpadding="0" summary="This table is being used for layout purposes">
							<tr bgcolor="#282871"> 
								<td width="136" height="8"></td>
								<td width="92%" height="8" align="right"></td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</div>
	</body>

</html:html>
