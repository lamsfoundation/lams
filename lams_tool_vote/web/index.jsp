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

<%@ taglib uri="tags-bean" prefix="bean"%> 
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-logic-el" prefix="logic-el" %>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="fck-editor" prefix="FCK" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

	<!-- this form  exists temporarily to represent tool icon, remove this form once the tool is deployed into authoring environment -->
<%
	String toolContentId="1234";
	String hardCodedCopiedToolContentId="9876";
	String toolUrl="/authoringStarter?toolContentID=" + toolContentId;
	String copiedToolUrl="/authoringStarter?toolContentID=" + hardCodedCopiedToolContentId;
	
	String strCopyToolContent="/authoringStarter?toolContentID=" + toolContentId + "&copyToolContent=1";
	String strRemoveToolContent="/authoringStarter?toolContentID=" + toolContentId + "&removeToolContent=1";
	String strRemoveCopiedToolContent="/authoringStarter?toolContentID=" + hardCodedCopiedToolContentId + "&removeToolContent=1";
	String strDefineLater="/defineLaterStarter?toolContentID=" + hardCodedCopiedToolContentId;
	
	String strSetDefineLater="/authoringStarter?toolContentID=" + hardCodedCopiedToolContentId + "&setDefineLater=1";
	String strSetRunoffline="/authoringStarter?toolContentID=" + hardCodedCopiedToolContentId + "&strSetRunoffline=1";	
%>


<html:form action="<%=toolUrl%>" method="post">
      <table border=1>
      </table><br/>
      <html:submit value="Authoring on the original content"/>
</html:form>

<html:form action="<%=strCopyToolContent%>" method="post">
      <table border=1>
      </table><br/>
      <html:submit value="Copy Original Tool Content"/>
</html:form>

<html:form action="<%=copiedToolUrl%>" method="post">
      <table border=1>
      </table><br/>
      <html:submit value="Authoring on the copied content"/>
</html:form>

<html:form action="<%=strRemoveToolContent%>" method="post">
      <table border=1>
      </table><br/>
      <html:submit value="Remove Original Tool Content"/>
</html:form>

<html:form action="<%=strRemoveCopiedToolContent%>" method="post">
      <table border=1>
      </table><br/>
      <html:submit value="Remove Copied Tool Content"/>
</html:form>


<html:form action="<%=strDefineLater%>" method="post">
      <table border=1>
      </table><br/>
      <html:submit value="Define Later URL"/>
</html:form>

<html:form action="<%=strSetDefineLater%>" method="post">
      <table border=1>
      </table><br/>
      <html:submit value="Set as define later on the copied content"/>
</html:form>

<html:form action="<%=strSetRunoffline%>" method="post">
      <table border=1>
      </table><br/>
      <html:submit value="Set as run offline on the copied content"/>
</html:form>


