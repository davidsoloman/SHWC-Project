<?php

/*
 * Following code will create a new device row
 * All device details are read from HTTP Post Request
 */
 
// array for JSON response

$response = array();

Try{
    // check for required fields
    if (isset($_POST['ID']) && isset($_POST['NAME']) && isset($_POST['LOGIN']) && isset($_POST['PWD']) && isset($_POST['URL']))  
    {
        // import database connection variables
        require_once __DIR__ . '/../db_config.php';

        $id = $_POST['ID'];
        $name = $_POST['NAME'];
        $login= $_POST['LOGIN'];
        $pwd = $_POST['PWD'];
        $url = $_POST['URL'];


        // get all devices from beacons table
        $bdd = new PDO(DB_DNS, DB_USER, DB_PASSWORD , array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION ));
        $query = $bdd->prepare("INSERT INTO box (id, name, login, pwd, url) VALUES (:id, :name, :login, :pwd, :url)"); 
        $query->bindValue('id',$id,PDO::PARAM_INT);
        $query->bindValue('name',$name,PDO::PARAM_STR);
        $query->bindValue('login',$login,PDO::PARAM_STR);
        $query->bindValue('pwd',$pwd,PDO::PARAM_STR);
        $query->bindValue('url',$url,PDO::PARAM_STR);
        
        // check if row inserted or not
        if ($query->execute()) 
        {
            // successfully inserted into database
            $response["success"] = 1;
            $response["message"] = "Box $id successfully created.";
     
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
