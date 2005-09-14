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
		<td><a href="#act<bean:write name="portfolio" property="activityId"/>"><bean:write name="portfolio" property="activityDescription"/></a></td>
		
	</tr>
</logic:iterate>
</logic:notEqual>
</table></td></tr>


<tr> <td> <table>
<bean:size id="counter2" name="portfolioList" />
<logic:notEqual name="counter2" value="0">
<logic:iterate name="portfolioList" id="portfolio">
	<tr>
	<td>
		<a name="act<bean:write name="portfolio" property="activityId"/>"><bean:write name="portfolio" property="activityName"/>:
		<bean:write name="portfolio" property="activityDescription"/></a>	
	</td>	
	<td>
		<IFRAME name="frame<bean:write name="portfolio" property="activityId"/>" src="http://localhost:8080/lams/tool/lanb11/exportPortfolio.do?mode=learner&toolSessionId=455&userId=555"
		frameborder="0"></IFRAME>
	</td>
	</tr>
</logic:iterate>
</logic:notEqual>
</table></td></tr>

</logic:present>



</table>
</body>
</html:html>