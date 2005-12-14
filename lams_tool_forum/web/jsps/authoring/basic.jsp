<script lang="javascript">
<!-- Common Javascript functions for LAMS -->

	/**
	 * Launches the popup window for the instruction files
	 */
	function launchPopup(url) {
		var instructionsWindow = null;
// add the mac test back in when we have the platform detection working.
//		if(mac){
//			window.open(url,'instructions','resizable,width=796,height=570,scrollbars');
//		}else{
			if(instructionsWindow && instructionsWindow.open && !instructionsWindow.closed){
				instructionsWindow.close();
			}
			instructionsWindow = window.open(url,'newtopic','resizable,width=796,height=570,scrollbars');
			instructionsWindow.window.focus();
//		}	
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
		<td colspan="2" class="formcontrol"><c:if test="${!empty topicList}">
			<%@ include file="/jsps/message/topiclist.jsp" %>
		</c:if></td>
	</tr>
	<td colspan="2" align="left">
			<a href="javascript:launchPopup('<html:rewrite page="/authoring/newTopic.do"/>');">
				<fmt:message key="label.authoring.create.new.topic" />
			</a>
		</td>
	</tr>
	<tr>
		<td colspan="2"><html:errors /></td>
	</tr>
	</table>

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
</table>

</div>
