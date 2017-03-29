<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.imageGallery.ImageGalleryConstants"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>

<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:choose>
	<c:when test="${sessionMap.mode == 'learner'}">
		<c:set var="UPLOAD_FILE_MAX_SIZE"><%=Configuration.get(ConfigurationKeys.UPLOAD_FILE_MAX_SIZE)%></c:set>
	</c:when>
	<c:otherwise>
		<c:set var="UPLOAD_FILE_MAX_SIZE"><%=Configuration.get(ConfigurationKeys.UPLOAD_FILE_LARGE_MAX_SIZE)%></c:set>
	</c:otherwise>
</c:choose>

<lams:html>
	<lams:head>
		<title>
			<fmt:message key="label.monitoring.title" />
		</title>
		<%@ include file="/common/tabbedheader.jsp" %>
		
		<link href="${lams}css/thickbox.css" rel="stylesheet" type="text/css" media="screen">
		<link href="${lams}css/jquery.jRating.css" rel="stylesheet"/>
		<link href="${lams}css/jquery.tablesorter.theme.bootstrap.css" rel="stylesheet" >
		<link rel="stylesheet" href="${lams}css/jquery.tablesorter.pager.css">
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
			//var for jquery.jRating.js
			var pathToImageFolder = "${lams}images/css/";
		
			//vars for rating.js
			var AVG_RATING_LABEL = '<fmt:message key="label.average.rating"><fmt:param>@1@</fmt:param><fmt:param>@2@</fmt:param></fmt:message>',
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
			var saveMultipleImagesUrl = "<c:url value='/learning/saveMultipleImages.do'/>";
			var UPLOAD_FILE_LARGE_MAX_SIZE = "${UPLOAD_FILE_MAX_SIZE}";
			var LABEL_ITEM_BLANK = '<fmt:message key="error.resource.item.file.blank"/>';
			var LABEL_MAX_FILE_SIZE = '<fmt:message key="errors.maxfilesize"><param>{0}</param></fmt:message>';
			var LABEL_NOT_ALLOWED_FORMAT = '<fmt:message key="error.resource.image.not.alowed.format"/>';
		</script>
		<script type="text/javascript" src="<html:rewrite page='/includes/javascript/imageGalleryitem.js'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/includes/javascript/uploadImageLearning.js'/>"></script>
    	<script type="text/javascript" src="${lams}includes/javascript/upload.js"></script>
 		<script type="text/javascript" src="${lams}includes/javascript/thickbox.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/monitorToolSummaryAdvanced.js" ></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/rating.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery.jRating.js"></script>
		<script>
			var initialTabId = "${initialTabId}";
	
			function init(){
				if (initialTabId) {
					selectTab(initialTabId);
				} else {
					selectTab(1);
				}
		   	}     
		        
		   	function doSelectTab(tabId) {
				// end optional tab controller stuff
			    selectTab(tabId);
		    }
		   	
		   	function onSelectTab(id) {
		   		return;
		   	}
		        
			function viewItem(imageUid,sessionMapID){
				var myUrl = "<c:url value="/reviewItem.do"/>?mode=teacher&imageUid=" + imageUid + "&sessionMapID="+sessionMapID;
				launchPopup(myUrl,"MonitoringReview");
			}
			
			function checkNew(){
				location.reload();
			}			
			
			$(document).ready(function(){

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
	</lams:head>
	
	<body class="stripes" onLoad="init()">
		<c:set var="title"><fmt:message key="label.learning.heading" /></c:set>
	 	<lams:Page type="navbar" title="${title}"> 
	
			<lams:Tabs control="true" title="${title}" helpToolSignature="<%= ImageGalleryConstants.TOOL_SIGNATURE %>" helpModule="monitoring" refreshOnClickAction="javascript:location.reload();">
				<lams:Tab id="1" key="monitoring.tab.summary"/>
				<lams:Tab id="2" key="monitoring.tab.edit.activity"/>
				<lams:Tab id="3" key="monitoring.tab.statistics"/>
			</lams:Tabs>
	
	  		<lams:TabBodyArea>
				<lams:TabBodys>
					<lams:TabBody id="1" titleKey="monitoring.tab.summary" page="summary.jsp" />
					<lams:TabBody id="2" titleKey="monitoring.tab.edit.activity" page="editactivity.jsp" />			
					<lams:TabBody id="3" titleKey="monitoring.tab.statistics" page="statistic.jsp" />
				</lams:TabBodys> 
			</lams:TabBodyArea> 
	
			<div id="footer"></div>
		</lams:Page>
</body>
</lams:html>
