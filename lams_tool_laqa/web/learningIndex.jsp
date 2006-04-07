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
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="fck-editor" prefix="FCK" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<%
	String toolSessionID="88888888";
	String toolUrl="/learningStarter?toolSessionID=" + toolSessionID + "&mode=learner";
	
	
	String toolSessionID2="55555555";
	String toolUrl2="/learningStarter?toolSessionID=" + toolSessionID2 + "&mode=learner";
	
	String toolUrlTeacher="/learningStarter?toolSessionID=" + toolSessionID + "&mode=teacher";
	
	String strCreateToolSession="/learningStarter?toolSessionID=" + toolSessionID2 +  "&mode=learner" + "&createToolSession=1";
	String strCreateToolSession2="/learningStarter?toolSessionID=" + toolSessionID +  "&mode=learner" + "&createToolSession=1";
	String strRemoveToolSession="/learningStarter?toolSessionID=" + toolSessionID2 +  "&mode=learner" + "&removeToolSession=1";
	String strLeaveToolSession="/learningStarter?toolSessionID=" + toolSessionID2 +  "&mode=learner" + "&leaveToolSession=1" +  "&learnerId=4";

	String strPreview="/learningStarter?toolSessionID=" + toolSessionID2 + "&mode=author";	
	String strLearnerProgress="/learningStarter?toolSessionID=" + toolSessionID2 +  "&mode=teacher" + "&userID=4";
	String strLearnerProgress2="/learningStarter?toolSessionID=" + toolSessionID2 +  "&mode=teacher" + "&userID=1";
	String strLearnerProgress3="/learningStarter?toolSessionID=" + toolSessionID +  "&mode=teacher" + "&userID=4";
	String strLearnerProgress4="/learningStarter?toolSessionID=" + toolSessionID +  "&mode=teacher" + "&userID=1";
	
	String exportPortfolioStudent8 = "/exportPortfolio.do?mode=learner&toolSessionID=88888888&userID=4";	
	String exportPortfolioStudent5 = "/exportPortfolio.do?mode=learner&toolSessionID=55555555&userID=1";	
	
	String exportPortfolioStudent9 = "/exportPortfolio.do?mode=learner&toolSessionID=55555555&userID=5";	
	String exportPortfolioStudent10 = "/exportPortfolio.do?mode=learner&toolSessionID=88888888&userID=6";	
%>

<html:form action="<%=strCreateToolSession%>" method="post">
      <table border=1>
      </table><br/><BR>
      <html:submit value="Create Tool Session: 55555555"/>
</html:form>

<html:form action="<%=strCreateToolSession2%>" method="post">
      <table border=1>
      </table><br/><BR>
      <html:submit value="Create Tool Session: 88888888"/>
</html:form>

<html:form action="<%=strCreateToolSession2%>" method="post">
      <table border=1>
      </table><br/><BR>
      <html:submit value="Create Tool Session - for sysadmin: 88888888"/>
</html:form>


<html:form action="<%=toolUrl%>" method="post">
      <table border=1>
      </table><br/><BR>
      <html:submit value="Session 1 - Learning Starter, mode:learner"/>
</html:form>

<html:form action="<%=toolUrl2%>" method="post">
      <table border=1>
      </table><br/><BR>
      <html:submit value="Session 2 - Learning Starter, mode:learner"/>
</html:form>

<html:form action="<%=toolUrlTeacher%>" method="post">
      <table border=1>
      </table><br/><BR>
      <html:submit value="Learning Starter, mode:teacher"/>
</html:form>

<html:form action="<%=strRemoveToolSession%>" method="post">
      <table border=1>
      </table><br/><BR>
      <html:submit value="Remove Tool Session"/>
</html:form>

<html:form action="<%=strLeaveToolSession%>" method="post">
      <table border=1>
      </table><br/><BR>
      <html:submit value="Leave Tool Session"/>
</html:form>

<html:form action="<%=strPreview%>" method="post">
      <table border=1>
      </table><br/>
      <html:submit value="Preview with ToolAcessMode=AUTHOR"/>
</html:form>

<html:form action="<%=strLearnerProgress%>" method="post">
      <table border=1>
      </table><br/><BR>
      <html:submit value="View Learner Progress 55555555- id:4"/>
</html:form>

<html:form action="<%=strLearnerProgress2%>" method="post">
      <table border=1>
      </table><br/><BR>
      <html:submit value="View Learner Progress 55555555- id:1"/>
</html:form>

<html:form action="<%=strLearnerProgress3%>" method="post">
      <table border=1>
      </table><br/><BR>
      <html:submit value="View Learner Progress 88888888 id:4"/>
</html:form>

<html:form action="<%=strLearnerProgress4%>" method="post">
      <table border=1>
      </table><br/><BR>
      <html:submit value="View Learner Progress 88888888 id:1"/>
</html:form>

<html:form action="<%=exportPortfolioStudent8%>" method="post">
      <table border=1>
      </table><br/><BR>
      <html:submit value="Export Portfolio Student, session id: 88888888"/>
</html:form>

<html:form action="<%=exportPortfolioStudent5%>" method="post">
      <table border=1>
      </table><br/><BR>
      <html:submit value="Export Portfolio Student, session id: 55555555"/>
</html:form>

<html:form action="<%=exportPortfolioStudent9%>" method="post">
      <table border=1>
      </table><br/><BR>
      <html:submit value="Export Portfolio Student, session id: 55555555, id:5"/>
</html:form>

<html:form action="<%=exportPortfolioStudent10%>" method="post">
      <table border=1>
      </table><br/><BR>
      <html:submit value="Export Portfolio Student, session id: 88888888, id=6"/>
</html:form>

