<?php
	require_once '../hprose/Hprose.php';
	$client = \Hprose\Client::create('tcp://127.0.0.1:1234/abc/111', false);
	$res=$client->t1("1","2");
	var_dump($res);



?>