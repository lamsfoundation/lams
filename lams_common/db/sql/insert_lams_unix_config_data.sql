insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('ServerURL','http://localhost:8080/lams/', 'config.server.url', 'config.header.system', 'STRING', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('ServerURLContextPath','lams/', 'config.server.url.context.path', 'config.header.system', 'STRING', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('Version','3.0', 'config.version', 'config.header.system', 'STRING', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('TempDir','@temp.directory@', 'config.temp.dir', 'config.header.system', 'STRING', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('EARDir','@ear.directory@', 'config.ear.dir', 'config.header.system', 'STRING', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required)
values ('EnableServerRegistration', 'false', 'config.server2server.registration.enable', 'config.header.system', 'BOOLEAN', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('SMTPServer','', 'config.smtp.server', 'config.header.email', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LamsSupportEmail','', 'config.lams.support.email', 'config.header.email', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('ContentRepositoryPath','@contentrepository.directory@', 'config.content.repository.path', 'config.header.uploads', 'STRING', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('UploadFileMaxSize','1048576', 'config.upload.file.max.size', 'config.header.uploads', 'LONG', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('UploadLargeFileMaxSize','10485760', 'config.upload.large.file.max.size', 'config.header.uploads', 'LONG', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('UploadFileMaxMemorySize','4096', 'config.upload.file.max.memory.size', 'config.header.uploads', 'LONG', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('ExecutableExtensions','.bat,.bin,.com,.cmd,.exe,.msi,.msp,.ocx,.pif,.scr,.sct,.sh,.shs,.vbs,.php,.jsp,.asp,.aspx,.pl,.do,.py,.tcl,.cgi,.shtml,.stm,.cfm,.adp', 'config.executable.extensions', 'config.header.uploads', 'STRING', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('UserInactiveTimeout','10800', 'config.user.inactive.timeout', 'config.header.system', 'LONG', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('CleanupPreviewOlderThanDays','7', 'config.cleanup.preview.older.than.days', 'config.header.system', 'LONG', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('AuthoringActivitiesColour', 'true', 'config.authoring.activities.colour', 'config.header.look.feel', 'BOOLEAN', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('AuthoringClientVersion','3.0.0.201701010000', 'config.authoring.client.version', 'config.header.versions', 'STRING', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('MonitorClientVersion','3.0.0.201701010000', 'config.monitor.client.version', 'config.header.versions', 'STRING', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LearnerClientVersion','3.0.0.201701010000', 'config.learner.client.version', 'config.header.versions', 'STRING', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('ServerVersionNumber','3.0.0.201701010000', 'config.server.version.number', 'config.header.versions', 'STRING', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('ServerLanguage','en_AU', 'config.server.language', 'config.header.look.feel', 'STRING', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('ServerPageDirection','LTR', 'config.server.page.direction', 'config.header.look.feel', 'STRING', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('DictionaryDateCreated','2017-01-01', 'config.dictionary.date.created', 'config.header.versions', 'STRING', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('HelpURL','http://wiki.lamsfoundation.org/display/lamsdocs/', 'config.help.url', 'config.header.system', 'STRING', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('DefaultTheme','defaultHTML', 'config.default.html.theme', 'config.header.look.feel', 'STRING', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('AllowDirectLessonLaunch','false', 'config.allow.direct.lesson.launch', 'config.header.features', 'BOOLEAN', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LAMS_Community_enable','false', 'config.community.enable', 'config.header.features', 'BOOLEAN', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('AllowLiveEdit','true', 'config.allow.live.edit', 'config.header.features', 'BOOLEAN', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('ShowAllMyLessonLink','true', 'config.show.all.my.lesson.link', 'config.header.features', 'BOOLEAN', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required)
values ('DisplayPrintButton','false', 'config.display.print.button', 'config.header.features', 'BOOLEAN', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPProvisioningEnabled','false', 'config.ldap.provisioning.enabled', 'config.header.ldap', 'BOOLEAN', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPProviderURL','ldap://192.168.111.15', 'config.ldap.provider.url', 'config.header.ldap', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPSecurityAuthentication','simple', 'config.ldap.security.authentication', 'config.header.ldap', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPSearchFilter','(cn={0})', 'config.ldap.search.filter', 'config.header.ldap', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPBaseDN','ou=Users,dc=melcoe,dc=mq,dc=edu,dc=au', 'config.ldap.base.dn', 'config.header.ldap', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPBindUserDN','', 'config.ldap.bind.user.dn', 'config.header.ldap', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPBindUserPassword','', 'config.ldap.bind.user.password', 'config.header.ldap', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPSecurityProtocol','', 'config.ldap.security.protocol', 'config.header.ldap', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPLoginAttr','uid', 'admin.user.login', 'config.header.ldap.attributes', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPFNameAttr','givenName', 'admin.user.first_name', 'config.header.ldap.attributes', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPLNameAttr','sn', 'admin.user.last_name', 'config.header.ldap.attributes', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPEmailAttr','mail', 'admin.user.email', 'config.header.ldap.attributes', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPAddr1Attr','postalAddress', 'admin.user.address_line_1', 'config.header.ldap.attributes', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPAddr2Attr','', 'admin.user.address_line_2', 'config.header.ldap.attributes', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPAddr3Attr','', 'admin.user.address_line_3', 'config.header.ldap.attributes', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPCityAttr','l', 'admin.user.city', 'config.header.ldap.attributes', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPStateAttr','st', 'admin.user.state', 'config.header.ldap.attributes', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPPostcodeAttr','postalCode', 'admin.user.postcode', 'config.header.ldap.attributes', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPCountryAttr','', 'admin.user.country', 'config.header.ldap.attributes', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPDayPhoneAttr','telephoneNumber', 'admin.user.day_phone', 'config.header.ldap.attributes', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPEveningPhoneAttr','homePhone', 'admin.user.evening_phone', 'config.header.ldap.attributes', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPFaxAttr','facsimileTelephoneNumber', 'admin.user.fax', 'config.header.ldap.attributes', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPMobileAttr','mobile', 'admin.user.mobile_phone', 'config.header.ldap.attributes', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPLocaleAttr','preferredLanguage', 'admin.organisation.locale', 'config.header.ldap.attributes', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPDisabledAttr','!accountStatus', 'sysadmin.disabled', 'config.header.ldap.attributes', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPOrgAttr','schoolCode', 'admin.course', 'config.header.ldap.attributes', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPRolesAttr','memberOf', 'admin.user.roles', 'config.header.ldap.attributes', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPLearnerMap','Student;SchoolSupportStaff;Teacher;SeniorStaff;Principal', 'config.ldap.learner.map', 'config.header.ldap.attributes', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPMonitorMap','SchoolSupportStaff;Teacher;SeniorStaff;Principal', 'config.ldap.monitor.map', 'config.header.ldap.attributes', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPAuthorMap','Teacher;SeniorStaff;Principal', 'config.ldap.author.map', 'config.header.ldap.attributes', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPGroupAdminMap','Teacher;SeniorStaff', 'config.ldap.group.admin.map', 'config.header.ldap.attributes', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPGroupManagerMap','Principal', 'config.ldap.group.manager.map', 'config.header.ldap.attributes', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPUpdateOnLogin', 'true', 'config.ldap.update.on.login', 'config.header.ldap', 'BOOLEAN', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPOrgField', 'code', 'config.ldap.org.field', 'config.header.ldap.attributes', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPOnlyOneOrg', 'true', 'config.ldap.only.one.org', 'config.header.ldap', 'BOOLEAN', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPSearchResultsPageSize', '100', 'config.ldap.search.results.page.size', 'config.header.ldap', 'LONG', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LearnerProgressBatchSize', '10', 'config.learner.progress.batch.size', 'config.header.look.feel', 'LONG', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('CustomTabLink','', 'config.custom.tab.link', 'config.header.look.feel', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('CustomTabTitle','', 'config.custom.tab.title', 'config.header.look.feel', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('AuthoringScreenSize','1280x720', 'config.authoring.screen.size', 'config.header.look.feel', 'STRING', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('MonitorScreenSize','1280x720', 'config.monitor.screen.size', 'config.header.look.feel', 'STRING', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LearnerScreenSize','1280x720', 'config.learner.screen.size', 'config.header.look.feel', 'STRING', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('AdminScreenSize','1280x720', 'config.admin.screen.size', 'config.header.look.feel', 'STRING', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('SMTPUser','', 'config.smtp.user', 'config.header.email', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('SMTPPassword','', 'config.smtp.password', 'config.header.email', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('Red5ServerUrl','', 'config.red5.server.url', 'config.header.red5', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('Red5RecordingsUrl','', 'config.red5.recordings.url', 'config.header.red5', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required)
values ('ProfileEditEnable','true', 'config.profile.edit.enable', 'config.header.features', 'BOOLEAN', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('ProfilePartialEditEnable','true', 'config.profile.partial.edit.enable', 'config.header.features', 'BOOLEAN', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('KalturaServer','http://www.kaltura.com', 'config.kaltura.server', 'config.header.kaltura', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('KalturaPartnerId','1073272', 'config.kaltura.partner.id', 'config.header.kaltura', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('KalturaSubPartnerId','107327200', 'config.kaltura.sub.partner.id', 'config.header.kaltura', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('KalturaUserSecret','f6b13f7e128e081b5cc9bb9664fd8717', 'config.kaltura.user.secret', 'config.header.kaltura', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('KalturaKCWUiConfId','1000741', 'config.kaltura.kcw.uiconfid', 'config.header.kaltura', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('KalturaKDPUiConfId','6308762', 'config.kaltura.kdp.uiconfid', 'config.header.kaltura', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('ConfigCacheRefreshInterval', 0, 'config.cache.refresh', 'config.header.system', 'LONG', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required)
values ('ErrorStackTrace','true', 'config.stacktrace.error', 'config.header.system', 'BOOLEAN', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required)
values ('FailedAttempts','3', 'config.failed.attempts', 'config.header.password.policy', 'LONG', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required)
values ('LockOutTime','5', 'config.lock.out.time', 'config.header.password.policy', 'LONG', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LearnerCollapsProgressPanel','true', 'config.learner.collapsible.progress.panel', 'config.header.features', 'BOOLEAN', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required)
values ('PasswordPolicyMinChars','8', 'config.password.minimum.characters', 'config.header.password.policy', 'LONG', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required)
values ('PasswordPolicyUppercase','true', 'config.password.uppercase', 'config.header.password.policy', 'BOOLEAN', 0);
 
insert into lams_configuration (config_key, config_value, description_key, header_name, format, required)
values ('PasswordPolicyLowercase','true', 'config.password.lowercase', 'config.header.password.policy', 'BOOLEAN', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required)
values ('PasswordPolicyNumerics','true', 'config.password.numerics', 'config.header.password.policy', 'BOOLEAN', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required)
values ('PasswordPolicySymbols','false', 'config.password.symbols', 'config.header.password.policy', 'BOOLEAN', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required)
values ('ShowTimezoneWarning','true', 'config.show.timezone.warning', 'config.header.features', 'BOOLEAN', 1);

INSERT INTO lams_configuration (config_key, config_value, description_key, header_name, format, required)
VALUES ('SiteName','LAMS', 'config.site.name', 'config.header.system', 'STRING', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('SuffixImportedLD','false', 'config.authoring.suffix', 'config.header.features', 'BOOLEAN', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('UserValidationUsername','true', 'config.user.validation.username', 'config.header.user.validation', 'BOOLEAN', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('UserValidationFirstLastName','true', 'config.user.validation.first.last.name', 'config.header.user.validation', 'BOOLEAN', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('UserValidationEmail','true', 'config.user.validation.emails', 'config.header.user.validation', 'BOOLEAN', 0);