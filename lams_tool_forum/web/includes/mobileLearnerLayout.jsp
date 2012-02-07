<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

<%@ taglib uri="tags-tiles" prefix="tiles"%>
<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>
<lams:html>
	<lams:head>
		<title><fmt:message key="activity.title" /></title>
		
		<style type="text/css">
/*layout of the table with the alternate color for the table heading cell */
table.alternative-color {
 	 width:100%; 
	 margin-left:0px; 
	 padding-top:0px; 
	 margin-bottom:15px; 
	 text-align:left;
}

table.alternative-color th {

	font-size: 12px;
	height: 30px;
}

table.alternative-color td {
	padding:5px;
	padding-left: 2px;
	font-size: 11px;
	border-bottom: 1px solid #efefef;
	vertical-align: middle; 
	background:url('../images/css/greyfade_bg.jpg') repeat-x 3px 0px
}

table.alternative-color td.first, table.alternative-color th.first {
	border-left: none;
	padding-left: 2px;
}
table.alternative-color .ui-btn-up-c {
font-weight: normal; cursor: default;
}
b.error {color:#cc0000;} 

/*layout of the forum tables with the colored table heading cell */
table.forum { 
	width:93%;
	margin-bottom:10px;
	background:url('../images/css/greyfade_bg.jpg') repeat-x 3px 49px; 
	text-align:left; 
	border-bottom:1px solid #efefef;}

table.forum th { 
	height:20px; 
	padding-top:5px; 
	padding-left:15px; 
	border-left:1px solid #cacdd1;  
	font-size:12px; 
	vertical-align:middle;
}

table.forum td.posted-by {
	background: url('../images/css/forum_postedby_bg.jpg') repeat-x; 
	height:20px; 
	padding-left:15px; 
	padding-top:0px; 
	font-size:10px;
	vertical-align:top; 
	border-left:1px solid #cacdd1; 
	color:#666;
}

table.forum td {
	padding-left:20px; 
	font-size:11px; 
	vertical-align:top; 
}

.ui-btn-hidden {line-height:0px !important;}
		</style>

		<!-- ********************  javascript from header.jsp ********************** -->
		<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/tabcontroller.js"></script>
		<script type="text/javascript">
			function closeAndRefreshParentMonitoringWindow() {
				refreshParentMonitoringWindow();
				window.close();
			}  				
		</script>
		<!-- End of javascript from header.jsp -->
		
		<script type="text/javascript">
			var removeItemAttachmentUrl = "<html:rewrite page="/learning/deleteAttachment.do" />";
		</script>		
		<script type="text/javascript" src="${tool}includes/javascript/message.js"></script>
		<script type="text/javascript">
			function removeAtt(mapID){
				removeItemAttachmentUrl =  removeItemAttachmentUrl + "?sessionMapID="+ mapID;
				removeItemAttachment();
			}
		</script>	
		
		<link rel="stylesheet" href="${lams}css/jquery.mobile.css" />
		<script src="${lams}includes/javascript/jquery-1.7.1.min.js"></script>
		<script src="${lams}includes/javascript/jquery.mobile.js"></script>		
		
	</lams:head>
	<body>
		<tiles:insert attribute="bodyMobile" />
	</body>
</lams:html>
