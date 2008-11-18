       <html >
		<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>Untitled Document</title>
		<script type="text/javascript">
		/* Functoin that passa parameter to the buttons frame and display the button */
        function appearbutton(id){
				parent.button.toggle("next");  /* makes button visible*/
						
		}
		</script>
		</head>
		<body  onLoad="appearbutton(<?php echo($cm->id);?>)">			  
		</body>
		</html>
