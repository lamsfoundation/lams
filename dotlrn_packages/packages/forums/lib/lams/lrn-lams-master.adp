<master src="/www/blank-master">
  <if @context@ not nil><property name="context">@context;noquote@</property></if>
    <else><if @context_bar@ not nil><property name="context_bar">@context_bar;noquote@</property></if></else>
  <if @focus@ not nil><property name="focus">@focus;noquote@</property></if>
  <if @doc@ defined><property name="&doc">doc</property></if>

<div id="skiptocontent">
  <p>
    <a href="#content-wrapper" title="#theme-zen.skip_to_main_content#" accesskey="2">#theme-zen.skip_to_main_content#</a> | <a href="/theme-zen/accessibility" title="#theme-zen.Accessibility_page#" accesskey="0">#theme-zen.Accessibility#</a>
  </p>
</div>

<div id="wrapper">
  <div id="header">
    <div id="logo">
      <if @img_attrib@ not nil>
        <img @img_attrib;noquote@>
      </if>
    </div>
    <div id="header-navigation">
      <ul class="compact">
        <if @untrusted_user_id@ ne 0>
          <li>
            <!-- user greeting or login message -->
            #acs-subsite.Welcome_user#
          </li>
<!--
          <li>
            <a href="@whos_online_url@" title="#acs-subsite.Whos_Online_link_label#">
              | @num_users_online@
              <if @num_users_online@ eq 1>
                #acs-subsite.Member#
              </if>
              <else>
                #acs-subsite.Members#
              </else>
              #theme-zen.online#
            </a>
          </li>
          <li>
            | 
            <a href="@logout_url@" title="#acs-subsite.Logout_from_system#">
              #acs-subsite.Logout# 
            </a>
          </li>
-->
        </if>
        <else>
          <li>
            <a href="/register/">
              #acs-subsite.Log_In#
            </a>
          </li>
        </else>
      </ul>
    </div>
<!--
    <div id="breadcrumbs">
      <span class="screen-reader-only">#theme-zen.You_are_here#</span>
      <ul class="compact">
        <if @context:rowcount@ not nil>
          <multiple name="context">
            <li>
              <if @context.url@ not nil>
                <a href="@context.url@">@context.label@</a> @separator@
              </if>
              <else>
                @context.label@
              </else>
            </li>
          </multiple>
        </if>
      </ul>
    </div>
-->
  </div> <!-- /header -->
<!--
  <div id="main-navigation">
    <div class="block-marker">#theme-zen.begin_main_navigation#</div>
      <if @navbar@ not nil>@navbar;noquote@</if> 
  </div>

  <if @subnavbar@ not nil>
    <div id="sub-navigation">
      <div class="block-marker">#theme-zen.begin_sub_navigation#</div>
      @subnavbar;noquote@
    </div>
  </if>
    
-->
  <div id="content-wrapper">
    <div class="block-marker">#theme-zen.begin_main_content#</div>
    <div id="inner-wrapper">

      <if @user_messages:rowcount@ gt 0>
        <div id="alert-message">
          <multiple name="user_messages">
            <div class="alert"><strong>@user_messages.message;noquote@</strong></div>
          </multiple>
        </div>
      </if>

      <if @portal_page_p@ nil>
        <div id="main">
          <div id="main-content">
            <div class="main-content-padding">
      </if>

      <slave>

      <if @portal_page_p@ nil>
            </div> <!-- /main-content-padding -->
          </div> <!-- /main-content -->
        </div> <!-- /main -->
      </if>

    </div> <!-- /inner-wrapper -->

  </div> <!-- /content-wrapper -->

  <div id="footer">
    <div class="block-marker">#theme-zen.begin_footer#</div>
    <div id="footer-icons">
      <img src="http://www.w3.org/Icons/valid-html401-blue" alt="Valid HTML 4.01 Strict" height="31" width="88">
    </div>
    <div id="footer-links">
      <ul class="compact">
        <li>#dotlrn.A_dotlrn_Site#</li>
        <li>#acs-subsite.Powered_by_OpenACS#</li>
      </ul>
    </div> <!-- /footer-links -->
  </div> <!-- /footer -->
</div> <!-- /wrapper -->
