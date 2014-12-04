<?php

//constants
define('ENDPOINT','https://ku-csm.symplicity.com/ws/report_api.php?wsdl');
define('USERNAME','emgroves421@gmail.com');
define('PASSWORD','ECC1001');

$client = new SoapClient(ENDPOINT, array('login'=>USERNAME,
					'password'=>PASSWORD));



$result = $client->__getFunctions();
//$repid = $client->runReport('d8c9bdb2be720479177a1516c5254986');

//echo "<reportid>";
//print_r($repid);
//echo "</reportid>";

echo "<pre>";
print_r($result);
echo "</pre>";

$check = $client->checkReportRun('c7a85a708e0eef22c780fad27c0d8f10');
$tabresult = $client->getReportData('c7a85a708e0eef22c780fad27c0d8f10');



//print($tabresult);
$myArray = explode(',', $tabresult);
$max = sizeof($myArray);
echo "MAX $max";

for ($i=0; $i<$max; $i++) {
    echo "$myArray[$i]\n";
}


//print_r($check);
//echo "@@";

?>

