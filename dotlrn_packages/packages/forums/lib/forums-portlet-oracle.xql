<queryset>
<rdbms><type>oracle</type><version>8.1.6</version></rdbms>

<fullquery name="hot_topics">
<querytext>
SELECT r.* FROM (
SELECT
  b.subject AS subject,
  b.message_id AS thread_id,
  a.message_id AS message_id
FROM
  (SELECT message_id, tree_sortkey, posting_date
   FROM forums_messages
   WHERE forum_id IN
    (SELECT forum_id FROM forums_forums WHERE package_id =
    :package_id and enabled_p = 't')
   ORDER BY posting_date DESC) a,
  forums_messages b
WHERE
  b.parent_id IS NULL
  AND b.tree_sortkey = tree.ancestor_key(a.tree_sortkey, 1)
  AND b.forum_id IN
   (SELECT forum_id FROM forums_forums WHERE package_id = :package_id
   and enabled_p = 't')
ORDER BY a.posting_date DESC
) r WHERE rownum <= $n
</querytext>
</fullquery>

<fullquery name="new_topics">
<querytext>
SELECT r.*
FROM
(SELECT
  subject, message_id
FROM
  forums_messages_approved
WHERE
  forum_id IN
   (SELECT forum_id FROM forums_forums WHERE package_id = :package_id
   and enabled_p = 't')
  AND parent_id IS NULL
ORDER BY
  posting_date DESC) r
WHERE rownum <= $n
</querytext>
</fullquery>

</queryset>