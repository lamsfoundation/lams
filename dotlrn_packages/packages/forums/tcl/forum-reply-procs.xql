<?xml version="1.0"?>
<queryset>

    <fullquery name="forum::notification::get_url.select_object_type">
        <querytext>	
	select object_type 
	from acs_objects
	where object_id = :object_id
        </querytext>
    </fullquery>

</queryset>










