<?php

/*
 * Following code will create a new device row
 * All device details are read from HTTP Post Request
 */
 
// array for JSON response

$response = array();

Try{
    // check for required fields
    if (isset($_POST['NAME']) && isset($_POST['AREA']))  
    {
        // import database connection variables
        require_once __DIR__ . '/../db_config.php';

        $name = $_POST['NAME'];
        $area = $_POST['AREA'];

        // insert room in rooms table
        $bdd = new PDO(DB_DNS, DB_USER, DB_PASSWORD , array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION ));
        $query = $bdd->prepare("INSERT INTO rooms (name, numb_users,area) VALUES (:name, 0,:area)"); 
        $query->bindValue('area',$area,PDO::PARAM_INT);
        $query->bindValue('name',$name,PDO::PARAM_STR);
        
        // check if row inserted or not
        if ($query->execute()) 
        {
            // successfully inserted into database
            $response["success"] = 1;
            $response["message"] = "Room $name successfully created.";
     
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
