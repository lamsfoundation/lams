ad_library {

    Reusable forms in the forums package

    @author lee@thaum.net
    @creation-date 2003-11-12
}

namespace eval forums {}
namespace eval forums::form {}

ad_proc -public forums::form::message {
    {-optional:boolean}
    {-prefix {}}
    form_name
} {
    adds form elements to form_name which represent the data held in a forum
    message
} {

    ##############################
    # Form definition
    #

    if { $optional_p } {
        set optional_switch "-optional"
    } else {
        set optional_switch ""
    }

    template::element create $form_name ${prefix}subject \
        -label [_ forums.Subject] \
        -datatype text \
        -widget text \
        -html {size 60} \
        $optional_switch

    template::element create $form_name ${prefix}message_body \
        -label [_ forums.Body] \
        -datatype richtext \
        -widget richtext \
        -html {rows 20 cols 60 style {width:100%}} \
        $optional_switch

}

ad_proc -public forums::form::post_message {
    {-optional:boolean}
    {-show_anonymous_p 1}
    {-show_attachments_p 1}
    {-prefix {}}
    form_name
} {
    adds form elements to a form for the default post message form
} {
    template::element create $form_name ${prefix}forum_id \
        -label [_ forums.forum_ID] \
        -datatype integer \
        -widget hidden

    template::element create $form_name ${prefix}parent_id \
        -label [_ forums.parent_ID] \
        -datatype integer \
        -widget hidden \
        -optional

    template::element create $form_name ${prefix}subscribe_p \
        -label [_ forums.Subscribe] \
        -datatype text \
        -widget hidden \
        -optional

    set options [list [list [_ forums.post_anonymously] 1 ] ]

    template::element create $form_name ${prefix}anonymous_p \
        -label [_ forums.Anonymous] \
        -datatype integer \
        -widget [ad_decode $show_anonymous_p 0 "hidden" "checkbox"] \
        -options $options \
        -optional

    set options [list [list [_ forums.No] 0] [list [_ forums.Yes] 1]]

    template::element create $form_name ${prefix}attach_p \
            -label [_ forums.Attach] \
            -datatype text \
            -widget [ad_decode $show_attachments_p 0 "hidden" "radio"] \
            -options $options

    if {$optional_p} {
        template::element set_properties $form_name ${prefix}forum_id -optional
        template::element set_properties $form_name ${prefix}attach_p -optional
    }
}

ad_proc -public forums::form::forward_message {
    {-prefix {}}
    form_name
} {
    adds form elements to form_name to allow the user to enter the details
    of a message they want to forward by email
} {
  template::element create $form_name ${prefix}to_email \
    -label [_ forums.Email] \
    -datatype email \
    -widget text \
    -html {size 60}

  template::element create $form_name ${prefix}subject \
    -label [_ forums.Subject] \
    -datatype text \
    -widget text \
    -html {size 80}

  template::element create $form_name ${prefix}pre_body \
    -label [_ forums.Your_Note] \
    -datatype text \
    -widget textarea \
    -html {cols 80 rows 10}
}

ad_proc -public forums::form::search {
    {-prefix {}}
    form_name
} {
    Constructs the elements of a  form for searching for a term 
    optionally in a particular forum
} {
  template::element create $form_name ${prefix}search_text \
    -label [_ forums.Search_1] \
    -datatype text \
    -widget text

  template::element create $form_name ${prefix}forum_id \
    -label [_ forums.ForumID] \
    -datatype text \
    -widget hidden \
    -optional
}

ad_proc -public forums::form::forum {
    {-prefix {}}
    form_name
} {
    Constructs the elements of a form for creating/editing a forum
} {
    template::element create $form_name ${prefix}name \
      -label [_ forums.Name] \
      -datatype text \
      -widget text \
      -html {size 60} \
      -validate { {expr ![empty_string_p [string trim $value]]} {Forum Name can not be blank} }

    template::element create $form_name ${prefix}charter \
      -label [_ forums.Charter] \
      -datatype richtext \
      -widget richtext \
      -html {cols 60 rows 10} \
      -validate { {expr [string length $value] <= 4000 } {#forums.charter_max_chars#} } \
      -optional

    template::element create $form_name ${prefix}presentation_type \
      -label [_ forums.Presentation] \
      -datatype text \
      -widget select \
      -help_text [_ forums.help_presentation] \
      -options [list [list [_ forums.Flat] flat] [list [_ forums.Threaded] threaded]]

    template::element create $form_name ${prefix}posting_policy \
      -label [_ forums.Posting_Policy] \
      -datatype text \
      -widget select \
      -help_text [_ forums.help_posting_policy] \
      -options [list [list [_ forums.open] open] [list [_ forums.moderated] moderated] [list [_ forums.closed] closed] ]

    template::element create $form_name ${prefix}new_threads_p \
      -label [_ forums.lt_Users_Can_Create_New_] \
      -datatype integer \
      -widget radio \
      -help_text [_ forums.help_new_threads] \
      -options [list [list [_ forums.Yes] 1] [list [_ forums.No] 0] ] 
}
