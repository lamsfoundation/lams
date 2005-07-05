alter table ATTACHMENT drop foreign key FKA7E145233FF9501;
alter table ATTACHMENT drop foreign key FKA7E14523D1B;
alter table FORUM drop foreign key FK3FF9501D1B;
alter table MESSAGE drop foreign key FK63B68BE78C3DFCAA;
alter table MESSAGE drop foreign key FK63B68BE73FF9501;
alter table MESSAGE drop foreign key FK63B68BE7D1B;
drop table if exists ATTACHMENT;
drop table if exists FORUM;
drop table if exists GENERICENTITY;
drop table if exists MESSAGE;





