<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-bean" prefix="bean" %>


<html:html>
<head><title>Export Portfolio</title></head>
<body>

<table>
<th>Portfolio</th>

<tr><td>Activities</td></tr>
<tr> <td> <table>
<logic:present name="portfolioList">
<bean:size id="count" name="portfolioList" />
<logic:notEqual name="count" value="0">
<logic:iterate name="portfolioList" id="portfolio">
	<tr>
	
		<td>*<bean:write name="portfolio" property="activityName"/>:</td>
		<td><a href="<bean:write name="portfolio" property="toolLink"/>"><bean:write name="portfolio" property="activityDescription"/></a></td>
		
	</tr>
</logic:iterate>
</logic:notEqual>
</table></td></tr>


</logic:present>



</table>
</body>
</html:html>