<%@ include file="/includes/taglibs.jsp"%>

<div>
<c:choose>
	<c:when test="${allowRichEditor}">
		<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
		<fck:editor id="message.body" basePath="/lams/fckeditor/"
			imageBrowserURL="/FCKeditor/editor/filemanager/browser/default/browser.html?Type=Image&amp;Connector=connectors/jsp/connector"
			linkBrowserURL="/FCKeditor/editor/filemanager/browser/default/browser.html?Connector=connectors/jsp/connector"
			flashBrowserURL="/FCKeditor/editor/filemanager/browser/default/browser.html?Type=Flash&amp;Connector=connectors/jsp/connector"
			imageUploadURL="/FCKeditor/editor/filemanager/upload/simpleuploader?Type=Image"
			linkUploadURL="/FCKeditor/editor/filemanager/upload/simpleuploader?Type=File"
			flashUploadURL="/FCKeditor/editor/filemanager/upload/simpleuploader?Type=Flash"
			toolbarSet="Default-Learner">
			<c:out value="${formBean.message.body}" escapeXml="false"/>
		</fck:editor>		
	</c:when>
	<c:otherwise>
		<lams:STRUTS-textarea rows="10" cols="40" tabindex="2" property="message.body" styleId="messageBody" />
		<BR>
		<fmt:message key="lable.char.left" />:  
		<span id="charleftDiv"></span>
		<input type="hidden" name="limitCount" id="limitCount" />
		<script type="text/javascript">
					<!--
					var limit = <c:out value="${limitedChars}"/>;
					var bodyTxt = document.getElementById("messageBody");
					var limitCount = document.getElementById("limitCount");
					
					var showDiv = document.getElementById("charleftDiv");
					limitCount.value=limit - bodyTxt.value.length;
					showDiv.innerHTML=limitCount.value;
					function calculateLeft(){
						if(this.value.length > limit){
							this.value = this.value.substring(0,limit);
							//fix a bug: when change "this.value", onchange event won't be fired any more. So this will 
							//manually handle onchange event. It is a kind of crack coding!
							filterData(document.getElementById('messageBody'),document.getElementById('message.body__lamshidden'));
						}else{
							limitCount.value = limit - this.value.length;
							showDiv.innerHTML=limitCount.value;							
						}
					}
					
					bodyTxt.onkeydown = calculateLeft;
					bodyTxt.onkeyup = calculateLeft;
					-->
				</script>
	</c:otherwise>
</c:choose>
<BR>
<html:errors property="message.body" />

<div>
