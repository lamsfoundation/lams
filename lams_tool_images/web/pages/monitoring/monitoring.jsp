<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.imageGallery.ImageGalleryConstants"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>

<lams:html>
	<lams:head>
		<title>
			<fmt:message key="label.monitoring.title" />
		</title>
		<%@ include file="/common/tabbedheader.jsp" %>
		
		<link href="${lams}css/thickbox.css" rel="stylesheet" type="text/css" media="screen">
		<link href="${lams}css/jquery.jRating.css" rel="stylesheet"/>
		<link href="${lams}css/jquery.tablesorter.theme-blue.css" rel="stylesheet" >
		<link rel="stylesheet" href="${lams}css/jquery.tablesorter.pager.css">
		<style media="screen,projection" type="text/css">
			.tablesorter {
				margin: 15px 10px 5px;
				width: 97%;
			}
			
			.tablesorter tbody > tr:nth-child(odd) > td, .tablesorter tbody > tr:nth-child(odd) > th {
			    background-color: #EBF2FA;
			}
			.tablesorter tbody > tr:nth-child(odd):hover > td, .tablesorter tbody > tr:nth-child(odd):hover > th {
			    background-color: #bfbfbf;
			}
			.tablesorter tr:nth-child(odd):hover .jStar {background-image: url(${lams}images/css/jquery.jRating-stars-grey.png)!important;}
			.tablesorter tr:nth-child(even):hover .jStar {background-image: url(${lams}images/css/jquery.jRating-stars-light-grey.png)!important;}
			.tablesorter tr:nth-child(odd) .jStar {background-image: url(${lams}images/css/jquery.jRating-stars-light-blue.png)!important;}
			
			h4 {
				color:#0087e5; 
				font-size:11px;
				margin-top:10px;
				margin-bottom:10px;
				padding:0;
			}
			.rating-comment-posted-by {
				color: #47BC23;
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
		</script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
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
			
			$(document).ready(function(){

				$(".tablesorter").tablesorter({
					theme: 'blue',
				    widthFixed: true,
				    widgets: ['zebra'],
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
		<div id="page">
			<h1>
				<fmt:message key="label.authoring.heading" />
			</h1>
			<div id="header">
				<lams:Tabs>
					<lams:Tab id="1" key="monitoring.tab.summary" />
					<lams:Tab id="2" key="monitoring.tab.edit.activity" />			
					<lams:Tab id="3" key="monitoring.tab.statistics" />
				</lams:Tabs>
			</div>
			<div id="content">
				<lams:help toolSignature="<%= ImageGalleryConstants.TOOL_SIGNATURE %>" module="monitoring"/>
			
				<lams:TabBody id="1" titleKey="monitoring.tab.summary" page="summary.jsp" />
				<lams:TabBody id="2" titleKey="monitoring.tab.edit.activity" page="editactivity.jsp" />			
				<lams:TabBody id="3" titleKey="monitoring.tab.statistics" page="statistic.jsp" />
			</div>
			<div id="footer"></div>
		
		</div>
	</body>
</lams:html>
