<?php
include_once './db_connect.php';
function getPost(){
    $db = new db_connect();
    // array for json response of full fixture\
	
	$topic_id=$_POST['topic_id'];
	$client_id=$_POST['client_id'];
    $response = array();
    $response["discussion_table"] = array();
  //$result = mysql_query("SELECT master_table.latitude, master_table.longitude, master_table.location,master_table.client_id,master_table.name,posttable.post, posttable.postid,client_interest.nmbr_of_viewers,client_interest.nmbr_of_followers,client_interest.rating, posttable.interest_id FROM posttable INNER JOIN master_table ON master_table.client_id=posttable.client_id INNER JOIN client_interest ON posttable.interest_id=client_interest.interest_id  WHERE client_interest.client_id='$client_id' AND posttable.client_id!='$client_id' ORDER BY posttable.postid DESC"); // Select all rows from fixture table
   //$result = mysql_query("SELECT postid FROM posttable ORDER BY postid DESC");
	
	$conn  = mysqli_connect("localhost","root","","discussionroom");
    $result = mysqli_query($conn,"select * from discussion_table");
	
	while($row = mysqli_fetch_array($result)){
		
		 $tmp["discussion_id"]=$row["discussion_id"];
	  $tmp["discussion"]=$row["discussion"];
	  $tmp["agree"]=$row["agree"];
	    $tmp["disagree"]=$row["disagree"];
		 $discussion_id=$row["discussion_id"];
		 
		 $agreeresult=mysqli_query($conn,"select * from disagree_table where discussion_id='$discussion_id'");
		 $flag=-1;
		 while($agreeclient=mysqli_fetch_assoc($agreeresult))
		 {
		 	if($agreeclient["client_id"]==$client_id)
			
			{
			
				$flag=$agreeclient[choose];
				
			}
		 }
		    $tmp["comments"]=array();
			$tmp["flag"]=$flag;
           
		$query=mysqli_query($conn,"select comment_table.*, masterable.name from comment_table INNER JOIN masterable ON comment_table.client_id=masterable.client_id where comment_table.discussion_id='$discussion_id' ORDER BY nmbr_of_logical DESC");
		for($i=0;$i<3 && ($myrow = mysqli_fetch_array($query))!=null;$i++)
		{
		
			if($discussion_id==$myrow["discussion_id"])	
			{
				$querys="select * from uplike";
	
				$results=mysqli_query($conn,$querys);
				while ($rows = mysqli_fetch_array($results)){
				//echo "1";
		
				if($rows["client_id"]==$client_id && $rows["comment_id"] == $myrow["comment_id"])
				{
				//echo "2".$rows["choose"]."<br>";
		
					$param='1'.$rows['choose'];
					
					$tmpo["param"]=$param;
				}
			else
				{
				///echo "3";
				$param='0'.$rows["choose"];
				$tmpo["param"]=$param;
				}
			
				}
 		
			//	$tmpo["param"]=$param;
		$tmpo["name"]=$myrow["name"];
		$tmpo["comment_id"]=$myrow["comment_id"];
		  $tmpo["discussion_id"]=$myrow["discussion_id"];
		   $tmpo["client_id"]=$myrow["client_id"];
		    $tmpo["comment"]=$myrow["comment"];
			 $tmpo["nmbr_of_logical"]=$myrow["nmbr_of_logical"];
			  $tmpo["nmbr_of_ilogical"]=$myrow["nmbr_of_ilogical"];
			  $tmpo["nmbr_of_reply"]=$myrow["nmbr_of_subcomment"];
			  array_push($tmp["comments"],$tmpo);
			 } 
		}
		 
	  	 

    array_push($response["discussion_table"], $tmp);
		
    }
	
	
	
    header('Content-Type: application/json');

    echo json_encode($response);
   
}
getPost();
?> 