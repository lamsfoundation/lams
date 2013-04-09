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
	<style media="screen,projection" type="text/css">
		#content {width: 90%; margin-top: 10px;}
		.row .span8 {width: 410px;}
		#kplayer { height:360px; width:400px;}
		#cuepoint-comments { height:70px; width:700px; overflow: auto; font-size: 18px; line-height: 23px;}
		#cuepoint-comments-container { height:70px;}
	</style>

	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/common.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/kaltura.min.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/swfobject.js"></script>
	<script type="text/javascript">
		var isCreationPhase = false;
		var isDisplayCuePointComments = true;
	</script>
	<script src="includes/javascript/CodeCuePoints.create.js"></script>
	<script>
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
	var KDP_UI_CONF_ID = "5692401";
	var KCLIP_UI_CONF_ID = "5730131";
	var USER_ID  = "${sessionMap.toolSessionID}";//constant used for tracking user info
		
	$(document).ready(function(){
		$("#close-button").click(function() {
			var kplayerDataAttr = $("#kplayer", parent.document).attr("data");
			$("#kplayer", parent.document).attr("data", kplayerDataAttr);
			self.parent.tb_remove();
		});
		
		$("#back-to-creation-mode").click(function() {
			location.href = "<c:url value='/learning.do'/>?dispatch=cuePoints&sessionMapID=${sessionMapID}&itemIndex=${itemIndex}&itemUid=${itemUid}&kalturaSession=${sessionMap.kalturaSession}";
		});
	});
	
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
		"ks"						: "${sessionMap.kalturaSession}",
		"autoPlay"					: "true"
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
					<div id="kplayer"></div>

				</div>
			</div>
			
			<div id="cuepoint-comments-container">
				<div id="cuepoint-comments"></div>
			</div>
		</div><!-- #kwrap -->
		<br>
			
		<lams:ImgButtonWrapper>
			<a href="#nogo" id="close-button" onmousedown="self.focus();" class="button space-left float-right">
				<fmt:message key="label.close" /> 
			</a>
			<a href="#nogo" id="back-to-creation-mode" onmousedown="self.focus();" class="button space-left float-right">
				<fmt:message key="label.back.to.creation.mode" /> 
			</a>			
		</lams:ImgButtonWrapper>
			
	</div>	
		
	<div id="footer">
	</div>
		
</body>
</lams:html>

