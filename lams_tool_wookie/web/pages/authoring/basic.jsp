<%@ include file="/common/taglibs.jsp"%>

<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
<c:set var="sessionMap" value="${sessionScope[formBean.sessionMapID]}" />
<c:set var="sessionMapID" value="${formBean.sessionMapID}" />

<script type="text/javascript">
<!--
	var pageCount = ${widgetPages};
	var browseAreaLoaded = false;
	
	function submitForm(dispatch)
	{
		document.getElementById("dispatch").value = dispatch;
		document.getElementById("authoringForm").submit();
	}

	jQuery(document).ready(function() {

		// Set the initial width of the pager div
		var width = getAppropriateWidth(pageCount) + "px";
		document.getElementById("container").style.width = width;

		<c:choose>
			<c:when test="${empty formBean.widgetAuthorUrl}">
				// No saved widget so load the view
				loadBrowseArea(1);
		        document.getElementById("widgetViewArea").style.display="none";
		        document.getElementById("widgetBrowseArea").style.display="block";
			</c:when>
			<c:otherwise>
				// Load the widget if it has previously been saved
				document.getElementById("widgetBrowseArea").style.display="none";
				document.getElementById("widgetViewArea").style.display="block";
				enableWidgetIframe("${formBean.widgetAuthorUrl}", "${formBean.widgetHeight}", "${formBean.widgetWidth}");
			</c:otherwise>
		</c:choose>
    });

    PageClick = function(pageclickednumber) {
    	loadBrowseArea(pageclickednumber);
    }

    // Loads the brows area to the specified page number
    function loadBrowseArea(pageNumber) {
    	jQuery("#pager").pager({ pagenumber: pageNumber, pagecount: pageCount, buttonClickCallback: PageClick });
        jQuery.ajax({		
			type: "GET",
			url: "<lams:WebAppURL/>/authoring.do",
			data: {dispatch : "getWidgets", pageNumber : pageNumber},
			cache: false,
			error: function (XMLHttpRequest, textStatus, errorThrown) {
				alert('<fmt:message key="error.wookie.server" />');
			},
			success: function (html) {
				jQuery("#result").html(html);
			}
		});
        browseAreaLoaded = true;
    }

	// Get the appropriate width for the pager div
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

    // Callback to server when a widget is selected
    function selectWookieWidget(widgetid) {
    	document.getElementById("widgetIdentifier").value = widgetid;
    	jQuery.ajax({		
			type: "GET",
			url: "<lams:WebAppURL/>/authoring.do",
			data: {dispatch :"initiateWidget", widgetid:widgetid, toolContentID:"${sessionMap.toolContentID}" },
			cache: false,
			error: function (XMLHttpRequest, textStatus, errorThrown) {
				alert('<fmt:message key="error.initiating.widget" />');
			},				
			success: function (xml) {

				// Set the parameters from the returned xml
				var widgetUrl = jQuery(xml).find("url").text();
				document.getElementById("widgetAuthorUrl").value = widgetUrl;
				
				var widgetHeight = jQuery(xml).find("height").text();
				document.getElementById("widgetHeight").value = widgetHeight;
				
				var widgetWidth = jQuery(xml).find("width").text();
				document.getElementById("widgetWidth").value = widgetWidth;
				
				var widgetMaximize = jQuery(xml).find("widgetMaximize").text();
				document.getElementById("widgetMaximise").value = widgetMaximize;

				enableWidgetIframe(widgetUrl, widgetHeight, widgetWidth);
				jQuery("#widgetBrowseArea").toggle("slow");
				setTimeout('jQuery("#widgetViewArea").toggle("slow");',1000);
			}
							
		});
    }

 	// Loads the widget in an iframe
    function enableWidgetIframe(url, height, width) {
    	var iframe = document.getElementById("widgetIframe");
		iframe.style.height = height + "px";
		iframe.style.width = width + "px";
		iframe.src=url;
		iframe.style.display="block";
    }

	// Cancel a widget and return to browse mode
    function cancelWidget(){
    	document.getElementById("widgetAuthorUrl").value = "";
    	document.getElementById("widgetHeight").value = "";
    	document.getElementById("widgetWidth").value = "";
    	document.getElementById("widgetMaximise").value = "";
    	document.getElementById("widgetIdentifier").value = "";
    	jQuery("#widgetViewArea").toggle("fast");
		if (browseAreaLoaded == false) {
			loadBrowseArea(1);
			document.getElementById("widgetBrowseArea").style.display="block";
		} else {
			jQuery("#widgetBrowseArea").toggle("fast");
		}
		
    }
	
//-->
</script>

<html:hidden property="widgetAuthorUrl" styleId="widgetAuthorUrl"/>
<html:hidden property="widgetHeight" styleId="widgetHeight"/>
<html:hidden property="widgetWidth" styleId="widgetWidth"/>
<html:hidden property="widgetMaximise" styleId="widgetMaximise"/>
<html:hidden property="widgetIdentifier" styleId="widgetIdentifier"/>

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
			<lams:CKEditor id="instructions"
				value="${formBean.instructions}"
				contentFolderID="${sessionMap.contentFolderID}"></lams:CKEditor>
		</td>
	</tr>
	<tr align="center">
		<td>
			<div id="wookieArea" style="min-height: 625px">
				<div id="widgetBrowseArea">
					<div id="result" ></div>
					<div id="container" class="wookie-list" >
						<div id="pager" ></div>
					</div>
				</div>
				
				<div id="widgetViewArea" >
					<iframe
						id="widgetIframe" 
						name="widgetIframe"
						style="width:0px;height:0px;border:0px;" 
						frameborder="no"
						scrolling="no">
					</iframe>
					<br />
					<a href="javascript:cancelWidget()" class="button"><fmt:message key="button.cancel" /></a>
				</div>
			</div>
		</td>
	</tr>
</table>
