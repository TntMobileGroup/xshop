<?php
require_once('../admin/util.php');

$conn = connectDB();
$goodsList = getGoodsWithEventByShop();
disconnectDB($conn);

?>
<html>
	<head>
		<meta charset="utf-8" />
		<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
		<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
		<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
		<script type="text/javascript">
			function toggleVisibility(elementId, visible)
			{
				document.getElementById(elementId).style.display = visible ? "" : "none";
			}
			function showProgressbar(show)
			{
				toggleVisibility("div_progress", show);
			}

			function updateEventInfoForProduct(uuid)
			{
				var type = document.getElementById(uuid + "type").value;
				var current_price = document.getElementById(uuid + "current_price").value;
				var goodsuuid = uuid;

				if(type=="unknown")
				{
					alert("Please select <event type>!");
					document.getElementById(uuid + "type").focus();
					return;
				}


				if(current_price.length==0)
				{
					alert("Please input <current price>!");
					document.getElementById(uuid + "current_price").focus();
					return;
				}
				showProgressbar(true);
				var xmlhttp;
				if (window.XMLHttpRequest)
				{
					xmlhttp=new XMLHttpRequest();
				}
				else
				{// code for IE6, IE5
					xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
				}
				xmlhttp.onreadystatechange=function()
				{
					if (xmlhttp.readyState==4 && xmlhttp.status==200)
					{
						showProgressbar(false);//xmlhttp.responseText;//
						document.getElementById("tip").innerHTML="Event info has been updated!";
						//document.getElementById("tip").innerHTML=xmlhttp.responseText;
					}
				}
				xmlhttp.open("POST","../index.php?op=UpdateProductEventsInfo", true);
				xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
				xmlhttp.send(
					"current_price=" + current_price
					+ "&type=" + type
					+ "&goodsuuid=" + goodsuuid);
			}
		</script>
	</head>
	<body>

<img id="div_progress" style="display:none" src="../../images/progressbar.gif" width="150px" height="30px">
<div id="tip"></div>
		<div id="div_products" style="display:none">

		</div>

<table border=0px>
<?php

$index = 0;
$optionsMap = getEventTypeStrMap();
foreach ($goodsList as $goods)
{
	if ($index == 0)
	{
		echo "<tr>";
	}
	$index++;
	$uuid = $goods['uuid'];
	echo "<td>"
	. "<img src='../../data/thumbs/" . $uuid . ".png'>"
	. "<br>" . $goods['name']
	. "<br>" . $goods['brand'] . "/" . $goods['model']
	. "<br>Type: 
<select id='" . $uuid . "type'>"
. makeOptionHtmlString($optionsMap, $goods['event_type'])
."</select>"
	. "<br>Original Price: " . $goods['original_price']
	. "<br>Current Price: <input type='text' id='" . $uuid . "current_price' value='"
 . (empty($goods['current_price']) ? $goods['original_price'] : $goods['current_price'])
 ."' style='width:50px'/>"
	. "<br><button type='button' onclick=\"updateEventInfoForProduct('" . $uuid . "')\">Update</button>"
	. "</td>";
	if ($index >= 4)
	{
		echo "</tr>";
		$index = 0;
	}
}
?>
</tr>
</table>
	</body>
</html>