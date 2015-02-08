
<?php
 
/*
 * Following code will list all the android devices
 */

// variable used to count rows
$nbr=0;

// array for JSON response
$response = array();


Try{

    // import database connection variables
    require_once __DIR__ . '/../db_config.php';

    // get all beacons from android_devices table
    $bdd = new PDO(DB_DNS, DB_USER, DB_PASSWORD , array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION ));
    $query = $bdd->query('SELECT * FROM android_devices');

     

        // looping through all results
        // android node
        $response["android_devices"] = array();
     
        while($result = $query->fetch())
        {
            // temp user array
            $android_device = array();
            $android_device["device_id"] = $result["device_id"];
            $android_device["user_id"] = $result["user_id"];
         
     
            // push single android device into final response array
            array_push($response["android_devices"], $android_device);

            $nbr++;
        }


    // check for empty result
    if ($nbr){

        // success
        $response["success"] = 1;
     
        // echoing JSON response
        echo json_encode($response);

    } else {
        // no device found
        $response["success"] = 0;
        $response["message"] = "No device found";
     
        // echo no device JSON
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