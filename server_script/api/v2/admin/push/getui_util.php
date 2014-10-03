<?php
header("Content-Type: text/html; charset=utf-8");

require_once(dirname(__FILE__) . '/' . 'IGt.Push.php');
define('APPKEY','VfKgTkBGnKA5FOpSPsZJh5');
define('APPID','QdJ0DwOUDK9KHMLCk0b7k8');
define('MASTERSECRET','1vXIEUw81L8kzRF8y0NrS6');
define('HOST','http://sdk.open.api.igexin.com/apiex.htm');

function pushMessageToApp($msg){
	$igt = new IGeTui(HOST,APPKEY,MASTERSECRET);
	
	$template =  new IGtTransmissionTemplate(); 

	$template ->set_transmissionType(2);
	$template ->set_appId(APPID);
	$template ->set_appkey(APPKEY);
	$template ->set_transmissionContent($msg);

	$message = new IGtAppMessage();

	$message->set_isOffline(true);
	$message->set_offlineExpireTime(30);
	$message->set_data($template);

	$message->set_appIdList(array(APPID));
	$message->set_phoneTypeList(array('ANDROID'));
	$rep = $igt->pushMessageToApp($message);

	var_dump($rep);
}

/*
$msg = "this is a test msg";//$GLOBALS["HTTP_RAW_POST_DATA"];
if (empty($msg))
{
	echo "nothing to push";
	exit(0);
}
echo $msg;
$result = pushMessageToApp($msg);
echo $result;
*/

?>
