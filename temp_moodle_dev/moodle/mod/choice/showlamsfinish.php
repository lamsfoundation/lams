       <html >
		<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>Untitled Document</title>
		<script type="text/javascript">
        function appearbutton(id){
        				parent.button.changeid("returnbutton",id);
						parent.button.toggle("hidden");				
		}
		</script>
		</head>
		<body  onLoad="appearbutton(<?php echo($cm->id);?>)">			  
		</body>
		</html>