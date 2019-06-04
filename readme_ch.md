### 轻量级多语言微服务框架(LM-MS)

### (Lightweight & multi-language microservices framework)
	
	一种兼容多语言、轻量级、高性能微服务框架，开发者只需要关心与业务逻辑相关的代码编写，
	无需过多的考虑部署，容错，负载等内容
	
## 开发本框架的初衷
		
		1. 	由于之前项目中后端需要对外提供多种服务，包括视频流传输，人脸faceid数据库同步，
			接收顾客消费数据，后台管理系统，店铺设备对接，接收客流统计数据等等，所以导致后端逻辑复杂，
			因此后端使用微服务的形式对外提供数据支持
			
		2. 	项目后端代码使用多种语言编写(java和php)，目前主流框架很少有支持多种语言的微服务
		
		3. 	目前主流微服务框架配置复杂，占用资源大
		
		4.	使用传统http调用效率较低，同时系统中有较多任务需要请求多次后端服务，导致性能损耗较大
		
		[注:]	之后有时间会写一份更详细的操作手册，到时候感兴趣的可以看一下	O(O_O)O

## 概述

		1.	本框架实现了一个多语言的微服务功能，可以通过本框架实现多语言的微服务编写，	→_→
			并对外统一提供服务(JAVA/PHP/PYTHON)	
		2.	本框架可以实现单节点多服务，和多节点多服务，多api网关多种模式的组合		O__O"
		3.	本框架与主流框架相比优势在于							O(O_O)O
				1）可以通过多种语言共同编写为应用提供后端服务
				2）轻量级
				3）部署简单，外部依赖较少
			
		4.	开发者可以最大程度的将工作的重心集中在编写和业务逻辑直接相关的服务代码	′(O﹏O)′
			上而不需要过多考虑与框架相关的内容		
		5.	上面这个开源的版本中可能存在部分bug，如果发现可以联系我	>_<|||
		6.	整个框架使用java编写，性能高效		O(O_O)O
			
		
## 微服务框架组成

		1.主控制节点 (main-control-server) 后面简称MCS
			主要用于接收外部控制命令，控制api网关和应用节点，并且监听心跳数据，
			和api网关共同维护服务表
			
		2.api网关(api-gateway)
			接收外部访问，并将外部访问以rpc形式转发到内部微服务
			提供流量控制，熔断，SLB，服务降级，等功能
			阻挡不正确协议等
			错误消息重定向等
		
		3.应用节点(app-server) 后面简称AS
			维护节点上的微服务应用，对外提供服务
			
## api网关
		
		1.黑白名单
			
			可以在网关配置文件中设置ip白名单或ip黑名单将限制ip拒绝到网关之外
			
		2.初步防止ddos(待完善) [TODO]
			
			本开源版本的框架只是简单的实现了一个通过ip来控制ddos的方法，之后会继续改进
		
		3.全局流量控制
		
			控制api网关的总体流量，当流量超出限制时，部分请求会被返回流量控制
			
		4.服务级流量控制		(使用token算法)
			
			本框架为每个服务单独添加了流量控制
			
		5.服务级熔断		
			
			当某个服务的流量超出限制时，网关会暂时停止将这个服务设为闭状态，
			之后一段时间会将服务变成半开状态，之后会转向开状态
			
			
		6.请求级别的缓存		(文本缓存/内存缓存，基于redis)
		
			本框架提供文档和内存两种缓存模式，可以在配置文件中设置缓存时间等
			内存缓存基于redis
			
			
		7.SLB负载均衡		(可使用随机法/轮询法/加权轮询法)
			
			框架提供了完整的负载均衡机制，可选算法随机法/轮询法/加权轮询法
			框架会将相同请求导向不同节点所在的同一个版本的服务
		
		8.服务降级			[TODO]
		
		
		
		9.聚合请求
			
			可以在网关配置文件中配置聚合请求，聚合请求是当发送一个http请求之后，
			这个http请求的数据会由斗个后端的微服务提供，目前只支持串行模式，
			之后会支持并行模式
		
		10.html转发
		
		11.同步/异步服务
		
		
		
## api网关处理流程
	
	![](https://github.com/ashjpo/LM-MS/blob/master/struct.jpg)
		
## 框架使用
	
		1.运行mic-service-run下	
			Run-MCS.bat->Run-API-GATEWAY.bat->Run-AS.bat
			分别是启动主控制节点，api网关，应用节点
		
		2.编写微服务
			在mic_service下面是应用节点的根目录，在本节点下面的serice文件夹中放微服务的文件包
			
		3.在mic_service下可以编写不同语言的微服务，只需要在json文件中指定
	
		
## 目录结构

		1.MCS根目录
			MCS
			|---config	配置文件路径
					|---api-gate.properties
					|---as.properties
					|---mqtt.properties
					|---mqtt_topic.properties
			
			
		2.api网关根目录
			Api_Gate
				|---aggregation			聚合请求配置文件路径
				|		|---aggregation_service.json
				|
				|---config	配置文件路径
						|---cache.properties
						|---ddos.properties
						|---flow_control.properties
						|---fusing.properties
						|---main.properties
						|---mqtt.properties
						|---mqtt_topic.properties
						|---server.properties
		
		
		
		3.AS根目录
			mic_service
				|---common	公共目录存放各种语言的公共库
				|---init	初始化脚本用于启动服务
				|---config	配置文件目录
				|---service	微服务程序存放目录
						|---serviceA	微服务名称
								|---common	本服务的公共库
								|---config	服务的配置文件json ***
								|---functions	服务程序
								|---test	测试程序
	
	

## 微服务编写示例(PHP版)

		本程序存放在functions文件夹中的mode1.class.php
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
		
		注：在同一个微服务中类名和方法名不能相同
	
## 微服务编写示例(JAVA版)

		本程序存放在functions文件夹中的testClass.jar
		
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
		
		注：在同一个微服务中类名和方法名不能相同
			
## 微服务配置json示例文件
	
		{	"name": "serviceB",													#微服务名称
			"call_type": "rpc",													#调用类型(rpc http)
			"service_type": "common",											
			"version": "v1.0",													#微服务版本
			"service_host": "127.0.0.1",										#ip
			"service_url": "/b",												#微服务路由地址
			"version_code": 1,													#版本号
			"language": "php",													#编写语言
			"api-gate": "api-gate1",											#暴露的网关
			"service_mes": "aaa",											
			"mods": [{															#一个模块
				"name": "mode1",												#模块名称
				"file_path": "D:/mic_service/service/serviceB/functions/mode1.class.php",			#模块文件路径
				"functions": [{													#方法
					"name": "a1",												#方法名
					"syn_asyn": "syn",											#同步或异步	(syn/asyn)
					"http_url": "/url1",										#http路由地址
					"params": [],												
					"cache": "memory"											#是否缓存(""/"text"/"memory")
				}, {
					"name": "b1",
					"syn_asyn": "syn",
					"http_url": "/url2",
					"params": [
						["http_a", "a", "get"]									#传参列表(第一个是网关外请求的参数key，第二个是函数对应的变量名称)
					],
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
	
## 聚合请求json示例文件

		aggregation文件夹下的aggregation_service.json文件
		
			[{
				"call_url": "/aaa",									#网关外调用路由
				"serial_parallel": "parallel",						#串行或并行(这个版本只支持串行)
				"ask": ["/b/url1", "/b/url2"],						#网关内请求的微服务url(和外部单独请求的url相同)
				"key": ["key1", "key2"]
			}]
			
		返回示例:
		
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

## 控制命令
	
		控制命令.md文件	O(∩_∩)O
	
## 控制程序
	
		程序里面提供了一个快速发送mqtt命令控制节点的类和一个查看节点信息的类
		
		1.在send_service_control_commond中control_service_as.java用于发送控制信息
		
			1)	** public void start_service(String service_name,String service_version,String node) **
					用于启动服务
					service_name->			服务名称
					service_version->		服务版本
					node->					需要启动本服务节点
					eg:
						start_service("*", "*", "*");
						start_service("serviceB|serviceD", "*", "*");
						
			
			
			2)	** public void stop_service(String service_name,String service_version,String node) **	
				** public void stop_service_id(String service_id,String node) **
					用于停止服务
					eg:
						stop_service("serviceA", "*", "*");
						stop_service_id("service_1559206182267_20","*");
						
			...(具体看代码)			⊙﹏⊙‖∣° 
		
		
		
		2.在control_service_as.java中查看各类节点信息
		
			show_service_as_mcs("*");
			show_api_gate_mcs("*");
			show_service_as_api_gate("*");
			
			...(具体看代码) 		⊙﹏⊙‖∣° 

## 压力测试

		[TODO]	└(^o^)┘; 
	
## 与其他框架对比

		[TODO]	⊙﹏⊙‖∣°

## 支持的语言
	
		目前开源的这个版本只支持PHP/java版本
	
## 依赖

		mqtt
		jdk
		hprose

## [TODO]
	
		目前开源的这个版本不带日志记录和埋点监测等功能
		压力测试
		目前开源的这个版本不带python版本的微服务

		
		

		

