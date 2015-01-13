<?php
include_once './db_connect.php';
function getPost(){
$db = new db_connect();
$client_id=1;//$_POST['client_id'];
$response = array();
$response["discussion_table"] = array();
$choice = array();
$conn  = mysqli_connect("localhost","root","","discussionroom");
/*$result = mysqli_query($conn,"SELECT comment_table.nmbr_of_logical, comment_table.nmbr_of_ilogical, masterable.name, masterable.rating, category_table.choice_name FROM masterable
INNER JOIN comment_table ON masterable.client_id = comment_table.client_id
INNER JOIN client_category_table ON client_category_table.client_id = comment_table.client_id
INNER JOIN category_table ON category_table.choices_id = client_category_table.choices_id
WHERE client_category_table.client_id =  '$client_id'
");*/
$result=mysqli_query($conn,"select * from masterable where client_id='$client_id'");
while($row = mysqli_fetch_array($result)){
		
		$tmp["choice_name"]=$row["choice_name"];
	    $tmp["name"]=$row["name"];
	    $tmp["nmbr_of_ilogical"]=$row["illogical"];
	    $tmp["nmbr_of_logical"]=$row["logical"];
		$tmp["rating"]=$row["rating"];
		$result_choices=mysqli_query($conn,"select category_table.choice_name from client_category_table INNER JOIN category_table ON client_category_table.choices_id=category_table.choices_id where client_id='$client_id'");
	 	//echo "Array :  ".mysqli_fetch_assoc($result_choices);
		$i = 0;
		while($cho = mysqli_fetch_array($result_choices))
		{
			array_push($choice,$cho["choice_name"]);
			
		}
		$tmp["choice_name"] = $choice;
		   

    array_push($response["discussion_table"], $tmp);
		
    }
	
	

    echo json_encode($response);
   
}
getPost();
?> 