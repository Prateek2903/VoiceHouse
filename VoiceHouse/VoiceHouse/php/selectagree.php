<?php

$response=array();

$conn = mysqli_connect("localhost","root","","discussionroom");
$client_id=$_POST['client_id'];
$discussion_id=$_POST['discussion_id'];
$choose=$_POST['choose'];
$result=mysqli_query($conn,"select * from disagree_table where discussion_id='$discussion_id'");
$flag=-1;

while($ro=mysqli_fetch_assoc($result))
{
if($client_id==$ro[client_id])

{

	$flag=$ro['choose'];
	
}


	
}



if($flag==$choose)
{

mysqli_query($conn,"delete from disagree_table where client_id='$client_id' and discussion_id='$discussion_id' ");
	
}
else
{

	mysqli_query($conn,"insert into disagree_table (client_id,discussion_id,choose) values ('$client_id','$discussion_id','$choose' )");
}
	echo json_encode($response);
?>