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
		var cur_type = "common";
			function toggleVisibilityById(elementId, visible)
			{
				toggleVisibility(document.getElementById(elementId), visible);
			}
			function toggleVisibility(element, visible)
			{
				element.style.display = visible ? "" : "none";
			}
			function setValText(element, text)
			{
				element.value = text;
			}
			function setKeyText(element, text)
			{
				element.innerHTML = text;
			}
			function showProgressbar(show)
			{
				toggleVisibilityById("div_progress", show);
			}
			function updateUIForType()
			{
				var type = document.getElementById("type").value;

				if (cur_type === type)
				{
					return;
				}
				cur_type = type;
				var param1 = document.getElementById("param1");
				var param2 = document.getElementById("param2");
				var param3 = document.getElementById("param3");
				var param4 = document.getElementById("param4");
				var param5 = document.getElementById("param5");
				var param6 = document.getElementById("param6");
				
				var param1Key = document.getElementById("param1_key");
				var param1Val = document.getElementById("param1_val");

				var param2Key = document.getElementById("param2_key");
				var param2Val = document.getElementById("param2_val");
				
				var param3Key = document.getElementById("param3_key");
				var param3Val = document.getElementById("param3_val");
				
				var param4Key = document.getElementById("param4_key");
				var param4Val = document.getElementById("param4_val");
				
				var param5Key = document.getElementById("param5_key");
				var param5Val = document.getElementById("param5_val");
				
				var param6Key = document.getElementById("param6_key");
				var param6Val = document.getElementById("param6_val");

				switch(type)
				{
					case "common":
						toggleVisibility(param1, true);
						toggleVisibility(param2, true);
						toggleVisibility(param3, true);
						toggleVisibility(param4, false);
						toggleVisibility(param5, false);
						toggleVisibility(param6, false);
						
						setKeyText(param1Key, "Message:");
						setValText(param1Val, "This is a common message.");
						
						setKeyText(param2Key, "Image URL:");
						setValText(param2Val, "http://www.intalker.com/xshop/api/data/test/test.jpg");
						
						setKeyText(param3Key, "City:");
						setValText(param3Val, "Shanghai");
					break;
					case "single_product":
						toggleVisibility(param1, true);
						toggleVisibility(param2, true);
						toggleVisibility(param3, true);
						toggleVisibility(param4, true);
						toggleVisibility(param5, true);
						toggleVisibility(param6, false);
						
						setKeyText(param1Key, "Message:");
						setValText(param1Val, "This is a message for single product.");
						
						setKeyText(param2Key, "Product ID:");
						setValText(param2Val, "a0a972fe-66b9-04a4-f6c8-c7744b92e1bd");
						
						setKeyText(param3Key, "Original Price:");
						setValText(param3Val, "3800");
						
						setKeyText(param4Key, "Current Price:");
						setValText(param4Val, "3040");
						
						setKeyText(param5Key, "City:");
						setValText(param5Val, "Shanghai");
					break;
					case "group_products":
						toggleVisibility(param1, true);
						toggleVisibility(param2, true);
						toggleVisibility(param3, true);
						toggleVisibility(param4, true);
						toggleVisibility(param5, true);
						toggleVisibility(param6, false);
						
						setKeyText(param1Key, "Message:");
						setValText(param1Val, "This is a message for group products.");
						
						setKeyText(param2Key, "Product List:");
						setValText(param2Val, "a0a972fe-66b9-04a4-f6c8-c7744b92e1bd,1;33735d19-1910-653d-cfb7-375919f38dd0,1;5c3090bb-cac9-b556-bb47-2137ab1e6f01,4");
						
						setKeyText(param3Key, "Original Price:");
						setValText(param3Val, "10000");
						
						setKeyText(param4Key, "Current Price:");
						setValText(param4Val, "7500");
						
						setKeyText(param5Key, "City:");
						setValText(param5Val, "Shanghai");
					break;
					case "to_one_user":
						toggleVisibility(param1, true);
						toggleVisibility(param2, true);
						toggleVisibility(param3, true);
						toggleVisibility(param4, true);
						toggleVisibility(param5, true);
						toggleVisibility(param6, true);
						
						setKeyText(param1Key, "Message:");
						setValText(param1Val, "This is a message to a special user.");
						
						setKeyText(param2Key, "Product ID:");
						setValText(param2Val, "a0a972fe-66b9-04a4-f6c8-c7744b92e1bd");
						
						setKeyText(param3Key, "Original Price:");
						setValText(param3Val, "3800");
						
						setKeyText(param4Key, "Current Price:");
						setValText(param4Val, "3040");
						
						setKeyText(param5Key, "City:");
						setValText(param5Val, "Shanghai");
						
						setKeyText(param6Key, "Device ID:");
						setValText(param6Val, ">Input device serial number here.<");
					break;
					default:
					break;
				}
				document.getElementById("tip").innerHTML="";
			}
			function getValueById(elementId)
			{
				return document.getElementById(elementId).value;
			}
			function validate(elementId)
			{
				var val = getValueById(elementId);
				if(val.length==0)
				{
					alert("Please input <" + elementId + ">!");
					document.getElementById(elementId).focus();
					return false;
				}
				return true;
			}
			function sendOut()
			{
				var version = getValueById("version");
				var sender = getValueById("sender");
				var type = getValueById("type");
				
				var param1 = getValueById("param1_val");
				var param2 = getValueById("param2_val");
				var param3 = getValueById("param3_val");
				var param4 = getValueById("param4_val");
				var param5 = getValueById("param5_val");
				var param6 = getValueById("param6_val");

				/*
				if(!validate("version"))
				{
					return;
				}
				if(!validate("sender"))
				{
					return;
				}
				if(!validate("type"))
				{
					return;
				}
				if(!validate("param1_val"))
				{
					return;
				}
				if(!validate("param2_val"))
				{
					return;
				}
				if(!validate("param3_val"))
				{
					return;
				}
				if(!validate("param4_val"))
				{
					return;
				}
				if(!validate("param5_val"))
				{
					return;
				}
				if(!validate("param6_val"))
				{
					return;
				}
				*/
				var pushStr = "";
				switch(type)
				{
					case "common":
						pushStr = version + "|" + sender + "|" + type + "|" + param1 + "|" + param2 + "|" + param3;
					break;
					case "single_product":
						pushStr = version + "|" + sender + "|" + type + "|" + param1 + "|" + param2 + "|" + param3 + "|" + param4 + "|" + param5;
					break;
					case "group_products":
						pushStr = version + "|" + sender + "|" + type + "|" + param1 + "|" + param2 + "|" + param3 + "|" + param4 + "|" + param5;
					break;
					case "to_one_user":
						pushStr = version + "|" + sender + "|" + type + "|" + param1 + "|" + param2 + "|" + param3 + "|" + param4 + "|" + param5 + "|" + param6;
					break;
					default:
					break;
				}
				showProgressbar(true);
				document.getElementById("tip").innerHTML="";
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
						document.getElementById("tip").innerHTML="<hr>Notification data:<br><span style='background-color:#FFFF00;'>" + pushStr + "</span><br>Has been sent out!<hr>" + "message return from getui:<br>" + xmlhttp.responseText;
					}
				}
				xmlhttp.open("POST","../admin/push_v2.php", true);
				xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
				xmlhttp.send("data=" + pushStr);
			}
		</script>
	</head>
	<body>

		<table border=0px>
			<tr>
				<td>Version:</td>
				<td><input type="text" id="version" value="1.0" readonly = "readonly"></td>
			</tr>
			<tr>
				<td>Sender:</td>
				<td><input type="text" id="sender" value="xshop_dev"></td>
			</tr>
			<tr>
				<td>Type:</td>
				<td>
					<select id="type" onclick="updateUIForType()">
						<option value="common" selected="selected">Common</option>
						<option value="single_product">Single product</option>
						<option value="group_products">Group products</option>
						<option value="to_one_user">To one user</option>
					</select>
				</td>
			</tr>
			<tr id="param1">
				<td id="param1_key">Message:</td>
				<td><input type="text" id="param1_val" value="This is a common message."></td>
			</tr>
			<tr id="param2">
				<td id="param2_key">Image URL:</td>
				<td><input type="text" id="param2_val" value="http://www.intalker.com/xshop/api/data/test/test.jpg"></td>
			</tr>
			<tr id="param3">
				<td id="param3_key">City:</td>
				<td><input type="text" id="param3_val" value="Shanghai"></td>
			</tr>
			<tr id="param4" style="display:none">
				<td id="param4_key"></td>
				<td><input type="text" id="param4_val" value=""></td>
			</tr>
			<tr id="param5" style="display:none">
				<td id="param5_key"></td>
				<td><input type="text" id="param5_val" value=""></td>
			</tr>
			<tr id="param6" style="display:none">
				<td id="param6_key"></td>
				<td><input type="text" id="param6_val" value=""></td>
			</tr>
		</table>

		<br>
	
		<button type="button" onclick="sendOut()">Send Out!</button>
	
		<br>
	
		<img id="div_progress" style="display:none" src="../../images/progressbar.gif" width="150px" height="30px">
		<div id="tip"></div>
	</body>
</html>