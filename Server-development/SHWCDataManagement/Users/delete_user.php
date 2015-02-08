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

            try
            {
                $bdd = new PDO(DB_DNS, DB_USER, DB_PASSWORD , array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION ));
                $query = $bdd->prepare("DELETE from users WHERE login=:login");
                $query->bindValue('login',$login,PDO::PARAM_STR);
                $query->execute();

                $response["success"] = 1;
                $response["message"] = "Utilisateur "+$login+" supprimé.";
                // echo user delete JSON
                echo json_encode($response);             

            }catch(Exception $e){
                
                $response["success"] = 0;
                $response["message"] = $e.getMessage();
         
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