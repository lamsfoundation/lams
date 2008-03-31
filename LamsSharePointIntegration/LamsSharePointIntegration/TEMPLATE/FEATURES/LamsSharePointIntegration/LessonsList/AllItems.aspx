<%@ Page language="C#" MasterPageFile="~masterurl/default.master"    Inherits="Microsoft.SharePoint.WebPartPages.WebPartPage,Microsoft.SharePoint,Version=12.0.0.0,Culture=neutral,PublicKeyToken=71e9bce111e9429c" %> <%@ Register Tagprefix="SharePoint" Namespace="Microsoft.SharePoint.WebControls" Assembly="Microsoft.SharePoint, Version=12.0.0.0, Culture=neutral, PublicKeyToken=71e9bce111e9429c" %> <%@ Register Tagprefix="Utilities" Namespace="Microsoft.SharePoint.Utilities" Assembly="Microsoft.SharePoint, Version=12.0.0.0, Culture=neutral, PublicKeyToken=71e9bce111e9429c" %> <%@ Import Namespace="Microsoft.SharePoint" %> <%@ Register Tagprefix="WebPartPages" Namespace="Microsoft.SharePoint.WebPartPages" Assembly="Microsoft.SharePoint, Version=12.0.0.0, Culture=neutral, PublicKeyToken=71e9bce111e9429c" %>
<asp:Content ContentPlaceHolderId="PlaceHolderPageTitle" runat="server"><SharePoint:ListProperty Property="Title" runat="server"/></asp:Content>
<asp:Content ContentPlaceHolderId="PlaceHolderPageTitleInTitleArea" runat="server">
	<SharePoint:ListProperty Property="Title" runat="server"/>
</asp:Content>
<asp:content contentplaceholderid="PlaceHolderAdditionalPageHead" runat="server">
	<SharePoint:RssLink runat="server" />
</asp:content>
<asp:Content ContentPlaceHolderId="PlaceHolderSearchArea" runat="server">
	<SharePoint:DelegateControl runat="server"
		ControlId="SmallSearchInputBox"/>
</asp:Content>
<asp:Content ContentPlaceHolderId="PlaceHolderPageImage" runat="server"><SharePoint:ViewIcon Width="145" Height="54" runat="server" /></asp:Content>
<asp:Content ContentPlaceHolderId="PlaceHolderLeftActions" runat="server">
<SharePoint:ModifySettingsLink runat="server" />
</asp:Content>
<asp:Content ContentPlaceHolderId ="PlaceHolderBodyLeftBorder" runat="server">
 <div height=100% class="ms-pagemargin"><IMG SRC="/_layouts/images/blank.gif" width=6 height=1 alt=""></div>
</asp:Content>
<asp:Content ContentPlaceHolderId="PlaceHolderMain" runat="server">
		<WebPartPages:WebPartZone runat="server" FrameType="None" ID="Main" Title="loc:Main" />
</asp:Content>
<asp:Content ContentPlaceHolderId="PlaceHolderBodyAreaClass" runat="server">
<style type="text/css">
.ms-bodyareaframe {
	padding: 0px;
}
</style>
</asp:Content>
<asp:Content ContentPlaceHolderId="PlaceHolderPageDescription" runat="server">
<SharePoint:ListProperty CssClass="ms-listdescription" Property="Description" runat="server"/>
</asp:Content>
<asp:Content ContentPlaceHolderId="PlaceHolderCalendarNavigator" runat="server">
	  <SharePoint:SPCalendarNavigator id="CalendarNavigatorId" runat="server"/>
</asp:Content>
