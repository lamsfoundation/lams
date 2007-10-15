-- Script to be run for LAMS 2.1 release, on LAMS 2.0.3/2.0.4 tables.
-- Adds all the data needed for branching, plus a small change to the tool table needed for tool admin screen.

ALTER TABLE lams_group
ADD COLUMN group_ui_id INT(11);

CREATE TABLE lams_input_activity (
       activity_id BIGINT(20) NOT NULL
     , input_activity_id BIGINT(20) NOT NULL
     , UNIQUE UQ_lams_input_activity_1 (activity_id, input_activity_id)
     , INDEX (activity_id)
     , CONSTRAINT FK_lams_input_activity_1 FOREIGN KEY (activity_id)
                  REFERENCES lams_learning_activity (activity_id) ON DELETE NO ACTION ON UPDATE NO ACTION
     , INDEX (activity_id)
     , CONSTRAINT FK_lams_input_activity_2 FOREIGN KEY (activity_id)
                  REFERENCES lams_learning_activity (activity_id)
)TYPE=InnoDB;


CREATE TABLE lams_branch_condition (
       condition_id BIGINT(20) NOT NULL   AUTO_INCREMENT
     , condition_ui_id INT(11)
     , order_id INT(11)
     , name VARCHAR(255) NOT NULL
     , display_name VARCHAR(255)
     , type VARCHAR(255) NOT NULL
     , start_value VARCHAR(255)
     , end_value VARCHAR(255)
     , exact_match_value VARCHAR(255)
     , PRIMARY KEY (condition_id)
)TYPE=InnoDB;

CREATE TABLE lams_branch_activity_entry (
       entry_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , entry_ui_id INT(11)
     , group_id BIGINT(20)
     , sequence_activity_id BIGINT(20) NOT NULL
     , branch_activity_id BIGINT(20) NOT NULL
     , condition_id BIGINT(20)
     , PRIMARY KEY (entry_id)
     , INDEX (group_id)
     , CONSTRAINT FK_lams_group_activity_1 FOREIGN KEY (group_id)
                  REFERENCES lams_group (group_id)
     , INDEX (sequence_activity_id)
     , CONSTRAINT FK_lams_branch_map_sequence FOREIGN KEY (sequence_activity_id)
                  REFERENCES lams_learning_activity (activity_id)
     , INDEX (branch_activity_id)
     , CONSTRAINT FK_lams_branch_map_branch FOREIGN KEY (branch_activity_id)
                  REFERENCES lams_learning_activity (activity_id)
     , INDEX (condition_id)
     , CONSTRAINT FK_lams_branch_activity_entry_4 FOREIGN KEY (condition_id)
                  REFERENCES lams_branch_condition (condition_id)
)TYPE=InnoDB;

ALTER TABLE lams_learning_activity 
ADD COLUMN default_activity_id BIGINT(20)
,ADD COLUMN start_xcoord INT(11)
,ADD COLUMN start_ycoord INT(11)
,ADD COLUMN end_xcoord INT(11)
,ADD COLUMN end_ycoord INT(11)
,ADD COLUMN stop_after_activity TINYINT NOT NULL DEFAULT 0
;
 

INSERT INTO lams_learning_activity_type VALUES (10, 'BRANCHING_CHOSEN');
INSERT INTO lams_learning_activity_type VALUES (11, 'BRANCHING_GROUP');
INSERT INTO lams_learning_activity_type VALUES (12, 'BRANCHING_TOOL');

INSERT INTO lams_system_tool (system_tool_id, learning_activity_type_id, tool_display_name, description, 
	learner_url, learner_preview_url, learner_progress_url, export_pfolio_learner_url, 
	export_pfolio_class_url, monitor_url, contribute_url, create_date_time)
VALUES (6, 10, 'Monitor Chosen Branching', 'Select between multiple sequence activities, with the branch chosen in monitoring.', 
	'learning/branching.do?method=performBranching', 'learning/branching.do?method=performBranching', 
	'learning/branching.do?method=viewBranching&mode=teacher', NULL,
	'monitoring/branchingExportPortfolio?mode=teacher', 'monitoring/branching.do?method=assignBranch', 
	'monitoring/branching.do?method=assignBranch', now());

INSERT INTO lams_system_tool (system_tool_id, learning_activity_type_id, tool_display_name, description,
        learner_url, learner_preview_url, learner_progress_url, export_pfolio_learner_url,
        export_pfolio_class_url, monitor_url, contribute_url, create_date_time)
VALUES (7, 11, 'Group Based Branching', 'Select between multiple sequence activities, with the branch chosen by an existing group.',
        'learning/branching.do?method=performBranching', 'learning/branching.do?method=performBranching',
        'learning/branching.do?method=viewBranching&mode=teacher', NULL,
        'monitoring/branchingExportPortfolio?mode=teacher', 'monitoring/branching.do?method=assignBranch',
        'monitoring/branching.do?method=assignBranch', now());

INSERT INTO lams_system_tool (system_tool_id, learning_activity_type_id, tool_display_name, description,
        learner_url, learner_preview_url, learner_progress_url, export_pfolio_learner_url,
        export_pfolio_class_url, monitor_url, contribute_url, create_date_time)
VALUES (8, 12, 'Tool Output Based Branching', 'Select between multiple sequence activities, with the branch chosen on results of another activity.',
        'learning/branching.do?method=performBranching', 'learning/branching.do?method=performBranching',
        'learning/branching.do?method=viewBranching&mode=teacher', NULL, 
        'monitoring/branchingExportPortfolio?mode=teacher', 'monitoring/branching.do?method=assignBranch',
        'monitoring/branching.do?method=assignBranch', now());

-- support tools having an admin screen

ALTER TABLE lams_tool ADD COLUMN admin_url TEXT;
ALTER TABLE lams_system_tool ADD COLUMN admin_url TEXT;


ALTER TABLE lams_configuration 
ADD COLUMN description_key VARCHAR(255)
, ADD COLUMN header_name VARCHAR(50)
, ADD COLUMN format VARCHAR(30)
, ADD COLUMN required TINYINT NOT NULL DEFAULT 0;

update lams_configuration st description_key='config.server.url', header_name='config.header.system', format='STRING', required=1 where config_key='ServerURL';
update lams_configuration set description_key='config.server.url.context.path', header_name='config.header.system', format='STRING', required=1 where config_key='ServerURLContextPath';
update lams_configuration set description_key='config.version', header_name='config.header.system', format='STRING', required=1 where config_key='Version';
update lams_configuration set description_key='config.temp.dir', header_name='config.header.system', format='STRING', required=1 where config_key='TempDir';
update lams_configuration set description_key='config.dump.dir', header_name='config.header.system', format='STRING', required=1 where config_key='DumpDir';
update lams_configuration set description_key='config.ear.dir', header_name='config.header.system', format='STRING', required=1 where config_key='EARDir';
update lams_configuration set description_key='config.smtp.server', header_name='config.header.email', format='STRING', required=1 where config_key='SMTPServer';
update lams_configuration set description_key='config.lams.support.email', header_name='config.header.email', format='STRING', required=1 where config_key='LamsSupportEmail';
update lams_configuration set description_key='config.content.repository.path', header_name='config.header.uploads', format='STRING', required=1 where config_key='ContentRepositoryPath';
update lams_configuration set description_key='config.upload.file.max.size', header_name='config.header.uploads', format='LONG', required=1 where config_key='UploadFileMaxSize';
update lams_configuration set description_key='config.upload.large.file.max.size', header_name='config.header.uploads', format='LONG', required=1 where config_key='UploadLargeFileMaxSize';
update lams_configuration set description_key='config.upload.file.max.memory.size', header_name='config.header.uploads', format='LONG', required=1 where config_key='UploadFileMaxMemorySize';
update lams_configuration set description_key='config.executable.extensions', header_name='config.header.uploads', format='STRING', required=1 where config_key='ExecutableExtensions';
update lams_configuration set description_key='config.user.inactive.timeout', header_name='config.header.system', format='LONG', required=1 where config_key='UserInactiveTimeout';
update lams_configuration set description_key='config.use.cache.debug.listener', header_name='config.header.system', format='BOOLEAN', required=1 where config_key='UseCacheDebugListener';
update lams_configuration set description_key='config.cleanup.preview.older.than.days', header_name='config.header.system', format='LONG', required=1 where config_key='CleanupPreviewOlderThanDays';
update lams_configuration set description_key='config.authoring.activities.colour', header_name='config.header.look.feel', format='BOOLEAN', required=1 where config_key='AuthoringActivitiesColour';
update lams_configuration set description_key='config.authoring.client.version', header_name='config.header.versions', format='STRING', required=1 where config_key='AuthoringClientVersion';
update lams_configuration set description_key='config.monitor.client.version', header_name='config.header.versions', format='STRING', required=1 where config_key='MonitorClientVersion';
update lams_configuration set description_key='config.learner.client.version', header_name='config.header.versions', format='STRING', required=1 where config_key='LearnerClientVersion';
update lams_configuration set description_key='config.server.version.number', header_name='config.header.versions', format='STRING', required=1 where config_key='ServerVersionNumber';
update lams_configuration set description_key='config.server.language', header_name='config.header.look.feel', format='STRING', required=1 where config_key='ServerLanguage';
update lams_configuration set description_key='config.server.page.direction', header_name='config.header.look.feel', format='STRING', required=1 where config_key='ServerPageDirection';
update lams_configuration set description_key='config.dictionary.date.created', header_name='config.header.versions', format='STRING', required=1 where config_key='DictionaryDateCreated';
update lams_configuration set description_key='config.help.url', header_name='config.header.system', format='STRING', required=1 where config_key='HelpURL';
update lams_configuration set description_key='config.xmpp.domain', header_name='config.header.chat', format='STRING', required=1 where config_key='XmppDomain';
update lams_configuration set description_key='config.xmpp.conference', header_name='config.header.chat', format='STRING', required=1 where config_key='XmppConference';
update lams_configuration set description_key='config.xmpp.admin', header_name='config.header.chat', format='STRING', required=1 where config_key='XmppAdmin';
update lams_configuration set description_key='config.xmpp.password', header_name='config.header.chat', format='STRING', required=1 where config_key='XmppPassword';
update lams_configuration set description_key='config.default.flash.theme', header_name='config.header.look.feel', format='STRING', required=1 where config_key='DefaultFlashTheme';
update lams_configuration set description_key='config.default.html.theme', header_name='config.header.look.feel', format='STRING', required=1 where config_key='DefaultHTMLTheme';
update lams_configuration set description_key='config.allow.direct.lesson.launch', header_name='config.header.features', format='BOOLEAN', required=1 where config_key='AllowDirectLessonLaunch';
update lams_configuration set description_key='config.community.enable', header_name='config.header.features', format='BOOLEAN', required=1 where config_key='LAMS_Community_enable';
update lams_configuration set description_key='config.allow.live.edit', header_name='config.header.features', format='BOOLEAN', required=1 where config_key='AllowLiveEdit';
update lams_configuration set description_key='config.ldap.provisioning.enabled', header_name='config.header.ldap', format='BOOLEAN', required=1 where config_key='LDAPProvisioningEnabled';
update lams_configuration set description_key='config.ldap.provider.url', header_name='config.header.ldap', format='STRING', required=0 where config_key='LDAPProviderURL';
update lams_configuration set description_key='config.ldap.security.authentication', header_name='config.header.ldap', format='STRING', required=0 where config_key='LDAPSecurityAuthentication';
update lams_configuration set description_key='config.ldap.principal.dn.prefix', header_name='config.header.ldap', format='STRING', required=0 where config_key='LDAPPrincipalDNPrefix';
update lams_configuration set description_key='config.ldap.principal.dn.suffix', header_name='config.header.ldap', format='STRING', required=0 where config_key='LDAPPrincipalDNSuffix';
update lams_configuration set description_key='config.ldap.security.protocol', header_name='config.header.ldap', format='STRING', required=0 where config_key='LDAPSecurityProtocol';
update lams_configuration set description_key='config.ldap.truststore.path', header_name='config.header.ldap', format='STRING', required=0 where config_key='LDAPTruststorePath';
update lams_configuration set description_key='config.ldap.truststore.password', header_name='config.header.ldap', format='STRING', required=0 where config_key='LDAPTruststorePassword';
update lams_configuration set description_key='config.ldap.login.attr', header_name='config.header.ldap.attributes', format='STRING', required=0 where config_key='LDAPLoginAttr';
update lams_configuration set description_key='config.ldap.fname.attr', header_name='config.header.ldap.attributes', format='STRING', required=0 where config_key='LDAPFNameAttr';
update lams_configuration set description_key='config.ldap.lname.attr', header_name='config.header.ldap.attributes', format='STRING', required=0 where config_key='LDAPLNameAttr';
update lams_configuration set description_key='config.ldap.email.attr', header_name='config.header.ldap.attributes', format='STRING', required=0 where config_key='LDAPEmailAttr';
update lams_configuration set description_key='config.ldap.addr1.attr', header_name='config.header.ldap.attributes', format='STRING', required=0 where config_key='LDAPAddr1Attr';
update lams_configuration set description_key='config.ldap.addr2.attr', header_name='config.header.ldap.attributes', format='STRING', required=0 where config_key='LDAPAddr2Attr';
update lams_configuration set description_key='config.ldap.addr3.attr', header_name='config.header.ldap.attributes', format='STRING', required=0 where config_key='LDAPAddr3Attr';
update lams_configuration set description_key='config.ldap.city.attr', header_name='config.header.ldap.attributes', format='STRING', required=0 where config_key='LDAPCityAttr';
update lams_configuration set description_key='config.ldap.state.attr', header_name='config.header.ldap.attributes', format='STRING', required=0 where config_key='LDAPStateAttr';
update lams_configuration set description_key='config.ldap.postcode.attr', header_name='config.header.ldap.attributes', format='STRING', required=0 where config_key='LDAPPostcodeAttr';
update lams_configuration set description_key='config.ldap.country.attr', header_name='config.header.ldap.attributes', format='STRING', required=0 where config_key='LDAPCountryAttr';
update lams_configuration set description_key='config.ldap.day.phone.attr', header_name='config.header.ldap.attributes', format='STRING', required=0 where config_key='LDAPDayPhoneAttr';
update lams_configuration set description_key='config.ldap.evening.phone.attr', header_name='config.header.ldap.attributes', format='STRING', required=0 where config_key='LDAPEveningPhoneAttr';
update lams_configuration set description_key='config.ldap.fax.attr', header_name='config.header.ldap.attributes', format='STRING', required=0 where config_key='LDAPFaxAttr';
update lams_configuration set description_key='config.ldap.mobile.attr', header_name='config.header.ldap.attributes', format='STRING', required=0 where config_key='LDAPMobileAttr';
update lams_configuration set description_key='config.ldap.locale.attr', header_name='config.header.ldap.attributes', format='STRING', required=0 where config_key='LDAPLocaleAttr';
update lams_configuration set description_key='config.ldap.disabled.attr', header_name='config.header.ldap.attributes', format='STRING', required=0 where config_key='LDAPDisabledAttr';
update lams_configuration set description_key='config.ldap.org.attr', header_name='config.header.ldap.attributes', format='STRING', required=0 where config_key='LDAPOrgAttr';
update lams_configuration set description_key='config.ldap.roles.attr', header_name='config.header.ldap.attributes', format='STRING', required=0 where config_key='LDAPRolesAttr';
update lams_configuration set description_key='config.ldap.learner.map', header_name='config.header.ldap.attributes', format='STRING', required=0 where config_key='LDAPLearnerMap';
update lams_configuration set description_key='config.ldap.monitor.map', header_name='config.header.ldap.attributes', format='STRING', required=0 where config_key='LDAPMonitorMap';
update lams_configuration set description_key='config.ldap.author.map', header_name='config.header.ldap.attributes', format='STRING', required=0 where config_key='LDAPAuthorMap';
update lams_configuration set description_key='config.ldap.group.admin.map', header_name='config.header.ldap.attributes', format='STRING', required=0 where config_key='LDAPGroupAdminMap';
update lams_configuration set description_key='config.ldap.group.manager.map', header_name='config.header.ldap.attributes', format='STRING', required=0 where config_key='LDAPGroupManagerMap';
update lams_configuration set description_key='config.ldap.update.on.login', header_name='config.header.ldap', format='BOOLEAN', required=1 where config_key='LDAPUpdateOnLogin';
update lams_configuration set description_key='config.ldap.org.field', header_name='config.header.ldap.attributes', format='STRING', required=0 where config_key='LDAPOrgField';
update lams_configuration set description_key='config.ldap.only.one.org', header_name='config.header.ldap', format='BOOLEAN', required=1 where config_key='LDAPOnlyOneOrg';
update lams_configuration set description_key='config.ldap.encrypt.password.from.browser', header_name='config.header.ldap', format='BOOLEAN', required=1 where config_key='LDAPEncryptPasswordFromBrowser';
update lams_configuration set description_key='config.ldap.search.results.page.size', header_name='config.header.ldap', format='LONG', required=0 where config_key='LDAPSearchResultsPageSize';

