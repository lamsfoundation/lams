<%-- 
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
License Information: http://lamsfoundation.org/licensing/lams/2.0/

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License version 2 as 
  published by the Free Software Foundation.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
  USA

  http://www.gnu.org/licenses/gpl.txt
--%>

<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<html:html locale="true" xhtml="true">

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link href="<lams:LAMSURL/>/css/default.css" rel="stylesheet" type="text/css"/>
		<title>Learner :: LAMS</title>
	</head>

	<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

		<!-- URL's used in the movie-->
		<!-- text used in the movie-->
		<!--flash button pane-->
		<OBJECT classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
				codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,0,0"
				WIDTH="100%" HEIGHT="100%" ALIGN="">
			<PARAM NAME=movie VALUE="LearnerTopUI.swf" />
			<PARAM NAME=quality VALUE=high />
			<PARAM NAME=scale VALUE=noscale />
			<PARAM NAME=salign VALUE=LT />
			<EMBED src="LearnerTopUI.swf" quality=high scale=noscale salign=LT  WIDTH="100%" HEIGHT="100%" ALIGN=""
					TYPE="application/x-shockwave-flash" PLUGINSPAGE="http://www.macromedia.com/go/getflashplayer">
			</EMBED>
		</OBJECT>
	
	</body>
</html:html>
