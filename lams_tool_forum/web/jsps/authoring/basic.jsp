<%@ include file="/includes/taglibs.jsp"%>
<c:set var="formBean"
	value="<%=request
									.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />

<!--  Basic Tab Content -->

<script type="text/javascript">
<!-- Common Javascript functions for LAMS -->
	/**
	 * Launches the popup window for the instruction files
	 */
	function showMessage(url) {
		var area=document.getElementById("messageArea");
		if(area != null){
			area.style.width="685px";
			area.style.height="100%";
			area.src=url;
			area.style.display="block";
		}
		document.getElementById("saveCancelButtons").style.display="none";
		var elem = document.getElementById("saveCancelButtons");
		if (elem != null) {
			elem.style.display="none";
		}
	}
	function hideMessage(){
		var area=document.getElementById("messageArea");
		if(area != null){
			area.style.width="0px";
			area.style.height="0px";
			area.style.display="none";
		}
		document.getElementById("saveCancelButtons").style.display="block";
		var elem = document.getElementById("saveCancelButtons");
		if (elem != null) {
			elem.style.display="block";
		}
	}
</script>

<table>
	<tr>
		<td colspan="2">
			<div class="field-name">
				<fmt:message key="label.authoring.basic.title"></fmt:message>
			</div>
			<html:text property="forum.title" style="width: 99%;"></html:text>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<div class="field-name">
				<fmt:message key="label.authoring.basic.instruction"></fmt:message>
			</div>
			<lams:FCKEditor id="forum.instructions"
				value="${formBean.forum.instructions}"
				contentFolderID="${formBean.contentFolderID}"></lams:FCKEditor>
		</td>
	</tr>
</table>
<!-- Topics List Row -->
<div id="messageListArea">
	<c:set var="sessionMapID" value="${formBean.sessionMapID}" />
	<%@ include file="/jsps/authoring/message/topiclist.jsp"%>
</div>

			<p class="small-space-bottom"><a
				href="javascript:showMessage('<html:rewrite page="/authoring/newTopic.do?sessionMapID=${formBean.sessionMapID}"/>');"
				 class="button"> <fmt:message
					key="label.authoring.create.new.topic" /> </a></p>
	
			<iframe
				onload="javascript:this.style.height=this.contentWindow.document.body.scrollHeight+'px'"
				id="messageArea" name="messageArea"
				style="width:0px;height:0px;border:0px;display:none"
				frameborder="no" scrolling="no">
			</iframe>
	
