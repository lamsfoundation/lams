
<script lang="javascript">
<!-- Common Javascript functions for LAMS -->

	/**
	 * Launches the popup window for the instruction files
	 */
	function showMessage(url) {
		var area=document.getElementById("messageArea");
		area.style.width="100%";
		area.style.height="100%";
		area.src=url;
		area.style.display="block";
	}
	function hideMessage(){
		var area=document.getElementById("messageArea");
		area.style.width="0px";
		area.style.height="0px";
		area.style.display="none";
	}
</script>
<!---------------------------Basic Tab Content ------------------------>

<div id='content_b' class="tabbody content_b">
<h2><fmt:message key="label.authoring.heading.basic.desc" /></h2>
<table class="forms">
	<tr>
		<td class="formlabel"><fmt:message key="label.authoring.basic.title" />:</td>
		<td class="formcontrol"><html:text property="forum.title" /></td>
	</tr>
	<tr>
		<td class="formlabel"><fmt:message
			key="label.authoring.basic.instruction" />:</td>
		<td class="formcontrol"><FCK:editor id="forum.instructions"
			basePath="/lams/fckeditor/" height="150" width="85%">
			<c:out value="${formBean.forum.instructions}" escapeXml="false" />
		</FCK:editor></td>
	</tr>
	<!-- Topics List Row -->
	<tr>
		<td colspan="2" class="formlabel">
		<div id="messageListArea">
		<%@ include file="/jsps/message/topiclist.jsp"%>
		</div>
		</td>
	</tr>
	<td colspan="2" align="left">
			<a href="javascript:showMessage('<html:rewrite page="/authoring/newTopic.do"/>');">
				<fmt:message key="label.authoring.create.new.topic" />
			</a>
		</td>
	</tr>
	<tr>
		<td colspan="2"><html:errors /></td>
	</tr>
	<tr>
	<td colspan="2">
	<!-- Button Row -->
	<HR>
	<p align="right">
		<html:submit property="save" styleClass="a.button">
			<fmt:message key="label.authoring.save.button" />
		</html:submit>
		<html:button property="cancel"
			onclick="window.close()" styleClass="a.button">
			<fmt:message key="label.authoring.cancel.button" />
		</html:button>
	</p>
	</td>
	</tr>
		<tr>
	<td colspan="2">
		<iframe onload="javascript:this.style.height=this.contentWindow.document.body.scrollHeight+'px'" 
		id="messageArea" name="messageArea" style="width:0px;height:0px;border:0px;display:none" frameborder="no"  scrolling="no">
		</iframe>
	</td>
	</tr>
	</table>

</div>
