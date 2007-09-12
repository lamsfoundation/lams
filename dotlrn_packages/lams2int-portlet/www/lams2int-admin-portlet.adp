<if @configured_p@ eq 0>

<div style="background-color: #9dc5e9;; padding-left: 12px; padding-right: 12px; padding-top: 8px; padding-bottom: 8px; width: 80%; font-size: 100%; border: 1px solid #dd9988">

<b>The LAMS .LRN Module parameters are not configured!</b>
<p>
Configure the module parameters or contact your system administrator.
</p>
<p>
 For assistance see <a href="http://lamscommunity.org" target="_new">LAMS Community</a>
</p>
</div>


</if>
<else>

<ul>
  <li>
    <a href="@url@/admin/add">Add a new LAMS lesson</a>
  </li>
  <li>
    <a href="@url@/admin/">LAMS Admin</a>
  </li>
</ul>

</else>


