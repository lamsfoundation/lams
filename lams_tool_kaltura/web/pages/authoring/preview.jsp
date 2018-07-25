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
		
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/swfobject.js"></script>
		<script type="text/javascript" src="http://cdnbakmi.kaltura.com/html5/html5lib/v1.6.10.4/mwEmbedLoader.php"></script>
		<script type="text/javascript">
		
			//Kaltura settings
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
		
			//enable HTML5 support
			mw.setConfig("Kaltura.ServiceUrl" , KALTURA_SERVER );
			mw.setConfig("Kaltura.CdnUrl" , KALTURA_SERVER );
			mw.setConfig("Kaltura.ServiceBase", "/api_v3/index.php?service=");
			mw.setConfig("EmbedPlayer.EnableIframeApi", true );
			mw.setConfig("EmbedPlayer.UseFlashOnAndroid", false );
			mw.setConfig("Kaltura.UseAppleAdaptive", false );
			mw.setConfig("EmbedPlayer.AttributionButton", false );
			mw.setConfig("EmbedPlayer.OverlayControls", false ); 
			
			var params = {
				allowscriptaccess: "always",
				allownetworking: "all",
				allowfullscreen: "true",
				wmode: "opaque"
			};	
			var flashVars = {
				entryId:  "${item.entryId}"
			};	
			swfobject.embedSWF(KALTURA_SERVER + '/kwidget/wid/_' + PARTNER_ID + '/uiconf_id/' + KDP_UI_CONF_ID, "kplayer", "400", "360", "9.0.0", "includes/expressInstall.swf", flashVars, params);

		</script>
	</lams:head>

	<body class="stripes">
		<lams:Page type="learner" title="${item.title}">
			
			<div id="kplayer">
				<object height="400" width="360">
					<embed height="400" type="application/x-shockwave-flash" width="360" src="#"></embed>
				</object>
			</div>
			
			<div>
				<a href="#" onclick="self.parent.tb_remove();" onmousedown="self.focus();" class="btn btn-sm btn-primary pull-right voffset10">
					<fmt:message key="label.close" /> 
				</a>
			</div>
			
		</lams:Page>	
	</body>
</lams:html>
