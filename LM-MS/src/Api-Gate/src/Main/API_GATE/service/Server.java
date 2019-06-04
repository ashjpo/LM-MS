package Main.API_GATE.service;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Context.Main_Context;


/**  
 * @Title:  Server.java   
 * @Description: �򿪷���
 * @author: Han   
 * @date:   2016��7��12�� ����7:22:47  
 */  
public class Server implements Runnable {
	int pooling_num=100;
	ExecutorService fixedThreadPool = Executors.newFixedThreadPool(pooling_num);

    private boolean interrupted = false;
    Main_Context mc;
    public Server(Main_Context mc) {
        this.mc = mc;
    }

    @Override
    public void run() {
        try {
            //��һ��ѡ����
            Selector selector = Selector.open();
            //��ServerSocketChannelͨ��
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            //�õ�ServerSocket����
            ServerSocket serverSocket = serverSocketChannel.socket();
            //ServerSocketChannelͨ������server.xml�����õĶ˿�
            //String portStr = XMLUtil.getRootElement("server.xml").element("port").getText(); 
            String portStr = this.mc.config_obj.server_port;
            serverSocket.setReuseAddress(true);  
            try {
                serverSocket.bind(new InetSocketAddress(Integer.parseInt(portStr)));
            } catch (Exception e) {
                //logger.error("�󶨶˿�ʧ��,����server.xml���Ƿ�������port����");
                return;
            }
            //logger.info("�ɹ��󶨶˿�" + portStr);
            //��ͨ������Ϊ������ģʽ
            serverSocketChannel.configureBlocking(false);
            //��serverSocketChannelע���ѡ����,����ACCEPT�¼�
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            //logger.info("�����������ɹ�");
            while(!interrupted) {
                //��ѯ������ͨ������
                int readyChannels = selector.select();
                
                //û�о��������������ѭ��
                if(readyChannels == 0)
                    continue;
                //��þ�����selectionKey��set����
                Set<SelectionKey> keys = selector.selectedKeys();
                //���set���ϵĵ�����
                Iterator<SelectionKey> iterator = keys.iterator();
                while(iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    try {
	                    if(key.isAcceptable()) {
	                        //��key��ACCEPT�¼�
	                        //�������õ���channelǿתΪServerSocketChannel
	                        ServerSocketChannel server = (ServerSocketChannel) key.channel();
	                        //�õ����յ���SocketChannel
	                        SocketChannel socketChannel = server.accept();
	                        if(socketChannel != null) {
	                            //logger.info("�յ�������" + ((InetSocketAddress)socketChannel.getRemoteAddress()).getHostString()
	                                    //+ "������");
	                            //��socketChannel����Ϊ����ģʽ
	                            socketChannel.configureBlocking(false);
	                            //��socketChannelע�ᵽѡ����
	                            socketChannel.register(selector, SelectionKey.OP_READ);
	                        }
	                    } else if (key.isReadable()) {
	                    	if(!key.isValid()) {
	                    		key.cancel();
	                    		continue;
	                    	}
	                    	//System.out.println("E");
	                        //��key��Read�¼�
	                        SocketChannel socketChannel = (SocketChannel) key.channel();
	                        String requestHeader = "";
	                        //�ó�ͨ���е�Httpͷ����
	                        try {
	                            requestHeader = receive(socketChannel);
	                        } catch (Exception e) {
	                            //logger.error("��ȡsocketChannel����");
	                            return;
	                        }
	                        //�����̴߳��������,if�����ж�һ�£���ֹ������
	                        if(requestHeader.length() > 0) {
	                            //new Thread(new HttpRequestDealer(requestHeader, key)).start();
	                            fixedThreadPool.execute(new HttpRequestDealer(requestHeader, key,mc));
	                        }
	                    } else if (key.isWritable()) {
	                    	//System.out.println("D");
	                        //��key��Write�¼�
	                        //logger.info("����д��!");
	                        SocketChannel socketChannel = (SocketChannel) key.channel();
	                        socketChannel.shutdownInput();
	                        socketChannel.close();
	                    }else if(key.isConnectable()) {
	                    	//System.out.println("B");
	                    }else if(key.isValid()) {
	                    	
	                    	//System.out.println("C");
	                    }
                     } catch (Exception e) {
                    	 
                    	 key.cancel();
                     }
                    //��key������ɾ��key����һ������Ҫ��������Ϊûд��䣬Selector.select()����һֱ���ص���0
                    //ԭ����������ǲ��Ӽ�����ɾ�����Ͳ���ص�I/O�����¼���
                    iterator.remove();
                }
            }
        
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private String receive(SocketChannel socketChannel) throws Exception {
        //����һ��1024��С�Ļ�����
        ByteBuffer buffer = ByteBuffer.allocate(1024);  
        byte[] bytes = null;  
        int size = 0;
        //����һ���ֽ����������
        ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
        //��socketChannel�е�����д�뵽buffer�У���ʱ��bufferΪдģʽ��sizeΪд�˶��ٸ��ֽ�
        while ((size = socketChannel.read(buffer)) > 0) {
            //��дģʽ��Ϊ��ģʽ
            //The limit is set to the current position and then the position is set to zero.
            //��limit����Ϊ֮ǰ��position������position��Ϊ0������java nio��֪ʶ��д�ɲ��͵�
            buffer.flip();
            bytes = new byte[size];
            //��Bufferд�뵽�ֽ�������
            buffer.get(bytes);
            //���ֽ�����д�뵽�ֽڻ�������
            baos.write(bytes);
            //��ջ�����
            buffer.clear();
        }
        //����ת���ֽ�����
        bytes = baos.toByteArray();
        return new String(bytes);
    }
    
    /*public static void main(String[] args) {
		Server server=new Server(false);
		server.run();
	}*/
}