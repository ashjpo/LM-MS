package Tools.mqtt;

import org.eclipse.paho.client.mqttv3.MqttMessage;

public interface My_MqttCallBack {
	void connect();
	void connectError();
    void connectionLost();
    void messageArrived(String topicName, String message);
    
}