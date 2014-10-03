<?php
require_once('util.php');

$conn = connectDB();
$goods = getGoodsByShop();
disconnectDB($conn);

var_dump($goods);
?>