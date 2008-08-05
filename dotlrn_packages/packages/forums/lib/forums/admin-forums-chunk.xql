<?xml version="1.0"?>
<queryset>

<fullquery name="select_forums">
<querytext>
select forum_id, name, posting_policy, enabled_p
from forums_forums
where package_id= :package_id
order by enabled_p desc, name
</querytext>
</fullquery>

</queryset>
