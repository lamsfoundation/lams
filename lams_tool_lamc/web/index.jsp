<%@ taglib uri="tags-html-el" prefix="html" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-logic-el" prefix="logic-el" %>
<%@ taglib uri="tags-c" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-fck-editor" prefix="FCK" %>

	<!-- this form  exists temporarily to represent tool icon, remove this form once the tool is deployed into authoring environment -->
<%
	String toolContentId="1234";
	String hardCodedCopiedToolContentId="9876";
	String toolUrl="/authoringStarter?toolContentID=" + toolContentId;
	String copiedToolUrl="/authoringStarter?toolContentID=" + hardCodedCopiedToolContentId;
	
	String strCopyToolContent="/authoringStarter?toolContentID=" + toolContentId + "&copyToolContent=1";
	String strRemoveToolContent="/authoringStarter?toolContentID=" + toolContentId + "&removeToolContent=1";
	String strRemoveCopiedToolContent="/authoringStarter?toolContentID=" + hardCodedCopiedToolContentId + "&removeToolContent=1";
	String strDefineLater="/defineLaterStarter?toolContentID=" + toolContentId;
	
	String strSetDefineLater="/authoringStarter?toolContentID=" + hardCodedCopiedToolContentId + "&setDefineLater=1";
	String strSetRunoffline="/authoringStarter?toolContentID=" + hardCodedCopiedToolContentId + "&strSetRunoffline=1";	
%>


<html:form action="<%=toolUrl%>" method="post">
      <table border=1>
      </table><br/>
      <html:submit value="Authoring on the original content"/>
</html:form>

<html:form action="<%=copiedToolUrl%>" method="post">
      <table border=1>
      </table><br/>
      <html:submit value="Authoring on the copied content"/>
</html:form>


<html:form action="<%=strCopyToolContent%>" method="post">
      <table border=1>
      </table><br/>
      <html:submit value="Copy Original Tool Content"/>
</html:form>

<html:form action="<%=strRemoveCopiedToolContent%>" method="post">
      <table border=1>
      </table><br/>
      <html:submit value="Remove Copied Tool Content"/>
</html:form>

<html:form action="<%=strRemoveToolContent%>" method="post">
      <table border=1>
      </table><br/>
      <html:submit value="Remove Original Tool Content"/>
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
