<%--
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
USA

http://www.gnu.org/licenses/gpl.txt
--%>
 
<%@ page language="java"%>
<%@ taglib uri="http://jakarta.apache.org/struts/struts-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/struts-html" prefix="html"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<body bgcolor="#9DC5EC">
	<c:if test="${!empty activityForm.activityURLs}">
		
		<script language="JavaScript" type="text/JavaScript"><!--
			function redirectPage() {
				setTimeout("doRedirect()", 500);
			}
			function doRedirect() {
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
							<fmt:message key="message.activity.loading" />
						</div>
					</td>
				</tr>
			</table>
		</div>
			
	</c:if>
</body>
