<!DOCTYPE html>
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
<%@ include file="/common/taglibs.jsp" %>
<%@ page import="org.lamsfoundation.lams.tool.rsrc.ResourceConstants"%>

<c:set var="initNavUrl"><c:url value="/pages/itemreview/initnav.jsp"/>?mode=${mode}&itemIndex=${itemIndex}&itemUid=${itemUid}&toolSessionID=${toolSessionID}&sessionMapID=${sessionMapID}</c:set>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
	
<lams:html>
<lams:head>
	<%@ include file="/common/header.jsp"%>

	<style media="screen,projection" type="text/css">
 	 	#collapseComments, #collapseComments .row {
	 		padding-left: 6px;
	 		padding-right: 6px;
	 	}
	 	
	 	#embedded-open-button, #download-button {
	 		display: none;
	 	}
	</style>

	<script>
		var isDownload = ${isDownload};
		
		$(document).ready(function(){
   			$('#headerFrame').load('${initNavUrl}');

   			if (isDownload) {
   				$('#download-button').show();
   				return;
   	   		}
   	   		
   			$('#embedded-open-button').show();

   			<c:if test="${not empty encodedResourceItemReviewUrl}">
	   			$.ajax({
	   			    url: "http://ckeditor.iframe.ly/api/oembed?url=${encodedResourceItemReviewUrl}",
	   			    dataType: "jsonp",
	   			    cache: true,
	   			    type: "POST",
	   			    jsonpCallback: 'iframelyCallback',
	   			    contentType: "application/json; charset=utf-8",
	   			    error: function (xhr, status, error) {
		   			    $('#embedded-open-button').removeClass('btn-default btn-sm pull-right').addClass('btn-primary');
	   			        console.log("Result: " + status + " " + error + " " + xhr.status + " " + xhr.statusText)
	   			    }
	   			});
   			</c:if>
   		});
		
		function setIframeHeight() {
			var frame = $('#embedded-content iframe');
			if (frame.length === 0) {
				return;
			}
			frame = frame[0];
			
		    var doc = frame.contentDocument? frame.contentDocument : frame.contentWindow.document,
	        	body = doc.body,
	        	html = doc.documentElement.
	        	height = Math.max(body.scrollHeight, body.offsetHeight, 
	            				  html.clientHeight, html.scrollHeight, html.offsetHeight);
		    frame.style.height = height + "px";
		}
		
		function iframelyCallback(response) {
			if (!response) {
				 $('#embedded-open-button').removeClass('btn-default btn-sm pull-right').addClass('btn-primary');
				return;
			}
			
			if (response.title) {
				$('#embedded-title').text(response.title);
			}
			if (response.description) {
				$('#embedded-description').text(response.description);
			}
			if (response.html) {
				$(response.html).appendTo('#embedded-content');
				setIframeHeight();
			}
		}
	</script>
</lams:head>

<body class="stripes">

	<lams:Page title="" type="learner" usePanel="false" hideProgressBar="${!sessionMap.runAuto}">
		<div class="panel panel-default">
 			<div class="panel-heading">
				<div id="headerFrame"></div>
			</div>
 			<c:if test="${allowComments and (mode eq 'learner' or mode eq 'author') and not empty toolSessionID}">
 				<c:set var="accordianTitle"><fmt:message key="label.comments"/></c:set>
				<lams:Comments toolSessionId="${toolSessionID}" toolSignature="<%=ResourceConstants.TOOL_SIGNATURE%>"
							  embedInAccordian="true" accordionTitle="${accordianTitle}" mode="${mode}" toolItemId="${itemUid}"
							  readOnly="${sessionMap.finishedLock}"/>	
			</c:if>
   			<div class="panel-body">
				<div class="text-center">
					<a id="download-button" href="<c:url value='${resourceItemReviewUrl}' />" class="btn btn-primary">
						<fmt:message key="label.download" />
					</a>
					<a id="embedded-open-button" href="${resourceItemReviewUrl}" target="_blank" class="btn btn-default btn-sm pull-right">
						<fmt:message key="open.in.new.window" />
					</a>
				</div>

   				<h4  id="embedded-title" class="clearfix"></h3>
   				<h5  id="embedded-description"></h4>
   				<div id="embedded-content"></div>
 			</div> 
   		</div>
	</lams:Page>

</body>	
</lams:html>
