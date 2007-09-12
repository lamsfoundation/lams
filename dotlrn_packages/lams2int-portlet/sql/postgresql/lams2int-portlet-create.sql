-- 
-- packages/lams2int-portlet/sql/postgresql/lams2int-portlet-create.sql
-- 
-- @author Ernie Ghiglione (ErnieG@melcoe.mq.edu.au)
-- @creation-date 2007-04-17
-- @cvs-id $Id$
--

create function inline_0 ()
returns integer as '
declare
  ds_id         portal_datasources.datasource_id%TYPE;
begin
        ds_id = portal_datasource__new(
                        ''lams2int_portlet'',
                        ''Displays a Folder_id''
        );


perform  portal_datasource__set_def_param(
                ds_id,
                ''t'',
                ''t'',
                ''shadeable_p'',
                ''t''
);

perform portal_datasource__set_def_param (
                ds_id,
                ''t'',
                ''t'',
                ''hideable_p'',
                ''t''
);

perform portal_datasource__set_def_param (
                ds_id,
                ''t'',
                ''t'',
                ''user_editable_p'',
                ''f''
);


perform portal_datasource__set_def_param (
                ds_id,
                ''t'',
                ''t'',
                ''shaded_p'',
                ''f''
);

perform portal_datasource__set_def_param (
                ds_id,
                ''t'',
                ''t'',
                ''link_hideable_p'',
                ''t''
);

perform portal_datasource__set_def_param (
                ds_id,
                ''t'',
                ''t'',
                ''style'',
                ''list''
);

perform portal_datasource__set_def_param (
                ds_id,
                ''t'',
                ''f'',
                ''package_id'',
                '' ''
);

return 0;

end; ' language 'plpgsql';


select inline_0 ();

drop function inline_0 ();

-- create the implementation
select acs_sc_impl__new(
        'portal_datasource',
        'lams2int_portlet',
        'lams2int_portlet'
);


-- add all the hooks
select acs_sc_impl_alias__new(
        'portal_datasource',
        'lams2int_portlet',
        'GetMyName',
        'lams2int_portlet::get_my_name',
        'TCL'
);

select acs_sc_impl_alias__new(
        'portal_datasource',
        'lams2int_portlet',
        'GetPrettyName',
        'lams2int_portlet::get_pretty_name',
        'TCL'
);

select acs_sc_impl_alias__new(
        'portal_datasource',
        'lams2int_portlet',
        'Link',
        'lams2int_portlet::link',
        'TCL'
);

select acs_sc_impl_alias__new(
        'portal_datasource',
        'lams2int_portlet',
        'AddSelfToPage',
        'lams2int_portlet::add_self_to_page',
        'TCL'
    );

select acs_sc_impl_alias__new(
        'portal_datasource',
        'lams2int_portlet',
        'Show',
        'lams2int_portlet::show',
        'TCL'
    );

select acs_sc_impl_alias__new(
        'portal_datasource',
        'lams2int_portlet',
        'Edit',
        'lams2int_portlet::edit',
        'TCL'
    );

select acs_sc_impl_alias__new(
        'portal_datasource',
        'lams2int_portlet',
        'RemoveSelfFromPage',
        'lams2int_portlet::remove_self_from_page',
        'TCL'
    );

    -- Add the binding
select acs_sc_binding__new (
        'portal_datasource',
        'lams2int_portlet'
);

\i lams2int-admin-portlet-create.sql
