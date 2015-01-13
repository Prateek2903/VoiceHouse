<?php



$conn = mysqli_connect("localhost","root","","discussionroom");

$response = array();
$comment_id =$_POST["comment_id"];
$client_id = $_POST["client_id"];
$choose =$_POST["do"];
$query = "SELECT * FROM uplike WHERE client_id='$client_id' AND comment_id ='$comment_id'";
$result = mysqli_query($conn,$query);
$flag=0;


while( $row = mysqli_fetch_assoc($result))
{


	if($row['client_id'] == $client_id && $row['comment_id'] == $comment_id)
	{
		$flag=1;
	}
}
if($flag==1)
{
	$query = "DELETE FROM uplike WHERE comment_id= '$comment_id' AND client_id = '$client_id'";
	$delete = mysqli_query($conn,$query);
	$response["success"] = 1;
      $response["message"] = "delete";
}
else
{	

	$query = "INSERT INTO uplike(comment_id,client_id,choose) VALUES('$comment_id','$client_id','$choose')";

	$insert = mysqli_query($conn,$query);
        // successfully updated
     $response["success"] = 1;
      $response["message"] = "done";
 
        // echoing JSON response
        
 }
$conn_comment_table = mysqli_connect("localhost","root","","discussionroom");
$query_for_nmbrs = "SELECT * FROM comment_table";
$query_result = mysqli_query($conn_comment_table,$query_for_nmbrs);
$respone["logical"]=" Logical ";
while( $row = mysqli_fetch_assoc($query_result))
{
	if($row['comment_id'] == $comment_id)
	{	
		if($choose == '1')
		{

		if($flag==1)
			$temp = $row['nmbr_of_logical']-1;
		else
			$temp = $row['nmbr_of_logical']+1;



		$query_for_update = "UPDATE comment_table SET nmbr_of_logical='$temp' where comment_id='$comment_id'";
		$result_For_upadte = mysqli_query($conn_comment_table,$query_for_update);
		$response["logical"]=$temp." Logical ";
		}
		else
		{
			if($flag==1)
		$temp = $row['nmbr_of_ilogical']-1;
		else
		{
		$temp = $row['nmbr_of_ilogical']+1;

		}

		$query_for_update = "UPDATE comment_table SET nmbr_of_ilogical='$temp' where comment_id='$comment_id'";
		$result_For_upadte = mysqli_query($conn_comment_table,$query_for_update);
		$response["logical"]=$temp." Illogical ";
		echo json_encode($response);

		

		}
		// check if row deleted or not
    
		
	}
	
    		
	
}

	echo json_encode($response);
?>