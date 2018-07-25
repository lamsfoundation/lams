<!DOCTYPE html>      
<%@ include file="/common/taglibs.jsp"%>

<html>
	<head>
		<script type="text/javascript">
			function resize() {
				var image = document.getElementById("image");
				window.resizeBy(image.width, image.height); 
			}
		</script>
	</head>
	
	<body onload="resize();">
		<img id="image" src='<%= request.getParameter("imageURL") %>' />
	</body>
</html>
	
	
	
