<%@ Assembly Name="Microsoft.SharePoint, Version=12.0.0.0, Culture=neutral, PublicKeyToken=71e9bce111e9429c"%> 
<%@ Assembly Name="LamsSharePointIntegration, Version=1.0.0.0, Culture=neutral, PublicKeyToken=2c5da6fd93fafb88" %>
<%@ Page Language="C#" MasterPageFile="~/_layouts/application.master" 
         Inherits="Microsoft.SharePoint.WebControls.LayoutsPageBase"  %>

<%@ Import Namespace="Microsoft.SharePoint" %>
<%@ Import Namespace="LamsSharePointIntegration" %>
<%@ Import Namespace="Microsoft.SharePoint.Administration" %>


<script runat="server">
    
    
    
    string url;
    string lsId;
    string authorised;
    string message;
    protected override void OnLoad(EventArgs e) 
    {
        SPSite site = SPContext.Current.Site;
        SPWeb rootSite = site.RootWeb;
        SPWeb web = null;
        SPUser user = rootSite.CurrentUser;
        string method = Request.Params["Method"];
        string siteUrl = Request.Params["SiteUrl"];
        message = "You do not have access to this page.";

        if (this.Request.Params["siteUrl"] != null)
        {
            web = new SPSite(this.Request.Params["siteUrl"]).OpenWeb();
        }
        else
        {
            web = SPContext.Current.Web;  
        }
        
        if (method == "preview")
        {
            string ldId = Request.Params["ldId"];
            lsId = LAMSSecurityUtil.startLesson(user, web, ldId, "preview", "", "preview");
            string previewUrl = LAMSSecurityUtil.generateLoginRequestUrl(user, web, "learner") + "&lsid=" + lsId;
            if (previewUrl == null || previewUrl == "")
            {
                message = "Could not get request url, user profile could not be found. Please contact your system administrator.";
            }
            else
            {
                Response.Redirect(previewUrl);
            }
        }
        else
        {    
            string listId = Request.Params["ListId"];
            string itemId = Request.Params["ItemId"];
            authorised = "no";
            SPList lessonList = web.Lists[new Guid(listId)];
            SPListItem lessonListItem = lessonList.Items.GetItemById(Convert.ToInt32(itemId));
            lsId = lessonListItem["LessonID"].ToString();


            // monitor param has generated GUID appended to prevent students from accessing the monitor page
            if (method == "monitor30B3268AB3C849b6971DEE8E0A1B66C4")
            {
                url = LAMSSecurityUtil.generateLoginRequestUrl(user, web, "monitor");
                if (url == null || url == "") 
                {
                    message = "Could not get request url, user profile could not be found. Please contact your system administrator.";
                    authorised = "no"; 
                    
                }
                else
                {
                    authorised = "yes";  
                }

                
            }
            else if (method == "learner")
            {
                DateTime now = DateTime.Now;
                DateTime nullDate = new DateTime();
                
                Boolean schedule = (Boolean)lessonListItem["Scheduled"];
                Boolean available = (Boolean)lessonListItem["Lesson Available"];
                DateTime start = new DateTime();
                if (lessonListItem["Start Date"] != null) { start = (DateTime)lessonListItem["Start Date"]; }
                DateTime finish = new DateTime();
                if (lessonListItem["End Date"] != null) { finish = (DateTime)lessonListItem["End Date"]; }


                if (available == false)
                {
                    // output that this lesson is hidden, not availavble
                    message = "Lesson is not currently available, it has been blocked by the teacher.";
                }
                else if (schedule == true)
                {
                    // Start date must be not null, and before now
                    if (start != nullDate)  
                    {
                        if (start <= now)
                        {
                            // End date must be null or after now
                            if (finish != nullDate)
                            {
                                if (finish >= now)
                                {
                                    // within scheduled time, open learner 
                                    authorised = "yes";  
                                }
                                else
                                {
                                    message = "Lesson no longer available, finished at: " + finish.ToShortDateString() + " " + finish.ToShortTimeString();
                                }
                            }
                            else
                            {
                                // start scheduled with no end, run learner
                                authorised = "yes";
                            }
                            
                        }
                        else
                        {
                            message = "Lesson not yet available. It is scheduled to start at: " + start.ToShortDateString() + " " + start.ToShortTimeString();
                        }
                    }
                    else
                    {
                        message = "Lesson is not available. It has been set for scheduling, but has no start time set yet. Please try again later.";
                    }
                       
                }
                else
                {
                    // open learner
                    authorised = "yes";
                }
                
                
                if (authorised == "yes")
                {
                    url = LAMSSecurityUtil.generateLoginRequestUrl(user, web, method);
                    if (url == null || url == "")
                    {
                        message = "Could not get request url, user profile could not be found. Please contact your system administrator.";
                        authorised = "no";
                        

                    } 
                       
                }
            }
        }
        
    }


</script>

<asp:Content ID="Main" contentplaceholderid="PlaceHolderMain" runat="server">


    <script language="JavaScript" type="text/javascript">
    <!--
	    var lamsWindow = null;
	    var url = null;
    	var authorised = "yes";
    	var success = "false";
    	var message = "<%=message %>";

    	
    	
    	if (authorised == '<%=authorised %>')
    	{
    	    openWindow('<%=url %>', '<%=lsId %>');
    	}
    	
    	if (success == "true")
    	{
    	    history.go(-1);
    	}
    	
    	
	    function openWindow(urlIn, lsId)
	    {    
		    url = urlIn + '&lsid=' + lsId; 
		    if(lamsWindow && lamsWindow.open && !lamsWindow.closed){
	            try {
	        	    lamsWindow.focus();
	        	    success = "true";
	            }catch(e){
	        	    // popups blocked by a 3rd party
	        	    success = "false";
	        	    message = "You must enable popups to use LAMS.";
	            }
	        }
	        else{
	            try {
	                lamsWindow = window.open(url,'lWin','width=800,height=600,resizable=1');
	                lamsWindow.focus();
	                success = "true";
	            }catch(e){
	        	    // popups blocked by a 3rd party
	        	    success = "false";
	        	    message = "You must enable popups to use LAMS.";
	            }
	        }
	    }
    //-->
    </script>
    
    
    <h3>
    <script language="JavaScript" type="text/javascript">
        <!--
        document.write(message);
        //-->
    </script>
    </h3>
    
    <br />
    <input type="button" id="Button1" onclick="javascript:history.go(-1)" value="Back" />     
</asp:Content>

<asp:Content ID="PageTitle" contentplaceholderid="PlaceHolderPageTitle" runat="server">
	LAMS Lesson
</asp:Content>

<asp:Content ID="PageTitleInTitleArea" runat="server"
             contentplaceholderid="PlaceHolderPageTitleInTitleArea" >
    LAMS Lesson
</asp:Content>



