<if @hot_topics:rowcount@ ne 0 and @new_topics:rowcount@ ne 0 or @show_empty_p@ true>
	<if @class@ eq "portalElement">
		<h3><a href="@base_url@">@title@</a></h3>

		<h4>Hot topics</h4>
		<if @hot_topics:rowcount@ eq 0><p>None</p></if><else>
		<p><multiple name="hot_topics">
		  <a href="@hot_topics.url@">@hot_topics.name@</a><br />
		</multiple></p>
		</else>

		<h4>New topics</h4>
		<if @new_topics:rowcount@ eq 0><p>None</p></if><else>
		<p><multiple name="new_topics">
		  <a href="@new_topics.url@">@new_topics.name@</a><br />
		</multiple></p>
		</else>
	</if>
	<else>
		<h2><a href="@base_url@">@title@</a></h2>

		<h3>Hot Topics</h3>
		<if @hot_topics:rowcount@ eq 0><p>None</p></if><else>
		<p><multiple name="hot_topics">
		  <a href="@hot_topics.url@">@hot_topics.name@</a><br />
		</multiple></p>
		</else>

		<h3>New Topics</h3>
		<if @new_topics:rowcount@ eq 0><p>None</p></if><else>
		<p><multiple name="new_topics">
		  <a href="@new_topics.url@">@new_topics.name@</a><br />
		</multiple></p>
		</else>
	</else>
</if>
