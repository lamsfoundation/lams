<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<c:set var="randomID"><lams:generateID/></c:set>
<c:set var="passonurl" value="../toolcontent/import_passon.swf?learningDesignID=${learningDesignID}&uniqueID=${randomID}"/>

<!-- URL's used in the movie-->
<!-- text used in the movie-->
<!--Library-->
<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,47,0" name="passon" width="1" height="1" align="left" id="passon">
	<param name="allowScriptAccess" value="sameDomain" />

	<param name="movie" value="${passonurl}" />
	<param name="quality" value="high">
	<param name="scale" value="noscale">
	<param name="bgcolor" value="#FFFFFF">
	<embed src="<c:out value="${passonurl}" escapeXml="false"/>" quality="high" scale="noscale" bgcolor="#FFFFFF" width="1" height="1" swliveconnect=true id="passon" name="passon" align="" type="application/x-shockwave-flash"
		pluginspage="http://www.macromedia.com/go/getflashplayer" />
</object>
