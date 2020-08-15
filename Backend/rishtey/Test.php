<?php 
$jsonDecodedPOSTData = json_decode(file_get_contents('php://input'));

$target_file_name = "upload/".rand()."_".time().".jpg";

if(file_put_contents($target_file_name, base64_decode($jsonDecodedPOSTData->input->data->stringImageBioData)))
{
	echo json_encode(["message" => "success", "success" => "ok"]);
}
else 
{
	echo json_encode(["message" => "failure", "success" => "nok"]);
}