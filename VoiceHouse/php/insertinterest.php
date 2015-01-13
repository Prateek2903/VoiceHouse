
<?php
 $conn  = mysqli_connect("localhost","root","","discussionroom");
 $response = array();
 if (isset($_POST['choicesid'])) {
 $client_id=$_POST['client_id'];
$choicesid=$_POST['choicesid'];
$result = mysqli_query($conn,"INSERT INTO client_topics(client_id, choicesid) VALUES('$client_id', '$choicesid)");
	
 
}

?>
	
