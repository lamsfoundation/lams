<?xml version="1.0"?>

<queryset>
   <rdbms><type>postgresql</type><version>7.1</version></rdbms>

   <fullquery name="forums_reading_info__user_add_msg">
      <querytext>
        select forums_reading_info__user_add_msg (
            :msg_id,
            :user_id
        );
      </querytext>
   </fullquery>

</queryset>