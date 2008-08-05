<?xml version="1.0"?>

<queryset>
   <rdbms><type>oracle</type><version>8.1.6</version></rdbms>

<fullquery name="forums_reading_info__user_add_forum">
      <querytext>
	begin
	  forum_reading_info.user_add_forum (
            :forum_id,
	    :user_id
        );
	end;
      </querytext>
</fullquery>
</queryset>