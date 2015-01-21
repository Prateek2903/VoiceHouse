<?php

$conn = mysqli_connect("localhost","root","","discussionroom");
$discussion_id = $_POST["discussion_id"];
$client_id = $_POST["client_id"];
$choose = $_POST["do"];
$query = "SELECT * FROM disagree_table WHERE client_id='$client_id' AND discussion_id ='$discussion_id'";
$result = mysqli_query($conn,$query);
$flag=0;
while( $row = mysqli_fetch_array($result))
{
	if($row['client_id'] == $client_id && $row['discussion_id'] == $discussion_id)
	{
		$flag=1;
	}
}
if($flag==1)
{
	$query = "DELETE FROM disagree_table WHERE discussion_id= '$discussion_id' AND client_id = '$client_id'";
	$delete = mysqli_query($conn,$query);
	$response["success"] = 1;
        $response["message"] = "delete";
}
else
{	
	$query = "INSERT INTO disagree_table(discussion_id,client_id,choose) VALUES('$discussion_id','$client_id','$choose')";
	$insert = mysqli_query($conn,$query);
	$response["success"] = 1;
        $response["message"] = "done";

}
$conn_comment_table = mysqli_connect("localhost","root","","discussionroom");
$query_for_nmbrs = "SELECT * FROM discussion_table";
$query_result = mysqli_query($conn_comment_table,$query_for_nmbrs);
while( $row = mysqli_fetch_assoc($query_result))
{
	if($row['discussion_id'] == $discussion_id)
	{	
		if($choose == '1')
		{

		if($flag==1)
			$temp = $row['agree']-1;
		else
			$temp = $row['agree']+1;



		$query_for_update = "UPDATE discussion_table SET agree='$temp' where discussion_id='$discussion_id'";
		$result_For_upadte = mysqli_query($conn_comment_table,$query_for_update);
$response["agree"]=$temp." AGREE ";
		echo json_encode($response);

		}
		else
		{
			if($flag==1)
		$temp = $row['disagree']-1;
		else
		{
		$temp = $row['disagree']+1;

		}

		$query_for_update = "UPDATE discussion_table SET disagree='$temp' where discussion_id='$discussion_id'";
		$result_For_upadte = mysqli_query($conn_comment_table,$query_for_update);	
		$response["agree"]=$temp." DISAGREE ";
		echo json_encode($response);

		}
		
	}
	
}
echo json_encode($response);
?>