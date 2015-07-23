<?php
 
/*
 * Following code will create a new product row
 * All product details are read from HTTP Post Request
 */
 
// array for JSON response
$response = array();
 
// check for required fields
if (isset($_POST['passId']) && isset($_POST['name']) && isset($_POST['college']) && isset($_POST['phone']) && isset($_POST['email']) &&
    $_POST['passId']!=null && $_POST['name']!=null && $_POST['college']!=null && $_POST['phone']!=null && $_POST['email']!=null) {
 
    $passId = $_POST['passId'];
    $name = $_POST['name'];
    $email = $_POST['email'];
    $phone = $_POST['phone'];
    $college = $_POST['college'];
 
    // include db connect class
    require_once __DIR__ . '/db_connect.php';
 
    // connecting to db
    $db = new DB_CONNECT();
    $con = $db->connect();
    // mysql inserting a new row
    $result = mysql_query("INSERT INTO student_details(pass_id, name, email, phone, college) VALUES('$passId', '$name', '$email', '$phone', '$college')");
 
    // check if row inserted or not
    if ($result) {
        // successfully inserted into database
        $response["success"] = 1;
        $response["message"] = "Pass successfully created.";
 
        // echoing JSON response
        echo json_encode($response);
    } else {
        // failed to insert row
        $response["success"] = 0;
        $response["message"] = mysql_errno($con) . ": " . mysql_error($con);
 
        // echoing JSON response
        echo json_encode($response);
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    // echoing JSON response
    echo json_encode($response);
}
?>