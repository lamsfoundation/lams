<%

    #
    #  Copyright (C) 2001, 2002 MIT
    #
    #  This file is part of dotLRN.
    #
    #  dotLRN is free software; you can redistribute it and/or modify it under the
    #  terms of the GNU General Public License as published by the Free Software
    #  Foundation; either version 2 of the License, or (at your option) any later
    #  version.
    #
    #  dotLRN is distributed in the hope that it will be useful, but WITHOUT ANY
    #  WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
    #  FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
    #  details.
    #

%>

<ul>
<multiple name="forums">
  <li>
    <a href="@url@admin/forum-edit?forum_id=@forums.forum_id@">@forums.name@</a>
<if @forums.enabled_p@ ne t><b>(#forums-portlet.disabled#)</b></if>
  </li>
</multiple>
</ul>
<ul>
  <li>
    <a href="@url@admin/forum-new?name=@encoded_default_name@">#forums-portlet.New_Forum#</a>
  </li>
</ul>
