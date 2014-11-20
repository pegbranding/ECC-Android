<?php

//constants
define('ENDPOINT','https://ku-csm.symplicity.com/ws/report_api.php?wsdl');
define('USERNAME','emgroves421@gmail.com');
define('PASSWORD','ECC1001');
define('REPORTID', '26533c7341e445f3201088f698898437');

$client = new SoapClient(ENDPOINT, array('login'=>USERNAME,
					'password'=>PASSWORD));


$result = $client->getReportData(REPORTID);

echo "<pre>";
print_r($result);
echo "</pre>";

?>

