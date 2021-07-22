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

<%@ attribute name="padId" required="false" rtexprvalue="true" %>
<%@ attribute name="showControls" required="false" rtexprvalue="true" %>
<%@ attribute name="showChat" required="false" rtexprvalue="true" %>
<%@ attribute name="height" required="false" rtexprvalue="true" %>
<%@ attribute name="heightAutoGrow" required="false" rtexprvalue="true" %>
<%@ attribute name="showOnDemand" required="false" rtexprvalue="true" %>

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

<%-- THIS TAG REQUIRES JQUERY --%>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/etherpad.js"></script>
<script type="text/javascript">
	var etherpadInitMethods = typeof etherpadInitMethods == 'undefined' ? {} : etherpadInitMethods;
	etherpadInitMethods['${groupId}'] = function intialiseEtherpad() {
		var showOnDemand = '${showOnDemand}' == 'true';
		// If there are mutliple Etherpads on a single page, there is a race condition while setting session ID cookie.
		// One of pads usually responds with "access denied". It is better to wait 1.5 seconds and initialise the next pad.
		var delayBeforeInitialise = showOnDemand || typeof lamsEtherpadTagInitialiseDelay == 'undefined' ? 0 : lamsEtherpadTagInitialiseDelay;
		if (!showOnDemand) {
			lamsEtherpadTagInitialiseDelay = delayBeforeInitialise + 1500;
		}
		
		<c:if test="${heightAutoGrow eq 'true'}">
			// Resize Etherpad iframe when its content grows.
			// It does not support shrinking, only growing.
			// This feature requires ep_resize plugin installed in Etherpad and customised with code in lams_build/conf/etherpad
			$(window).on('message onmessage', function (e) {
				var msg = e.originalEvent.data;
		        if (msg.name === 'ep_resize') {
		        	var src = msg.data.location.substring(0, msg.data.location.indexOf('?')),
		        		iframe = $('iframe[src^="' + src + '"]'),
		            	// height should be no less than 200 px
		            	height = Math.max(200, msg.data.height + (${showControls} ? 0 : 30));
		           	iframe.height(height);
		        }
		    });
		</c:if>
		
		let initPad = function(padId) {
			// proper initialisation
			$('#etherpad-container-${groupId}').pad({
				'padId': padId,
				'host':'${etherpadServerUrl}',
				'lang':'${fn:toLowerCase(localeLanguage)}',
				'showControls':${empty showControls ? false : showControls},
				'showChat': ${empty showChat ? false : showChat},
				'height': ${empty height ? 'undefined' : height}
				<c:if test="${showControls}">
					<c:set var="fullName"><lams:user property="firstName" />&nbsp;<lams:user property="lastName" /></c:set>
					,'userName':'<c:out value="${fullName}" />'
				</c:if>
			}).addClass('initialised');
		}
		
		window.setTimeout(function(){
			
			<c:choose>
				<c:when test="${empty padId}">
					<%-- If pad ID was not provided, fetch it from back end --%>
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
							if ( status == "error" ) {
								console.log( xhr.status + " " + xhr.statusText );
							} else {
								initPad(padId);
							}
						}
					});
				</c:when>
				<c:otherwise>
					initPad('${padId}');
				</c:otherwise>
			</c:choose>

			// wait for other pad on the page to initialise
		}, delayBeforeInitialise);
	}
	
	if ('${showOnDemand}' != 'true') {
		$(document).ready(function(){
			etherpadInitMethods['${groupId}']();
		});
	}

</script>

<div class="etherpad-container" id='etherpad-container-${groupId}'></div>

<%-- If content was provided, put it into a hidden div so new lines will not break JavaScript --%>
<c:if test="${not empty content.trim()}">
	<span class="etherpad-initial-content" id="etherpad-initial-content-${groupId}">
		${content}
	</span>
</c:if>