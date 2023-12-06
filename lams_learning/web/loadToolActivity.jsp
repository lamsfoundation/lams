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

<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="learnerAppUrl"><lams:WebAppURL /></c:set>
<c:set var="title"><fmt:message key="learner.title" /></c:set>

<lams:PageLearner title="${title}" toolSessionID="" hideHeader="true" hideTitle="true">
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/nprogress.js"></script>		
	<c:if test="${!empty activityForm.activityURLs}">
		<script type="text/javascript">
			function redirectPage() {
				NProgress.configure({
					easing : 'ease',
					speed : 30
				});
				NProgress.configure({
					showSpinner: false, 
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
					
			$(document).ready(function(){
				// submit lesson total mark to the integrated server in case request comes from an integrated server
				if (${not empty activityFinishUrl}) {
					$.ajax({
					    url: '${activityFinishUrl}',
					    type: "POST",
					    dataType: 'html',
						cache: false,
						async: 'false',
					    error: function (ajaxContext) {
					        alert("There was an error on trying to submit activity mark to the integrated server: " + ajaxContext.responseText)
					    }
					});
				}
						
				redirectPage();
			});
		</script>
			
		<div id="container-main">
			<div class="text-center mt-4">
				<button class="btn btn-outline-secondary btn-lg" type="button" disabled>
					<span class="spinner-border me-2" role="status" aria-hidden="true" style="width: 1.35rem; height: 1.35rem;"></span>
					<fmt:message key="message.activity.loading" />
				</button>
			</div>
			
			<div class="text-center placeholder-glow">
				<span class="placeholder col-12 col-md-10 placeholder-xs mt-5 mb-3"></span>
			    <c:forEach var="i" begin="1" end="7">
			    	<span class="placeholder col-12 col-md-10 placeholder-xs my-3"></span>
			    </c:forEach>
			</div>
		</div>
	</c:if>
</lams:PageLearner>
