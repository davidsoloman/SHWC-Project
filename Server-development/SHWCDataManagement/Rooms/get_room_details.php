<?php
 
/*
 * Following code will get single room details
 * A room is identified by room id (id)
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
            // get a room from rooms table

            // Connecting to mysql database

            $bdd = new PDO(DB_DNS, DB_USER, DB_PASSWORD , array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION ));
            $query = $bdd->prepare("SELECT * FROM rooms WHERE id=:id");
            $query->bindValue('id',$id,PDO::PARAM_STR);
            $query->execute();
            
            // check for empty result
            
            $room = array();
            while($result = $query->fetch())
            {
                $room["id"] = $result['id'];
                $room["name"] = $result['name'];
                $room["numb_users"] = $result['numb_users'];
                $nbr++;
            }

            if ($nbr) 
            {
                // success
                $response["success"] = 1;

                // room node
                $response["room"] = array();

                array_push($response["room"], $room);

                // echoing JSON response
                echo json_encode($response);
            
            }else{
                // no room found
                $response["success"] = 0;
                $response["message"] = "No room found";
         
                // echo no room JSON
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