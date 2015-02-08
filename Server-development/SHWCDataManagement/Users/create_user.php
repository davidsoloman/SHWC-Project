<?php

/*
 * Following code will create a new user
 * All user details are read from HTTP Post Request
 */
 
// array for JSON response

$response = array();

Try{
    // check for required fields
    if (isset($_POST['LOGIN']) && isset($_POST['PWD']) && isset($_POST['TYPE']))  
    {
        // import database connection variables
        require_once __DIR__ . '/../db_config.php';

        $login = $_POST['LOGIN'];
        $pwd = $_POST['PWD'];
        $type = $_POST['TYPE'];

        // create a user
        $bdd = new PDO(DB_DNS, DB_USER, DB_PASSWORD , array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION ));
        $query = $bdd->prepare("INSERT INTO users (login, pwd, type) VALUES (:login, :pwd, :type)"); 
        $query->bindValue('login',$login,PDO::PARAM_STR);
        $query->bindValue('pwd',$pwd,PDO::PARAM_STR);
        $query->bindValue('type',$type,PDO::PARAM_INT);
        
        // check if row inserted or not
        if ($query->execute()) 
        {
            // successfully inserted into database
            $response["success"] = 1;
            $response["message"] = "User $login successfully created.";
     
            // echoing JSON response
            echo json_encode($response);
        } else {
            // failed to insert row
            $response["success"] = 0;
            $response["message"] = "Oops! An error occurred.";
     
            // echoing JSON response
            echo json_encode($response);
        }
    }else {
        // required field is missing
        $response["success"] = 0;
        $response["message"] = "Required field(s) is missing";
     
        // echoing JSON response
        echo json_encode($response);
    }
}catch(Exception $e)
{
    // required field is missing
    $response["error"] = 0;
    $response["message"] = $e->getMessage();
 
    // echoing JSON response
    echo json_encode($response);  
}

?>
