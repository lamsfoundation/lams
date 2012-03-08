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
		
	<lams:css style="core"/>
	<style>
		body { margin: 10px; overflow:hidden }
	</style>
		
	<script type="text/javascript" src="kaltura.min.js"></script>
	<script type="text/javascript" src="swfobject/swfobject.js"></script>
	<script language="JavaScript" type="text/javascript">
	<!--
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
				alert("Sorry, there was an error");
				return;
			} else if (kSession.code != null) {
				alert("Sorry, there was an error: " + kSession.message);
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
					alert("An error occurred processing this video. Please, reupload it.");
					return;
				}
			}
			
			var swfobject = CKGlobal.dom.element.createFromHtml( '<script type="text/javascript" src="' + CKGlobal.plugins.getPath('kaltura') + '/swfobject/swfobject.js"><\/script>' );
			CK.document.getBody().append(swfobject);
			
			//enable HTML5 support
			var html5 = CKGlobal.dom.element.createFromHtml( '<script type="text/javascript" src="' + KALTURA_SERVER + '/p/' + PARTNER_ID + '/sp/' + SUB_PARTNER_ID + '/embedIframeJs/uiconf_id/' + KDP_UI_CONF_ID + '/partner_id/' + PARTNER_ID + '"><\/script>' );
			html5.insertAfter(swfobject);
			
			// this is the callback function; execute after external script finishes loading
			var innerHTML =	'<script type="text/javascript">';			
			for(var i = 0; i < entries.length; i++) {
				var entryId = entries[i].entryId;
				
				innerHTML +=	'	var params'+entryId+' = {';
				innerHTML +=	'		allowscriptaccess: "always",';
				innerHTML +=	'		allownetworking: "all",';
				innerHTML +=	'		allowfullscreen: "true",';
				innerHTML +=	'		wmode: "opaque"';
				innerHTML +=	'	};	';
				innerHTML +=	'	var flashVars'+entryId+' = {';
				innerHTML +=	'		entryId: "'+entryId+'"';
				innerHTML +=	'	};	';
				innerHTML +=	'	swfobject.embedSWF("' + KALTURA_SERVER + '/kwidget/wid/_' + PARTNER_ID + '/uiconf_id/' + KDP_UI_CONF_ID + '", "kplayer' + entryId + '", "400", "360", "9.0.0", "' + CKGlobal.plugins.getPath('kaltura') + '/swfobject/expressInstall.swf", flashVars'+entryId+', params'+entryId+');';
			}		
			innerHTML +='<\/script>';
			var embedSwf = CKGlobal.dom.element.createFromHtml( innerHTML );
			embedSwf.insertAfter(html5);
			
			//add divs which will contain videos
			for(var i = 0; i < entries.length; i++) {
				var entryId = entries[i].entryId;
				var div = CKGlobal.dom.element.createFromHtml('<div id="kplayer' + entryId + '"><object height="400" width="360"><embed height="400" type="application/x-shockwave-flash" width="360" src="#"></embed></object></div>' );
				div.insertAfter(embedSwf);
			}
			
		}
	// -->
	</script>

</lams:head>

<body scroll="no" onload="initialize();">
	<div id="kcw"></div>
	<br/><br/>
	<div id="explanationNotes">
		<span>Select Upload tab to upload videos or Webcam to start webcam recording. After you've done with this press Next button. Assign a title to your video and press Next button again.</span>
	</div>
</body>
</lams:html>
