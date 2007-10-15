insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('ServerURL','http://shaun.melcoe.mq.edu.au/rams/', 'config.server.url', 'config.header.system', 'STRING', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('ServerURLContextPath','rams/', 'config.server.url.context.path', 'config.header.system', 'STRING', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('Version','1.0 Beta', 'config.version', 'config.header.system', 'STRING', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('TempDir','/var/opt/rams/temp', 'config.temp.dir', 'config.header.system', 'STRING', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('DumpDir','/var/opt/rams/dump', 'config.dump.dir', 'config.header.system', 'STRING', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('EARDir','/usr/local/jboss-4.0.2/server/default/deploy/rams.ear', 'config.ear.dir', 'config.header.system', 'STRING', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('SMTPServer','', 'config.smtp.server', 'config.header.email', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LamsSupportEmail','lams_support@melcoe.mq.edu.au', 'config.lams.support.email', 'config.header.email', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('ContentRepositoryPath','/var/opt/rams/repository', 'config.content.repository.path', 'config.header.uploads', 'STRING', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('UploadFileMaxSize','1048576', 'config.upload.file.max.size', 'config.header.uploads', 'LONG', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('UploadLargeFileMaxSize','10485760', 'config.upload.large.file.max.size', 'config.header.uploads', 'LONG', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('UploadFileMaxMemorySize','4096', 'config.upload.file.max.memory.size', 'config.header.uploads', 'LONG', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('ExecutableExtensions','.bat,.bin,.com,.cmd,.exe,.msi,.msp,.ocx,.pif,.scr,.sct,.sh,.shs,.vbs', 'config.executable.extensions', 'config.header.uploads', 'STRING', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('UserInactiveTimeout','86400', 'config.user.inactive.timeout', 'config.header.system', 'LONG', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('UseCacheDebugListener','false', 'config.use.cache.debug.listener', 'config.header.system', 'BOOLEAN', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('CleanupPreviewOlderThanDays','7', 'config.cleanup.preview.older.than.days', 'config.header.system', 'LONG', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('AuthoringActivitiesColour', 'true', 'config.authoring.activities.colour', 'config.header.look.feel', 'BOOLEAN', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('AuthoringClientVersion','0.8.1.@datetimestamp@', 'config.authoring.client.version', 'config.header.versions', 'STRING', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('MonitorClientVersion','0.8.1.@datetimestamp@', 'config.monitor.client.version', 'config.header.versions', 'STRING', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LearnerClientVersion','0.8.1.@datetimestamp@', 'config.learner.client.version', 'config.header.versions', 'STRING', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('ServerVersionNumber','0.8.1.@datetimestamp@', 'config.server.version.number', 'config.header.versions', 'STRING', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('ServerLanguage','en_AU', 'config.server.language', 'config.header.look.feel', 'STRING', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('ServerPageDirection','LTR', 'config.server.page.direction', 'config.header.look.feel', 'STRING', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('DictionaryDateCreated','2007-05-24', 'config.dictionary.date.created', 'config.header.versions', 'STRING', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('HelpURL','http://wiki.lamsfoundation.org/display/lamsdocs/', 'config.help.url', 'config.header.system', 'STRING', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('XmppDomain','shaun.melcoe.mq.edu.au', 'config.xmpp.domain', 'config.header.chat', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('XmppConference','conference.shaun.melcoe.mq.edu.au', 'config.xmpp.conference', 'config.header.chat', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('XmppAdmin','admin', 'config.xmpp.admin', 'config.header.chat', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('XmppPassword','wildfire', 'config.xmpp.password', 'config.header.chat', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('DefaultFlashTheme','default', 'config.default.flash.theme', 'config.header.look.feel', 'STRING', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('DefaultHTMLTheme','defaultHTML', 'config.default.html.theme', 'config.header.look.feel', 'STRING', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('AllowDirectLessonLaunch','false', 'config.allow.direct.lesson.launch', 'config.header.features', 'BOOLEAN', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LAMS_Community_enable','false', 'config.community.enable', 'config.header.features', 'BOOLEAN', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('AllowLiveEdit','true', 'config.allow.live.edit', 'config.header.features', 'BOOLEAN', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPProvisioningEnabled','false', 'config.ldap.provisioning.enabled', 'config.header.ldap', 'BOOLEAN', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPProviderURL','ldap://192.168.111.15', 'config.ldap.provider.url', 'config.header.ldap', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPSecurityAuthentication','simple', 'config.ldap.security.authentication', 'config.header.ldap', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPPrincipalDNPrefix','cn=', 'config.ldap.principal.dn.prefix', 'config.header.ldap', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPPrincipalDNSuffix',',ou=Users,dc=melcoe,dc=mq,dc=edu,dc=au', 'config.ldap.principal.dn.suffix', 'config.header.ldap', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPSecurityProtocol','', 'config.ldap.security.protocol', 'config.header.ldap', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPTruststorePath','', 'config.ldap.truststore.path', 'config.header.ldap', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPTruststorePassword','', 'config.ldap.truststore.password', 'config.header.ldap', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPLoginAttr','uid', 'config.ldap.login.attr', 'config.header.ldap.attributes', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPFNameAttr','givenName', 'config.ldap.fname.attr', 'config.header.ldap.attributes', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPLNameAttr','sn', 'config.ldap.lname.attr', 'config.header.ldap.attributes', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPEmailAttr','mail', 'config.ldap.email.attr', 'config.header.ldap.attributes', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPAddr1Attr','postalAddress', 'config.ldap.addr1.attr', 'config.header.ldap.attributes', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPAddr2Attr','', 'config.ldap.addr2.attr', 'config.header.ldap.attributes', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPAddr3Attr','', 'config.ldap.addr3.attr', 'config.header.ldap.attributes', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPCityAttr','l', 'config.ldap.city.attr', 'config.header.ldap.attributes', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPStateAttr','st', 'config.ldap.state.attr', 'config.header.ldap.attributes', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPPostcodeAttr','postalCode', 'config.ldap.postcode.attr', 'config.header.ldap.attributes', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPCountryAttr','', 'config.ldap.country.attr', 'config.header.ldap.attributes', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPDayPhoneAttr','telephoneNumber', 'config.ldap.day.phone.attr', 'config.header.ldap.attributes', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPEveningPhoneAttr','homePhone', 'config.ldap.evening.phone.attr', 'config.header.ldap.attributes', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPFaxAttr','facsimileTelephoneNumber', 'config.ldap.fax.attr', 'config.header.ldap.attributes', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPMobileAttr','mobile', 'config.ldap.mobile.attr', 'config.header.ldap.attributes', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPLocaleAttr','preferredLanguage', 'config.ldap.locale.attr', 'config.header.ldap.attributes', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPDisabledAttr','!accountStatus', 'config.ldap.disabled.attr', 'config.header.ldap.attributes', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPOrgAttr','deetITSchoolCode', 'config.ldap.org.attr', 'config.header.ldap.attributes', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPRolesAttr','memberOf', 'config.ldap.roles.attr', 'config.header.ldap.attributes', 'STRING', 0);

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
values ('LDAPEncryptPasswordFromBrowser', 'true', 'config.ldap.encrypt.password.from.browser', 'config.header.ldap', 'BOOLEAN', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPSearchResultsPageSize', '100', 'config.ldap.search.results.page.size', 'config.header.ldap', 'LONG', 0);
