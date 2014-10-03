<?php

require_once('util.php');

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

switch ($op)
{
	case "AddGoods":
		$uuid = guid();
		uploadPreviewImage($uuid);

		$shop_uuid = '530a01ad-6b79-437f-9ac5-efB690566798';

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
	case "show":
		$conn = connectDB();
		echo json_encode(getGoodsByShop());
		disconnectDB($conn);
		break;
	default:
		break;
}

?>