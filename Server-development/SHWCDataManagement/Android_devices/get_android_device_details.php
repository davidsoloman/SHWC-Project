<?php
 
/*
 * Following code will get single android device
 * A product is identified by android device id (device_id)
 */

// array for JSON response
$response = array();

// import database connection variables
require_once __DIR__ . '/../db_config.php';


try{
    // check for post data
    if (isset($_GET["device_id"])) {
            $device_id = $_GET['device_id'];
            $nbr=0;
            // get a product from android devices table

            // Connecting to mysql database

            $bdd = new PDO(DB_DNS, DB_USER, DB_PASSWORD , array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION ));
            $query = $bdd->prepare("SELECT * FROM android_devices WHERE device_id=:device_id");
            $query->bindValue('device_id',$device_id,PDO::PARAM_STR);
            $query->execute();
            // check for empty result
            
            $android_device = array();
            while($result = $query->fetch())
            {
                $android_device["device_id"] = $result['device_id'];
                $android_device["user_id"] = $result['user_id'];
                $nbr++;
            }

            if ($nbr) 
            {
                // success
                $response["success"] = 1;

                // user node
                $response["android_device"] = array();

                array_push($response["android_device"], $android_device);

                // echoing JSON response
                echo json_encode($response);
            
            } else {
                // no android device found
                $response["success"] = 0;
                $response["message"] = "No device found";
         
                // echo no android device JSON
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