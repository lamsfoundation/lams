<?php
require_once("../../config.php");
require_once($CFG->dirroot.'/mod/lamstwo/lib.php');

echo lamstwo_verify($_POST['u'], $_POST['i'], $_POST['k']);
?>