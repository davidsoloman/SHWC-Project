<?php

/*
 * Following code will create a new device row
 * All device details are read from HTTP Post Request
 */
 
// array for JSON response

$response = array();

Try{
    // check for required fields
    if (isset($_POST['ID']) && isset($_POST['NAME']) && isset($_POST['TYPE']) && isset($_POST['ROOMID']))  
    {
        // import database connection variables
        require_once __DIR__ . '/../db_config.php';

        $id = $_POST['ID'];
        $name = $_POST['NAME'];
        $type= $_POST['TYPE'];
        $roomID = $_POST['ROOMID'];

        // get all devices from beacons table
        $bdd = new PDO(DB_DNS, DB_USER, DB_PASSWORD , array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION ));
        $query = $bdd->prepare("INSERT INTO devices (id, name, type, room_id) VALUES (:id, :name, :type, :roomID)"); 
        $query->bindValue('id',$id,PDO::PARAM_STR);
        $query->bindValue('name',$name,PDO::PARAM_STR);
        $query->bindValue('type',$type,PDO::PARAM_INT);
        $query->bindValue('roomID',$roomID,PDO::PARAM_INT);
        
        // check if row inserted or not
        if ($query->execute()) 
        {
            // successfully inserted into database
            $response["success"] = 1;
            $response["message"] = "Device $id successfully created.";
     
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
