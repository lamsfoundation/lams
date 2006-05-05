<div id="login_pane">
	<div id="login_err"></div>
	<p>
		Logging in, please wait .....
	</p>
</div>

<div id="sendmsg_pane" style="display:none;">
	<h1>
		Incoming:
	</h1>
	<div name="iResp" id="iResp"></div>
	<h1>
		Send Message
	</h1>
	<form name="sendForm" onSubmit="return sendMsg(this);">
		<div class="spaced">
			<textarea name="msg" id='msgArea' rows=3 cols=80></textarea>
		</div>
		<div class="spaced">
			<input type="submit" value="Send">
		</div>
	</form>
</div>
