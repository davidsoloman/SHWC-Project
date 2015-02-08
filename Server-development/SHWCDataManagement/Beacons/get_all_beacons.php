
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

    // get all beacons from beacons table
    $bdd = new PDO(DB_DNS, DB_USER, DB_PASSWORD , array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION ));
    $query = $bdd->query('SELECT * FROM beacons');

     

        // looping through all results
        // beacons node
        $response["beacons"] = array();
     
        while($result = $query->fetch())
        {
            // temp user array
            $beacon = array();
            $beacon["mac"] = $result["mac"];
            $beacon["name"] = $result["name"];
            $beacon["room_id"] = $result["room_id"];
     
            // push single beacons into final response array
            array_push($response["beacons"], $beacon);

            $nbr++;
        }


    // check for empty result
    if ($nbr){

        // success
        $response["success"] = 1;
     
        // echoing JSON response
        echo json_encode($response);

    } else {
        // no beacons found
        $response["success"] = 0;
        $response["message"] = "No beacons found";
     
        // echo no beacons JSON
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