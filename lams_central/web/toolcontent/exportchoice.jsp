<%@ page language="java"  pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<c:set var="lams">
	<lams:LAMSURL />
</c:set>

<!DOCTYPE html>
<lams:html>
	<lams:head>
		<title><fmt:message key="title.export.choose.format" /></title>
		<!-- ********************  CSS ********************** -->
		<lams:css />
		<script type="text/javascript">
			// if the download starts and finishes too quickly, then the page doesn't get a chance to finish rendering. So slow it down a tad.
			function goDownload() {
				var format = 1;
				if(document.getElementById("ims").checked)
					format = 2;
				location.href="<c:url value='/authoring/exportToolContent.do?method=loading&learningDesignID=${learningDesignID}&format='/>" + format;
			}
			function closeWin(){
				window.close();
			}
		</script>
		<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/monitorToolSummaryAdvanced.js"></script>
	</lams:head>

	<body class="stripes">
	<div id="content">
		<h2>
			<img src="<lams:LAMSURL/>/images/tree_closed.gif" id="treeIcon" 
				onclick="javascript:toggleAdvancedOptionsVisibility(document.getElementById('advancedDiv'), document.getElementById('treeIcon'), '<lams:LAMSURL/>');" />
		
			<a href="javascript:toggleAdvancedOptionsVisibility(document.getElementById('advancedDiv'), document.getElementById('treeIcon'),'<lams:LAMSURL/>');" >
				<fmt:message key="label.export.advanced.options" />
			</a>
		</h2>
	<br />

	<div class="monitoring-advanced" id="advancedDiv" style="display:none">
		<h1>
			<fmt:message key="title.export.choose.format" />
		</h1>
		<table><tr><td>
		<input type="radio" name="format" id="lams" value="1" checked="checked" class="noBorder"><fmt:message key="msg.export.choose.format.lams" />
		<BR/><BR/>
		<input type="radio" name="format" id="ims" value="2" class="noBorder"><fmt:message key="msg.export.choose.format.ims" />
		<BR/><BR/>
		</td></tr></table>
	</div>
	<div class="right-buttons">
		<a href="#" onclick="goDownload();" class="button"><span class="import"><fmt:message key="button.export" /></span></a>
		<a href="javascript:;" onclick="closeWin();" class="button"><span class="close"><fmt:message key="button.close" /></span></a>
	</div>

		</div>  <!--closes content-->
			
		<div id="footer">
		</div><!--closes footer-->

	</body>
</lams:html>
