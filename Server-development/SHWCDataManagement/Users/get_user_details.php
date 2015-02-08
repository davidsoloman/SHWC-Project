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
    if (isset($_GET["login"])) {
            $login = $_GET["login"];
            $userFound=0;
            $nbr=0;
            // get a product from products table

            // Connecting to mysql database

            $bdd = new PDO(DB_DNS, DB_USER, DB_PASSWORD , array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION ));
            $query = $bdd->prepare("SELECT id,login,type FROM users WHERE login=:login");
            $query->bindValue('login',$login,PDO::PARAM_STR);
            $query->execute();
            
            $query2 = $bdd->prepare("SELECT device_id FROM android_devices WHERE user_id=:user_id");
            // check for empty result
            
            $user_info = array();
            while($result = $query->fetch())
            {
                $user_info["id"] = $result['id'];
                $user_info["login"] = $result['login'];
                $user_info["type"] = $result['type'];

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
                
                $userFound++;
            }

            if ($userFound) 
            {
                // success
                $response["success"] = 1;

                // user node
                $response["user_info"] = array();

                array_push($response["user_info"], $user_info);

                $query1 = $bdd->prepare('SELECT box.name as bname, box.login as blogin, box.pwd as bpwd, box.url as burl,profiles.id as pid, box.id as bid FROM profiles
                                        INNER JOIN box
                                        ON profiles.box_id = box.id
                                        where profiles.user_id=:user_id');
                $query1->bindValue('user_id',$user_info["id"],PDO::PARAM_INT);
                $query1->execute(); 

                

                // looping through all results
                // profiles node
                $response["profiles"] = array();
             
                while($result = $query1->fetch())
                {
                    // temp user array
                    $profile = array();
                    $profile["id"] = $result["pid"];
                    $profile["box"]=array();

                    $OneBox=array();
                    $OneBox["id"]=$result["bid"];
                    $OneBox["name"]=$result["bname"];
                    $OneBox["login"]=$result["blogin"];
                    $OneBox["pwd"]=$result["bpwd"];
                    $OneBox["url"]=$result["burl"];

                    // push single box into box parameter
                    array_push($profile["box"], $OneBox);

             
                    // push single profile into final response array
                    array_push($response["profiles"], $profile);

                    $nbr++;
                }                

                if($nbr>0)
                {
                    $response["numb_profile"]=$nbr;
                }else
                {
                    $response["numb_profile"]=0;
                }                

                // echoing JSON response
                echo json_encode($response);
            
            }else{
                // no user found
                $response["success"] = 0;
                $response["message"] = "No user found";
         
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