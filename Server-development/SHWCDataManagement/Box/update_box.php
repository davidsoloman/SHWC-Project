<?php
 
/*
 * Following code will update a box information
 * A box is identified by box id (id)
 */
 
// array for JSON response
$response = array();
 
// import database connection variables
require_once __DIR__ . '/../db_config.php';

Try{ 
    // check for required fields
    if (isset($_POST['ID']) && ((isset($_POST['LOGIN']) && isset($_POST['PWD'])) or isset($_POST['URL']))) 
    {
        $id = $_POST['ID'];
        $login = $_POST['LOGIN'];
        $pwd = $_POST['PWD'];
        $url = $_POST['URL'];
        
        // check if row updated or not
        Try{

            $bdd = new PDO(DB_DNS, DB_USER, DB_PASSWORD , array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION ));

            // set a box
            if(isset($_POST['LOGIN']) && isset($_POST['PWD']))
            {
                $query = $bdd->prepare("UPDATE box SET login = :login, pwd=:pwd WHERE id = :id"); 
                $query->bindValue('id',$id,PDO::PARAM_STR);
                $query->bindValue('login',$login,PDO::PARAM_STR);
                $query->bindValue('pwd',$pwd,PDO::PARAM_STR);  
                $query->execute();          
            }
            
            if(isset($_POST['URL']))
            {
                $query1 = $bdd->prepare("UPDATE box SET url = :url WHERE id = :id"); 
                $query1->bindValue('id',$id,PDO::PARAM_STR); 
                $query1->bindValue('url',$url,PDO::PARAM_STR);            
                $query1->execute();
            }

        
            // successfully inserted into database
            $response["success"] = 1;
            $response["message"] = "Box $id successfully updated.";
     
            // echoing JSON response
            echo json_encode($response);

        }catch(Exception $e) 
        {
            // failed to update row
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