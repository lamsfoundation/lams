ad_page_contract {

    Forums Administration

    @author Ben Adida (ben@openforce.net)
    @creation-date 2002-05-24
    @cvs-id $Id$

}
set subsite_url [subsite::get_element -element url -notrailing]

template::list::create \
    -name forums \
    -multirow forums \
    -actions [list \
                  [_ forums.Create_a_New_Forum] forum-new {} \
                  [_ forums.Parameters] [export_vars -base "$subsite_url/shared/parameters" { { return_url [ad_return_url] } { package_id {[ad_conn package_id]} } }] {} \
                  [_ acs-subsite.Permissions] [export_vars -base "permissions" { { object_id {[ad_conn package_id]} } }] {}
              ]\
    -elements {
        edit {
            label {}
            sub_class narrow
            display_template {
                <img src="/shared/images/Edit16.gif" height="16" width="16" border="0">
            }
            link_url_col edit_url
        }
        name {
            label "#forums.Forum_Name#"
            link_url_col view_url
        }
        enabled {
            label "Enabled"
            html { align center }
            display_template {
                <if @forums.enabled_p@ true>
                  <a href="@forums.disable_url@" title="Disable this forum"><img src="/resources/acs-subsite/checkboxchecked.gif" height="13" width="13" border="0" style="background-color: white;" alt="\#forums.disable\#"></a>
                </if>
                <else>
                  <a href="@forums.enable_url@" title="Enable this forum"><img src="/resources/acs-subsite/checkbox.gif" height="13" width="13" border="0" style="background-color: white;" alt="\#forums.enable\#"></a>
                </else>
            }
        }
        permissions {
            label "#acs-subsite.Permissions#"
            display_template "#acs-subsite.Permissions#"
            link_url_col permissions_url
        }
    }


# List of forums
set package_id [ad_conn package_id]
db_multirow -extend {
    view_url
    edit_url
    permissions_url
    enable_url
    disable_url
}  forums select_forums {} {
    if { [template::util::is_true $enabled_p] } {
        set view_url [export_vars -base "[ad_conn package_url]forum-view" { forum_id }]
    } else {
        set view_url {}
    }
    set edit_url [export_vars -base "forum-edit" { forum_id }]
    set permissions_url [export_vars -base permissions { { object_id $forum_id } }]
    set enable_url [export_vars -base "forum-enable" { forum_id }]
    set disable_url [export_vars -base "forum-disable" { forum_id }]
}

if {[exists_and_not_null alt_template]} {
  ad_return_template $alt_template
}
