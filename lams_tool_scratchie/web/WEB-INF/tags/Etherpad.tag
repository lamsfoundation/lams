<% 
 /**
  * Etherpad.tag
  *	Author: Marcin Cieslak
  *	Description: Etherpad pad
  */
 %>
<%@ tag body-content="scriptless" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-function" prefix="fn" %>

<%@ attribute name="groupId" required="true" rtexprvalue="true" %>
<%@ attribute name="showControls" required="false" rtexprvalue="true" %>
<%@ attribute name="showChat" required="false" rtexprvalue="true" %>
<%@ attribute name="height" required="false" rtexprvalue="true" %>
<%@ attribute name="heightAutoGrow" required="false" rtexprvalue="true" %>

<%@ tag  import="org.lamsfoundation.lams.util.Configuration"%>
<%@ tag  import="org.lamsfoundation.lams.util.ConfigurationKeys"%>

<c:set var="etherpadServerUrl"><%=Configuration.get(ConfigurationKeys.ETHERPAD_SERVER_URL)%></c:set>
<c:set var="localeLanguage"><lams:user property="localeLanguage" /></c:set>
<jsp:doBody var="content" />

<%-- ---------------------------------------%>
<style type="text/css">
	.etherpad-initial-content {
		display: none;
	}
</style>

<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/etherpad.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		// If there are mutliple Etherpads on a single page, there is a race condition while setting session ID cookie.
		// One of pads usually responds with "access denied". It is better to wait 1.5 seconds and initialise the next pad.
		var delayBeforeInitialise = typeof lamsEtherpadTagInitialiseDelay == 'undefined' ? 0 : lamsEtherpadTagInitialiseDelay;
		lamsEtherpadTagInitialiseDelay = delayBeforeInitialise + 1500;
		
		<c:if test="${heightAutoGrow eq 'true'}">
			// Resize Etherpad iframe when its content grows.
			// It does not support shrinking, only growing.
			// This feature requires ep_resize plugin installed in Etherpad and customised with code in Doku tool
			$(window).on('message onmessage', function (e) {
				var msg = e.originalEvent.data;
		        if (msg.name === 'ep_resize') {
		        	var src = msg.data.location.substring(0, msg.data.location.indexOf('?')),
		        		iframe = $('iframe[src^="' + src + '"]'),
		            	// height should be no less than 200 px
		            	height = Math.max(200, msg.data.height);
		           	iframe.height(height);
		        }
		    });
		</c:if>
		
		window.setTimeout(function(){
			$.ajax({
				url: '<lams:LAMSURL/>etherpad/getPad.do',
				cache : false,
				type: "GET",
				data: {
					groupId : '${groupId}',
					content : $('#etherpad-initial-content-${groupId}').html()
				},
				dataType : 'text',
				success: function(padId, status, xhr) {
					lamsEtherpadTagInitialising = false;
					if ( status == "error" ) {
						console.log( xhr.status + " " + xhr.statusText );
					} else {
						// proper initialisation
						$('#etherpad-container-${groupId}').pad({
							'padId': padId,
							'host':'${etherpadServerUrl}',
							'lang':'${fn:toLowerCase(localeLanguage)}',
							'showControls':${empty showControls ? false : showControls},
							'showChat': ${empty showChat ? false : showChat},
							'height': ${empty height ? 'undefined' : height}
							<c:if test="${showControls}">,'userName':'<lams:user property="firstName" />&nbsp;<lams:user property="lastName" />'</c:if>
						});
					}
				}
			});
			// wait for other pad on the page to initialise
		}, delayBeforeInitialise);
	});
</script>

<div class="etherpad-container" id='etherpad-container-${groupId}'></div>

<%-- If content was provided, put it into a hidden div so new lines will not break JavaScript --%>
<c:if test="${not empty content.trim()}">
	<span class="etherpad-initial-content" id="etherpad-initial-content-${groupId}">
		${content}
	</span>
</c:if>