<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>

<c:set var="KALTURA_SERVER"><%=Configuration.get(ConfigurationKeys.KALTURA_SERVER)%></c:set>
<c:set var="PARTNER_ID"><%=Configuration.get(ConfigurationKeys.KALTURA_PARTNER_ID)%></c:set>
<c:set var="SUB_PARTNER_ID"><%=Configuration.get(ConfigurationKeys.KALTURA_SUB_PARTNER_ID)%></c:set>
<c:set var="USER_SECRET"><%=Configuration.get(ConfigurationKeys.KALTURA_USER_SECRET)%></c:set>
<c:set var="KCW_UI_CONF_ID"><%=Configuration.get(ConfigurationKeys.KALTURA_KCW_UI_CONF_ID)%></c:set>
<c:set var="KDP_UI_CONF_ID"><%=Configuration.get(ConfigurationKeys.KALTURA_KDP_UI_CONF_ID)%></c:set>

<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
<c:set var="sessionMap" value="${sessionScope[formBean.sessionMapID]}" />
<c:set var="sessionMapID" value="${formBean.sessionMapID}" />

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
			var kalturaEntryId = entries[i].entryId;
				
			kClient.media.get(
				function (success, data){
					
					if(!success) {
						alert("Error on getting entry title. Data: " + data);
					} else if (data.code && data.message){
						alert("Error on getting entry title: " + data.message);
					} else {
						var title = data.name;
						var duration = data.duration;
						var entryId = data.id;
						
						addItem(entryId, title, duration);
					}
				},
				kalturaEntryId
			);
		}
	}
		
	function onContributionWizardClose() {
		$("#kcwHolder").toggle("slow");
		$("#kcwOpener").toggle("fast");
	}
	
	function displayKCW() {
		$("#kcwHolder").toggle("slow");
		$("#kcwOpener").toggle("fast");
		
		return false;
	}
		
	function refreshThickbox(){
		tb_init('a.thickbox, area.thickbox, input.thickbox');//pass where to apply thickbox
	};
	
	//The panel of assessment list panel
	var itemListTargetDiv = "#itemListArea";
	function deleteItem(idx){
		var	deletionConfirmed = confirm("<fmt:message key="warning.msg.authoring.do.you.want.to.delete"></fmt:message>");

		if (deletionConfirmed) {
			$(itemListTargetDiv).load(
				"<c:url value="/authoring.do"/>",
				{
					dispatch: "removeItem",
					itemIndex: idx, 
					sessionMapID: "${sessionMapID}"
				},
				function(){
					refreshThickbox();
				}
			);
		};
	}
	function addItem(entryId, title, duration){
		$(itemListTargetDiv).load(
			"<c:url value="/authoring.do"/>",
			{
				dispatch: "addItem",
				sessionMapID: "${sessionMapID}",
				itemEntryId: entryId,
				itemTitle: title,
				itemDuration: duration
			},
			function(){
				refreshThickbox();
			}
		);
	}
	function upItem(idx){
		$(itemListTargetDiv).load(
			"<c:url value="/authoring.do"/>",
			{
				dispatch: "upItem",
				itemIndex: idx, 
				sessionMapID: "${sessionMapID}"
			},
			function(){
				refreshThickbox();
			}
		);
	}
	function downItem(idx){
		$(itemListTargetDiv).load(
			"<c:url value="/authoring.do"/>",
			{
				dispatch: "downItem",
				itemIndex: idx, 
				sessionMapID: "${sessionMapID}"
			},
			function(){
				refreshThickbox();
			}
		);
	}

</script>

<!-- ========== Basic Tab ========== -->
<div class="form-group">
    <label for="title">
    	<fmt:message key="label.authoring.basic.title"/>
    </label>
    <html:text property="title" styleClass="form-control"/>
</div>

<div class="form-group">
    <label for="instructions">
    	<fmt:message key="label.authoring.basic.instructions"/>
    </label>
	<lams:CKEditor id="instructions" value="${formBean.instructions}"
			contentFolderID="${sessionMap.contentFolderID}"/>
</div>

<div id="kcwHolder" style="text-align:center;">

	<div id="kcw"></div>
</div>
				
<div id="kcwOpener" style="display: none;">
	<a href="#nogo" onclick="javascript: return displayKCW();" class="btn btn-xs btn-default pull-right voffset10">
		<fmt:message key="label.upload.more.video"/>
	</a>
</div>
	
<div id="itemListArea">
	<%@ include file="/pages/authoring/itemlist.jsp"%>
</div>
