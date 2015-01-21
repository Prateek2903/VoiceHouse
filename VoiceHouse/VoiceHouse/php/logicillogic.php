<?php

include_once './db_connect.php';
function getPost(){
    $db = new db_connect();
    // array for json response of full fixture\
	
	
   $client_id = $_POST['client_id'];
   $comment_id= $_POST['comment_id'];
    $response = array();
    $response["discussion_table"] = array();
 
	
	$conn  = mysqli_connect("localhost","root","","discussionroom");
    $result = mysqli_query($conn,"SELECT comment_table.nmbr_of_logical, comment_table.nmbr_of_ilogical FROM comment_table INNER JOIN uplike ON uplike.client_id = comment_table.client_id WHERE uplike.comment_id =  '$comment_id'");
	
	
	
	while(($row = mysqli_fetch_assoc($result))){
		
		 $tmp["nmbr_of_logical"]=$row["nmbr_of_logical"];
	     $tmp["nmbr_of_ilogical"]=$row["nmbr_of_ilogical"];
	  $do=$_POST['do'];
		$temp=(int)$nmbr_of_logical+$do;
   echo "".$temp;
   
   if($do==1){
	 $qury=mysqli_query($conn,"select * from uplike where client_id='$client_id' AND comment_id='$comment_id'");
	 echo "checking1";
	
	 
	if($qury==null)
	 {
	 $querys=mysqli_query($conn,"select nmbr_of_logical,nmbr_of_ilogical from comment_table INNER JOIN like ON like.client_id=comment_table.client_name where client_id='$client_id' AND comment_id='$comment_id'");

echo "checking2";

   	$result = mysqli_query($conn,"INSERT INTO uplike(comment_id,client_id) VALUES('$comment_id', '$client_id')");
   	$tempo["$nmbroflogical"]=$tmp["nmbr_of_logical"]+1;
	$num = $tempo["$nmbroflogical"];
	$increaselike=mysqli_query($conn,"UPDATE  comment_table SET nmbr_of_logical ='$num' where comment_id='$comment_id' ");
	$increaselike=mysql_query("INSERT INTO comment_table(nmbr_of_logical,nmbr_of_ilogical) values ('$nmbroflogical','$nmbrofilogical')");
		}
	else
	{
			$result = mysqli_query($conn,"DELETE FROM uplike WHERE comment_id='$comment_id' AND client_id='$client_id'");
	     $tempo["$nmbroflogical"]=$tmp["$nmbr_of_logical"] -1;
	       $decreaselike=mysqli_query($conn,"UPDATE  comment_table SET nmbroflogical ='$nmbroflogical' where comment_id='$comment_id'") ;
	echo "checking3";
		$increaseunlike=mysql_query("INSERT INTO comment_table(nmbr_of_logical,nmbr_of_ilogical) values ('$nmbroflogical','$nmbrofilogical')");
	}
	
	
	 }
	 else{
		$myqury=mysqli_query($conn,"select * from uplike where client_id='$client_id' AND comment_id='comment_id'");
		echo $myqury;
	 if($qury==null)
	 {
	     $myresult = mysqli_query($conn,"INSERT INTO uplike(comment_id,client_id) VALUES('$comment_id', '$client_id')");
		 echo "checking4";
		$tempo["$nmbrofilogical"]=$tmp["$nmbr_of_ilogical"] +1;
	 $increaseunlike=mysqli_query($conn,"UPDATE  comment_table SET nmbr_of_ilogical ='$nmbrofilogical' where comment_id='$comment_id' ");
	 echo $increaseunlike;
//$increaselike=mysql_query("INSERT INTO comment_table(nmbr_of_logical,nmbr_of_ilogical) values ('$nmbroflogical','$nmbrofilogical')");
}
	else
	{
	$result = mysqli_query($conn,"DELETE FROM uplike WHERE comment_id='$comment_id' AND client_id='$client_id'");
	$tempo["$nmbrofilogical"]=$tmp["$nmbr_of_ilogical"] -1;
	$decreaseunlike=mysqli_query($conn,"UPDATE  comment_table SET nmbr_of_ilogical ='$nmbrofilogical' where comment_id='$comment_id'") ;
	echo $decreaseunlike;
	//increaseunlike=mysql_query("INSERT INTO comment_table(nmbr_of_logical,nmbr_of_ilogical) values ('$nmbroflogical','$nmbrofilogical')");
			
			
	}
	
   
   
   
   
		   
		

    array_push($response["discussion_table"], $tmp);
		
    }
	
	
	
   
    echo json_encode($response);
   
}
}
getPost();

 ?>