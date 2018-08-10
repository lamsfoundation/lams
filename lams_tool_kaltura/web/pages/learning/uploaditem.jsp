<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>

<c:set var="KALTURA_SERVER"><%=Configuration.get(ConfigurationKeys.KALTURA_SERVER)%></c:set>
<c:set var="PARTNER_ID"><%=Configuration.get(ConfigurationKeys.KALTURA_PARTNER_ID)%></c:set>
<c:set var="SUB_PARTNER_ID"><%=Configuration.get(ConfigurationKeys.KALTURA_SUB_PARTNER_ID)%></c:set>
<c:set var="USER_SECRET"><%=Configuration.get(ConfigurationKeys.KALTURA_USER_SECRET)%></c:set>
<c:set var="KCW_UI_CONF_ID"><%=Configuration.get(ConfigurationKeys.KALTURA_KCW_UI_CONF_ID)%></c:set>
<c:set var="KDP_UI_CONF_ID"><%=Configuration.get(ConfigurationKeys.KALTURA_KDP_UI_CONF_ID)%></c:set>

<lams:html>
	<lams:head>
		<lams:css />

		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
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
			//constant used for tracking user info
			var USER_ID  = 'ANONYMOUS';
				
			$(document).ready(function(){
				initialize();
			});
			
			var kClient;
			var lastUploadedItemUid = null;
			
			function initialize() {
				
				//check if LAMS has a Kaltura server configured
				if ((KALTURA_SERVER == "") || (PARTNER_ID == "") || (USER_SECRET == "") || (KCW_UI_CONF_ID == "")) {
					alert('<fmt:message key="label.kaltura.server.not.configured"/>');
					return false;
				}
					
				//start Kaltura session
				var kConfig = new KalturaConfiguration(parseInt(PARTNER_ID));
				kConfig.serviceUrl = KALTURA_SERVER;
				kClient = new KalturaClient(kConfig);
				var expiry = null;
				var privileges = null;
				kClient.session.start(onSessionCreated, USER_SECRET, USER_ID, KalturaSessionType.USER, PARTNER_ID, expiry, privileges);
			}
				
			function onSessionCreated(isSuccess, kSession) {
				if (! isSuccess) {
					alert('<fmt:message key="error.there.was.error"/>');
					return;
				} else if (kSession.code != null) {
					alert('<fmt:message key="error.there.was.error"/>: ' + kSession.message);
					return;
				}
				
				kClient.setKs(kSession);
					
				//Prepare variables to be passed to embedded flash object.
				var flashVars = {
					uid: USER_ID,
					partnerId: parseInt(PARTNER_ID),
					ks: kSession,
					afterAddEntry: "onContributionWizardAfterAddEntry",
					close: "onContributionWizardClose",
					showCloseButton: false,
					enableTagging: false,
					Permissions: 1
				};
				
				 var params = {
				    allowScriptAccess: "always",
				   	allowNetworking: "all",
				    wmode: "opaque"
				 };
				
				//embed flash object
				 swfobject.embedSWF(KALTURA_SERVER + "/kcw/ui_conf_id/" + KCW_UI_CONF_ID, "kcw", "680", "360", "9.0.0", "includes/expressInstall.swf", flashVars, params);
			}
			
			function onContributionWizardAfterAddEntry(entries) {
					
				//check all videos uploaded successfully
				for(var i = 0; i < entries.length; i++) {
					var entryId = entries[i].entryId;
		
					if ((entryId == null) || (entryId == "")) {
						alert("An error occurred processing this video. Please, reupload it.");
						return;
					}
				}
					
				// Get etries titles from Kaltura server. Regrdless of success save the video with LAMS.
				for(var i = 0; i < entries.length; i++) {
					var entryId = entries[i].entryId;
						
					kClient.media.get(
						function (success, data){
							var title = "";
							var duration = 0;
								
							if(!success) {
								alert("Error on getting entry title. Data: " + data);
							} else if (data.code && data.message){
								alert("Error on getting entry title: " + data.message);
							} else {
								title = data.name;
								duration = data.duration;
							}
								
							addItem(entryId, title, duration);
						}, 
						entryId
					);
				}
			}
				
			function onContributionWizardClose() {
				closeThickbox();
			}
			
			function addItem(entryId, title, duration){
				$.post(
					"<c:url value="/learning/saveNewItem.do"/>", 
					{
						sessionMapID: "${param.sessionMapID}",
						itemEntryId: entryId,
						itemTitle: title,
						itemDuration: duration
					},
					function(data) {
						lastUploadedItemUid = data.itemUid;
					}
				);
			}
			
			function closeThickbox(){
				if (lastUploadedItemUid != null) {
					self.parent.location.href = '<c:url value="/learning/viewItem.do"/>?sessionMapID=${param.sessionMapID}&itemUid=' + lastUploadedItemUid;
				} else {
					self.parent.tb_remove();
				}
			}
		
		</script>
	</lams:head>

	<c:set scope="request" var="title">
		<fmt:message key="label.authoring.basic.video"/>
	</c:set>
	<body class="stripes">
		<lams:Page type="learner" title="${title}" hideProgressBar="true" >
						
			<div id="kcwHolder">	
				<div id="kcw"></div>
			</div>
			
			<a href="#nogo" onclick="return closeThickbox();" onmousedown="self.focus();" class="btn btn-sm btn-default voffset10 pull-right">
				<fmt:message key="label.close" /> 
			</a>
		</lams:Page>
		
	</body>
</lams:html>
