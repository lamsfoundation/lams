<!DOCTYPE html>  
<%@ include file="/common/taglibs.jsp"%>

<%
String imageURL = request.getParameter("imageURL");
%>

<html>
	<head>
		<script type="text/javascript">
			function resize() {
				var image = document.getElementById("image");
				window.resizeTo(image.width, image.height); 
			}
		</script>
	</head>
	
	<body onload="resize();">
		<img id="image" src='<%= imageURL %>' />
	</body>
</html>
	
	
	
