<?php

require_once("config.php");
require_once("string_map.php");

header("Content-Type:text/html;charset=utf-8");
date_default_timezone_set('UTC');
function guid()
{
    mt_srand((double)microtime()*10000);
    $charid = strtolower(md5(uniqid(rand(), true)));
    $hyphen = chr(45);// "-"
    $uuid = //chr(123)// "{"
           //.
           substr($charid, 0, 8).$hyphen
           .substr($charid, 8, 4).$hyphen
           .substr($charid,12, 4).$hyphen
           .substr($charid,16, 4).$hyphen
           .substr($charid,20,12);
           //.chr(125);// "}"
    return $uuid;
}

function connectDB()
{
	$con = mysql_connect(DB_URL, DB_USER, DB_PWD);
	mysql_query("SET NAMES 'UTF8'");
	mysql_select_db(DB_NAME, $con);
	return $con;
}

function disconnectDB($con)
{
    mysql_close($con);
}

function getEventsInfoByShop($shop_uuid='all')
{
	$sql = "select * from __events where shop_uuid=" . wrapStr($shop_uuid);
	if (strcmp($shop_uuid, 'all') == 0)
	{
		$sql = "select * from __events where 1=1";
	}
//echo $sql;exit(0);
	$result = mysql_query($sql);

	$tempArray = array();
	if(mysql_num_rows($result) > 0)
	{
		$typeStrMap=getEventTypeStrMap();
		while($row = mysql_fetch_assoc($result))
		{
			unset($item);
			$item[DB_EVENTS_GOODS_UUID] = $row[DB_EVENTS_GOODS_UUID];
			$item[DB_EVENTS_EVENT_TYPE] = urlencode($typeStrMap[$row[DB_EVENTS_EVENT_TYPE]]);
			$item[DB_EVENTS_CURRENT_PRICE] = $row[DB_EVENTS_CURRENT_PRICE];
			$item[DB_EVENTS_STATUS] = $row[DB_EVENTS_STATUS];
			$item[DB_EVENTS_UPDATE_TIME] = $row[DB_EVENTS_UPDATE_TIME];
			$tempArray[] = $item;
		}
	}
	return $tempArray;
}

function makeOptionHtmlString($options, $selectedKey)
{
$html = "";
foreach($options as $key=>$val)
{
$html .= "<option value='" . $key . "'"
. (strcmp($key, $selectedKey)==0? "selected='selected'" : "")
. ">" . $val . "</option>";
}
return $html;
}

function getGoodsByShop($shop_uuid='all')
{
	$sql = "select __goods.* from __shop_goods_map inner join __goods where shop_uuid=" . wrapStr($shop_uuid);
	if (strcmp($shop_uuid, 'all') == 0)
	{
		$sql = "select __goods.* from __shop_goods_map inner join __goods where 1=1";
	}
//echo $sql;exit(0);
	$result = mysql_query($sql);
	return queryResultToArray($result);
}

//[TODO] Currently, we haven't setup the __shop_goods_map table data, just ignore this for first release, remember to revisit this later.
function getGoodsWithEventByShop($shop_uuid='all')
{
	$sql = "select __goods.*, __events.* from __goods left join __events on __goods.uuid = __events.goods_uuid";
	$result = mysql_query($sql);
	return queryResultToArray($result);
}

function queryResultToArray($result)
{
	$tempArray = array();
	if(mysql_num_rows($result) > 0)
	{
		while($row = mysql_fetch_assoc($result))
		{
			unset($item);
			foreach ($row as $key=>$val)
			{
				$item[$key] = $val;
			}
			$tempArray[] = $item;
		}
	}
	return $tempArray;
}

function encodeQueryResult2Json($arr)
{
	$encodedStr = "[]";
	if(count($arr) > 0)
	{
		$encodedStr = json_encode($arr);
	}
	return $encodedStr;
}

function getSelectOptionHtmlString($internalValue, $displayValue, $selectedValue)
{
	if (strcmp($internalValue, $selectedValue) == 0)
	{
		return '<option value="' . $internalValue . '" selected="selected">' . $displayValue . '</option>';
	}
	return '<option value="' . $internalValue . '">' . $displayValue . '</option>';
}

function getValueHtmlString($val)
{
	return ' value="' . $val . '"';
}

function getSelectedOptionHtmlString($option1, $option2)
{
	if (strcmp($option1, $option2) == 0)
	{
		return '<option value="' . $option1 . '" selected="true">';
	}
	return '<option value="' . $option1 . '">';
}

function getValueFromRequest($id)
{
	if(isset($_REQUEST[$id]))
	{
		$val = $_REQUEST[$id];
		return $val;
	}
	return NULL;
}

function wrapStr($str, $avoidSpecialChar=true)
{
	if ($avoidSpecialChar)
	{
		return "'" . htmlspecialchars($str, ENT_QUOTES) . "'";
	}
	else
	{
		return "'" . $str . "'";
	}
    //return "'" . $str . "'";
}

function wrapCol($str)
{
    return "`" . $str . "`";
}

function hasRecordOf($tableName, $key, $val)
{
	$sql = "select * from " . $tableName . " where " . wrapCol($key) . "=" . wrapStr($val);
	$result = mysql_query($sql);
	if(mysql_num_rows($result) > 0)
	{
		return true;
	}
	return false;
}

//improve later, should be able to process multi key-val conditions.
function hasRecordOfTwoConds($tableName, $key1, $val1, $key2, $val2)
{
	$sql = "select * from " . $tableName . " where " . wrapCol($key1) . "=" . wrapStr($val1) . " and " . wrapCol($key2) . "=" . wrapStr($val2);
//echo $sql;
//exit(0);
	$result = mysql_query($sql);
	if(mysql_num_rows($result) > 0)
	{
		return true;
	}
	return false;
}

function insertRecord($tableName, $record, $avoidSpecialChar=true)
{
    $keys = "";
    $vals = "";
    foreach($record as $key=>$val)
    {
        if(strlen($keys) == 0)
        {
            $keys = wrapCol($key);
            $vals = wrapStr($val, $avoidSpecialChar);
        }
        else
        {
            $keys .= "," . wrapCol($key);
            $vals .= "," . wrapStr($val, $avoidSpecialChar);
        }
    }
    $sql = "insert into " . $tableName . "(" . $keys . ") values(" . $vals . ")";
    //echo $sql;exit();
    $result = mysql_query($sql);
    return $result;
}

function updateRecord($tableName, $conditions, $newValues)
{
    $condition = "";
    $newValue = "";
    foreach($conditions as $key=>$val)
    {
        if(strlen($condition) == 0)
        {
            $condition = $key . "=" . wrapStr($val);
        }
        else
        {
            $condition .= " and " . $key . "=" . wrapStr($val);
        }
    }
    foreach($newValues as $key=>$val)
    {
        if(strlen($newValue) == 0)
        {
            $newValue = $key . "=" . wrapStr($val);
        }
        else
        {
            $newValue .= ", " . $key . "=" . wrapStr($val);
        }
    }
    $sql = "update " . $tableName . " set " . $newValue . " where " . $condition;
    $result = mysql_query($sql);
    return $result;
}

function updateFileContent($filePath, $oldStr, $newStr)
{
	$content = file_get_contents($filePath);
	$content = str_replace($oldStr, $newStr, $content);
	$fp=fopen($filePath, 'w');
	fwrite($fp, $content);
	fclose($fp);
}

function deleteContent($storeid, $languageInternalName)
{
	$basePath = ROOT . 'notifications' . DS . $storeid;

	$zipPath = $basePath . DS . 'lang_' . $languageInternalName . '.zip';
	$pagePath = $basePath . DS . 'lang_' . $languageInternalName . '.html';
	$pageHDPath = $basePath . DS . 'lang_' . $languageInternalName . '_hd.html';
echo $zipPath . "<br>";
echo $pagePath . "<br>";
echo $pageHDPath . "<br>";

	unlink($zipPath);
	unlink($pagePath);
	unlink($pageHDPath);
}

function logCurVisit($serverData, $tag=NULL)
{
	$uri = $serverData[DB_STATISTICS_REQUEST_URI];
	if (NULL != $tag)
	{
		$uri .= "|" . $tag;
	}
	$record[DB_STATISTICS_REQUEST_URI] = $uri;
	$record[DB_STATISTICS_HTTP_ACCEPTLANGUAGE] = $serverData[DB_STATISTICS_HTTP_ACCEPTLANGUAGE];
	$cookie_md5 = $serverData['HTTP_COOKIE'];
	if (strlen($cookie_md5) > 0)
	{
		$cookie_md5 = md5($cookie_md5);
	}
	$record[DB_STATISTICS_HTTP_COOKIE_MD5] = $cookie_md5;
	$record[DB_STATISTICS_HTTP_USERAGENT] = $serverData[DB_STATISTICS_HTTP_USERAGENT];


	//$moreinfo = extractInfo($serverData[DB_STATISTICS_HTTP_USERAGENT]);
	//$record[DB_STATISTICS_PLATFORM] = $moreinfo[DB_STATISTICS_PLATFORM];
	//$record[DB_STATISTICS_OS] = $moreinfo[DB_STATISTICS_OS];

	//Currently we have a better way to get geo-ip info.
	//$record[DB_STATISTICS_IP] = getUserIP($serverData);
	$record[DB_STATISTICS_IP] = $serverData['GEOIP_ADDR'];
	$record[DB_STATISTICS_CONTINENTCODE] = $serverData['GEOIP_CONTINENT_CODE'];
	$record[DB_STATISTICS_COUNTRYCODE] = $serverData['GEOIP_COUNTRY_CODE'];
	$record[DB_STATISTICS_COUNTRYNAME] = $serverData['GEOIP_COUNTRY_NAME'];
	$record[DB_STATISTICS_REGIONCODE] = $serverData['GEOIP_REGION'];
	$record[DB_STATISTICS_REGIONNAME] = $serverData['GEOIP_REGION_NAME'];
	$record[DB_STATISTICS_CITY] = $serverData['GEOIP_CITY'];

	insertRecord(DB_TABLE_STATISTICS, $record, false);
}

function getUserInfoByName($username)
{
    $sql = "select * from " . DB_TABLE_USERS . " where " . DB_USER_USERNAME . "=" . wrapStr($username);
    $result = mysql_query($sql);
    while($row = mysql_fetch_array($result))
    {
        if(NULL != $row)
        {
            return $row;
        }
    }
    return NULL;
}

function checkUsernamePassword($username, $password, &$uid)
{
    $userInfo = getUserInfoByName($username);
    if(NULL == $userInfo)
    {
        return NO_SUCH_USER;
    }
    $encryptedPwdInDB = $userInfo[DB_USER_PWDENC];
    $encryptedPwd = md5($password);
    if(strcmp($encryptedPwd, $encryptedPwdInDB) == 0)
    {
        $uid = $userInfo[DB_USER_ID];
        return AUTHENTICATION_PASSED;
    }
    else
    {
        return WRONG_PWD;
    }
}

function login($newSessionData)
{
	insertRecord(DB_TABLE_SESSIONS, $newSessionData);
	$sessionId = $newSessionData[DB_SESSION_ID];
	$_SESSION[SESSION_NAME] = $sessionId;
	setcookie(SESSION_NAME, $sessionId, time() + SESSION_EXPIRE_TIME);
}

function getSessionFromCookie()
{
    if(isset($_COOKIE[SESSION_NAME]))
    {
        return $_COOKIE[SESSION_NAME];
    }
    return NULL;
}

function getSessionId()
{
    $sessionId = "";
    if(isset($_SESSION[SESSION_NAME]))
    {
        $sessionId = $_SESSION[SESSION_NAME];
        if(!empty($sessionId))
        {
            return $sessionId;
        }
    }
    return getSessionFromCookie();
}

function getUserIdBySession($sessionId)
{
    $sql = "select * from " . DB_TABLE_SESSIONS . " where " . DB_SESSION_ID . "=" . wrapStr($sessionId);
    $result = mysql_query($sql);
    $uid = "";
    while($row = mysql_fetch_array($result))
    {
        $uid = $row[DB_SESSION_USERID];
        break;
    }
    return $uid;
}

function getUserInfoByUserId($uid)
{
    $sql = "select * from " . DB_TABLE_USERS . " where " . DB_USER_ID . "=" . wrapStr($uid);
    $result = mysql_query($sql);
    while($row = mysql_fetch_array($result, MYSQL_ASSOC))
    {
        if(NULL != $row)
        {
            return $row;
        }
    }
    return NULL;
}

function getUserInfoBySession($sessionId)
{
    $uid = getUserIdBySession($sessionId);
    if(empty($uid))
    {
        return NULL;
    }
    return getUserInfoByUserId($uid);
}

function isLoggedin(&$sessionId)
{
    $sessionId = getSessionId();
    return checkSession($sessionId);
}

function checkSession($sessionId)
{
    if(NULL == $sessionId)
    {
        return false;
    }
    $sql = "select * from " . DB_TABLE_SESSIONS . " where " . DB_SESSION_ID . "=" . wrapStr($sessionId);
    $result = mysql_query($sql);
    $isValid = false;
    while($row = mysql_fetch_array($result))
    {
        $createTime = $row[DB_SESSION_CREATETIME];
        $now = date("Y-m-d H:i:s");
        $timespan = strtotime($now) - strtotime($createTime);
        if($timespan < SESSION_EXPIRE_TIME)
        {
            $isValid = true;
            break;
        }
    }
    return $isValid;
}

function logout()
{
    $sessionId = getSessionId();
    if(NULL != $sessionId)
    {
        $sql = "DELETE from " . DB_TABLE_SESSIONS . " where " . DB_SESSION_ID . "=" . wrapStr($sessionId);
        $result = mysql_query($sql);
        @session_destroy();

        if(isset($_COOKIE[SESSION_NAME]))
        {
            setcookie(SESSION_NAME, "", time() - 3600);
        }
    }
}

function loginCheck()
{
	$userInfo = NULL;
	$con = connectDB();
	$sessionId = "";
	if(isLoggedin($sessionId))
	{
		$userInfo = getUserInfoBySession($sessionId);
	}
	else
	{
		header("Location: " . HTTP_ADMIN_PREFIX . "login/");
	}
	disconnectDB($con);
	return $userInfo;
}

function makeThumb($uuid, $type)
{
	$fullsize_filepath = '../../data/images/' . $uuid . '.png';
	$thumb_filepath = '../../data/thumbs/' . $uuid . '.png';

	if (strcmp($type, "image/jpeg") == 0)
	{
		$src_image = ImageCreateFromJPEG($fullsize_filepath);
	}
	elseif (strcmp($type, "image/png") == 0)
	{
		$src_image = ImageCreateFromPNG($fullsize_filepath);
	}
	elseif (strcmp($type, "image/gif") == 0)
	{
		$src_image = ImageCreateFromGIF($fullsize_filepath);
	}
	$srcW = ImageSX($src_image);
	$srcH = ImageSY($src_image);
	$scale = 1;
	if($srcW > THUMBNAIL_WIDTH)
	{
		$scale = THUMBNAIL_WIDTH / $srcW;
	}
	$dstW = $srcW * $scale;
	$dstH = $srcH * $scale;
	$dst_image = ImageCreateTrueColor($dstW, $dstH);
	imagecopyresampled($dst_image, $src_image, 0, 0, 0, 0, $dstW, $dstH, $srcW, $srcH);
	imagepng($dst_image, $thumb_filepath, 9);
}

function uploadPreviewImage($uuid)
{
	$filepath = '../../data/images/' . $uuid . '.png';

	foreach($_FILES as $key=>$file)
	{
		if(empty($file['name']))
		{
			continue;
		}

		try
		{
			if (!move_uploaded_file($file['tmp_name'], $filepath))
			{
				echo "Failed to upload" . $file['name'];
			}
			else
			{
				echo $file['name'] . 'has been uploaded as ' . $filepath . '<br>';
				makeThumb($uuid, $file['type']);
			}
		}
		catch(Exception $ex)
		{
			echo $ex;
		}
		break;
	}
}

?>