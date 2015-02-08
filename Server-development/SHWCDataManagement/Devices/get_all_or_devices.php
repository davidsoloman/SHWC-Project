
<?php
 
/*
 * Following code will list all the products
 */

// variable used to count rows
$nbr=0;

// array for JSON response
$response = array();


Try{

    // import database connection variables
    require_once __DIR__ . '/../db_config.php';

    // get all devices from devices table
    $bdd = new PDO(DB_DNS, DB_USER, DB_PASSWORD , array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION ));
    $query = $bdd->query('SELECT * FROM devices where room_id IS NULL');

     

        // looping through all results
        // devices node
        $response["devices"] = array();
     
        while($result = $query->fetch())
        {
            // temp user array
            $device = array();
            $device["id"] = $result["id"];
            $device["name"] = $result["name"];
            $device["type"] = $result["type"];
            $device["room_id"] = $result["room_id"];
     
            // push single device into final response array
            array_push($response["devices"], $device);

            $nbr++;
        }


    // check for empty result
    if ($nbr){

        // success
        $response["success"] = 1;
     
        // echoing JSON response
        echo json_encode($response);

    } else {
        // no devices found
        $response["success"] = 0;
        $response["message"] = "No devices found";
     
        // echo no devices JSON
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