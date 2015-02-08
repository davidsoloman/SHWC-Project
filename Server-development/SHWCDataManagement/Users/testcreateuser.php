<!DOCTYPE html>
<html>
	<head>
	<meta charset="UTF-8">
	<title>Title of the document</title>
	</head>

	<body>
		<form action="create_user.php" method="POST">
			<fieldset>
				<legend>Test de création d'un utilisateur :</legend>
				<br>
				Login:<br>
				<input type="text" name="LOGIN"><br>
				Password:<br>
				<input type="password" name="PWD"><br>
				<select name="TYPE" > 
				   <option value="0">Utilisateur</option> 
				   <option value="1">Admin</option> 
				</select> 
				<input type="submit" Value="Submit">	
			</fieldset>		
		</form>
	</body>

</html>