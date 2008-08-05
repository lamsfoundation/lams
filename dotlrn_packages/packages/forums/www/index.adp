<master>
<property name="title">#forums.Forums#</property>
<property name="context">@context;noquote@</property>

<if @searchbox_p@ true>
  <include src="/packages/forums/lib/search/search-form">
</if>

<include src="/packages/forums/lib/forums/forums-chunk">
