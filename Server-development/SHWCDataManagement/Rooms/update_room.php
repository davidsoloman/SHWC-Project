<?php
 
/*
 * Following code will update a room information
 * A room is identified by room id (id)
 */
 
// array for JSON response
$response = array();
 
// import database connection variables
require_once __DIR__ . '/../db_config.php';

Try{ 
    // check for required fields
    if (isset($_POST['ID']) && isset($_POST['NAME']) && isset($_POST['NUMB_USERS'])) 
    {
        $id = $_POST['ID'];
        $name = $_POST['NAME'];
        $numb_users = $_POST['NUMB_USERS'];
     
        // set a room
        $bdd = new PDO(DB_DNS, DB_USER, DB_PASSWORD , array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION ));
        $query = $bdd->prepare("UPDATE rooms SET id = :id, name = :name, numb_users=:numb_users WHERE id = :id"); 
        $query->bindValue('id',$id,PDO::PARAM_INT);
        $query->bindValue('name',$name,PDO::PARAM_STR);
        $query->bindValue('numb_users',$numb_users,PDO::PARAM_INT);

        // check if row updated or not
        if ($query->execute()) 
        {
            // successfully inserted into database
            $response["success"] = 1;
            $response["message"] = "Room $id successfully updated.";
     
            // echoing JSON response
            echo json_encode($response);
        } else {
            // failed to update row
            $response["success"] = 0;
            $response["message"] = "Oops! An error occurred.";
     
            // echoing JSON response
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