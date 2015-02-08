<?php
 
/*
 * Following code will create a new beacon row
 * All beacon details are read from HTTP Post Request
 */
 
// array for JSON response
$response = array();

Try{
    // check for required fields
    if (isset($_POST['MAC']) && isset($_POST['NAME']) && isset($_POST['ROOMID'])) 
    {
        // import database connection variables
        require_once __DIR__ . '/../db_config.php';

        $MAC = $_POST['MAC'];
        $name = $_POST['NAME'];
        $roomID = $_POST['ROOMID'];

        // get all beacons from beacons table
        $bdd = new PDO(DB_DNS, DB_USER, DB_PASSWORD , array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION ));
        $query = $bdd->prepare("INSERT INTO beacons(mac, name, room_id) VALUES(:mac, :name, :roomID)"); 
        $query->bindValue('mac',$MAC,PDO::PARAM_STR);
        $query->bindValue('name',$name,PDO::PARAM_STR);
        $query->bindValue('roomID',$roomID,PDO::PARAM_INT);   

        // check if row inserted or not
        if ($query->execute()) 
        {
            // successfully inserted into database
            $response["success"] = 1;
            $response["message"] = "Beacon $MAC successfully created.";
     
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
