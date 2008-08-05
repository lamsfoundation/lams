ad_library {
    Automated tests.
    @author Gerardo Morales (gerardo.morales@gmail.net)
    @author Mounir Lallali

    @creation-date 14 June 2005
   
}

namespace eval forums::twt {

    ad_proc new { name } {
	
	set response 0
    
	# The Faq Admin page url
	set forum_admin_page [aa_get_first_url -package_key forums]       
	::twt::do_request $forum_admin_page
	
	# Seting the charter that would be used in the forum creation form
	set charter "[ad_generate_random_string] [ad_generate_random_string]"    

	# Follows the link of New Forum
	tclwebtest::link follow "New Forum" 
	tclwebtest::form find ~n "forum"
	tclwebtest::field find ~n "name"
	tclwebtest::field fill "$name"
	tclwebtest::field find ~n "charter" 
	tclwebtest::field fill "$charter" 
	tclwebtest::form submit
	aa_log "Forum $name just to be created"       

	set response_url [tclwebtest::response url]
	aa_log  $forum_admin_page
       
	if {[string first $forum_admin_page $response_url]!=-1} { 
	    
	    if { [catch {tclwebtest::link find $name} testerror1] ||[catch {tclwebtest::assert text $charter} testerror2 ]  } {
		aa_error "The forum $name was not created. The forum name or the charter was not founded in the admin page of forums"
	    } else { 
		aa_log "The forum $name was succesfully created"
		set response 1
	    }

	} else {
            aa_error "forums::twt::new failed, bad response url : $response_url"
        }
	
	return $response     	
    }						      

    ad_proc edit { name } {

	set response 0
    
	# Call to the faq admin page
	set forum_admin_page [aa_get_first_url -package_key forums]       
	::twt::do_request $forum_admin_page 

	# Follows the link of administration and then admin the forum
	tclwebtest::link follow $name
	tclwebtest::link follow "Administer"
	aa_log "The data of the $name forum will be changed"

	set charter "[ad_generate_random_string] [ad_generate_random_string 8]"

	# Enter the new data and submit
	tclwebtest::form find ~n "forum"
	tclwebtest::field find ~n "name"
	tclwebtest::field fill "Edited $name"
	tclwebtest::field find ~n "charter"
	tclwebtest::field fill "Edited $charter"
	tclwebtest::form submit
	aa_log "Form Submited"

	# Testing the edition
        set response_url [tclwebtest::response url]
        
	if {[string first "forum-view" $response_url]!=-1 && [string first $forum_admin_page $response_url]!=-1 } {
	    
	    ::twt::do_request $forum_admin_page
	    
	    if { [catch {tclwebtest::link find "Edited $name"} testerror1] ||[catch {tclwebtest::assert text "$charter"} testerror2 ]  } { 
		aa_error "The forum $name was not Edited. The forum name or the charter was not founded in the admin page of forums" 
	    } else {  
		aa_log "The forum $name was succesfully edited, new name Edited $name"
		set response 1 
	    } 
	
	} else {
	    aa_error "forums::twt::edit failed, bad response url : $response_url"
        }

	return $response
    }

    ad_proc new_post {name subject} { 

	set response 0 
	# Seting the Subject and Body of the new post 
	set msgb "[ad_generate_random_string] [ad_generate_random_string 20]"  
 
	# Call to the faq admin page 
	set forum_admin_page [aa_get_first_url -package_key forums]        
	::twt::do_request $forum_admin_page 

	# Follows the link of administration and then the Post a new message 
	tclwebtest::link follow $name 
	tclwebtest::link follow "Post a New Message" 

	# Enter the data and submit 
	tclwebtest::form find ~n "message" 
	tclwebtest::field find ~n "subject" 
	tclwebtest::field fill "$subject" 
	tclwebtest::field find ~n "message_body" 
	tclwebtest::field fill "$msgb" 
	tclwebtest::form submit 
	aa_log "New message form submited"

	set response_url [tclwebtest::response url]
	
	if {[string first "message-view" $response_url]!=-1 && [string first $forum_admin_page $response_url]!=-1 } {

	    tclwebtest::link follow "$name"

	    # Testing the message
	    if {[catch {tclwebtest::link follow "$subject"}]} {
		aa_error "The messaje was not posted"
	    }

	    if { [catch {tclwebtest::assert text "$msgb"} testerror2 ]  } {  
		aa_error "The body of the message was not correctly posted"  
	    } else {   
		aa_log "The message was succesfully posted"
		set response 1
	    }        
	
	} else {
            aa_error "forums::twt::new_post failed, bad response url : $response_url"
        }
  
	return $response
    }

    ad_proc edit_post {name subject} {  
  
	set response 0
    
	# Seting the new body of the message
	set msgb2 "[ad_generate_random_string] [ad_generate_random_string 20]"  
 
	# Call to the faq admin page  
	set forum_admin_page [aa_get_first_url -package_key forums]        
	::twt::do_request $forum_admin_page 

      
	# Follows the link of administration and then Edit the posted message  
	tclwebtest::link follow $name  
	tclwebtest::link follow $subject  
	tclwebtest::link follow Edit

	# Fill and submit the form for editing
	tclwebtest::form find ~n "message"  
	tclwebtest::field find ~n "subject"  
	tclwebtest::field fill "Edited $subject"  
	tclwebtest::field find ~n "message_body"  
	tclwebtest::field fill "$msgb2"  
	tclwebtest::form submit  
	aa_log "Edit message form submited" 

        set response_url [tclwebtest::response url]

	if {[string first "message-view" $response_url]!=-1 && [string first $forum_admin_page $response_url]!=-1 } {

	    # Testing if the new text is in the message
	    if {[catch {tclwebtest::link follow "Edited $subject"}]} {
		aa_error "The messaje was not edited"
	    }

	    if { [catch {tclwebtest::assert text "$msgb2"} testerror2 ]  } {  
		aa_error "The body of the message was not correctly edited"  
	    } else {   
		aa_log "The message $subject of the forum $name was succesfully edited"  
		set response 1
	    }  
	
	} else {
            aa_error "forums::twt::edit_post failed, bad response url : $response_url"
	}
      
	return $response 
    }

    ad_proc delete_post {name subject} {   
   
	set response 0 
	# Call to the faq admin page   
	set forum_admin_page [aa_get_first_url -package_key forums]        
	::twt::do_request $forum_admin_page 
       
	# Follows the link of administration and then Edit the posted message   
	tclwebtest::link follow $name   
	tclwebtest::link follow $subject   
	tclwebtest::link follow Delete
	tclwebtest::link follow Yes

	set response_url [tclwebtest::response url]

	# Testing if the the message is not in the forum
	if {[string first "forum-view" $response_url]!=-1 && [string first $forum_admin_page $response_url]!=-1 } {
	    
	    if {[catch {tclwebtest::link follow "Edited $subject"}]} { 
		aa_log "The message $subject was succesfully deleted in the forum $name" 
		set  response 1
	    } else {
		aa_error "The message $subject of the forum $name was not deleted"
	    }

	} else {
           aa_error "forums::twt::delete_post failed, bad response url : $response_url"
	}
	
	return $response
    } 

    ad_proc reply_msg {name subject} {   
   
	set response 0
	
	# Seting the new body of the message 
	set msgb_reply "[ad_generate_random_string] [ad_generate_random_string 20]"   
  
	# Call to the faq admin page   
	set forum_admin_page [aa_get_first_url -package_key forums]        
	::twt::do_request $forum_admin_page 

       
	# Follows the link of administration and then Edit the posted message   
	tclwebtest::link follow $name   
	tclwebtest::link follow $subject   
	tclwebtest::link follow "Post a Reply" 
 
	# Fill and submit the form for editing 
	tclwebtest::form find ~n "message"   
	tclwebtest::field find ~n "message_body"   
	tclwebtest::field fill "$msgb_reply"   
	tclwebtest::form submit   
	aa_log "Reply message submitted"  

	set response_url [tclwebtest::response url]

	# Testing if the new text is in the message 
	if {[string first "message-view" $response_url]!=-1 && [string first $forum_admin_page $response_url]!=-1 } {
	
	    if {[catch {tclwebtest::link find "Re: $subject"}]} { 
		aa_error "The reply message was not posted" 
	    } 
 
	    if { [catch {tclwebtest::assert text "$msgb_reply"} testerror2 ]  } {   
		aa_error "The body of the replyed message was not correctly posted"   
	    } else {    
		aa_log "The reply message to $subject of the forum $name was succesfully posted"   
		set response 1
	    }

	} else {
            aa_error "forums::twt::reply_msg failed, bad response url : $response_url"
        }
   
	return $response
    }

}