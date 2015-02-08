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
    if (isset($_GET["NAME"])) {
            $name = $_GET["NAME"];
            $nbr=0;
            // get a box from box table

            // Connecting to mysql database

            $bdd = new PDO(DB_DNS, DB_USER, DB_PASSWORD , array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION ));
            $query = $bdd->prepare("SELECT * FROM box WHERE name=:name");
            $query->bindValue('name',$name,PDO::PARAM_STR);
            $query->execute();
            
            // check for empty result
            
            $box = array();
            while($result = $query->fetch())
            {  
                $box["id"] = $result["id"];
                $box["name"] = $result["name"];
                $box["login"] = $result["login"];
                $box["pwd"] = $result["pwd"];
                $box["url"] = $result["url"];
                $nbr++;
            }

            if ($nbr) 
            {
                // success
                $response["success"] = 1;

                // user node
                $response["box"] = array();

                array_push($response["box"], $box);

                // echoing JSON response
                echo json_encode($response);
            
            }else{
                // no box found
                $response["success"] = 0;
                $response["message"] = "No box found";
         
                // echo no box JSON
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