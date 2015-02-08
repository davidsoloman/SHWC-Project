<?php
 
/*
 * Following code will get single product details
 * A product is identified by product id (pid)
 */

// array for JSON response
$response = array();

// import database connection variables
require_once __DIR__ . '/../db_config.php';


try{
    // check for post data
    if (isset($_GET["MAC"])) {
            $MAC = $_GET['MAC'];
            $nbr=0;
            // get a product from products table

            // Connecting to mysql database

            $bdd = new PDO(DB_DNS, DB_USER, DB_PASSWORD , array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION ));
            $query = $bdd->prepare("SELECT * FROM beacons WHERE mac=:mac");
            $query->bindValue('mac',$MAC,PDO::PARAM_STR);
            $query->execute();
            // check for empty result
            
            $beacon = array();
            while($result = $query->fetch())
            {
                $beacon["mac"] = $result['mac'];
                $beacon["name"] = $result['name'];
                $beacon["room_id"] = $result['room_id'];
                $nbr++;
            }

            if ($nbr) 
            {
                // success
                $response["success"] = 1;

                // user node
                $response["beacon"] = array();

                array_push($response["beacon"], $beacon);

                // echoing JSON response
                echo json_encode($response);
            
            } else {
                // no beacon found
                $response["success"] = 0;
                $response["message"] = "No beacon found";
         
                // echo no beacon JSON
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