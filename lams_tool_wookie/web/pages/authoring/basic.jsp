<%@ include file="/common/taglibs.jsp"%>

<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
<c:set var="sessionMap" value="${sessionScope[formBean.sessionMapID]}" />
<c:set var="sessionMapID" value="${formBean.sessionMapID}" />

<script type="text/javascript">
<!--
	var image;
	var origImageHeight;
	var origImageWidth;
	var pageCount = ${widgetPages};
	
	function submitForm(dispatch)
	{
		document.getElementById("dispatch").value = dispatch;
		document.getElementById("authoringForm").submit();
	}
	
	function openImage(url)
	{
		openPopup(url, origImageHeight, origImageWidth);
	}

	jQuery(document).ready(function() {

		// Set the initial width of the pager div
		var width = getAppropriateWidth(pageCount) + "px";
		document.getElementById("container").style.width = width;

		jQuery("#pager").pager({ pagenumber: 1, pagecount: pageCount, buttonClickCallback: PageClick });
        jQuery.ajax({		
			type: "GET",
			url: "<lams:WebAppURL/>/authoring.do",
			data: {dispatch : "getWidgets", pageNumber : 1},
			cache: false,
			success: function (html) {
				jQuery("#result").html(html);
			}
							
		});
    });

    PageClick = function(pageclickednumber) {
    	jQuery("#pager").pager({ pagenumber: pageclickednumber, pagecount: pageCount, buttonClickCallback: PageClick });
        jQuery.ajax({		
			type: "GET",
			url: "<lams:WebAppURL/>/authoring.do",
			data: {dispatch : "getWidgets", pageNumber : pageclickednumber},
			cache: false,
			success: function (html) {
				jQuery("#result").html(html);
			}
							
		});
    }

    function getAppropriateWidth(pages) {
		var maxPageButtons = 9;
		var width = 170;

    	if (pages <= maxPageButtons) {
    		width += (pages * 25);
    	} else {
    		width += (maxPageButtons * 25);
    	}	
    	return width;
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
	<tr align="center">
		<td>
			<div id="result" ></div>
			<div id="container" class="wookie-list" >
				<div id="pager" ></div>
			</div>
		</td>
	</tr>
</table>

<br />
<br />