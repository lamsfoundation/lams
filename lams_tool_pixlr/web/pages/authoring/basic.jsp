<%@ include file="/common/taglibs.jsp"%>

<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
<c:set var="sessionMap" value="${sessionScope[formBean.sessionMapID]}" />
<c:set var="sessionMapID" value="${formBean.sessionMapID}" />


<script type="text/javascript">
<!--
	var image;
	var origImageHeight;
	var origImageWidth;
	
	function submitForm(dispatch)
	{
		document.getElementById("dispatch").value = dispatch;
		document.getElementById("authoringForm").submit();
	}
	
	function openImage(url)
	{
		openPopup(url, origImageHeight, origImageWidth);
	}
	
//-->
</script>


<!-- ========== Basic Tab ========== -->
<table cellpadding="0">
	<tr>
		<td>
			<div class="field-name">
				<fmt:message key="label.authoring.basic.title"></fmt:message>
			</div>
			<html:text property="title" style="width: 99%;"></html:text>
		</td>
	</tr>
	<tr>
		<td>
			<div class="field-name">
				<fmt:message key="label.authoring.basic.instructions"></fmt:message>
			</div>
			<lams:FCKEditor id="instructions"
				value="${formBean.instructions}"
				contentFolderID="${sessionMap.contentFolderID}"></lams:FCKEditor>
		</td>
	</tr>
	<tr>
		<td>
			
			<c:choose>
				<c:when test='${imageExists}'>
					<div style="text-align:center;">
					<img id="image"  title="<fmt:message key="tooltip.openfullsize" />" onclick="openImage('${imageURL}')" src="${imageURL}"/>
					<br><br>
				
					<a href="javascript:submitForm('deleteImage')"><fmt:message key="label.authoring.remove"></fmt:message></a>
					</div>
				</c:when>
				<c:otherwise>
					<div class="field-name">
						<fmt:message key="label.authoring.basic.add.image"></fmt:message>
					</div>
					<input type="file" name="file" />
				</c:otherwise>
			</c:choose>
		</td>
	</tr>
</table>
