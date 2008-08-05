<p>
  #forums.Users_that_wrote_in_the_forum# <b>@name@</b>
</p>


<if @persons:rowcount@ gt 0>
    <listtemplate name="persons"></listtemplate>
</if>
<else>
    <p>#forums.No_Postings#</p>
</else>
