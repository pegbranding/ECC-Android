<?php

//constants
define('ENDPOINT','https://ku-csm.symplicity.com/ws/report_api.php?wsdl');
define('USERNAME','emgroves421@gmail.com');
define('PASSWORD','ECC1001');

$client = new SoapClient(ENDPOINT, array('login'=>USERNAME,
					'password'=>PASSWORD));

$connection = new MongoClient("mongodb://localhost");
$collection = $connection->selectCollection("companylist","fall2014");

$result = $client->__getFunctions();
//$repid = $client->runReport('d8c9bdb2be720479177a1516c5254986');

$check = $client->checkReportRun('c7a85a708e0eef22c780fad27c0d8f10');
$tabresult = $client->getReportData('c7a85a708e0eef22c780fad27c0d8f10');


while (strlen($tabresult) > 0) {
  $myArray = explode('",', $tabresult,2);
  $approved = $myArray[0];
  //echo "APPROVED: $approved \n";
  $tabresult = isset($myArray[1]) ? $myArray[1] : "";
  
  $myArray = explode('",', $tabresult,2);
  $cancelled = $myArray[0];
  //echo "CANCELLED: $cancelled \n";
  $tabresult = isset($myArray[1]) ? $myArray[1] : "";

  $myArray = explode('",', $tabresult,2);
  $days_attending = $myArray[0];
  //echo "DAYS_ATTENDING: $days_attending \n";
  $tabresult = isset($myArray[1]) ? $myArray[1] : "";

  $myArray = explode('",', $tabresult,2);
  $ewi_table = $myArray[0];
  //echo "EWI TABLE: $ewi_table \n";
  $tabresult = isset($myArray[1]) ? $myArray[1] : "";

  $myArray = explode('",', $tabresult,2);
  $ecc_table = $myArray[0];
  //echo "ECC TABLE: $ecc_table \n";
  $tabresult = isset($myArray[1]) ? $myArray[1] : "";

  $myArray = explode('",', $tabresult,2);
  $company_name = $myArray[0];
  //echo "COMPANY NAME: $company_name \n";
  $tabresult = isset($myArray[1]) ? $myArray[1] : "";

  $myArray = explode('",', $tabresult,2);
  $overview = $myArray[0];
  //echo "OVERVIEW: $overview \n";
  $tabresult = isset($myArray[1]) ? $myArray[1] : "";

  $myArray = explode('",', $tabresult,2);
  $website = $myArray[0];
  //echo "WEBSITE: $website \n";
  $tabresult = isset($myArray[1]) ? $myArray[1] : "";

  $myArray = explode('",', $tabresult,2);
  $majors = $myArray[0];
  //echo "MAJORS: $majors \n";
  $tabresult = isset($myArray[1]) ? $myArray[1] : "";

  $myArray = explode('",', $tabresult,2);
  $degree_levels = $myArray[0];
  //echo "DEGREE LEVELS: $degree_levels \n";
  $tabresult = isset($myArray[1]) ? $myArray[1] : "";

  $myArray = explode('"', $tabresult,3);
  $position_types = $myArray[1];
  //echo "POSITION TYPES: $position_types \n";
  $tabresult = isset($myArray[2]) ? $myArray[2] : "";

  $thisElement = array('approved' => $approved, 
                       'cancelled' => $cancelled,
                       'days_attending' => $days_attending,
                       'ewi_table' => $ewi_table,
                       'ecc_table' => $ecc_table,
                       'company_name' => $company_name,
                       'overview' => $overview,
                       'website' => $website,
                       'majors' => $majors,
                       'degree_levels' => $degree_levels,
                       'position_types' => $position_types
                       );

  
  $collection->insert($thisElement);

}


echo "\n\n NEW TABRESULT \n\n $tabresult ";

?>

