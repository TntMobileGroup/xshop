<?php

require_once('push/getui_util.php');
$title = $_REQUEST['title'];
$content = $_REQUEST['content'];
$goodsuuid = $_REQUEST['goodsuuid'];
pushMessageToApp("0|" . $title . "|" . $content . "|" . $goodsuuid);
?>