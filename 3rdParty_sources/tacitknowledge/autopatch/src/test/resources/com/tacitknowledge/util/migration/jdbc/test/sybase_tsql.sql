/* just some sane sql at first             */
 

PRINT 'Creating photo table'
go                  
create table photo
(
id                              numeric(14,0)       	NOT NULL,
ownerid                         numeric(14,0)       	NOT NULL,
date_created                    datetime            	NOT NULL,
status                          tinyint             	NULL
)
lock ALLPAGES
go
 
create clustered index c_ownerid_status_date on 
photos(ownerid,status,date_created) 
go

alter table photo add version default 0
go
 
create unique nonclustered index unc_id on 
photo(id) 
go
 
create nonclustered index nc_ownerid on 
photo(ownerid) 
Go  
 
grant delete,insert,select,update on photo to user
 go

/* will this table name screw up the parser looking for GO delimiter :)? */
create table gogo
(
id numeric(14,0) NOT NULL
value varchar(32)
)
GO