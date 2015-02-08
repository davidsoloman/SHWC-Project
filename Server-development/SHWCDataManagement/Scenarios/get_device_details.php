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
    if (isset($_GET["ID"])) {
            $id = $_GET["ID"];
            $nbr=0;
            // get a product from products table

            // Connecting to mysql database

            $bdd = new PDO(DB_DNS, DB_USER, DB_PASSWORD , array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION ));
            $query = $bdd->prepare("SELECT * FROM devices WHERE id=:id");
            $query->bindValue('id',$id,PDO::PARAM_STR);
            $query->execute();
            
            // check for empty result
            
            $device = array();
            while($result = $query->fetch())
            {
                $device["id"] = $result['id'];
                $device["name"] = $result['name'];
                $device["type"] = $result['type'];
                $device["room_id"] = $result['room_id'];
                $nbr++;
            }

            if ($nbr) 
            {
                // success
                $response["success"] = 1;

                // user node
                $response["device"] = array();

                array_push($response["device"], $device);

                // echoing JSON response
                echo json_encode($response);
            
            }else{
                // no device found
                $response["success"] = 0;
                $response["message"] = "No device found";
         
                // echo no users JSON
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