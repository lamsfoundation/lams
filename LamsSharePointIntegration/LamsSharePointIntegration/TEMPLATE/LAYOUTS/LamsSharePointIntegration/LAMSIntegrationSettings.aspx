<%@ Assembly Name="Microsoft.SharePoint, Version=12.0.0.0, Culture=neutral, PublicKeyToken=71e9bce111e9429c"%> 
<%@ Page Language="C#" MasterPageFile="~/_layouts/application.master" Inherits="Microsoft.SharePoint.WebControls.LayoutsPageBase"  %>
<%@ Register TagPrefix="wssuc" TagName="InputFormSection" Src="~/_controltemplates/InputFormSection.ascx" %>
<%@ Register TagPrefix="wssuc" TagName="InputFormControl" Src="~/_controltemplates/InputFormControl.ascx" %>
<%@ Register TagPrefix="wssuc" TagName="ButtonSection" Src="~/_controltemplates/ButtonSection.ascx" %>
<%@ Register TagPrefix="wssawc" Namespace="Microsoft.SharePoint.WebControls" Assembly="Microsoft.SharePoint, Version=12.0.0.0, Culture=neutral, PublicKeyToken=71e9bce111e9429c" %>
<%--<%@ Register TagPrefix="SPSWC" Namespace="Microsoft.SharePoint.Portal.WebControls" Assembly="Microsoft.SharePoint.Portal, Version=12.0.0.0, Culture=neutral, PublicKeyToken=71e9bce111e9429c" %>--%>
<%@ Register TagPrefix="SharePoint" Namespace="Microsoft.SharePoint.WebControls" Assembly="Microsoft.SharePoint, Version=12.0.0.0, Culture=neutral, PublicKeyToken=71e9bce111e9429c" %>
<%@ Register TagPrefix="Utilities" Namespace="Microsoft.SharePoint.Utilities" Assembly="Microsoft.SharePoint, Version=12.0.0.0, Culture=neutral, PublicKeyToken=71e9bce111e9429c" %>
  

<%@ Import Namespace="Microsoft.SharePoint" %>
<%@ Import Namespace="Microsoft.SharePoint.Administration" %>

 <script runat="server">
    SPWeb Web;

     protected override void OnLoad(EventArgs e)
     {
         base.OnLoad(e);
         Web = SPControl.GetContextWeb(Context);

         if (!Page.IsPostBack)
         {
             SPFarm myFarm = SPFarm.Local;
             txtServerID.Text = (string)myFarm.Properties["LAMSServerID"];
             txtServerKey.Text = (string)myFarm.Properties["LAMSServerKey"];
             txtLAMSUrl.Text = (string)myFarm.Properties["LAMSUrl"];
             txtRequestSource.Text = (string)myFarm.Properties["LAMSRequestSource"];
             //cbxUseProfiles.Checked = ((string)myFarm.Properties["LAMSUseProfiles"]).ToLower() == "true";
         }
     }

     protected void OnClickOK(Object Sender, EventArgs e)
     {
         SPFarm myFarm = SPFarm.Local;
         myFarm.Properties["LAMSServerID"] = txtServerID.Text.ToLower();
         myFarm.Properties["LAMSServerKey"] = txtServerKey.Text.ToLower();
         myFarm.Properties["LAMSUrl"] = txtLAMSUrl.Text;
         myFarm.Properties["LAMSRequestSource"] = txtRequestSource.Text;
         //myFarm.Properties["LAMSUseProfiles"] = cbxUseProfiles.Checked.ToString();
         myFarm.Update();

         Response.Redirect("../Settings.aspx");
     }

     protected void OnClickCancel(Object Sender, EventArgs e)
     {
         Response.Redirect("../Settings.aspx");
     } 
     
     
   
</script>

<asp:Content ID="Main" contentplaceholderid="PlaceHolderMain" runat="server">
  <style type="text/css">
    table.ms-propertysheet {
      height: 100%;
    }
  </style>
  
  <table cellspacing="0" cellpadding="0" border="0" class="ms-settingsframe">
    <tr>
      <td width="100%" colspan="4" style="padding-top: 0px;">
        <table class="ms-pageinformation" width="100%" cellpadding="0" cellspacing="0">
          <tr>
            <td valign="top" style="padding: 10px;" width="100%" height="100px">
              <table height="100%" width="100%" id="idItemHoverTable">
                <tr>
                  <th scope="col" colspan="2" style="padding-bottom: 8px;">
                    <span class="ms-linksectionheader">
                      <h3 class="ms-standardheader">
                        <SharePoint:EncodedLiteral
                          ID="EncodedLiteral1"
                          runat="server"
                          Text="Admin page for LAMS Integration Settings"
                          EncodeMethod='HtmlEncode' />
                      </h3>
                    </span>
                  </th>
                </tr>
                <tr>
                  <th scope="row" nowrap="nowrap">
                    <SharePoint:EncodedLiteral
                      ID="EncodedLiteral2"
                      runat="server" Text="<% $Resources:wss,settings_siteurl %>"
                      EncodeMethod='HtmlEncode' />:
                  </th>
                  <td dir="ltr">
                    <% SPHttpUtility.HtmlEncode(Web.Url + "/", Response.Output); %>
                  </td>
                </tr>
              </table>
            </td>
          </tr>
        </table>
      </td>
    </tr>
    <tr>
      <td valign="top" style="padding: 4px 0px 4px 0px;" height="100%">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" class="ms-propertysheet">
          <wssuc:InputFormSection Title="Global Settings" id="ifmSQL"
            Description="LAMS Global Settings (server-wide settings)." runat="server">
            <template_inputformcontrols>            
             <wssuc:InputFormControl LabelText="LAMS Server ID" runat="server">
              <Template_Control>
                <wssawc:InputFormTextBox CssClass="ms-input"
                  ID="txtServerID" Runat="server" Columns="60" />
                <wssawc:InputFormRequiredFieldValidator ID="reqDomain"
                  ControlToValidate="txtServerID" Text="Error" runat="server"
                  ErrorMessage ="Required" EnableClientScript="true" Display="Dynamic">
                </wssawc:InputFormRequiredFieldValidator>
              </Template_Control>
            </wssuc:InputFormControl>
            <wssuc:InputFormControl LabelText="LAMS Server Key" runat="server">
              <Template_Control>
                <wssawc:InputFormTextBox CssClass="ms-input"
                  ID="txtServerKey" Runat="server" Columns="60" />
                <wssawc:InputFormRequiredFieldValidator ID="InputFormRequiredFieldValidator1"
                  ControlToValidate="txtServerKey" Text="Error" runat="server"
                  ErrorMessage ="Required" EnableClientScript="true" Display="Dynamic">
                </wssawc:InputFormRequiredFieldValidator>
              </Template_Control>
            </wssuc:InputFormControl>
            <wssuc:InputFormControl LabelText="LAMS Server Url" runat="server">
              <Template_Control>
                <wssawc:InputFormTextBox CssClass="ms-input"
                  ID="txtLAMSUrl" Runat="server" Columns="60" />
                <wssawc:InputFormRequiredFieldValidator ID="InputFormRequiredFieldValidator2"
                  ControlToValidate="txtLAMSUrl" Text="Error" runat="server"
                  ErrorMessage ="Required" EnableClientScript="true" Display="Dynamic">
                </wssawc:InputFormRequiredFieldValidator>
              </Template_Control>
            </wssuc:InputFormControl>
            <wssuc:InputFormControl LabelText="LAMS Request Source" runat="server">
              <Template_Control>
                <wssawc:InputFormTextBox CssClass="ms-input"
                  ID="txtRequestSource" Runat="server" Columns="60" />
                <wssawc:InputFormRequiredFieldValidator ID="InputFormRequiredFieldValidator3"
                  ControlToValidate="txtRequestSource" Text="Error" runat="server"
                  ErrorMessage ="Required" EnableClientScript="true" Display="Dynamic">
                </wssawc:InputFormRequiredFieldValidator>
              </Template_Control>
            </wssuc:InputFormControl>
            <%--<wssuc:InputFormControl LabelText="Use user profiles? (Requires MOSS 2007)" runat="server"
              Description="Use user profiles (Requires MOSS 2007)?">
              <Template_Control>
                 
                 <wssawc:InputFormCheckBox CssClass="ms-input" ID="cbxUseProfiles" runat="server"  />
               
              </Template_Control>
            </wssuc:InputFormControl> --%>
            </template_inputformcontrols>
          </wssuc:InputFormSection>

        </table>
      </td>
    </tr>
    <tr>
        <td width="100%" colspan="4" style="padding-top: 0px;" align="right">
            <asp:Button runat="server" class="ms-ButtonHeightWidth" ID="cmdOK" OnClick="OnClickOK" Text="Ok" /> 
            <asp:Button runat="server" class="ms-ButtonHeightWidth" ID="cmdCancel" OnClick="OnClickCancel" Text="Cancel" />
        </td>
    </tr>
  </table>
   
   
 
</asp:Content>

<asp:Content ID="PageTitle" contentplaceholderid="PlaceHolderPageTitle" runat="server">
	LAMS Integration Settings
</asp:Content>

<asp:Content ID="PageTitleInTitleArea" runat="server"
             contentplaceholderid="PlaceHolderPageTitleInTitleArea" >
    LAMS Integration Settings
</asp:Content>