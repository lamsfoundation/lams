<!-- Parse the normal JSP error page and display the error message in an alert box. Used for ajax calls -->
function checkForErrorScreen(baseErrorMessage, responseText) {
	if ( responseText.indexOf('id=\"errorForm\"') > 0 ) {
		var embeddedMessageIndex = responseText.indexOf('name=\"errorMessage\"');
		if ( embeddedMessageIndex > 0 ) {
			embeddedMessageIndex = responseText.indexOf('value=\"',embeddedMessageIndex);
			if ( embeddedMessageIndex > 0 ) {
				embeddedMessageIndex = embeddedMessageIndex + 7;
				var endMessageIndex = responseText.indexOf('\"/>',embeddedMessageIndex);
				if ( endMessageIndex > 0)
					alert(baseErrorMessage+" "+responseText.substring(embeddedMessageIndex, endMessageIndex));
				else
					alert(baseErrorMessage+" "+responseText.substring(embeddedMessageIndex));
			}
		}
	}
}

