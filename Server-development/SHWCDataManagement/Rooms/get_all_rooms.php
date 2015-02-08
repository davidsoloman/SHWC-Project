
<?php
 
/*
 * Following code will list all the rooms
 */

// variable used to count rows
$nbr=0;

// array for JSON response
$response = array();


Try{

    // import database connection variables
    require_once __DIR__ . '/../db_config.php';

    // get all rooms from rooms table
    $bdd = new PDO(DB_DNS, DB_USER, DB_PASSWORD , array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION ));
    $query = $bdd->query('SELECT * FROM rooms');

    $query1 = $bdd->prepare('SELECT * FROM beacons
                            where room_id=:room_id');     

    $query2 = $bdd->prepare("SELECT * FROM devices WHERE room_id=:room_id");

     

        // looping through all results
        // rooms node
        $response["rooms"] = array();
     
        while($result = $query->fetch())
        {
            // temp user array
            $room = array();
            $room["id"] = $result["id"];
            $room["name"] = $result["name"];
            $room["numb_users"] = $result["numb_users"];
            $room["area"]=$result["area"];
            $room["beacons"] = array();
            $room["devices"] = array();

            $query1->bindValue('room_id',$room["id"],PDO::PARAM_INT);
            $query1->execute(); 
            $beacon=array();
            while($result1=$query1->fetch())
            {
                // push single device id into devices_id parameter
                
                $beacon["mac"]=$result1["mac"];
                $beacon["name"]=$result1["name"];
                $beacon["radius"]=$result1["radius"];
                array_push($room["beacons"], $beacon);
            }

            $query2->bindValue('room_id',$room["id"],PDO::PARAM_INT);
            $query2->execute(); 
            $device=array();
            while($result2=$query2->fetch())
            {
                // push single device id into devices_id parameter
                
                $device["id"]=$result2["id"];
                $device["name"]=$result2["name"];
                $device["type"]=$result2["type"];
                array_push($room["devices"], $device);
            }            

            // push single room into final response array
            array_push($response["rooms"], $room);

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
        $response["message"] = "No room found";
     
        // echo no room JSON
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