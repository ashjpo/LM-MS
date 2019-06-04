package Tools.mqtt;


import java.util.LinkedList;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MyMqtt {
    private MqttClient client;
    private MqttConnectOptions options;
    String disconnect_topic;
    String disconnect_message;
    boolean if_connect=false;
    
    //鐢ㄤ簬鍥炶皟
    LinkedList<My_MqttCallBack> mqtt_list=new LinkedList<>();
    
    public MyMqtt(String host,String port,String userName,String passWord,String disconnect_topic,String disconnect_message){
        this.disconnect_topic=disconnect_topic;
        this.disconnect_message=disconnect_message;
        try {
            client = new MqttClient("tcp://" + host + ":" + port, "mcs_user"+System.currentTimeMillis(), new MemoryPersistence());
            //System.out.println("tcp://" + host + ":" + port);
        } catch (MqttException e) {
            e.printStackTrace();
        }

        options = new MqttConnectOptions();
        options.setCleanSession(true);
        options.setUserName(userName);
        options.setPassword(passWord.toCharArray());
        options.setConnectionTimeout(10);
        options.setKeepAliveInterval(20);

        //娑撳瓨妞�
        options.setWill(client.getTopic(this.disconnect_topic), this.disconnect_message.getBytes(), 1, false);

        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                //杩炴帴鏂紑
            	for(int i=0;i<mqtt_list.size();i++) {
            		mqtt_list.get(i).connectionLost();
            	}
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                
            	
            }

            @Override
            public void messageArrived(String topicName, MqttMessage message)
                    throws Exception {
                //鏀跺埌娑堟伅
            	for(int i=0;i<mqtt_list.size();i++) {
            		mqtt_list.get(i).messageArrived(topicName,message.toString());
            	}
            }
        });
        //MqttTopic topic = client.getTopic(this.disconnect_topic);
        //options.setWill(topic, disconnect_message.getBytes(), 2, true);

    }
    
    public void set_callback(My_MqttCallBack mc) {
    	mqtt_list.add(mc);
    }

    public void send_message(String msg,String topic) {
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setQos(2);
        mqttMessage.setRetained(false);
        mqttMessage.setPayload(msg.getBytes());
        MqttDeliveryToken token;
        try {
            //token = this.client.getTopic(this.brand+"_"+this.shop+"_"+topic).publish(mqttMessage);
            //System.out.println(topic);
            token = this.client.getTopic(topic).publish(mqttMessage);
            token.waitForCompletion();
        } catch (MqttException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void connect() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                if(!if_connect) {
                    try {
                        //瀹屾垚杩炴帴
                    	client.connect(options);
                        if_connect = true;
                        for(int i=0;i<mqtt_list.size();i++) {
                    		mqtt_list.get(i).connect();
                    	}
                    } catch (Exception e) {
                    	//杩炴帴鍑洪敊
                        if_connect = false;
                        e.printStackTrace();
                        for(int i=0;i<mqtt_list.size();i++) {
                    		mqtt_list.get(i).connectError();
                    	}
                    }
                }
            }

        }).start();
    }

    public void add_subscribe(String topic) {
        try {
            client.subscribe(topic,2);
        } catch (MqttException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}