<?xml version="1.0"?>

<queryset>
   <rdbms><type>postgresql</type><version>7.1</version></rdbms>

<fullquery name="forums_reading_info__user_add_forum">
      <querytext>

        select forums_reading_info__user_add_forum (
            :forum_id,
            :user_id
        );

      </querytext>
</fullquery>
</queryset>