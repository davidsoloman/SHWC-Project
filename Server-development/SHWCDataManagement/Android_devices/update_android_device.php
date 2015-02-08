<?php
 
/*
 * Following code will update a beacon information
 * A beacon is identified by beacon id (UUID)
 */
 
// array for JSON response
$response = array();
 


Try{ 
    // check for required fields
    if (isset($_POST['DEVICE_ID']) && isset($_POST['USER_ID'])) 
    {
        // import database connection variables
        require_once __DIR__ . '/../db_config.php';
        
        $device_id = $_POST['DEVICE_ID'];
        $user_id = $_POST['USER_ID'];
     
        // get all beacons from beacons table
        $bdd = new PDO(DB_DNS, DB_USER, DB_PASSWORD , array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION ));
        $query = $bdd->prepare("UPDATE beacons SET user_id = :user_id WHERE device_id = :device_id"); 
        $query->bindValue('user_id',$user_id,PDO::PARAM_INT);
        $query->bindValue('device_id',$device_id,PDO::PARAM_STR);      

        // check if row updated or not
        if ($query->execute()) 
        {
            // successfully inserted into database
            $response["success"] = 1;
            $response["message"] = "Android device $device_id successfully updated.";
     
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