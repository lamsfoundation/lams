<%// Discussion Grader, Copyright 2004 Joliet Junior College  
// Home Page : http://www.jjc.edu/distance/
// Author : Jeff Nuckles jnuckles@jjc.edu
%>
<%@ page import="java.util.*,
        java.text.*,
         blackboard.data.*,
         blackboard.data.user.*,
         blackboard.data.course.*,
		 blackboard.data.gradebook.*,
         blackboard.persist.*,
         blackboard.persist.user.*,
	     blackboard.persist.course.*,
		 blackboard.persist.gradebook.*,
		 blackboard.persist.gradebook.impl.*,
		 blackboard.data.gradebook.impl.*,
         blackboard.base.*,
         blackboard.base.BbList.*,
         blackboard.platform.*,
         blackboard.platform.session.*,
         blackboard.platform.persistence.*,
         blackboard.platform.security.*,
		 blackboard.platform.plugin.PlugInUtil"
        errorPage="/error.jsp"
 %>
<%@ taglib uri="/bbData" prefix="bbData"%>
<%@ taglib uri="/bbUI" prefix="bbUI"%>

<bbData:context id="ctx" entitlement="course.gradebook.MODIFY">
<%
Course course = ctx.getCourse();
Id courseId = ctx.getCourse().getId();
String cidString = courseId.toExternalString();
String fid = request.getParameter("forum_pk1");
BbPersistenceManager bbPm = BbServiceManager.getPersistenceService().getDbPersistenceManager();
blackboard.persist.gradebook.impl.OutcomeDefinitionCategoryDbLoader ocdloader = (blackboard.persist.gradebook.impl.OutcomeDefinitionCategoryDbLoader) bbPm.getLoader( blackboard.persist.gradebook.impl.OutcomeDefinitionCategoryDbLoader.TYPE );
BbList ocdlist = ocdloader.loadByCourseId(courseId);
BbList.Iterator ocdListIter = ocdlist.getFilteringIterator();
blackboard.persist.gradebook.impl.OutcomeDefinitionScaleDbLoader ocdcloader = (blackboard.persist.gradebook.impl.OutcomeDefinitionScaleDbLoader) bbPm.getLoader( blackboard.persist.gradebook.impl.OutcomeDefinitionScaleDbLoader.TYPE );
BbList ocdclist = (BbList) ocdcloader.loadByCourseId(courseId);
BbList.Iterator ocdcListIter = ocdclist.getFilteringIterator();
OutcomeDefinitionDbLoader ocdLoader = (OutcomeDefinitionDbLoader)bbPm.getLoader(OutcomeDefinitionDbLoader.TYPE);
OutcomeDefinitionScaleDbLoader ods2Loader = (OutcomeDefinitionScaleDbLoader)bbPm.getLoader(OutcomeDefinitionScaleDbLoader.TYPE);
OutcomeDefinitionDbPersister ocdPersister = (OutcomeDefinitionDbPersister)bbPm.getPersister(OutcomeDefinitionDbPersister.TYPE);
Calendar cur_date= Calendar.getInstance(); 
%>


<bbUI:docTemplate title = "Add Discussion Gradebook Item">
<bbUI:breadcrumbBar handle="jjcd-jjcdg-nav-1">
<bbUI:breadcrumb>Add/Modify Gradebook Item</bbUI:breadcrumb>
</bbUI:breadcrumbBar>
<bbUI:titleBar iconUrl ="/images/ci/icons/bookopen_u.gif">Add/Modify Gradebook Item</bbUI:titleBar>


<%if (request.getParameter("title") == null) {%>

<form name="newliform" method="POST" action="newlineitem.jsp">
   <bbUI:step title="Item Information">
        <bbUI:dataElement label="Item Name" required="true">
			<input type="text" name="title" maxlength="255" size="32" value="">
		</bbUI:dataElement>
	    <bbUI:dataElement label="Category">
			<select name="categoryId">
			<option selected="selected">Discussion Participation</option>
			<%
			while (ocdListIter.hasNext()) 
			{
			try {
				OutcomeDefinitionCategory ocdCategory = (blackboard.data.gradebook.impl.OutcomeDefinitionCategory) ocdListIter.next();
				if (!ocdCategory.getTitle().equals("Weighted Total") || !ocdCategory.getTitle().equals("Total")) {%>
<OPTION VALUE="<%=ocdCategory.getTitle()%>"><%=ocdCategory.getTitle()%></OPTION>
<%}%>

<%} catch (Exception c){
%>Key Not Found <%=c%><%
}}%>
			</select>
	   </bbUI:dataElement>
	   <bbUI:dataElement label="Description">
	   <textarea name="description" cols="35" rows="4"></textarea>
	   </bbUI:dataElement>
	   <bbUI:dataElement label="Date">
	   <bbUI:datePicker formName="newliform" startDate="<%=cur_date%>"></bbUI:datePicker>
	   </bbUI:dataElement>
	   
        <bbUI:dataElement label="Points Possible">
 		<input type="text" name="possible" maxlength="8" size="4" value="">
    	</bbUI:dataElement>
		<bbUI:dataElement label="Display As">

		<select name="DisplayAs">
		<%
		BbList odsList = (BbList)ods2Loader.loadByCourseId(courseId);
		BbList.Iterator odsListIter = odsList.getFilteringIterator(); 
		while (odsListIter.hasNext()) 
			{
			OutcomeDefinitionScale ods2 = (OutcomeDefinitionScale)odsListIter.next();
			%>
			<OPTION<%if (ods2.getTitle().equals("Score")){%> selected<%}%>><%=ods2.getTitle()%></OPTION>
			<%
			}

			%>
		</select>
    	</bbUI:dataElement>
</bbUI:step>
   <bbUI:step title="Options">
   <bbUI:instructions> Select <B>No</B> for the first option to make this Gradebook item unavailable in the Student Gradebook. Select <B>No</B> for the second option to exclude this Gradebook item from summary calculations. Gradebook items excluded from summary calculations are also excluded from weighting. Also note that if some weighted items are included in calculations and other weighted items are not, grade weight calculations will be skewed.
   	</bbUI:instructions>
    	<bbUI:dataElement label="Make item available to users">
	      <input type="radio" name="visible" value="true" checked="checked">Yes
          <input type="radio" name="visible" value="false">No
    	</bbUI:dataElement>
	    <bbUI:dataElement label="Include item in Gradebook score calculations.">
	      <input type="radio" name="scorable" value="true" checked="checked">Yes
          <input type="radio" name="scorable" value="false">No
    	</bbUI:dataElement>
	</bbUI:step>
<bbUI:stepSubmit title="Submit"/>
    <input type="hidden" name="course_id" value="<%=courseId.toExternalString()%>"/>
	<input type="hidden" name="forum_pk1" value="<%=request.getParameter("forum_pk1")%>"/>
    </form>
<% } else { %>
<%
String result="A new discussion gradebook entry has been created and assigned to this forum.";
try{

LineitemDbLoader liLoader = (LineitemDbLoader) bbPm.getLoader(LineitemDbLoader.TYPE);
LineitemDbPersister liPersister = (LineitemDbPersister) bbPm.getPersister(LineitemDbPersister.TYPE);
Lineitem li = new Lineitem();

li.setCourseId(courseId);
li.setName(request.getParameter("title"));
li.setType(request.getParameter("categoryId"));
li.setAssessmentLocation( Lineitem.AssessmentLocation.EXTERNAL );
li.setAssessmentId(request.getParameter("forum_pk1"),Lineitem.AssessmentLocation.EXTERNAL);
li.setAnalysisHandlerUrl("/webapps/jjde-jjcdd-bb_bb60/modules/index.jsp");
li.setPointsPossible((new Float(request.getParameter("possible"))).floatValue());
li.setDateAdded();
li.setColumnOrder(3000);
if (request.getParameter("visible").equals("true")){
li.setIsAvailable(true);
} else {
li.setIsAvailable(false);
}
li.validate();
liPersister.persist(li);

OutcomeDefinition ocd = li.getOutcomeDefinition();
if (request.getParameter("scorable").equals("true")){
ocd.setScorable(true);
} else {
ocd.setScorable(false);
}
ocd.setDescription(request.getParameter("description"));
OutcomeDefinitionScale ods;
try{
ods = ods2Loader.loadByCourseIdAndTitle(courseId,request.getParameter("DisplayAs"));
} catch (Exception e){
ods = ods2Loader.loadByCourseIdAndTitle(courseId,request.getParameter("DisplayAs")+".title");
}
ocd.setScale(ods);

SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
Calendar cstart = Calendar.getInstance();
cstart.setTime(formatter.parse(request.getParameter("start_date_0")));
ocd.setDueDate(cstart);
ocdPersister.persist(ocd);
} catch (java.lang.NumberFormatException e){
result = "Sorry, some required fields may have been left blank, please return the previous page to correct this situations and submit the form again.";
}
%>
<table cellspacing="0" cellpadding="0" width="100%">
  <tr> 
    <td> 
      <table cellspacing="0" border="0" cellpadding="5" width="100%">
        <tr>  
          <td width="20" valign="top"><img src="/images/spacer.gif" height="22" width="22" hspace="0" vspace="0" alt="" border="0" /></td>           
          <td width="100%" valign="top"> 
<%=result%>
<br>         
<br>         
<span class="receiptDate"><%=(new Date()).toString()%></span>  
</td>                                        
                </tr>                    
                <tr>                         
                        <td align="right" colspan="6">    
<a href="select.jsp?course_id=<%=cidString%>&forum_pk1=<%=fid%>"><img ALT="OK" name="img_ok" src="/images/ci/formbtns/ok_off.gif" WIDTH="69" HEIGHT="20" BORDER="0" HSPACE="5"></a>

          </td>      
                </tr>
                <tr> 
                        <td align="right" colspan="6"><img src="/images/spacer.gif" height="1" width="10" hspace="0" vspace="0" alt="" border="0" /></td>                         
                </tr>       
        </table></td>       
</tr>                       

</table>        
<%}%>




</bbUI:docTemplate>

</bbData:context>