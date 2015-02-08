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
    if (isset($_POST["LOGIN"]) && isset($_POST["PWD"]) && isset($_POST["ANDROID_ID"]) && isset($_POST["BOX"])) {
            $login = $_POST["LOGIN"];
            $pwd = $_POST["PWD"];
            $android_id = $_POST["ANDROID_ID"];
            $box=$_POST["BOX"];
            $logOk=0;
            $nbr=0;
            // get a product from products table

            // Connecting to mysql database

            $bdd = new PDO(DB_DNS, DB_USER, DB_PASSWORD , array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION ));
            $query = $bdd->prepare("SELECT * FROM users WHERE login=:login and pwd=:pwd");
            $query->bindValue('login',$login,PDO::PARAM_STR);
            $query->bindValue('pwd',$pwd,PDO::PARAM_STR);
            $query->execute();
            
            // check for empty result
            
            $user_info = array();
            while($result = $query->fetch())
            {
                $user_info["id"] = $result['id'];
                $user_info["login"] = $result['login'];
                $user_info["type"] = $result['type'];

                $logOk++;
            }       

            if ($logOk) 
            {
                $query2=$bdd->prepare("SELECT * from box WHERE name=:name");
                $query2->bindValue('name',$box,PDO::PARAM_STR);

                $query2->execute();
                $user_info["box"]=array();
                while($result2=$query2->fetch())
                {
     
                    $temp=array();
                    $temp["id"]=$result2["id"];
                    $temp["name"]=$result2["name"];               
                    $temp["login"]=$result2["login"];
                    $temp["pwd"]=$result2["pwd"];
                    $temp["url"]=$result2["url"]; 

                    array_push($user_info["box"], $temp);                         
                }                
                // success
                $response["success"] = 1;

                // user node
                $response["user_info"] = array();

                array_push($response["user_info"], $user_info);


                // save android device
                $query = $bdd->prepare("DELETE FROM android_devices WHERE device_id = :android_id"); 
                $query->bindValue('android_id',$android_id,PDO::PARAM_STR);
                $query->execute(); 

                $query1 = $bdd->prepare("INSERT INTO android_devices (device_id, user_id) VALUES (:android_id, :user_id)"); 
                $query1->bindValue('android_id',$android_id,PDO::PARAM_STR);
                $query1->bindValue('user_id',$user_info["id"],PDO::PARAM_INT);  
                $query1->execute();

                $query3 = $bdd->prepare("UPDATE users SET box_id=:box_id WHERE id=:user_id"); 
                $query3->bindValue('box_id',$temp["id"],PDO::PARAM_INT);
                $query3->bindValue('user_id',$user_info["id"],PDO::PARAM_INT);  
                $query3->execute();     

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