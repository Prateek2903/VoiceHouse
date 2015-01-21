

<?php
include_once './db_connect.php';
function getPost(){
    $db = new db_connect();
    // array for json response of full fixture\
	
	$discussion_id='1';//$_POST['discussion_id'];
$type='0';//$_POST['type'];
    $response = array();
    $response["comments"] = array();
  //$result = mysql_query("SELECT master_table.latitude, master_table.longitude, master_table.location,master_table.client_id,master_table.name,posttable.post, posttable.postid,client_interest.nmbr_of_viewers,client_interest.nmbr_of_followers,client_interest.rating, posttable.interest_id FROM posttable INNER JOIN master_table ON master_table.client_id=posttable.client_id INNER JOIN client_interest ON posttable.interest_id=client_interest.interest_id  WHERE client_interest.client_id='$client_id' AND posttable.client_id!='$client_id' ORDER BY posttable.postid DESC"); // Select all rows from fixture table
   //$result = mysql_query("SELECT postid FROM posttable ORDER BY postid DESC");
	
	$conn  = mysqli_connect("localhost","root","","discussionroom");
   $result = mysqli_query($conn,"select comment_table.*, masterable.name from comment_table INNER JOIN masterable ON comment_table.client_id=masterable.client_id where comment_table.discussion_id='$discussion_id' AND comment_table.type='$type' ORDER BY nmbr_of_logical DESC");
	
	while($row = mysqli_fetch_array($result)){
		
		$tmp["name"]=$row["name"];
	  $tmp["comment_id"]=$row["comment_id"];
	  $tmp["client_id"]=$row["client_id"];
	    $tmp["comment"]=$row["comment"];	
		$tmp["nmbr_of_logical"]=$row["nmbr_of_logical"];
				$tmp["nmbr_of_ilogical"]=$row["nmbr_of_ilogical"];
				$tmp["nmbr_of_reply"] = $row["nmbr_of_subcomment"];
				//$conn_comment = mysqli_connect("localhost","root","","discussionroom");
				//$comment_id = $row["comment_id"];
				//$valuess = mysqli_query($conn_comment,"select count(*) from replytocomment where comment_id='$comment_id'");
				//$tmp["nmbr_of_reply"]=$valuess;
    array_push($response["comments"], $tmp);
		
    }
	
	
	
  //  header('Content-Type: application/json');

    echo json_encode($response);
   
}
getPost();
?> 