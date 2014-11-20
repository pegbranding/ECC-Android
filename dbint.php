<?php

//constants
define('ENDPOINT','https://ku-csm.symplicity.com/ws/report_api.php?wsdl');
define('USERNAME','emgroves421@gmail.com');
define('PASSWORD','ECC1001');

$client = new SoapClient(ENDPOINT, array('login'=>USERNAME,
					'password'=>PASSWORD));



$result = $client->__getFunctions();
$repid = $client->runReport('d8c9bdb2be720479177a1516c5254986');

echo "<reportid>";
print_r($repid);
echo "</reportid>";

echo "<pre>";
print_r($result);
echo "</pre>";

$check = $client->checkReportRun('a293e1079e87572ec27e6e0b896d8695');
$tabresult = $client->getReportData('a293e1079e87572ec27e6e0b896d8695');

echo "<tab>";
print_r($check);
echo "@@";
print_r($tabresult);
echo "</tab>";

?>

