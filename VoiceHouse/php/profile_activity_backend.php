<?php
include_once './db_connect.php';
function getPost(){
    $db = new db_connect();
    // array for json response of full fixture\
	
	$client_id=$_POST['client_id'];
    $response = array();
    $response["Master_Table"] = array();
  //$result = mysql_query("SELECT master_table.latitude, master_table.longitude, master_table.location,master_table.client_id,master_table.name,posttable.post, posttable.postid,client_interest.nmbr_of_viewers,client_interest.nmbr_of_followers,client_interest.rating, posttable.interest_id FROM posttable INNER JOIN master_table ON master_table.client_id=posttable.client_id INNER JOIN client_interest ON posttable.interest_id=client_interest.interest_id  WHERE client_interest.client_id='$client_id' AND posttable.client_id!='$client_id' ORDER BY posttable.postid DESC"); // Select all rows from fixture table
   //$result = mysql_query("SELECT postid FROM posttable ORDER BY postid DESC");
	
	$conn  = mysqli_connect("localhost","root","","discussionroom");
    $result = mysqli_query($conn,"select * from mastertable where client_id='$client_id'");
	
	while($row = mysqli_fetch_array($result)){
		
		$tmp["Name"]=$row["name"];
		$tmp["Choices1"]=$row[""];
		$tmp["agree"]=$row["agree"];
	    $tmp["disagree"]=$row["disagree"];
		$discussion_id=$row["discussion_id"];
		   $tmp["comments"]=array();
           
						$query=mysqli_query($conn,"select * from comment_table where discussion_id='$discussion_id' ORDER BY  nmbr_of_logical DESC ");
		for($i=0;$i<3 && $myrow = mysqli_fetch_array($query);$i++)
		{
 
		
		$tmpo["comment_id"]=$myrow["comment_id"];
		  $tmpo["discussion_id"]=$myrow["discussion_id"];
		   $tmpo["client_id"]=$myrow["client_id"];
		    $tmpo["comment"]=$myrow["comment"];
			 $tmpo["nmbr_of_logical"]=$myrow["nmbr_of_logical"];
			  $tmpo["nmbr_of_ilogical"]=$myrow["nmbr_of_ilogical"];
			  $tmpo["nmbr_of_reply"]=$myrow["nmbr_of_subcomment"];
			  array_push($tmp["comments"],$tmpo);
			  
		}
		 
	  	 

    array_push($response["discussion_table"], $tmp);
		
    }
	
	
	
    header('Content-Type: application/json');

    echo json_encode($response);
   
}
getPost();
?> 