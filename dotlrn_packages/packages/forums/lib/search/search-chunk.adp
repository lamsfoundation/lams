<if @searchbox_p@ true>
    <div style="display: block; text-align: right;">
      <formtemplate id="search">
        <div><formwidget id="forum_id"></div>
        <div style="display: inline">
          <label for="search_text">#forums.Search#&nbsp;
            <formwidget id="search_text">
          </label>
        </div>
      </formtemplate>
    </div>

    <div style="display: block; padding-top: 0.4em">
      <include src="../message/messages-table" &messages="messages">
    </div>
</if>
