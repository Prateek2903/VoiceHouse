
<?php
 $conn  = mysqli_connect("localhost","root","","discussionroom");
 $response = array();
 if (isset($_POST['client_id'])) {
 $client_id=$_POST['client_id'];
$name=$_POST['name'];
$email=$_POST['email'];

$myquery=mysqli_query($conn,"select client_id from masterable where client_id='$client_id' ");

if(mysqli_fetch_assoc($myquery))
{
	 $response['message']="already there";
}
else
{
		$result = mysqli_query($conn,"INSERT INTO masterable(client_id,name,email) VALUES('$client_id','$name','$email')");
		$response['message']="success";
}

echo json_encode($response);


 }

?>
	