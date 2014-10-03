<?php

require_once('admin/util.php');

if (!isset($_GET['op']))
{
	echo "Wrong arg(s).";
	exit(0);
}

$op = $_GET['op'];
if (empty($op))
{
	echo "Wrong arg(s).";
	exit(0);
}

$default_shop_uuid = '530a01ad-6b79-437f-9ac5-efB690566798';

switch ($op)
{
	case "AddGoods":
		$uuid = guid();
		uploadPreviewImage($uuid);

		$shop_uuid = $default_shop_uuid;

		$goods_record[DB_GOODS_UUID] = $uuid;
		$goods_record[DB_GOODS_NAME] = getValueFromRequest('name');
		$goods_record[DB_GOODS_CATEGORY] = getValueFromRequest('category');
		$goods_record[DB_GOODS_BRAND] = getValueFromRequest('brand');
		$goods_record[DB_GOODS_MODEL] = getValueFromRequest('model');
		$goods_record[DB_GOODS_DESCRIPTION] = getValueFromRequest('description');

		$sgm_record[DB_SGM_SHOP_UUID] = $shop_uuid;
		$sgm_record[DB_SGM_GOODS_UUID] = $uuid;

		$conn = connectDB();
		insertRecord(DB_TABLE_GOODS, $goods_record);
		insertRecord(DB_TABLE_SGM, $sgm_record);
		disconnectDB($conn);

		break;
	case "GetAllProductInfo":
		$conn = connectDB();
		echo json_encode(getGoodsByShop());
		disconnectDB($conn);
		break;
	case "GetAllProductSummaryInfo":
		$conn = connectDB();
		echo json_encode(getGoodsSummaryInfoByShop());
		disconnectDB($conn);
		break;
	case "GetEventsInfo":
		$conn = connectDB();
		echo urldecode(json_encode(getEventsInfoByShop()));
		disconnectDB($conn);
		break;
	case "UpdateProductEventsInfo":

//var_dump($_REQUEST);
//exit(0);
		$conn = connectDB();

$goods_uuid = getValueFromRequest('goodsuuid');
$type = getValueFromRequest('type');
$current_price = getValueFromRequest('current_price');

if (hasRecordOfTwoConds(DB_TABLE_EVENTS, DB_EVENTS_SHOP_UUID, $default_shop_uuid, DB_EVENTS_GOODS_UUID, $goods_uuid))
{
		$update_condition[DB_EVENTS_SHOP_UUID] = $default_shop_uuid;
		$update_condition[DB_EVENTS_GOODS_UUID] = $goods_uuid;

		$value_record[DB_EVENTS_EVENT_TYPE] = $type;
		$value_record[DB_EVENTS_CURRENT_PRICE] = $current_price;
		$value_record[DB_EVENTS_STATUS] = 'enabled';
	updateRecord(DB_TABLE_EVENTS, $update_condition, $value_record);
}
else
{
		$goods_record[DB_EVENTS_SHOP_UUID] = $default_shop_uuid;
		$goods_record[DB_EVENTS_GOODS_UUID] = $goods_uuid;
		$goods_record[DB_EVENTS_EVENT_TYPE] = $type;
		$goods_record[DB_EVENTS_CURRENT_PRICE] = $current_price;
		$goods_record[DB_EVENTS_STATUS] = 'enabled';
		insertRecord(DB_TABLE_EVENTS, $goods_record);
}
		disconnectDB($conn);
		break;
	default:
		echo $op . " is not ready for test, if you want this urgently, let tom know.";
		break;
}

?>