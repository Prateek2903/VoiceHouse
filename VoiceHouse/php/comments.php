<?php
 
/*
 * Following code will create a new product row
 * All product details are read from HTTP Post Request
 */
 
// array for JSON response
$response = array();
 
// check for required fields
if (isset($_POST['comment'])) {
 
   $post_id = $_POST['postid'];
   $commentby_client_id= $_POST['commentby_client_id'];
    $comment = $_POST['comment'];
	$nmbr_of_comments = $_POST['nmbr_of_comments'];
		$temp=(int)$nmbr_of_comments+1;
		
   

	
    // include db connect class
    require_once __DIR__ . '/db_connect.php';
 
    // connecting to db
    $db = new DB_CONNECT();
 
    // mysql inserting a new row
    $result = mysql_query("INSERT INTO comment_table_temp(post_id,client_id,comment) VALUES('$post_id', '$commentby_client_id','$comment')");
	
		
	
	
 
   // check if row deleted or not
    if (mysql_affected_rows() > 0) {
        // successfully updated
        $response["success"] = 1;
        $response["message"] = "Success";
 $commented=mysql_query("UPDATE  posttable SET nmbr_of_comments ='$temp' where postid='$post_id' ");
        // echoing JSON response
        echo json_encode($response);
    } else {
        // no product found
        $response["success"] = 0;
        $response["message"] = "Not success";
 
        // echo no users JSON
        echo json_encode($response);
    }
}
else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    // echoing JSON response
    echo json_encode($response);
}
?>
	