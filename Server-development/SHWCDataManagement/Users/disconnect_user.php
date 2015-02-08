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
    if (isset($_POST['ANDROID_ID'])) 
    {
        $android_id = $_POST['ANDROID_ID'];
        
        Try
        {
             // set a device
            $bdd = new PDO(DB_DNS, DB_USER, DB_PASSWORD , array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION ));

            $query = $bdd->prepare("DELETE FROM android_devices WHERE device_id = :android_id"); 
            $query->bindValue('android_id',$android_id,PDO::PARAM_STR);
            $query->execute(); 
        
            
            // successfully disconnected database
            $response["success"] = 1;
            $response["message"] = "Device $android_id successfully disconnected.";
     
            // echoing JSON response
            echo json_encode($response);                             
        }catch(Exception $e)
        {
            // required field is missing
            $response["success"] = 0;
            $response["message"] = $e->getMessage();
         
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