### Lightweight & multi-language microservices framework (LM-MS)

		A multi-language, lightweight, high-performance microservices framework, 
		developers only need to care about code writing related to business logic,
		without having to think about deployment, fault tolerance, service load balance, etc.
		
		[百度](http://baidu.com)
	
	 

	
## The original intention of developing this framework

		1. 	Because the server of the previous project needs to provide a variety of services, 
			including video streaming, faceid database synchronization, receiving customer 
			consumption data, background management system,Shop equipment docking, receiving passenger 
			flow statistics, etc., so the back-end logic is complex, so the server provides data support 
			in the form of micro-services.
			
		2.	Project backend code is written in multiple languages (java and php), and current mainstream 
			frameworks rarely have microservices that support multiple languages.
			
		3.	At present, the mainstream microservices framework is complex in configuration 
			and takes up large resources.
		
		4.	Using traditional http calls is less efficient, and there are more tasks in the system 
			that require multiple backend services, resulting in a large performance penalty.
			
		Note:	After that, I will write a detailed operation manual.At present, part of the code 
				comment for this program is Chinese, which will be translated into English.
		

## Overview

		1. 	This framework implements a multi-language micro-service function 		¡ú_¡ú
			which can be used to implement multi-language 	
			micro-service writing and provide services to the outside (JAVA/PHP/PYTHON)
		
		2.	This framework can realize single-node multi-service, multi-node multi-service, O__O"
			multi-api gateway combination of multiple modes				
		
		3.	The advantage of this framework over the mainstream framework is that:		O(O_O)O
			1)	Can be written in multiple languages to provide backend services for applications
			2)	Lightweight
			3)	Simple to deploy, less external dependencies
			
		4.	Developers can focus their work on writing service code that is directly related to business 
			logic without having to think too much about framework-related content.		¡ä(O©nO*)¡ä
			
		5. 	There may be some bugs in this open source version, if you find it, you can contact me.	>_<|||
		
		6.	The entire framework is written in Java, and the performance is efficient.	O(O_O)O
			

## Microservices framework
		
		1.	main-control-server is referred to as MCS
			
			Mainly used to receive external control commands, control the api gateway and application nodes, 
			and monitor heartbeat data,and maintain the service table together with the api gateway.
			
		2.	api-gateway
			
			Receive external access and forward external access to rpc to internal microservices
			Provides flow control, blown, SLB, service downgrade, and more
			Block incorrect protocols, etc.
			Error message redirection, etc.
			
		3.	app-server is referred to as AS
		
			Maintain the microservice application on the node and provide external services.
			
## api-gateway
		
		1.	Black list and white list
		
			You can set the ip whitelist or ip blacklist in the gateway configuration file to 
			restrict ip rejection to the gateway.
			
		2.	Initially prevent ddos (to be improved) [TODO]
			
			The framework of this open source version simply implements a method of controlling ddos 
			via ip, and will continue to improve afterwards.
			
		3.	Global flow control
		
			Control the overall traffic of the api gateway. When the traffic exceeds the limit, 
			some requests will be returned to the flow control.
			
		4.	Service level flow control (using the token algorithm)
		
			This framework adds separate flow control to each service.
			
		5.	Service level blown
			
			When the traffic of a service exceeds the limit, the gateway will temporarily stop setting 
			the service to a closed state, and then the service will be turned into a half-open state 
			for a while,and then will turn to the open state.
			
		6.	Request level cache (text cache/memory cache, based on redis)
		
			This framework provides two cache modes of document and memory, which can set the cache time 
			in the configuration file, etc. Memory cache based on redis.
			
		7.	service load balance (can use random method / polling method / weighted rounding method)
			
			The framework provides a complete load balancing mechanism, optional algorithm random method 
			/ polling method / weighted rounding method.
			The framework will direct the same request to the same version of the service where the 
			different nodes are located.
			
		8.	Service downgrade [TODO]
		
		9.	Aggregation request
		
			The aggregation request can be configured in the gateway configuration file. After the HTTP 
			request is sent, the data of the http request will be provided by the micro-service of the backend. 
			Currently, only the serial mode is supported, and then the parallel mode is supported.
			
		10.	Html forwarding
		
		11.	Synchronous/asynchronous service
		
		
		
		
		
		
## Api gateway how to processing

		![](https://github.com/ashjpo/LM-MS/blob/master/struct.jpg)
		
		
## using Frame

		1.	Run mic-service-run
				Run-MCS.bat->Run-API-GATEWAY.bat->Run-AS.bat
				Start the main control node, api gateway, application node
			
		2.	Writing microservices
				Below the mic_service is the root directory of the application node, 
				and the microservice package is placed in the serice folder below the node.
			
		3.	In mic_service you can write micro-services in different languages, 
				you only need to specify in the json file.
		
## Directory Structure
		
		1.MCS
			MCS
			|---config	
					|---api-gate.properties
					|---as.properties
					|---mqtt.properties
					|---mqtt_topic.properties
			
			
		2.api
			Api_Gate
				|---aggregation						#Aggregation request profile path
				|		|---aggregation_service.json
				|
				|---config	
						|---cache.properties
						|---ddos.properties
						|---flow_control.properties
						|---fusing.properties
						|---main.properties
						|---mqtt.properties
						|---mqtt_topic.properties
						|---server.properties
		
		
		
		3.AS¸ùÄ¿Â¼
			mic_service
				|---common		#Public directory for public libraries in various languages
				|---init			#The initialization script is used to start the service
				|---config			#Configuration file directory
				|---service			#Microservice program storage directory
					|---serviceA	#Microservice name
							|---common	#Common library of the service
							|---config	#Service profile json ***
							|---functions	#Service program
							|---test		#test program
		
		
		
		
## Microservice example (PHP)

		This program is stored in mode1.class.php in the functions folder.
		
		``` php
		  <?php
				require "Test.class.php";
				class mode1{
					public function a1(){
						return "hehe";
					}
				
					public function b1($a){
						$t=new Test();
						return "hehe".$a;
					}
				}
				
			
			?>
		```
		
		Class names and method names cannot be the same in the same microservice
		
## Microservice example (JAVA)

		This program is stored in the testsClass.jar in the functions folder.
		
		``` java
		public class testClass{
			public String hello() {
				
				return "hehejava";
			}
			
			public String testjava() {
				
				return "t-java";
			}
		}
		```
		
		Class names and method names cannot be the same in the same microservice
	
## Microservice configuration json sample file
	
		{	"name": "serviceB",											#Microservice name
			"call_type": "rpc",											#Call type(rpc http)
			"service_type": "common",											
			"version": "v1.0",											#Microservice version
			"service_host": "127.0.0.1",								#ip
			"service_url": "/b",										#Microservice routing address
			"version_code": 1,											#version number
			"language": "php",											#Writing language
			"api-gate": "api-gate1",									#Exposed gateway
			"service_mes": "aaa",											
			"mods": [{													#One module
				"name": "mode1",										#Module name
				"file_path": "D:/mic_service/service/serviceB/functions/mode1.class.php",	#Module file path
				"functions": [{											#function
					"name": "a1",										#function name
					"syn_asyn": "syn",									#Synchronous or asynchronous	(syn/asyn)
					"http_url": "/url1",								#http Routing address
					"params": [],												
					"cache": "memory"									#cache(""/"text"/"memory")
				}, {
					"name": "b1",
					"syn_asyn": "syn",
					"http_url": "/url2",
					"params": [
						["http_a", "a", "get"]			#parameter list (first is the parameter key of the request 
					],										outside the gateway, second is name of the function)
					"cache": ""
				}, {
					"name": "btest",
					"syn_asyn": "syn",
					"http_url": "/url3",
					"params": [],
					"cache": ""
				}]
			}]
		}		

## Aggregate request json sample file

		Aggregation_service.json file under the aggregation folder
		
		[{
			"call_url": "/aaa",									#Call routing outside the gateway
			"serial_parallel": "parallel",						#Serial or parallel (this version only supports serial)
			"ask": ["/b/url1", "/b/url2"],						#The microservice url requested in the gateway 
			"key": ["key1", "key2"]									(same as the externally requested url)
		}]
	
		Return example:
		
			{
			    "key1": {
			        "ask_name": "a1",
			        "ask_url": "/url1",
			        "service_name": "serviceB",
			        "mes": "hehe---",
			        "version": "v1.0"
			    },
			    "key2": {
			        "ask_name": "b1",
			        "ask_url": "/url2",
			        "service_name": "serviceB",
			        "mes": "haha---",
			        "version": "v1.0"
			    }
			}
		
## control commands
	
		[TODO]
	
## control program

		The program provides a class that quickly sends the mqtt command control 
		node and a class that views node information.
		
		1.Control_service_as.java is used to send control information in send_service_control_commond
		
			1)	** public void start_service(String service_name,String service_version,String node) **
			
					Used to start the service
					
					service_name->			service name
					service_version->		Service version
					node->					Need to start this service node
					eg:
						start_service("*", "*", "*");
						start_service("serviceB|serviceD", "*", "*");
						
			
			
			2)	** public void stop_service(String service_name,String service_version,String node) **	
				** public void stop_service_id(String service_id,String node) **
				
					Used to stop the service
					
					eg:
						stop_service("serviceA", "*", "*");
						stop_service_id("service_1559206182267_20","*");
						
			...(Look at the code)			¡Ñ©n¡Ñ¡¬¨O¡ã 
		
		
		
		2.View various types of node information in control_service_as.java
		
			show_service_as_mcs("*");
			show_api_gate_mcs("*");
			show_service_as_api_gate("*");
			
			...(Look at the code) 		¡Ñ©n¡Ñ¡¬¨O¡ã 
		
## pressure test
	
		[TODO]	©¸(^o^)©¼;
	
## Compared with other frames

		[TODO]	¡Ñ©n¡Ñ¡¬¨O¡ã
	
## Supported language

		Currently open source version only supports PHP/java version
	
## Require
	
		mqtt
		jdk
		hprose
	
## [TODO]

		Currently, this version of open source does not have functions such as logging and burying monitoring.
		pressure test.
		Currently open source version of this microservice without python version.











		
		
