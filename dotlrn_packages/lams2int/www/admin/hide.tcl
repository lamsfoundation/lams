# packages/lams2int/www/admin/hide.tcl

ad_page_contract {
    
        Hide or displays a sequence
	changes the display info for a sequence and returns back to the admin page
	
	@author Ernie Ghiglione (ErnieG@melcoe.mq.edu.au)
	@creation-date 2007-04-17
	@cvs-id $Id$
} {
    lesson_id:integer
    hide
} -properties {
} -validate {
} -errors {
}

# checks whether the user has permissions on the lesson. 

permission::require_permission -object_id $lesson_id -privilege admin

if { [string equal $hide "f"] } {

    db_dml update_lesson_display {
        update lams_lessons  set hide = 't' where lesson_id = :lesson_id
    }
    
} else {

    db_dml update_seq_display {
        update lams_lessons  set hide = 'f' where lesson_id = :lesson_id
    }


}
ad_returnredirect -message "Display changed successfully!" [export_vars  -base index lesson_id]
