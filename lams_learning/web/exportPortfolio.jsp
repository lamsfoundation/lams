<%@ taglib uri="/WEB-INF/jstl/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>


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