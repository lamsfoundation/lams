<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>

<c:set var="KALTURA_SERVER"><%=Configuration.get(ConfigurationKeys.KALTURA_SERVER)%></c:set>
<c:set var="PARTNER_ID"><%=Configuration.get(ConfigurationKeys.KALTURA_PARTNER_ID)%></c:set>
<c:set var="SUB_PARTNER_ID"><%=Configuration.get(ConfigurationKeys.KALTURA_SUB_PARTNER_ID)%></c:set>
<c:set var="USER_SECRET"><%=Configuration.get(ConfigurationKeys.KALTURA_USER_SECRET)%></c:set>
<c:set var="KCW_UI_CONF_ID"><%=Configuration.get(ConfigurationKeys.KALTURA_KCW_UI_CONF_ID)%></c:set>
<c:set var="KDP_UI_CONF_ID"><%=Configuration.get(ConfigurationKeys.KALTURA_KDP_UI_CONF_ID)%></c:set>

<c:if test="${not empty param.sessionMapID}">
	<c:set var="sessionMapID" value="${param.sessionMapID}" />
</c:if>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<lams:html>
<lams:head>
	<lams:css />
		
	<link rel="stylesheet" href="includes/css/bootstrap.css"> 
	<link href="<lams:LAMSURL/>css/jquery-ui-smoothness-theme.css" rel="stylesheet">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui.timepicker.css">
	<style media="screen,projection" type="text/css">
		#content {width: 90%; margin-top: 10px;}
		.row .span8 {width: 410px;}
		.row .span4 {width: 300px;}
		#kplayer { height:360px; width:400px;}
		h2 { margin-bottom:5px;}
		h3 {color:black;}
		#related_files { width:100%; margin-top:-10px;}
		#related_files label { text-align:left; white-space:nowrap; cursor:pointer;}
		#related_files input { cursor:default;}
		#timeline {height:140px; width:700px; margin-top: 30px; display:none;}
		#timeline-container{ height:140px;}
		#create-actions { margin: 15px 0 5px; text-align:center; width: 700px;}
		#create-actions button.btn { margin-left:10px; #padding-bottom:5px;}
		#cuepoint-comment-container, #cuepoint-comment-container textarea {width: 270px;}
		#comment-author {float:right; font-style: italic; margin-bottom: 10px; color: grey; margin-right: -8px; margin-top: 5px;}
		.ui-datepicker-current {visibility: hidden;}
		#duration {width: 70px;}
	</style>
	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/common.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.timepicker.js"></script>
	<script type="text/javascript" src="includes/javascript/jquery.tools.flashembed.js"></script>
	<script type="text/javascript">
		var previewCuePointsUrl = "<c:url value='/learning.do'/>?dispatch=previewCuePoints&sessionMapID=${sessionMapID}&itemUid=${itemUid}&kalturaSession=${sessionMap.kalturaSession}";
		var isCreationPhase = true;
		var userId = "<lams:user property='userID'/>";
		var userFullName = "<lams:user property='firstName'/> <lams:user property='lastName'/>"
		var isDisplayCuePointComments = true;
		var isMonitor = ${sessionMap.isGroupMonitoring == true};
		var USER_ID  = "${sessionMap.toolSessionID}";//constant used for tracking user info
	</script>
	<script type="text/javascript" src="includes/javascript/CodeCuePoints.create.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/kaltura.min.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/swfobject.js"></script>
		
	<script type="text/javascript">
		
		//Specify Kaltura settings
		//kaltura server 
		var KALTURA_SERVER = "${KALTURA_SERVER}";
		//your actual parner id
		var PARTNER_ID = "${PARTNER_ID}";
		//your actual subparner id
		var SUB_PARTNER_ID = "${SUB_PARTNER_ID}";
		//your actual user secret
		var USER_SECRET = "${USER_SECRET}";
		//ui_conf_id of Kaltura Contribution Wizard(KCW)
		var KCW_UI_CONF_ID = "${KCW_UI_CONF_ID}";
		//ui_conf_id of Kaltura Dynamic Player(KDP)
		var KDP_UI_CONF_ID = "${KDP_UI_CONF_ID}";
		var KCLIP_UI_CONF_ID = "5730131";
			
		$(document).ready(function(){
			
			$("#duration").datetimepicker({
				timeText: "Duration",
				timeOnlyTitle: "Choose Duration",
				showHour: false,
				showSecond: true,
				timeOnly: true,
				timeFormat: "mm:ss",
				onClose: function(dateText, inst) {
					KApps.CodeCuePoints.updateCuepoint();
				}
			});
			
			$("#timeline").css("display", "block");
			
			$("#close-button").click(function() {
				var kplayerDataAttr = $("#kplayer", parent.document).attr("data");
				$("#kplayer", parent.document).attr("data", kplayerDataAttr);
				self.parent.tb_remove();
			});

		});
		
		// kclip settings
		KApps.CodeCuePoints.vars.ks			= "${sessionMap.kalturaSession}";
		KApps.CodeCuePoints.vars.entry_id	= "${item.entryId}";
		
		// kdp configuration
		var params = {
			allowscriptaccess: "always",
			allownetworking: "all",
			allowfullscreen: "true",
			wmode: "opaque"
		};	
		var flashVars = {
			entryId:  "${item.entryId}",
			// enable kdp-js interaction:
			"externalInterfaceDisabled" : "false",
			"ks"						: "${sessionMap.kalturaSession}"
		};
		swfobject.embedSWF(KALTURA_SERVER + '/kwidget/wid/_' + PARTNER_ID + '/uiconf_id/' + KDP_UI_CONF_ID, "kplayer", "100%", "100%", "9.0.0", "includes/expressInstall.swf", flashVars, params);

	</script>
</lams:head>

<body class="stripes" onload="parent.resizeIframe();">
	<div id="content">
		<h1>
			${item.title}
		</h1>
			
		<br>
			
		<div id="kwrap">
			<div class="row">
				<div class="span8 columns">
					<div id="kplayer">
					</div>

				</div>
				<div class="span4 columns">
					<form id="related_files">
						<h3><fmt:message key="label.related.comment" /></h3>

						<p><fmt:message key="label.comments.will.appear" /></p>
						
						<div id="cuepoint-comment-container">
							<textarea id="cuepoint-comment" name="cuepoint-comment" rows="5" value="" cols="100" disabled></textarea>
							
							<div id="comment-author">&nbsp;</div>
							
							<div style="clear: both;">
							 
							 	<input type="text" name="duration" id="duration" value="" disabled/>
							 	
							 	<span><fmt:message key="label.duration" /></span>
							</div>
						</div>
					</form>
				</div>
			</div>

			<div id="timeline-container">
			<div id="timeline">
				<script>
					flashembed("timeline",
						{ // attributes and params:
							src				: KALTURA_SERVER + "/kgeneric/ui_conf_id/" + KCLIP_UI_CONF_ID,
							wmode			: "opaque",
							bgcolor			: "#f8f8f8",
							allowNetworking	: "all",
							version			: [10,0],
							expressInstall	: "http://cdn.kaltura.org/apis/seo/expressinstall.swf"
						},
						{ // flashvars:
							jsReadyFunc		: "KApps.CodeCuePoints.kclipReady"
						}
					);
				</script>
			</div>
			</div>
			
			<div id="create-actions">
				<a href="#nogo" id="save_all" class="button space-left disabled"><fmt:message key="label.save.changes" /></a>
				<a href="#nogo" id="reload" class="button space-left disabled"><fmt:message key="label.reaload.reset" /></a>
				<a href="#nogo" id="preview" class="button space-left disabled"><fmt:message key="label.view.finished.result" /></a>
			</div>
		</div><!-- #kwrap -->
			
		<lams:ImgButtonWrapper>
			<a href="#nogo" id="close-button" onmousedown="self.focus();" class="button space-left float-right">
				<fmt:message key="label.close" /> 
			</a>
		</lams:ImgButtonWrapper>
			
	</div>
		
	<div id="footer">
	</div>
		
</body>
</lams:html>
