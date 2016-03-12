<%-- 
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
License Information: http://lamsfoundation.org/licensing/lams/2.0/

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License version 2 as 
  published by the Free Software Foundation.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
  USA

  http://www.gnu.org/licenses/gpl.txt
--%>
<!-- Load Tool Activity (comment needed for the test harness) -->

<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="tags-bean" prefix="bean"%>
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<c:set var="learnerAppUrl">
	<lams:WebAppURL />
</c:set>
<link href="${learnerAppUrl}css/nprogress.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${learnerAppUrl}includes/javascript/nprogress.js"></script>



<c:if test="${!empty activityForm.activityURLs}">

	<script type="text/javascript">
		function redirectPage() {
			NProgress.configure({
				easing : 'ease',
				speed : 30
			});
			NProgress.configure({
				trickleRate : 0.2,
				trickleSpeed : 110
			});
			NProgress.start();
			setTimeout("doRedirect()", 1000);

		}

		function doRedirect() {
			var url = "<c:out value='${activityForm.activityURLs[0].url}' escapeXml="false" />";
			if (url.substring(0, 4) != "http") {
				if (url.substring(0, 1) == "/") {
					url = "${learnerAppUrl}.." + url;
				} else {
					url = "${learnerAppUrl}../" + url;
				}
			}
			window.location.href = url;
		}
	</script>

	<script language="JavaScript" type="text/JavaScript">
		window.onload = redirectPage;
	</script>

	<lams:Page type="admin">
		<div class="text-center" style="margin-top: 10px; margin-bottom: 15px;">
			<i class="fa fa-2x fa-refresh fa-spin text-primary"></i>
			<p class="voffset5">
				<fmt:message key="message.activity.loading" />
			</p>
		</div>
	</lams:Page>
	 

</c:if>
