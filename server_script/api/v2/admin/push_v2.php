<?php

require_once('push/getui_util.php');
$data = $_REQUEST['data'];
pushMessageToApp($data);
?>