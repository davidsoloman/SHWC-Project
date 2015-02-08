<!DOCTYPE html>
<html>
	<head>
	<meta charset="UTF-8">
	<title>Title of the document</title>
	</head>

	<body>
		<form action="update_user.php" method="POST">
			<fieldset>
				<legend>Test de MAJ d'un utilisateur :</legend>
				<br>
				User ID:<br>
				<input type="text" name="ID"><br>
				Password:<br>
				<input type="password" name="PWD"><br>
				<input type="login" name="LOGIN"><br>
				<!--
				<select name="TYPE" > 
				   <option value="0">Utilisateur</option> 
				   <option value="1">Admin</option> 
				</select> 
				-->
				<input type="submit" Value="Submit">	
			</fieldset>		
		</form>
	</body>

</html>