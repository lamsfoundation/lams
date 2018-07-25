<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
            "http://www.w3.org/TR/html4/loose.dtd">
            
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-core" prefix="c"%>

<c:set var="KALTURA_SERVER"><%=Configuration.get(ConfigurationKeys.KALTURA_SERVER)%></c:set>
<c:set var="PARTNER_ID"><%=Configuration.get(ConfigurationKeys.KALTURA_PARTNER_ID)%></c:set>
<c:set var="SUB_PARTNER_ID"><%=Configuration.get(ConfigurationKeys.KALTURA_SUB_PARTNER_ID)%></c:set>
<c:set var="USER_SECRET"><%=Configuration.get(ConfigurationKeys.KALTURA_USER_SECRET)%></c:set>
<c:set var="KCW_UI_CONF_ID"><%=Configuration.get(ConfigurationKeys.KALTURA_KCW_UI_CONF_ID)%></c:set>
<c:set var="KDP_UI_CONF_ID"><%=Configuration.get(ConfigurationKeys.KALTURA_KDP_UI_CONF_ID)%></c:set>

<lams:html>
<lams:head>
	<title>Kaltura Contribution Widget</title>
		
	<lams:css/>
	<style>
		body { margin: 10px; overflow:hidden }
	</style>
		
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/kaltura.min.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/swfobject.js"></script>
	<script language="JavaScript" type="text/javascript">
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
		
		var kalturaObject=null;
		//constant used for tracking user info
		var USER_ID  = 'ANONYMOUS';
		var CKGlobal;
		var CK;
		function initialize() {
			//initialize ckeditor related variables
			CKGlobal = window.opener.CKEDITOR;
			CK = CKGlobal.instances["${param.ckEditorName}"];
			
			//check if LAMS has a Kaltura server configured
			if ((KALTURA_SERVER == "") || (PARTNER_ID == "") || (USER_SECRET == "") || (KCW_UI_CONF_ID == "")) {
				alert(CK.lang.kaltura.KalturaErrorNoconfig);
				return false;
			}
			
			//start Kaltura session
			var kConfig = new KalturaConfiguration(parseInt(PARTNER_ID));
			kConfig.serviceUrl = KALTURA_SERVER;
			var kClient = new KalturaClient(kConfig);
			var expiry = null;
			var privileges = null;
			kClient.session.start(onSessionCreated, USER_SECRET, USER_ID, KalturaSessionType.USER, PARTNER_ID, expiry, privileges);
		}
		
		function onSessionCreated(isSuccess, kSession) {
			if (! isSuccess) {
				alert('<fmt:message key="kaltura.error"/>');
				return;
			} else if (kSession.code != null) {
				alert('<fmt:message key="kaltura.error"/>' + kSession.message);
				return;
			}
			
			//Prepare variables to be passed to embedded flash object.
			var flashVars = {
				uid: USER_ID,
				partnerId: parseInt(PARTNER_ID),
				ks: kSession,
				afterAddEntry: "onContributionWizardAfterAddEntry",
				close: "onContributionWizardClose",
				showCloseButton: true,
				enableTagging: false,
				Permissions: 1
			};
		
			 var params = {
			    allowScriptAccess: "always",
			   	allowNetworking: "all",
			    wmode: "opaque"
			 };
		
			<!--embed flash object-->
			 swfobject.embedSWF(KALTURA_SERVER + "/kcw/ui_conf_id/" + KCW_UI_CONF_ID, "kcw", "680", "360", "9.0.0", "swfobject/expressInstall.swf", flashVars, params);
		}
	
		// implement callback scripts
		function onContributionWizardAfterAddEntry(entries) {
			saveToCKEditor(entries);
		}
		function onContributionWizardClose() {
			self.close ();
		}
		
		//in order to use CKEditor preview feature with IE9 applying http://www.aaronpeters.nl/blog/prevent-double-callback-execution-in-IE9 
		function saveToCKEditor(entries) {
			//check all videos uploaded successfully
			for(var i = 0; i < entries.length; i++) {
				var entryId = entries[i].entryId;
				if ((entryId == null) || (entryId == "")) {
					alert('<fmt:message key="kaltura.upload.error"/>');
					return;
				}
			}
			
			var kalturaHtml = '<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/swfobject.js"><\/script>';
			
			//enable HTML5 support
			kalturaHtml += '<script type="text/javascript" src="http://cdnbakmi.kaltura.com/html5/html5lib/v1.6.10.4/mwEmbedLoader.php"><\/script>';
			kalturaHtml += '<script type="text/javascript">';
			kalturaHtml += 'mw.setConfig("Kaltura.ServiceUrl" , "' + KALTURA_SERVER + '" );';
			kalturaHtml += 'mw.setConfig("Kaltura.CdnUrl" , "' + KALTURA_SERVER + '" );';
			kalturaHtml += 'mw.setConfig("Kaltura.ServiceBase", "/api_v3/index.php?service=");';
			kalturaHtml += 'mw.setConfig("EmbedPlayer.EnableIframeApi", true );';
			kalturaHtml += 'mw.setConfig("EmbedPlayer.UseFlashOnAndroid", false );';
			kalturaHtml += 'mw.setConfig("Kaltura.UseAppleAdaptive", false );';
			kalturaHtml += 'mw.setConfig("EmbedPlayer.AttributionButton", false );';
			kalturaHtml += 'mw.setConfig("EmbedPlayer.OverlayControls", false ); ';
			kalturaHtml += '<\/script>';
			
			// this is the callback function; execute after external script finishes loading
			kalturaHtml += '<script type="text/javascript">';			
			for(var i = 0; i < entries.length; i++) {
				var entryId = entries[i].entryId;
				
				kalturaHtml +=	'	var params'+entryId+' = {';
				kalturaHtml +=	'		allowscriptaccess: "always",';
				kalturaHtml +=	'		allownetworking: "all",';
				kalturaHtml +=	'		allowfullscreen: "true",';
				kalturaHtml +=	'		wmode: "opaque"';
				kalturaHtml +=	'	};	';
				kalturaHtml +=	'	var flashVars'+entryId+' = {';
				kalturaHtml +=	'		entryId: "'+entryId+'"';
				kalturaHtml +=	'	};	';
				kalturaHtml +=	'	swfobject.embedSWF("' + KALTURA_SERVER + '/kwidget/wid/_' + PARTNER_ID + '/uiconf_id/' + KDP_UI_CONF_ID + '", "kplayer' + entryId + '", "400", "360", "9.0.0", "' + CKGlobal.plugins.getPath('kaltura') + '/swfobject/expressInstall.swf", flashVars'+entryId+', params'+entryId+');';
			}		
			kalturaHtml +='<\/script>';
			
			//add divs which will contain videos
			for(var i = 0; i < entries.length; i++) {
				var entryId = entries[i].entryId;
				kalturaHtml += '<div id="kplayer' + entryId + '"><object height="400" width="360"><embed height="400" type="application/x-shockwave-flash" width="360" src="#"></embed></object></div>';
			}
			
			//append resulted html to CKEditor
			CK.setData( CK.getData() + kalturaHtml );
		}
	</script>

</lams:head>

<body scroll="no" onload="initialize();">
	<div id="kcw"></div>
	<br/><br/>
	<div id="explanationNotes">
		<span><fmt:message key="kaltura.select.upload"/></span>
	</div>
</body>
</lams:html>
