<?php
require_once('../../config.php');
require_once($CFG->dirroot.'/mod/lamslesson/lib.php');
$u = required_param('u', PARAM_URL);
$i = required_param('i', PARAM_ALPHANUM);
$k = required_param('k', PARAM_ALPHANUM);

echo lamslesson_verify($u, $i, $k);
?>
