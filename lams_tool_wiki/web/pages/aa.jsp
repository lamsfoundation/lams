<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
                    "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
  <script type="text/javascript"src="<lams:WebAppURL />/includes/javascript/jquery-latest.pack.js"></script>
  <link rel="stylesheet" href="<lams:WebAppURL />/includes/css/screen.css" type="text/css" />
  <link rel="stylesheet" href="<lams:WebAppURL />/includes/css/jquery.treeview.css" type="text/css" />
  <script type="text/javascript" src="<lams:WebAppURL />/includes/javascript/jquery.treeview.pack.js"></script>
  <script>
  $(document).ready(function(){
    $("#example").treeview();
  });
  </script>
  
</head>
<body>
  <ul id="example" class="filetree">
		<li><span class="file">&nbsp;<a href="http://yahoo.com">yahoo.com</a></span>
			<ul>
				<li><span class="file">Item 1.1</span></li>
			</ul>
		</li>
  </ul>
</body>
</html>