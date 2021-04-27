<script type="text/javascript">
	$(document).ready(function(){
		// command websocket stuff for refreshing
		// trigger is an unique ID of page and action that command websocket code in Page.tag recognises
		commandWebsocketHookTrigger = 'whiteboard-refresh-${sessionMap.toolContentID}';
		// if the trigger is recognised, the following action occurs
		commandWebsocketHook = function() {
			location.reload();
		};
	});
</script>