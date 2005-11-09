<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" %>
<c:set var="lams"><lams:LAMSURL/></c:set>

<tr>
<td>
<table width="100%" border="0" cellpadding="0" cellspacing="0" summary="This table is being used for layout purposes only">
  <tr> 
    <td width="136" height="10"></td>
    <td width="92%" height="10"></td>
  </tr>
  <tr bgcolor="#282871"> 
    <td width="50%" height="15" align="left">
    		<img height=8 width=8 src="<c:out value="${lams}"/>/images/spacer.gif">
    		<font color="#FFFFFF" size="1" face="Verdana, Arial, Helvetica, sans-serif"><c:out value="${pageheader}" />
</font>
    </td>        
    <td width="50%" height="15" align="right" > 
    		<a href="<c:out value="${lams}"/>/doc/LAMS_Learner_Guide_b60.pdf" target = 0>
    			<font color="#FFFFFF" size="1" face="Verdana, Arial, Helvetica, sans-serif">[HELP]</font>
    		</a>
    		<img height=8 width=8 src="<c:out value="${lams}"/>/images/spacer.gif"  alt="space image">
    </td>
  </tr>
</table>
</td>
</tr>
