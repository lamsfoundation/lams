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
			isPage = ${isDisplayablePage},
			panel = $('#item-content-${itemUid}');

		if (isDownload) {
			$('.download-button', panel).removeClass('hidden');
			return;
   		}
   		if (isPage) {
   			$('.iframe-open-button', panel).removeClass('hidden');
   	   		return;
   	   	}
   		
		$.ajax({
		    url: "https://ckeditor.iframe.ly/api/oembed?url=" + encodeURIComponent("${resourceItemReviewUrl}"),
		    dataType: "jsonp",
		    cache: true,
		    type: "POST",
		    jsonpCallback: 'iframelyCallback${itemUid}',
		    contentType: "application/json; charset=utf-8",
		    error: function (xhr, status, error) {
			    $('.embedded-open-button', panel).removeClass('btn-secondary hidden btn-sm float-end').addClass('btn-primary');
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
	
	<div class="content-panel">
	
		<div class="text-center">
			<a href="<c:url value='${resourceItemReviewUrl}' />&preferDownload=true" class="download-button hidden btn btn-primary">
				<fmt:message key="label.download" />
			</a>
			<a href="${resourceItemReviewUrl}" target="_blank" class="embedded-open-button hidden btn btn-secondary btn-sm float-end">
				<i class="fa fa-external-link" aria-hidden="true"></i>
				<fmt:message key="open.in.new.window" />
			</a>
			<a href="<c:url value='${resourceItemReviewUrl}'/>" target="_blank" class="iframe-open-button hidden btn btn-secondary btn-sm float-end">
				<i class="fa fa-external-link" aria-hidden="true"></i>
				<fmt:message key="open.in.new.window" />
			</a>
		</div>

		<a title="<fmt:message key='open.in.new.window' />" class="embedded-title" href="${resourceItemReviewUrl}"  target="_blank"   ></a>&nbsp;&nbsp;
		<i title="<fmt:message key='open.in.new.window' />" class="new-window-icon fa fa-1 hidden fa-external-link" aria-hidden="true"></i>
		<div class="embedded-description"></div>
		<div class="embedded-content">
		
			<c:if test="${isDisplayableImage}">
				<div class="embedded-file">
					<img src="<c:url value='${resourceItemReviewUrl}' />&preferDownload=false" />
				</div>
			</c:if>
			<c:if test="${isDisplayableMedia}">
				<div class="embedded-file">
					<video playsinline controls src="<c:url value='${resourceItemReviewUrl}' />&preferDownload=false"></video>
				</div>
			</c:if>
			<c:if test="${isDisplayableEmbed}">
				<div class="embedded-file">
					<embed src="<c:url value='${resourceItemReviewUrl}' />&preferDownload=false#toolbar=0" />
				</div>
			</c:if>		
			<c:if test="${isDisplayablePage}">
				<iframe src="<c:url value='${resourceItemReviewUrl}'/>"></iframe>
			</c:if>		
		</div>

	</div>
	<hr>
	<c:if test="${mode eq 'learner' or mode eq 'author'}">
		<c:if test="${sessionMap.rateItems && allowRating}">
			<lams:Rating5 itemRatingDto="${ratingDTO}" 
						 disabled="${mode == 'teacher' || finishedLock}" allowRetries="true" />
		</c:if>

		<c:if test="${allowComments and not empty toolSessionID}">
			<c:set var="accordianTitle"><fmt:message key="label.comments"/></c:set>
			<lams:Comments toolSessionId="${toolSessionID}" toolSignature="<%=ResourceConstants.TOOL_SIGNATURE%>"
						  embedInAccordian="true" accordionTitle="${accordianTitle}" mode="${mode}" toolItemId="${itemUid}"
						  readOnly="${finishedLock}"/>	
		</c:if>
	</c:if>	
</div>
