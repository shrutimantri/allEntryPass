<?php
 
/*
 * Following code will get single product details
 * A product is identified by product id (pid)
 */
 
// array for JSON response
$response = array();
 
// include db connect class
require_once __DIR__ . '/db_connect.php';
 
// connecting to db
$db = new DB_CONNECT();
 
// check for post data
if (isset($_GET["passId"]) && $_GET["passId"]!=null) {
    $pid = $_GET['passId'];
 
    // get a product from products table
    $result = mysql_query("SELECT * FROM student_details WHERE pass_id = $pid");
 
    if (!empty($result)) {
        // check for empty result
        if (mysql_num_rows($result) > 0) {
 
            $result = mysql_fetch_array($result);
 
            $product = array();
            $product["pid"] = $result["pass_id"];
            $product["name"] = $result["name"];
            $product["email"] = $result["email"];
            $product["phone"] = $result["phone"];
            $product["college"] = $result["college"];
            
            $events = array();
            $resultEvent = mysql_query("SELECT event_name FROM student_event_map WHERE pass_id = $pid");
            if(!empty($resultEvent)){
	            if (mysql_num_rows($resultEvent) > 0) {
				    // looping through all results
				    // products node
				    
				    while ($row = mysql_fetch_array($resultEvent)) {
				  
				        // push single product into final response array
				        array_push($events, $row["event_name"]);
				    }
				    $product["events"]=$events;
				}
			}

			$resultAllEvents = mysql_query("SELECT event_name FROM event_details");
            if(!empty($resultAllEvents)){
	            if (mysql_num_rows($resultAllEvents) > 0) {
				    // looping through all results
				    // products node
				    $allEvents = array();
				 
				    while ($row = mysql_fetch_array($resultAllEvents)) {
				  
				        if(!in_array($row["event_name"], $events))
				        array_push($allEvents, $row["event_name"]);
				    }
				    $product["allEvents"]=$allEvents;
				}
			}
            // success
            $response["success"] = 1;
 
            // user node
            $response["student"] = array();
 
            array_push($response["student"], $product);
 
            // echoing JSON response
            echo json_encode($response);
        } else {
            // no product found
            $response["success"] = 0;
            $response["message"] = "No student found";
 
            // echo no users JSON
            echo json_encode($response);
        }
    } else {
        // no product found
        $response["success"] = 0;
        $response["message"] = "No student found";
 
        // echo no users JSON
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