<?php
$clientid=$_POST['client_id'];
$interest_string=$_POST['interest_id'];
/*$rating = $_POST['rating'];
$nmbr_of_followers=$_POST['nmbr_of_followers'];
$nmbr_of_viewers=$_POST['nmbr_of_viewers'];*/

$interest=explode(";",$interest_string);
for($i=0;$i<sizeof($interest);$i++){
$single_interest=implode(array_slice($interest, $i, 1));

//require_once__DIR__ . '/db_connect.php';
//$db=new DB_CONNECT();

$conn=mysqli_connect("localhost","root","","discussionroom");
if($single_interest!=null && $single_interest!="null")
{

	$query="INSERT INTO client_category_table (client_id, choices_id) VALUES('$clientid', '$single_interest')";
	
	$result=mysqli_query($conn,$query);
}


//$result = mysqli_query ($query);
}
if ($result) {
        $response["success"] = 1;
        $response["message"] = "Success";
        echo json_encode($response);
    } else {
        $response["success"] = 0;
        $response["message"] = "Not success";
        echo json_encode($response);
    }
?>
