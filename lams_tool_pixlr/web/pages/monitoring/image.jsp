<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">
<%@ include file="/common/taglibs.jsp"%>

<%
String imageURL = request.getParameter("imageURL");
%>

<html>
	<head>
	
		<script type="text/javascript">
		<!--
			function resize() 
			{
				var image = document.getElementById("image");
				window.resizeBy(image.width, image.height); 
			}
		-->
		</script>
	</head>
	<body onload="resize();">
		<img id="image" src='<%= imageURL %>' />
	</body>
</html>
	
	
	
