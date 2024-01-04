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
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/lottie/lottie-player.js"></script>
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

		<header id="header" class="d-flex justify-content-between align-items-center mt-n5 ms-n5 me-n5" role="banner">
			<div class="d-flex align-items-center w-100">
				<button class="btn btn-light no-decoration disabled" id="hamb" type="button" disabled>
					<i class="fa-solid fa-fw fa-bars" aria-hidden="true"></i>
				</button>

				<h1 id="lesson-name" class="placeholder-wave flex-grow-2 col-md-6 col-9">
					<span class="placeholder placeholder-sm bg-secondary bg-opacity-50 col-5"></span>
					<span class="placeholder placeholder-sm bg-secondary bg-opacity-50 col-4"></span>
				</h1>
			</div>

			<div class="top-menu">
				<button id="profile-picture" class="btn btn-light no-decoration px-3 disabled" type="button" disabled>
					<img class="portrait-sm portrait-round" src="http://localhost:8080/lams/images/css/john-doe-portrait.jpg" alt="Your profile portrait">
				</button>
		                    
				<button type="button" id="progress-bar-widget" class="btn btn-light no-decoration d-none d-sm-none d-md-block disabled" disabled>
					<div class="row m-0">
						<div class="col-6 text-start p-0">
							<fmt:message key='label.progress'/>
						</div>
						<div class="col-6 text-end p-0" id="progress-bar-widget-value">0%</div>
					</div>
					<div class="progress mt-1 mb-2">
						<div class="progress-bar bg-success" role="progressbar" aria-valuemin="0" aria-valuemax="100" aria-valuenow="0" style="width: 0%;"></div>
					</div>
				</button>

				<c:if test="${not isIntegrationLogin}"> 
					<a href="/" id="return-to-index" class="btn btn-light btn-close btn-sm disabled" disabled></a>
				</c:if>
			</div>
		</header>
			
		<div id="container-main">	
			<div class="text-center mx-auto" aria-live="assertive">
				<lottie-player
				  autoplay
				  loop
				  mode="normal"
				  src="<lams:LAMSURL/>includes/javascript/lottie/loading_sphere.json"
				  style="width: 320px; height: 200px; margin-top: 5rem;"
				  class="mx-auto"
				>
				</lottie-player>
			
				<h2 class="visually-hidden">
					<fmt:message key="message.activity.loading" />
				</h2>
			</div>
		</div>
	</c:if>
</lams:PageLearner>
