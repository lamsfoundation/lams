<%@ taglib uri="tags-lams" prefix="lams"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
		<title>Import tool content</title>
		<!-- ********************  CSS ********************** -->
		<lams:css />
	</head>

	<BODY>
		<h1>
			Upload Learning Design
		</h1>

		<form action="/ImportToolContent" method="post" enctype="multipart/form-data">
			Learning Design zip file: <input type="file" name="UPLOAD_FILE" />
			<input type="submit" name="Upload" />
		</form>

	</BODY>
</HTML>
