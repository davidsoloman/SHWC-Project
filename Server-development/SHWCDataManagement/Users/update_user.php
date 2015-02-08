<?php
 
/*
 * Following code will update a beacon information
 * A beacon is identified by beacon id (UUID)
 */
 
// array for JSON response
$response = array();
 
// import database connection variables
require_once __DIR__ . '/../db_config.php';

Try{ 
    // check for required fields
    if (isset($_POST['ID']) && (isset($_POST['PWD']) or isset($_POST['TYPE']) or isset($_POST['LOGIN'])) )
    {
        $id = $_POST['ID'];
        
        Try
        {
             // set a device
            $bdd = new PDO(DB_DNS, DB_USER, DB_PASSWORD , array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION ));

            if(isset($_POST['PWD']))
            {
                $pwd = $_POST['PWD'];
                $query = $bdd->prepare("UPDATE users SET pwd = :pwd WHERE id = :id"); 
                $query->bindValue('id',$id,PDO::PARAM_INT);
                $query->bindValue('pwd',$pwd,PDO::PARAM_STR); 
                $query->execute(); 
            }


            if(isset($_POST['TYPE']))
            {
                $type = $_POST['TYPE'];

                $query = $bdd->prepare("UPDATE users SET type = :type WHERE id = :id"); 
                $query->bindValue('id',$id,PDO::PARAM_INT);
                $query->bindValue('type',$type,PDO::PARAM_INT);
                $query->execute();                 
            }

            if(isset($_POST['LOGIN']))
            {
                $login = $_POST['LOGIN'];

                $query = $bdd->prepare("UPDATE users SET login = :login WHERE id = :id"); 
                $query->bindValue('id',$id,PDO::PARAM_INT);
                $query->bindValue('login',$login,PDO::PARAM_STR); 
                $query->execute();               
            } 
            
            // successfully inserted into database
            $response["success"] = 1;
            $response["message"] = "User $login ($id) successfully updated.";
     
            // echoing JSON response
            echo json_encode($response);                             
        }catch(Exception $e)
        {
            // required field is missing
            $response["success"] = 0;
            $response["message"] = $e->getMessage();
         
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