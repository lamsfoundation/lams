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

<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="mode" value="${sessionMap.mode}" />
<c:set var="finishedLock" value="${sessionMap.finishedLock}" />

<script>
	$(document).ready(function(){

		<c:if test="${sessionMap.rateItems}">
			initializeJRating();
		</c:if>
		
		var isDownload = ${isDownload},
			panel = $('#item-content-${itemUid}');

		if (isDownload) {
			$('.download-button', panel).removeClass('hidden');
			return;
   		}
   		
		$('#embedded-open-button', panel).removeClass('hidden');
		$.ajax({
		    url: "http://ckeditor.iframe.ly/api/oembed?url=" + encodeURIComponent("${resourceItemReviewUrl}"),
		    dataType: "jsonp",
		    cache: true,
		    type: "POST",
		    jsonpCallback: 'iframelyCallback${itemUid}',
		    contentType: "application/json; charset=utf-8",
		    error: function (xhr, status, error) {
			    	$('.embedded-open-button', panel).removeClass('btn-default btn-sm pull-right').addClass('btn-primary');
		        console.log("Result: " + status + " " + error + " " + xhr.status + " " + xhr.statusText)
		    }
		});
  	});
	
	function iframelyCallback${itemUid}(response) {
		iframelyCallback(${itemUid}, response);
	}
</script>
	
<div id="item-content-${itemUid}" class="item-content">
	<c:if test="${not empty instructions}">
		<div class="item-instructions">
			<c:out value="${instructions}" escapeXml="false" />
		</div>
	</c:if>
	
	<c:if test="${mode eq 'learner' or mode eq 'author'}">
		<c:if test="${sessionMap.rateItems && allowRating}">
			<lams:Rating itemRatingDto="${ratingDTO}" 
						 disabled="${mode == 'teacher' || finishedLock}" allowRetries="true" />
		</c:if>

		<c:if test="${allowComments and not empty toolSessionID}">
			<c:set var="accordianTitle"><fmt:message key="label.comments"/></c:set>
			<lams:Comments toolSessionId="${toolSessionID}" toolSignature="<%=ResourceConstants.TOOL_SIGNATURE%>"
						  embedInAccordian="true" accordionTitle="${accordianTitle}" mode="${mode}" toolItemId="${itemUid}"
						  readOnly="${finishedLock}"/>	
		</c:if>
	</c:if>
	
	<div class="content-panel">
		<div class="text-center">
			<a href="<c:url value='${resourceItemReviewUrl}' />" class="download-button hidden btn btn-primary">
				<fmt:message key="label.download" />
			</a>
			<a href="${resourceItemReviewUrl}" target="_blank" class="embedded-open-button hidden btn btn-default btn-sm pull-right">
				<fmt:message key="open.in.new.window" />
			</a>
		</div>
	
		<h4  class="embedded-title"></h3>
		<h5  class="embedded-description"></h4>
		<div class="embedded-content"></div>
	</div>
</div>