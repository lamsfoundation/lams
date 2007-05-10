update lams_configuration set config_value='2.0.2' where config_key='Version';
update lams_configuration set config_value='2007-04-15' where config_key='DictionaryDateCreated';
update lams_configuration set config_value='http://137.111.246.57:8080/lams/' where config_key='ServerURL';
update lams_configuration set config_value='/var/opt/lams/temp' where config_key='TempDir';
update lams_configuration set config_value='/var/opt/lams/dump' where config_key='DumpDir';
update lams_configuration set config_value='/usr/local/jboss-4.0.2/server/default/deploy/lams.ear' where config_key='EARDir';
update lams_configuration set config_value='2.0.2.200703220556' where config_key='AuthoringClientVersion' or config_key='MonitorClientVersion' or config_key="LearnerClientVersion" or config_key='ServerVersionNumber';
update lams_configuration set config_value='' where config_key='SMTPServer';
update lams_configuration set config_value='/var/opt/lams/repository' where config_key='ContentRepositoryPath';
update lams_configuration set config_value='en_AU' where config_key='ServerLanguage';
update lams_configuration set config_value='LTR' where config_key='ServerPageDirection';
update lams_configuration set config_value='localhost' where config_key='XmppDomain';
update lams_configuration set config_value='conference.localhost' where config_key='XmppConference';
update lams_configuration set config_value='admin' where config_key='XmppAdmin';
update lams_configuration set config_value='' where config_key='XmppPassword';
update lams_user set login='sysadmin', password=sha1('sysadmin') where user_id=1;
update lams_user set password=sha1('sysadmin') where user_id>1;
update lams_user set locale_id=(select locale_id from lams_supported_locale where language_iso_code=(SELECT SUBSTRING_INDEX('en_AU', '_', 1)) and country_iso_code=(SELECT SUBSTRING_INDEX('en_AU', '_', -1))) where user_id=1;


