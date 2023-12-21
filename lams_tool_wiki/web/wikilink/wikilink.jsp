<!DOCTYPE html>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta content="noindex, nofollow" name="robots">
<style type="text/css">
select {
	width: 100%
}
</style>
<script type="text/javascript" src="wikilink.js"></script>
</head>
<body scroll="no" style="OVERFLOW: hidden" onload="init()">
<form name="wikiform" action="">
<table height="100%" cellSpacing="0" cellPadding="0" width="100%"
	border="0">
	<tr>
		<td>
		<table cellSpacing="3" cellPadding="3" align="center" border="0">
			<tr>
				<td colspan="2"><span id="linkAliasLabel">&nbsp;</span></td>
				<td><input id="linkAlias" type="text"></td>
			</tr>
			<tr>
				<td><span id="existingLinkMenuLabel">&nbsp;</span></td>
				<td></td>
				<td>
					<select name="existingWikiDropDownMenu" id="existingWikiDropDownMenu" class="form-select">
						<option value="noSelection">Please Select</option>
						<script type="text/javascript">
							<!--
								var i;
								for (i=0; i<wikiArray.length; ++i) 
								{	
									var wikiURL="javascript:changeWikiPage('" + wikiArray[i] + "')";
									addOption(document.getElementById('existingWikiDropDownMenu'), prettyWikiLink(wikiArray[i]), wikiURL);
								}
							//-->		
						</script>
					</select>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</form>
</body>
</html>
