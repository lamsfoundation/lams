<%@ Assembly Name="Microsoft.SharePoint, Version=12.0.0.0, Culture=neutral, PublicKeyToken=71e9bce111e9429c"%> 
<%@ Page Language="C#" MasterPageFile="~/_layouts/application.master" 
         Inherits="Microsoft.SharePoint.WebControls.LayoutsPageBase"  %>
<%@ Register Tagprefix="SharePoint" Namespace="Microsoft.SharePoint.WebControls" Assembly="Microsoft.SharePoint, Version=12.0.0.0, Culture=neutral, PublicKeyToken=71e9bce111e9429c" %> 
<%@ Register Tagprefix="WebPartPages" Namespace="Microsoft.SharePoint.WebPartPages" Assembly="Microsoft.SharePoint, Version=12.0.0.0, Culture=neutral, PublicKeyToken=71e9bce111e9429c" %>
<%@ Register TagPrefix="Utilities" Namespace="Microsoft.SharePoint.Utilities" Assembly="Microsoft.SharePoint, Version=12.0.0.0, Culture=neutral, PublicKeyToken=71e9bce111e9429c" %>
<%@ Import Namespace="Microsoft.SharePoint" %>
<%@ Import Namespace="System" %>


 <script runat="server">
    
    private Boolean hasStartDate;
    SPList lessonList;
    SPListItem lessonListItem;
    protected override void OnLoad(EventArgs e)
    {
        string listId = Request.Params["ListId"];
        string itemId = Request.Params["ItemId"];
        SPSite site = SPContext.Current.Site;
        SPWeb rootSite = site.RootWeb;
        SPWeb web = SPContext.Current.Web;
        lessonList = web.Lists[new Guid(listId)];
        lessonListItem = lessonList.Items.GetItemById(Convert.ToInt32(itemId));
        if (lessonListItem["Start Date"] != null) { hasStartDate = true; }
        
        if (!Page.IsPostBack)
        {
            DateTime now = DateTime.Now;
            now = now.AddMinutes(10); // add 5 minutes to now 
            DateTime nullDate = new DateTime();
            DateTime start = new DateTime();
            DateTime end = new DateTime();

            if (lessonListItem["Start Date"] != null) { start = (DateTime)lessonListItem["Start Date"]; }
            if (lessonListItem["End Date"] != null) { end = (DateTime)lessonListItem["End Date"]; }

            
            if (start == nullDate)
            {
                // Populate start with todays date
                populateDateTimeField(now, true);
            }
            else
            {
                populateDateTimeField(start, true);
            }

            if (end != nullDate)
            {
                populateDateTimeField(end, false);
            }
        }
        
    }

    private void populateDateTimeField(DateTime dateTime, Boolean isStart)
    {
        TextBox txtDate;
        DropDownList ddlHour;
        DropDownList ddlMin;
        DropDownList ddlAMPM;

        if (isStart)
        {
            txtDate = this.txtStartDate;
            ddlHour = this.ddlStartHour;
            ddlMin = this.ddlStartMin;
            ddlAMPM = this.ddlStartAMPM;
        }
        else
        {
            txtDate = this.txtEndDate;
            ddlHour = this.ddlEndHour;
            ddlMin = this.ddlEndMin;
            ddlAMPM = this.ddlEndAMPM;
        }

        string dayStr = dateTime.Day >= 10 ? dateTime.Day.ToString() : "0" + dateTime.Day.ToString();
        string monthStr = dateTime.Month >= 10 ? dateTime.Month.ToString() : "0" + dateTime.Month.ToString(); ;
        txtDate.Text = dayStr + "/" + monthStr + "/" + dateTime.Year.ToString();

        int hour = dateTime.Hour;
        Boolean isAM = false;

        if (hour == 0)
        {
            hour = 12;
            isAM = true;
        }
        else if (hour < 12)
        {
            isAM = true;
        }
        else if (hour > 12)
        {
            hour -= 12;
        }


        int minute = dateTime.Minute;

        if (minute >= 55)
        {
            minute = 11;
        }
        else
        {
            minute = (minute / 5);
        }


        ddlHour.Items[hour - 1].Selected = true;
        ddlMin.Items[minute].Selected = true;

        if (isAM)
        {
            ddlAMPM.Items[0].Selected = true;
        }
        else
        {
            ddlAMPM.Items[1].Selected = true;
        }
    }

    protected void clear(object sender, EventArgs e)
    {
        txtEndDate.Text = "";
        txtStartDate.Text = "";
    }
     
    protected void saveSchedule(object sender, EventArgs e)
    {
        lblMessage.Text = "";
        startDateErrorFlag.Text = "";
        endDateErrorFlag.Text = "";
        
        //SPSite site = SPContext.Current.Site;
        //SPWeb rootSite = site.RootWeb;
        //string listId = Request.Params["ListId"];
        //string itemId = Request.Params["ItemId"];
        //SPList lessonList = rootSite.Lists[new Guid(listId)];
        //SPListItem lessonListItem = lessonList.Items.GetItemById(Convert.ToInt32(itemId));



        Boolean validStartDate = false;
        Boolean validEndDate = false;
        DateTime start = new DateTime();
        DateTime end = new DateTime();
        
        
        string startStr = txtStartDate.Text;
        string endStr = txtEndDate.Text;

        if (startStr != null && startStr != "") 
        {
            if (!validateDateString(startStr))
            {
                lblMessage.Text = "Error: Incorrect form for start date, required in the form dd/mm/yyyy.";
                startDateErrorFlag.Text = "*";
                return;
            }
            
            
            char[] splitChar = {'/'};
            string[] startSplit = startStr.Split(splitChar);
            DateTime now = DateTime.Now;
            
            int hour = Convert.ToInt32(ddlStartHour.SelectedValue);
            Boolean isAM = true;
            if (ddlStartAMPM.SelectedValue == "PM")
            {
                isAM = false;
                hour = hour < 12 ? hour += 12 : hour;
            }
            else if (hour == 12)
            {
                hour = 0;
            }

            start = new DateTime();
            
            try
            {
                start = new DateTime(
                    Convert.ToInt32(startSplit[2]),
                    Convert.ToInt32(startSplit[1]),
                    Convert.ToInt32(startSplit[0]),
                    hour,
                    Convert.ToInt32(ddlStartMin.SelectedValue),
                    0);
            }
            catch (Exception)
            {
                lblMessage.Text = "Error: Date out of range. Please select a valid date.";
                startDateErrorFlag.Text = "*";
                return;
            }



            if (!hasStartDate)
            {
                if (start < now && hasStartDate == false)
                {
                    lblMessage.Text = "Error: Start time must be after now. Current time: " + now.ToLongDateString() + " " + now.ToShortTimeString();
                    startDateErrorFlag.Text = "*";
                    return;
                }
                else
                {
                    validStartDate = true;
                    //saveDateTime(start, true);
                }
            }
            else
            {
                if (start < now && start != (DateTime)lessonListItem["Start Date"])
                {
                    lblMessage.Text = "Error: Cannot modify existing start date to start in the past.";
                    startDateErrorFlag.Text = "*";
                    return;
                }
                else
                {
                    validStartDate = true;
                    //saveDateTime(start, true);
                }
            }     
            
            if (endStr != null && endStr != "")
            {
                if (validateDateString(endStr))
                {
                    string[] endSplit = endStr.Split(splitChar);
                    int hourEnd = Convert.ToInt32(ddlEndHour.SelectedValue);
                    Boolean isEndAM = true;
                    if (ddlEndAMPM.SelectedValue == "PM")
                    {
                        isEndAM = false;
                        hourEnd = hourEnd == 12 ? hourEnd : hourEnd + 12;
                    }
                    
                    try
                    {
                        end = new DateTime(
                            Convert.ToInt32(endSplit[2]),
                            Convert.ToInt32(endSplit[1]),
                            Convert.ToInt32(endSplit[0]),
                            hourEnd,
                            Convert.ToInt32(ddlEndMin.SelectedValue),
                            0);

                        if (end <= start)
                        {
                            lblMessage.Text = "Error: End date and time must be after start date and time.";
                            endDateErrorFlag.Text = "*";
                            return;
                        }
                        else
                        {
                            validEndDate = true;
                            //saveDateTime(end, false);
                        }
                    }
                    catch (Exception)
                    {
                        lblMessage.Text = "Error: Date out of range. Please select a valid date.";
                        endDateErrorFlag.Text = "*";
                        return;
                    }
                }
                else
                {
                    // validation error, end time is not in correct form
                    lblMessage.Text = "Error: End Date should be empty or in the form dd/mm/yyyy.";
                    endDateErrorFlag.Text = "*";
                    return;
                }
            }
        }
        else
        {
            if (endStr == null || endStr == "")
            {
                unSchedule();
            }
            else
            {
                lblMessage.Text = "Error: Start date is missing.";
                startDateErrorFlag.Text = "*";
                return;
            }    
        }
        
        if (validStartDate) {saveDateTime(start, true);}
        if (validEndDate)   {saveDateTime(end, false);}
        redirect();
    }

    private void saveDateTime(DateTime date, Boolean isStart)
    {
        if (isStart)
        {
            lessonListItem["Start Date"] = date;
        }
        else
        {
            lessonListItem["End Date"] = date;
        }

        lessonListItem["Scheduled"] = true;
        lessonListItem["Lesson Available"] = true;
        lessonListItem.Update();
        lessonList.Update();
        
        
    }

    protected void unSchedule()
    {
        Boolean scheduled = (Boolean)lessonListItem["Scheduled"];

        if (scheduled == true)
        {
            lessonListItem["Scheduled"] = false;
            lessonListItem["Start Date"] = null;
            lessonListItem["End Date"] = null;
            lessonListItem.Update();
        }
        lessonList.Update();
        redirect();
    }

    private Boolean validateDateString(string date)
    {
        if (Regex.IsMatch(date, @"^\d\d/\d\d/\d\d\d\d$"))
        {
            return true;
        }
        else
        {
            return false;
        }
            
           
    }

    public void cancel(object sender, EventArgs e)
    {
        redirect();
    }

    protected void redirect()
    {
        SPUtility.Redirect( lessonList.DefaultViewUrl,
                           SPRedirectFlags.Default,
                           HttpContext.Current);
        
    }
        

    

     

</script>

<asp:Content ID="Main" contentplaceholderid="PlaceHolderMain" runat="server">
  
 
  <table cellpadding="2" cellspacing="0" id="Tablez">
       
    <tr><td>
      <table border="0" cellpadding="10" cellspacing="0" class="ms-formtable">
        <tr>
          <td nowrap="true" valign="top" width="190px" class="ms-formlabel">
            <asp:Label ID="startDateErrorFlag" runat="server" Text="" ForeColor="Red" />
            <h3 class="ms-standardheader"><nobr>Start Date: </nobr></h3>
          </td>
          <td valign="top" class="ms-formbody" width="400px">
             <asp:TextBox ID="txtStartDate" runat="server"/>
             <asp:DropDownList ID="ddlStartHour" runat="server">
                <asp:ListItem Text="1" Value="1"/>
                <asp:ListItem Text="2" Value="2"/>
                <asp:ListItem Text="3" Value="3"/>
                <asp:ListItem Text="4" Value="4"/>
                <asp:ListItem Text="5" Value="5"/>
                <asp:ListItem Text="6" Value="6"/>
                <asp:ListItem Text="7" Value="7"/>
                <asp:ListItem Text="8" Value="8"/>
                <asp:ListItem Text="9" Value="9"/>
                <asp:ListItem Text="10" Value="10"/>
                <asp:ListItem Text="11" Value="11"/>
                <asp:ListItem Text="12" Value="12"/>
             </asp:DropDownList>
             
             <asp:DropDownList ID="ddlStartMin" runat="server">
                <asp:ListItem Text="00" Value="0"/>
                <asp:ListItem Text="05" Value="5"/>
                <asp:ListItem Text="10" Value="10"/>
                <asp:ListItem Text="15" Value="15"/>
                <asp:ListItem Text="20" Value="20"/>
                <asp:ListItem Text="25" Value="25"/>
                <asp:ListItem Text="30" Value="30"/>
                <asp:ListItem Text="35" Value="35"/>
                <asp:ListItem Text="40" Value="40"/>
                <asp:ListItem Text="45" Value="45"/>
                <asp:ListItem Text="50" Value="50"/>
                <asp:ListItem Text="55" Value="55"/>   
             </asp:DropDownList>
             
             <asp:DropDownList ID="ddlStartAMPM" runat="server">
                <asp:ListItem Text="AM" Value="AM"/>
                <asp:ListItem Text="PM" Value="PM"/>
             </asp:DropDownList>
             <br />Start date and time for the lesson (dd/mm/yyyy).
          </td>
       </tr>
       <tr>
          <td nowrap="true" valign="top" width="190px" class="ms-formlabel">
            <asp:Label ID="endDateErrorFlag" runat="server" Text="" ForeColor="Red" />
            <h3 class="ms-standardheader"><nobr>End Date:</nobr></h3></td>
          <td valign="top" class="ms-formbody" width="400px">
             <asp:TextBox ID="txtEndDate" runat="server"/>
             
             <asp:DropDownList ID="ddlEndHour" runat="server">
                <asp:ListItem Text="1" Value="1"/>
                <asp:ListItem Text="2" Value="2"/>
                <asp:ListItem Text="3" Value="3"/>
                <asp:ListItem Text="4" Value="4"/>
                <asp:ListItem Text="5" Value="5"/>
                <asp:ListItem Text="6" Value="6"/>
                <asp:ListItem Text="7" Value="7"/>
                <asp:ListItem Text="8" Value="8"/>
                <asp:ListItem Text="9" Value="9"/>
                <asp:ListItem Text="10" Value="10"/>
                <asp:ListItem Text="11" Value="11"/>
                <asp:ListItem Text="12" Value="12"/>
             </asp:DropDownList>
             
             <asp:DropDownList ID="ddlEndMin" runat="server">
                <asp:ListItem Text="00" Value="0"/>
                <asp:ListItem Text="05" Value="5"/>
                <asp:ListItem Text="10" Value="10"/>
                <asp:ListItem Text="15" Value="15"/>
                <asp:ListItem Text="20" Value="20"/>
                <asp:ListItem Text="25" Value="25"/>
                <asp:ListItem Text="30" Value="30"/>
                <asp:ListItem Text="35" Value="35"/>
                <asp:ListItem Text="40" Value="40"/>
                <asp:ListItem Text="45" Value="45"/>
                <asp:ListItem Text="50" Value="50"/>
                <asp:ListItem Text="55" Value="55"/>  
             </asp:DropDownList>
             
             <asp:DropDownList ID="ddlEndAMPM" runat="server">
                <asp:ListItem Text="AM" Value="AM"/>
                <asp:ListItem Text="PM" Value="PM"/>
             </asp:DropDownList>
             <br />Optional end date and time for the lessson (dd/mm/yyyy).        
          </td>
        </tr>
        
        </table> 
        </td></tr>
        <tr><td align = right>
            <asp:Button ID="btnsave" runat="server" OnClick="saveSchedule" Text="Apply Schedule" />
            <asp:Button ID="Button2" runat="server" OnClick="clear" Text="Clear" />
            <asp:Button ID="Button1" runat="server" OnClick="cancel" Text="Cancel" />
            <br />
            <br />
            
        </td></tr>
    </table>

    <asp:Label ID="lblMessage" runat="server" ForeColor="Red"></asp:Label>
        
        
</asp:Content>

<asp:Content ID="PageTitle" contentplaceholderid="PlaceHolderPageTitle" runat="server">
	LAMS Schedule Lesson
</asp:Content>

<asp:Content ID="PageTitleInTitleArea" runat="server"
             contentplaceholderid="PlaceHolderPageTitleInTitleArea" >
    LAMS Schedule Lesson
</asp:Content>