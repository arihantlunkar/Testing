<?php 

move_uploaded_file($_FILES['biodata']['tmp_name'], "1.jpg");

move_uploaded_file($_FILES['picture1']['tmp_name'], "2.jpg");

move_uploaded_file($_FILES['picture2']['tmp_name'], "3.jpg");

move_uploaded_file($_FILES['picture3']['tmp_name'], "4.jpg");

move_uploaded_file($_FILES['picture4']['tmp_name'], "5.jpg");

move_uploaded_file($_FILES['picture5']['tmp_name'], "6.jpg");

echo json_encode(array("Success" => "Msg"));