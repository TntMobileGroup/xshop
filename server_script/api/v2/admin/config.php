<?php
// Database
require_once('db_config.php');

//------------------------------------------------------
// Sessions table
define("DB_TABLE_SESSION", "__session");

define("DB_SESSION_USERID", "userid");
define("DB_SESSION_CREATETIME", "create_time");

define("SESSION_EXPIRE_TIME", 7200);  // 2 hours
define("SESSION_NAME", "acg_noti_sid");

define("AUTHENTICATION_PASSED","Authentication passed.");
define("NO_SUCH_USER","No such user.");
define("WRONG_PWD","Wrong password.");

//------------------------------------------------------
// Goods table
define("DB_TABLE_GOODS", "__goods");

define("DB_GOODS_ID", "id");
define("DB_GOODS_UUID", "uuid");
define("DB_GOODS_CATEGORY", "category");
define("DB_GOODS_BRAND", "brand");
define("DB_GOODS_MODEL", "model");
define("DB_GOODS_NAME", "name");
define("DB_GOODS_DESCRIPTION", "description");
define("DB_GOODS_UPDATE_TIME", "update_time");

//------------------------------------------------------
// Goods table
define("DB_TABLE_SGM", "__shop_goods_map");

define("DB_SGM_SHOP_UUID", "shop_uuid");
define("DB_SGM_GOODS_UUID", "goods_uuid");
define("DB_SGM_UPDATE_TIME", "update_time");

//------------------------------------------------------
// Goods table
define("DB_TABLE_EVENTS", "__events");

define("DB_EVENTS_SHOP_UUID", "shop_uuid");
define("DB_EVENTS_GOODS_UUID", "goods_uuid");
define("DB_EVENTS_EVENT_TYPE", "event_type");
define("DB_EVENTS_CURRENT_PRICE", "current_price");
define("DB_EVENTS_STATUS", "status");
define("DB_EVENTS_UPDATE_TIME", "update_time");

define("THUMBNAIL_WIDTH", 160);
?>