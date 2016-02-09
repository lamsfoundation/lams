<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="tool"><lams:WebAppURL/></c:set>
<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
	</lams:head>
	<body class="stripes">
		<script type="text/javascript">
			var reqIDVar = new Date();
			window.parent.location.href = "${tool}/pages/learning/learning.jsp?sessionMapID=${sessionMapID}&mode=${mode}&reqID="+reqIDVar.getTime();
		</script>
		<div style="align:center">
			Add File success, <a href="<c:url value='/learning/addfile.do'/>?sessionMapID=${sessionMapID}&mode=${mode}" target="newImageGalleryFrame">click here to return</a>.
		</div>
	</body>
</lams:html>
