<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">

	/**
	 * Launches the popup window for the instruction files
	 */
	function showMessage(url) {
		var area=document.getElementById("iframeArea");
		if(area != null){
			area.style.width="670px";
			area.style.height="100%";
			area.src=url;
			area.style.display="block";
		}
	}
	function hideMessage(){
		var area=document.getElementById("iframeArea");
		if(area != null){
			area.style.width="0px";
			area.style.height="0px";
			area.style.display="none";
		}
	}
</script>


<c:set var="authoringForm"
	value="<%=request
									.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />

<!-- ========== Basic Tab ========== -->
<table cellpadding="0">
	<tbody>
		<tr>
			<td>
				<div class="field-name" style="text-align: left;">
					<fmt:message key="label.authoring.basic.title"></fmt:message>
				</div>
				<html:text property="title" style="width: 100%;"></html:text>
			</td>
		</tr>
		<tr>
			<td>
				<div class="field-name" style="text-align: left;">
					<fmt:message key="label.authoring.basic.instructions"></fmt:message>
				</div>
				<lams:FCKEditor id="instructions"
					value="${authoringForm.instructions}"
					contentFolderID="${authoringForm.contentFolderID}"></lams:FCKEditor>
			</td>
		</tr>
		<tr>
			<td>
				<iframe
					onload="javascript:this.style.height=this.contentWindow.document.body.scrollHeight+'px'"
					id="reourceInputArea" name="iframeArea"
					style="width:0px;height:0px;border:0px;display:none"
					frameborder="no" scrolling="no">
				</iframe>
			</td>
		</tr>
	</tbody>
</table>
