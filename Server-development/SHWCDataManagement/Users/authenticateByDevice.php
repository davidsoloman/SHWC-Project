<?php
 
/*
 * Following code is used to authenticate a user to the SHWC Platform
 */

// array for JSON response
$response = array();

// import database connection variables
require_once __DIR__ . '/../db_config.php';


try{
    // check for post data
    if (isset($_POST["DEVICE_ID"])) {
            $device_id = $_POST["DEVICE_ID"];

            $logOk=0;
            $nbr=0;
            // get a product from products table

            // Connecting to mysql database

            $bdd = new PDO(DB_DNS, DB_USER, DB_PASSWORD , array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION ));
            $query = $bdd->prepare("SELECT ad.device_id,ad.user_id,u.login as ulogin,u.type as utype,b.login as blogin,b.pwd as bpwd,b.url as burl,b.name as bname 
                                    FROM android_devices as ad 
                                    INNER JOIN users as u
                                    ON ad.user_id=u.id
                                    INNER JOIN box as b
                                    ON b.id=u.box_id
                                    WHERE ad.device_id=:device_id");
            $query->bindValue('device_id',$device_id,PDO::PARAM_STR);
            $query->execute();
            
            // check for empty result
            
            $user_info = array();
            while($result = $query->fetch())
            {
                $user_info["device_id"] = $result['device_id'];
                $user_info["user_id"] = $result['user_id'];
                $user_info["login"] = $result['ulogin'];
                $user_info["type"] = $result['utype'];
                $user_info["box"]=array();

                $temp=array();
                $temp["name"]=$result["bname"];               
                $temp["login"]=$result["blogin"];
                $temp["pwd"]=$result["bpwd"];
                $temp["url"]=$result["burl"]; 

                array_push($user_info["box"], $temp);
                $logOk++;
            }

            if ($logOk) 
            {
                // success
                $response["success"] = 1;

                // user node
                $response["user_info"] = array();

                array_push($response["user_info"], $user_info);

                // echoing JSON response
                echo json_encode($response);
            
            }else{
                // no device found
                $response["success"] = 0;
                $response["message"] = "Authentification failed !";
         
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