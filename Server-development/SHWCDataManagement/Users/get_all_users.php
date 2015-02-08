
<?php
 
/*
 * Following code will get all users details
 */

// array for JSON response
$response = array();

// import database connection variables
require_once __DIR__ . '/../db_config.php';


try{
        $userFound=0;
        
        // get a product from products table

        // Connecting to mysql database

        $bdd = new PDO(DB_DNS, DB_USER, DB_PASSWORD , array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION ));
        $query = $bdd->query("SELECT users.id,users.login,users.type,users.box_id FROM users");
        
        // check for empty result
        $response["users_info"] = array();
        $query1 = $bdd->prepare('SELECT *  
                                FROM box
                                where id=:box_id');     

        $query2 = $bdd->prepare("SELECT device_id FROM android_devices WHERE user_id=:user_id");

        while($result = $query->fetch())
        {
            $user_info = array();
            $nbr=0;
            $user_info["id"] = $result['id'];
            $user_info["login"] = $result['login'];
            $user_info["type"] = $result['type'];
            $user_info["box_id"] = $result['box_id'];

            $user_info["devices_id"]=array();
            
            $query2->bindValue('user_id',$user_info["id"],PDO::PARAM_INT);
            $query2->execute(); 
            $device_id=array();
            while($result1=$query2->fetch())
            {
                // push single device id into devices_id parameter
                
                $device_id["device_id"]=$result1["device_id"];
                array_push($user_info["devices_id"], $device_id);
            }
            


            $query1->bindValue('box_id',$user_info["box_id"],PDO::PARAM_INT);
            $query1->execute(); 

            // looping through all results
            // profiles node
            $user_info["box"]=array();
            $OneBox=array();
            while($result2 = $query1->fetch())
            {
                // temp user array

                
                $OneBox["id"]=$result2["id"];
                $OneBox["name"]=$result2["name"];
                $OneBox["login"]=$result2["login"];
                $OneBox["pwd"]=$result2["pwd"];
                $OneBox["url"]=$result2["url"];

                $nbr++;                

            }  
            // push single box into box parameter
            array_push( $user_info["box"], $OneBox);

               
            // user node
            array_push($response["users_info"], $user_info);  
            $userFound++;
        }

        if ($userFound) 
        {
            // success
            $response["success"] = 1;

            // echoing JSON response
            echo json_encode($response);
        
        }else{
            // no user found
            $response["success"] = 0;
            $response["message"] = "No user found";
     
            // echo no users JSON
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