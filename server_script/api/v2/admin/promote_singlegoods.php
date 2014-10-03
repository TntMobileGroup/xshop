<?php
require_once('../admin/util.php');

$conn = connectDB();
$goodsList = getGoodsByShop();
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

			function pushNotification(uuid)
			{
				var title = document.getElementById(uuid + "title").value;
				var content = document.getElementById(uuid + "content").value;
				var goodsuuid = uuid;//document.getElementById("goodsuuid").value;

				if(title.length==0)
				{
					alert("Please input <Title>!");
					document.getElementById("title").focus();
					return;
				}
				if(content.length==0)
				{
					alert("Please input <Content>!");
					document.getElementById("content").focus();
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
						showProgressbar(false);
						document.getElementById("tip").innerHTML="Notification has been sent!";
					}
				}
				xmlhttp.open("POST","push.php", true);
				xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
				xmlhttp.send(
					"title=" + title
					+ "&content=" + content
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
	. "<br><input type='text' id='" . $uuid . "title' value='Test Title'/>"
	. "<br><input type='text' id='" . $uuid . "content' value='Test Message'/>"
	. "<br><button type='button' onclick=\"pushNotification('" . $uuid . "')\">Push</button>"
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