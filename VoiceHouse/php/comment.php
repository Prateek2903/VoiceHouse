<?php
 
/*
 * Following code will create a new product row
 * All product details are read from HTTP Post Request
 */
 
// array for JSON response
$response = array();
 
// check for required fields
//if (isset($_POST['comment'])) {
 
    
   $discussion_id= 1;//$_POST['discussion_id'];
  $client_id=3;// $_POST['client_id'];
    $comment = 1;//$_POST['comment'];	
	$type =1;
	
	//$_POST['type'];
		

	
    // include db connect class
    require_once __DIR__ . '/db_connect.php';
 
    // connecting to db
    $db = new DB_CONNECT();
	$conn=mysqli_connect("localhost","root","","discussionroom");
	$query=mysqli_query($conn,"select * from comment_table where discussion_id='$discussion_id' AND client_id='$client_id'");
	$count=0;
	while($ro=mysqli_fetch_assoc($query))
	{
		
		$count++;
	}
	
 $rating=mysqli_query($conn,"select rating from masterable where client_id='$client_id'");
 $rating=mysqli_fetch_assoc($rating);
 if($rating["rating"]<50 )
 {
 if($count<1)
 	  $result = mysql_query("INSERT INTO comment_table(discussion_id,client_id,comment,type) VALUES('$discussion_id', '$client_id','$comment','$type')");
 }
 else
 {
 	$result = mysql_query("INSERT INTO comment_table(discussion_id,client_id,comment,type) VALUES('$discussion_id', '$client_id','$comment','$type')");
 
 }
 
	
		
	
	
 
   // check if row deleted or not
    if (mysql_affected_rows() > 0) {
        // successfully updated
        $response["success"] = 1;
        $response["message"] = "Success";
 //$commented=mysql_query("UPDATE  posttable SET nmbr_of_comments ='$temp' where postid='$post_id' ");
        // echoing JSON response
        echo json_encode($response);
    } else {
        // no product found
        $response["success"] = 0;
        $response["message"] = "Not success";
 
        // echo no users JSON
        echo json_encode($response);
    }

/*else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    // echoing JSON response
    echo json_encode($response);
}*/
?>
	
