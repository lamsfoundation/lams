 
<%@ page language="java"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<html:html locale="true" xhtml="true">

	<head>
		<title>LAMS</title>
	</head>
	<body>
	
		<c:if test="${!empty activity.activityURLs}">
		
			<script language="JavaScript" type="text/JavaScript"><!--
				function redirectPage() {
					setTimeout("go_now()",1);
				}
				function go_now () {
					top.frames['LDContent'].location.href = "<c:out value='${activity.activityURLs[0].url}' />";
				}
				document.load = redirectPage;
				//-->
			</script>
		
			<div align="center"> 
				<table width="95%" border="0" align="center" cellpadding="5" cellspacing="0" bgcolor="#9DC5EC">
					<tr>
						<td valign="top">
							<div align="center" class="heading"> 
								The next task is loading. Please wait....
							</div>
						</td>
					</tr>
				</table>
			</div>
			
		</c:if>
		
	</body>
</html:html>
