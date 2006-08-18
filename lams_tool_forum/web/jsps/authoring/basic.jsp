<%@ include file="/includes/taglibs.jsp"%>
<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />

<!--  Basic Tab Content -->

<script type="text/javascript">
<!-- Common Javascript functions for LAMS -->
	/**
	 * Launches the popup window for the instruction files
	 */
	function showMessage(url) {
		var area=document.getElementById("messageArea");
		area.style.width="670px";
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

<table >
	<tr>
		<td  colspan="2">
			<lams:SetEditor id="forum.title" text="${formBean.forum.title}" small="true" key="label.authoring.basic.title" />
		</td>
	</tr>
	<tr>
		<td  colspan="2">
			<lams:SetEditor id="forum.instructions" text="${formBean.forum.instructions}" key="label.authoring.basic.instruction"/>
		</td>
	</tr>
</table>
	<!-- Topics List Row -->
	<div id="messageListArea">
		<c:set var="sessionMapID" value="${formBean.sessionMapID}"/>
		<%@ include file="/jsps/authoring/message/topiclist.jsp"%>
	</div>
<table >
	<tr>
		<td colspan="2" align="left">
			<a href="javascript:showMessage('<html:rewrite page="/authoring/newTopic.do?sessionMapID=${formBean.sessionMapID}"/>');" style="float:left;width:150px"  class="button">
				<fmt:message key="label.authoring.create.new.topic" />
			</a>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<iframe onload="javascript:this.style.height=this.contentWindow.document.body.scrollHeight+'px'" 
			id="messageArea" name="messageArea" 
			style="width:0px;height:0px;border:0px;display:none" frameborder="no" scrolling="no">
			</iframe>
		</td>
	</tr>
</table>
