<?php
	use Hprose\Socket\Server;
	require_once 'hprose/Hprose.php';
	//error_reporting(0);
	//var_dump($argv);
	define('ROOT', str_replace("\\", "/",dirname(__FILE__,2)));

	if(count($argv)==3){
		$this_service_url=$argv[1];			//服务独立添加url
		$service_path=$argv[2];
	}else{
		echo "param error";
		die();
	}

	$myfile = fopen($service_path, "r") or die("Unable to open file!");
	$service_config_json=fread($myfile,filesize($service_path));
	$service_config=json_decode($service_config_json,true);
	//service_root
	$service_root_path=ROOT."/service/".$service_config['name'];
	if($service_config['call_type']=="rpc"){
		//service_url
		echo $service_url="tcp://".$service_config['service_host'].":".$service_config['service_port']."/".$this_service_url.$service_config['service_url'];
		$server = new Server($service_url);
		$fun_name_array=array();
		$fun_mod_name_array=array();
		for($i=0;$i<count($service_config['mods']);$i++){
			$mod_file_path=$service_config['mods'][$i]['file_path'];
			$mod_fun_name=$service_config['mods'][$i]['name'];
			require($mod_file_path);
			for($j=0;$j<count($service_config['mods'][$i]['functions']);$j++){
				$fun_name=$service_config['mods'][$i]['functions'][$j]['name'];
				if(!in_array($fun_name, $fun_name_array)){
					array_push($fun_name_array,$fun_name);
					array_push($fun_mod_name_array,$mod_fun_name);
				}else{
					echo "same function name error";
				}
			}
		}
		for($i=0;$i<count($fun_name_array);$i++){
			//$server->addFunction($fun_name_array[$i]);
			echo $fun_name_array[$i];
			echo $fun_mod_name_array[$i];
			$server->addMethod($fun_name_array[$i],new $fun_mod_name_array[$i]);
		}

		$server->start();
	}
	
	


