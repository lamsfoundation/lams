 
<%@ page language="java"%>
<%@ taglib uri="http://jakarta.apache.org/struts/struts-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/struts-html" prefix="html"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>

<body bgcolor="#9DC5EC">
	<c:if test="${!empty activityForm.activityURLs}">
		
		<script language="JavaScript" type="text/JavaScript"><!--
			function redirectPage() {
				setTimeout("doRedirect()", 1000);
			}
			function doRedirect() {
				//top.frames['LDContent'].location.href = "<c:out value='${activityForm.activityURLs[0].url}' />";
				window.location.href = "<c:out value='${activityForm.activityURLs[0].url}' escapeXml="false" />";
			}
			window.onload = redirectPage;
			//-->
		</script>
		
		<div align="center">
			<table width="100%" border="0" align="center" bgcolor="#9DC5EC" summary="This table is being used for layout purposes">
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
