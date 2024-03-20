<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.imageGallery.ImageGalleryConstants"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<%@ page import="org.lamsfoundation.lams.util.FileUtil" %>

<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" scope="request"/>
<c:set var="summaryList" value="${sessionMap.summaryList}" scope="request"/>
<c:choose>
	<c:when test="${sessionMap.mode == 'learner'}">
		<c:set var="UPLOAD_FILE_MAX_SIZE"><%=Configuration.get(ConfigurationKeys.UPLOAD_FILE_MAX_SIZE)%></c:set>
	</c:when>
	<c:otherwise>
		<c:set var="UPLOAD_FILE_MAX_SIZE"><%=Configuration.get(ConfigurationKeys.UPLOAD_FILE_LARGE_MAX_SIZE)%></c:set>
	</c:otherwise>
</c:choose>
<c:set var="ALLOWED_EXTENSIONS_IMAGE"><%=FileUtil.ALLOWED_EXTENSIONS_IMAGE%></c:set>
<c:set var="language"><lams:user property="localeLanguage"/></c:set>

<c:set var="title"><fmt:message key="label.monitoring.title" /></c:set>
<lams:PageMonitor title="${title}" 
		helpToolSignature="<%= ImageGalleryConstants.TOOL_SIGNATURE %>"
		initialTabId="${initialTabId}">

		<link href="${lams}css/thickbox.css" rel="stylesheet" type="text/css">
		<link href="${lams}css/rating.css" rel="stylesheet" type="text/css">
		<link href="${lams}css/jquery.tablesorter.theme.bootstrap5.css" rel="stylesheet" >
		<link rel="stylesheet" href="${lams}css/jquery.tablesorter.pager5.css">
		<link href="${lams}css/uppy.min.css" rel="stylesheet" type="text/css" />
		<link href="${lams}css/uppy.custom.css" rel="stylesheet" type="text/css" />
		<style media="screen,projection" type="text/css">
			.tablesorter,.tablesorter tr {
				border: 1px solid #ddd;
			}
			.tablesorter td {
				padding-bottom: 10px;
				padding-top: 10px;
			}
			#manage-image-buttons {
				margin-bottom: 10px;
				overflow: hidden;
			}
		</style>
		
		<script type="text/javascript">
			//vars for rating.js
			var AVG_RATING_LABEL = '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.average.rating"><fmt:param>@1@</fmt:param><fmt:param>@2@</fmt:param></fmt:message></spring:escapeBody>',
			YOUR_RATING_LABEL = '',
			IS_DISABLED =  true,
			COMMENTS_MIN_WORDS_LIMIT = 0,
			MAX_RATES = 0,
			MIN_RATES = 0,
			LAMS_URL = '${lams}',
			COUNT_RATED_ITEMS = 0,
			COMMENT_TEXTAREA_TIP_LABEL = '',
			WARN_COMMENTS_IS_BLANK_LABEL = '',
			WARN_MIN_NUMBER_WORDS_LABEL = '';

			<%-- used for  imageGalleryitem.js --%>
			var UPLOAD_FILE_LARGE_MAX_SIZE = "${UPLOAD_FILE_MAX_SIZE}";
			// convert Java syntax to JSON
			var UPLOAD_ALLOWED_EXTENSIONS = JSON.parse("[" + "${ALLOWED_EXTENSIONS_IMAGE}".replace(/\.\w+/g, '"$&"') + "]");
			var LABEL_ITEM_BLANK = '<spring:escapeBody javaScriptEscape="true"><fmt:message key="error.resource.item.file.blank"/></spring:escapeBody>';
		</script>
		<lams:JSImport src="includes/javascript/imageGallerycommon.js" relative="true" />
		<lams:JSImport src="includes/javascript/imageGalleryitem.js" relative="true" />
		<%@ include file="/common/uppylang.jsp"%>
		<lams:JSImport src="includes/javascript/uploadImageLearning.js" relative="true" />
    	<lams:JSImport src="includes/javascript/upload.js" />
 		<script type="text/javascript" src="${lams}includes/javascript/thickbox.js"></script>
		<lams:JSImport src="includes/javascript/monitorToolSummaryAdvanced.js" />
		<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter.js"></script>
		<lams:JSImport src="includes/javascript/rating.js" />
		<script type="text/javascript" src="${lams}/includes/javascript/portrait5.js" ></script>
		<script>
			function viewItem(imageUid,sessionMapID){
				var myUrl = "<c:url value="/reviewItem.do"/>?mode=teacher&imageUid=" + imageUid + "&sessionMapID="+sessionMapID;
				launchPopup(myUrl,"MonitoringReview");
			}
			
			function checkNew(){
				location.reload();
			}
			
			$(document).ready(function(){
				doStatistic();
				initializePortraitPopover('${lams}');

				$(".tablesorter").tablesorter({
					theme: 'bootstrap',
					headerTemplate : '{content} {icon}',
				    widgets: ["uitheme", "resizable", "filter"],
			        headers: {
			            0: { sorter: false }
			            <c:if test="${sessionMap.imageGallery.allowRank || sessionMap.imageGallery.allowVote}"> 
			            	, 3: { sorter: false }
			            </c:if>
			        } 
				});
				
				$(".toggle-image-visibility").click(function() {
					var imageToggleLink = $(this);
					var imageUid = $(this).data("image-uid");
					
					$.ajax({
				    	type: 'POST',
				    	url: "<c:url value='/monitoring/toggleImageVisibility.do'/>?<csrf:token/>",
				    	data : {
							'imageUid' : imageUid,
							'sessionMapID' : '${sessionMapID}'
						},
				    	success: function(data) {
				    		var isHidden = imageToggleLink.data("is-hidden");
				    		imageToggleLink.data("is-hidden", !isHidden);
				    		
				    		var imageToggleLinkText = isHidden ? 
						    		"<i class='fa-regular fa-eye-slash me-1'></i> <spring:escapeBody javaScriptEscape="true"><fmt:message key='monitoring.label.hide' /></spring:escapeBody>" 
						    		: "<i class='fa-regular fa-eye me-1'></i> <spring:escapeBody javaScriptEscape="true"><fmt:message key='monitoring.label.show' /></spring:escapeBody>";
				    		imageToggleLink.html(imageToggleLinkText);
				    	},
				    	error: function(jqXHR, textStatus, errorMessage) {   		
				        	alert(errorMessage);
				    	}
					});
					
				});

				function resizeIframe() {
					if (document.getElementById('TB_iframeContent') != null) {
					    var height = top.window.innerHeight;
					    if ( height == undefined || height == 0 ) {
					    	// IE doesn't use window.innerHeight.
					    	height = document.documentElement.clientHeight;
					    	// alert("using clientHeight");
					    }
						// alert("doc height "+height);
					    height -= document.getElementById('TB_iframeContent').offsetTop + 100;
					    document.getElementById('TB_iframeContent').style.height = height +"px";
				
						TB_HEIGHT = height + 28;
						tb_position();
					}
				};
				window.onresize = resizeIframe;
			});
		</script>		 
</lams:PageMonitor>