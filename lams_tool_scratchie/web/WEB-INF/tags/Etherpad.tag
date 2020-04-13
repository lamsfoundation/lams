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
		// One of pads usually responds with "access denied". It is better to wait 3 seconds and initialise the other pad.
		// If there is only one pad, initialisation starts immediately.
		var isOtherPadInitialising = typeof lamsEtherpadTagInitialising == 'undefined' ? false : lamsEtherpadTagInitialising;
		lamsEtherpadTagInitialising = true;
		
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
							'height': ${empty height ? '+$(window).height() - 200' : height}
							<c:if test="${showControls}">,'userName':'<lams:user property="firstName" />&nbsp;<lams:user property="lastName" />'</c:if>
						});
					}
				}
			});
			// wait for other pad on the page to initialise
		}, isOtherPadInitialising ? 3000 : 0);
	});
</script>

<div class="etherpad-container" id='etherpad-container-${groupId}'></div>

<%-- If content was provided, put it into a hidden div so new lines will not break JavaScript --%>
<c:if test="${not empty content.trim()}">
	<span class="etherpad-initial-content" id="etherpad-initial-content-${groupId}">
		${content}
	</span>
</c:if>