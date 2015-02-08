
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

    // get all box from box table
    $bdd = new PDO(DB_DNS, DB_USER, DB_PASSWORD , array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION ));
    $query = $bdd->query('SELECT * FROM box');

     

        // looping through all results
        // box  node
        $response["box"] = array();
     
        while($result = $query->fetch())
        {
            // temp user array
            $box = array();
            $box["id"] = $result["id"];
            $box["name"] = $result["name"];
            $box["login"] = $result["login"];
            $box["pwd"] = $result["pwd"];
            $box["url"] = $result["url"];
     
            // push single device into final response array
            array_push($response["box"], $box);

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