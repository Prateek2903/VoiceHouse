<?php
include_once './db_connect.php';
function getPost(){
    $db = new db_connect();
    // array for json response of full fixture\
	
	$comment_id=$_POST['comment_id'];
    $response = array();
    $response["reply"] = array();
  //$result = mysql_query("SELECT master_table.latitude, master_table.longitude, master_table.location,master_table.client_id,master_table.name,posttable.post, posttable.postid,client_interest.nmbr_of_viewers,client_interest.nmbr_of_followers,client_interest.rating, posttable.interest_id FROM posttable INNER JOIN master_table ON master_table.client_id=posttable.client_id INNER JOIN client_interest ON posttable.interest_id=client_interest.interest_id  WHERE client_interest.client_id='$client_id' AND posttable.client_id!='$client_id' ORDER BY posttable.postid DESC"); // Select all rows from fixture table
   //$result = mysql_query("SELECT postid FROM posttable ORDER BY postid DESC");
	
	$conn  = mysqli_connect("localhost","root","","discussionroom");
    $result = mysqli_query($conn,"select replytocomment.*, masterable.name from replytocomment INNER JOIN masterable ON replytocomment.client_id=masterable.client_id where replytocomment.comment_id='$comment_id'");
	
	while($row = mysqli_fetch_array($result)){
		
		$tmp["name"]=$row["name"];
	  $tmp["comment_id"]=$row["comment_id"];
	  $tmp["client_id"]=$row["client_id"];
	    $tmp["comment"]=$row["reply"];	

    array_push($response["reply"], $tmp);
		
    }
	
	
	
    header('Content-Type: application/json');

    echo json_encode($response);
   
}
getPost();
?> 