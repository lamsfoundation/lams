--
--  Copyright (C) 2001, 2002 MIT
--
--  This file is part of dotLRN.
--
--  dotLRN is free software; you can redistribute it and/or modify it under the
--  terms of the GNU General Public License as published by the Free Software
--  Foundation; either version 2 of the License, or (at your option) any later
--  version.
--
--  dotLRN is distributed in the hope that it will be useful, but WITHOUT ANY
--  WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
--  FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
--  details.
--

--
-- Support for searching of forum messages
--
-- @author yon@openforce.net
-- @creation-date 2002-07-01
-- @version $Id$
--



-- Call this script like this:
-- 
-- sqlplus /nolog @forums-search-create.sql <ctxsys-password> <openacs-db-user> <openacs-db-password>
-- 
--   &1 = ctxsys password
--   &2 = OpenACS database user
--   &3 = OpenACS database password


connect &2/&3;

-- as normal user
create or replace procedure index_message (
    rid                             in rowid,
    tlob                            in out NOCOPY clob
)
is
    offset                          number := 1;
begin

    for row in (select forums_messages.subject,
                       forums_messages.content,
                       persons.first_names,
                       persons.last_name
                from forums_messages,
                     persons
                where forums_messages.rowid = rid
                and forums_messages.user_id = persons.person_id)
    loop
        dbms_lob.writeappend(tlob, length(row.subject) + 1, ' ' || row.subject);
        dbms_lob.append(tlob, row.content);
        dbms_lob.writeappend(tlob, length(row.first_names) + 1, ' ' || row.first_names);
        dbms_lob.writeappend(tlob, length(row.last_name) + 1, ' ' || row.last_name);
    end loop;

end;
/
show errors

-- as ctxsys
connect ctxsys/&1;

create or replace procedure s_index_message (
    rid                             in rowid,
    tlob                            in out NOCOPY clob
)
is
begin
    &2..index_message(rid, tlob);
end;
/
show errors

grant execute on s_index_message to &2;
grant execute on ctx_ddl to &2;

-- as normal user
connect &2/&3;

execute ctx_ddl.create_preference('forums_user_datastore', 'user_datastore');
execute ctx_ddl.set_attribute('forums_user_datastore', 'procedure', 's_index_message');

create index forums_content_idx
    on forums_messages (content)
    indextype is ctxsys.context
    parameters ('datastore forums_user_datastore');

declare
    v_job                           number;
begin
    dbms_job.submit(
        job => v_job,
        what => 'ctxsys.ctx_ddl.sync_index(''forums_content_idx'');',
        interval => 'sysdate + 1/24'
    );
end;
/
show errors

-- ripped off from site-wide-search

-- if we pass in a very long string to im_convert, we will end up with internal 
--  strings that are too long. Because this is relatively hard to debug,
--  we wrote im_convert_length_check to throw a more appropriate error message
-- Note that we raise an exception because passing such a long query to
--  interMedia is pretty slow. Alternative to the application error are to 
--    1. return the string as is
--    2. increase the max length from 256 to 1024
-- mbryzek@arsdigita.com, 7/6/2000
create or replace procedure im_convert_length_check ( 
    p_string in varchar2,
    p_number_chars_to_append in number,
    p_max_length in number, 
    p_variable_name in varchar2 
)
is
begin
    if nvl(length(p_string),0) + p_number_chars_to_append > p_max_length
    then
	raise_application_error(-20000, 'Variable "' || p_variable_name || '" exceeds ' || p_max_length || ' character declaration');
    end if;
end;
/
show errors;

-- this proc takes user supplied free text and transforms it into an interMedia
-- friendly query string. (provided by oracle).
create or replace function im_convert (
    query in varchar2 default null
) return varchar2
is
    i number := 0;
    len number := 0;
    char varchar2(1);
    minusString varchar2(256) := '';
    plusString varchar2(256) := ''; 
    mainString varchar2(256) := ''; 
    mainAboutString varchar2(500) := ''; 
    finalString varchar2(500) := ''; 
    hasMain number := 0;
    hasPlus number := 0;
    hasMinus number := 0;
    token varchar2(256);
    tokenStart number := 1;
    tokenFinish number := 0;
    inPhrase number := 0;
    inPlus number := 0;
    inWord number := 0;
    inMinus number := 0;
    completePhrase number := 0;
    completeWord number := 0;
    code number := 0;  
begin
  
    len := length(query);

    -- we iterate over the string to find special web operators
    for i in 1..len loop
	char := substr(query,i,1);
	if(char = '"') then
	    if(inPhrase = 0) then
		inPhrase := 1;
		tokenStart := i;
	    else
		inPhrase := 0;
		completePhrase := 1;
		tokenFinish := i-1;
	    end if;
	elsif(char = ' ') then
	    if(inPhrase = 0) then
		completeWord := 1;
		tokenFinish := i-1;
	    end if;
	elsif(char = '+') then
	    inPlus := 1;
	    tokenStart := i+1;
	elsif((char = '-') and (i = tokenStart)) then
	    inMinus :=1;
	    tokenStart := i+1;
	end if;

	if(completeWord=1) then
	    token := '{ '||substr(query,tokenStart,tokenFinish-tokenStart+1)||' }';      
	    if(inPlus=1) then
		im_convert_length_check(plusString, 4+length(token), 256, 'plusString');
		plusString := plusString||','||token||'*10';
		hasPlus :=1;	
	    elsif(inMinus=1) then
		im_convert_length_check(minusString, 4+length(token), 256, 'minusString');
		minusString := minusString||'OR '||token||' ';
		hasMinus :=1;
	    else
		im_convert_length_check(mainString, 6+length(token), 256, 'mainString');
		mainString := mainString||' NEAR '||token;
		im_convert_length_check(mainAboutString, 1+length(token), 500, 'mainAboutString');
		mainAboutString := mainAboutString||' '||token; 
		hasMain :=1;
	    end if;
	    tokenStart  :=i+1;
	    tokenFinish :=0;
	    inPlus := 0;
	    inMinus :=0;
	end if;
	completePhrase := 0;
	completeWord :=0;
    end loop;

    -- find the last token
    token := '{ '||substr(query,tokenStart,len-tokenStart+1)||' }';
    if(inPlus=1) then
	im_convert_length_check(plusString, 4+length(token), 256, 'plusString');
	plusString := plusString||','||token||'*10';
	hasPlus :=1;	
    elsif(inMinus=1) then
	im_convert_length_check(minusString, 4+length(token), 256, 'minusString');
	minusString := minusString||'OR '||token||' ';
	hasMinus :=1;
    else
	im_convert_length_check(mainString, 6+length(token), 256, 'mainString');
	mainString := mainString||' NEAR '||token;
	im_convert_length_check(mainAboutString, 1+length(token), 500, 'mainAboutString');
	mainAboutString := mainAboutString||' '||token; 
	hasMain :=1;
    end if;
  
    mainString := substr(mainString,6,length(mainString)-5);
    mainAboutString := replace(mainAboutString,'{',' ');
    mainAboutString := replace(mainAboutString,'}',' ');
    mainAboutString := replace(mainAboutString,')',' ');	
    mainAboutString := replace(mainAboutString,'(',' ');
    plusString := substr(plusString,2,length(plusString)-1);
    minusString := substr(minusString,4,length(minusString)-4);

    -- let's just check once for the length of finalString... note this uses the 
    -- longest possible string that is created in the rest of this function
    im_convert_length_check(finalString, nvl(length(mainString),0) + nvl(length(mainAboutString),0) + nvl(length(minusString),0) + nvl(length(plusString),0) + 30, 500, 'finalString');

    -- we find the components present and then process them based on the specific combinations
    code := hasMain*4+hasPlus*2+hasMinus;
    if(code = 7) then
	finalString := '('||plusString||','||mainString||'*2.0,about('||mainAboutString||')*0.5) NOT ('||minusString||')';
    elsif (code = 6) then  
	finalString := plusString||','||mainString||'*2.0'||',about('||mainAboutString||')*0.5';
    elsif (code = 5) then  
	finalString := '('||mainString||',about('||mainAboutString||')) NOT ('||minusString||')';
    elsif (code = 4) then  
	finalString := mainString; 
	finalString := replace(finalString,'*1,',NULL); 
	finalString := '('||finalString||')*2.0,about('||mainAboutString||')';
    elsif (code = 3) then  
	finalString := '('||plusString||') NOT ('||minusString||')';
    elsif (code = 2) then  
	finalString := plusString;
    elsif (code = 1) then  
	-- not is a binary operator for intermedia text
	finalString := 'totallyImpossibleString'||' NOT ('||minusString||')';
    elsif (code = 0) then  
	finalString := '';
    end if;

    return finalString;
end;
/
show errors;

exit

