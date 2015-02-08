<?php
 
/*
 * Following code will update a beacon information
 * A beacon is identified by beacon id (UUID)
 */
 
// array for JSON response
$response = array();
 
// import database connection variables
require_once __DIR__ . '/../db_config.php';

Try{ 
    // check for required fields
    if (isset($_POST['ID']) && isset($_POST['NAME']) && isset($_POST['TYPE']) && isset($_POST['ROOMID'])) 
    {
        $id = $_POST['ID'];
        $name = $_POST['NAME'];
        $roomID = $_POST['ROOMID'];
        $type = $_POST['TYPE'];
     
        // set a device
        $bdd = new PDO(DB_DNS, DB_USER, DB_PASSWORD , array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION ));
        $query = $bdd->prepare("UPDATE devices SET id = :id, name = :name, type=:type, room_id = :roomID WHERE id = :id"); 
        $query->bindValue('id',$id,PDO::PARAM_STR);
        $query->bindValue('name',$name,PDO::PARAM_STR);
        $query->bindValue('type',$type,PDO::PARAM_INT);
        $query->bindValue('roomID',$roomID,PDO::PARAM_INT);

        // check if row updated or not
        if ($query->execute()) 
        {
            // successfully inserted into database
            $response["success"] = 1;
            $response["message"] = "Device $id successfully updated.";
     
            // echoing JSON response
            echo json_encode($response);
        } else {
            // failed to update row
            $response["success"] = 0;
            $response["message"] = "Oops! An error occurred.";
     
            // echoing JSON response
            echo json_encode($response);
        }
    }else{
        // required field is missing
        $response["success"] = 0;
        $response["message"] = "Required field(s) is missing";
     
        // echoing JSON response
        echo json_encode($response);
    }
}catch(Exception $e)
{
    // required field is missing
    $response["success"] = 0;
    $response["message"] = $e->getMessage();
 
    // echoing JSON response
    echo json_encode($response);     
}
?>