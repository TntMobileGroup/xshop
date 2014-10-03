<?php

require_once('util.php');



?>


<html>
	<head>
		<title>Add Goods</title>
	</head>
	<body>
		<form action="api.php?op=AddGoods" method="post" enctype="multipart/form-data">
			<table border=0px>
				<tr>
					<td>
						Name:
					</td>
					<td>
						<input type="text" name="name" size=20><br>
					</td>
				</tr>
				<tr>
					<td>
						Category:
					</td>
					<td>
						<input type="text" name="category" size=20><br>
					</td>
				</tr>
				<tr>
					<td>
						Brand:
					</td>
					<td>
						<input type="text" name="brand" size=20><br>
					</td>
				</tr>
				<tr>
					<td>
						Model:
					</td>
					<td>
						<input type="text" name="model" size=20><br>
					</td>
				</tr>
				<tr>
					<td>
						Image:
					</td>
					<td>
						<input type="file" name="preview">
					</td>
				</tr>
				<tr>
					<td>
						Description:
					</td>
					<td>
						<textarea rows="5" cols="20" name="description" size=20 maxlength=255></textarea>
					</td>
				</tr>
			</table>
			<input type="submit" value="Submit" />
		</form>
	</body>
</html>