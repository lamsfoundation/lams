<?xml version="1.0"?>
<queryset>
    <rdbms><type>oracle</type><version>8.1.6</version></rdbms>

    <fullquery name="select_date">
        <querytext>
          select to_char(sysdate, 'YYYY-MM-DD HH24:MI:SS') 
          from dual
        </querytext>
    </fullquery>

    <fullquery name="forums_reading_info__remove_msg">
        <querytext>
	begin
	  forum_reading_info.remove_msg(:parent_id);
	end;
        </querytext>
    </fullquery>


</queryset>
