package Main.API_GATE.functions.flow;


import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;


/**
 * <pre>
 * Created by inter12 on 15-3-18.
 * </pre>
 */
public class TokenBucket {

    // Ĭ��Ͱ��С���� �����˲��������64M
    private int default_bucket_size = 1024 * 1024 * 64;

    // һ��Ͱ�ĵ�λ��1�ֽ�
    private int everyTokenSize = 1;

    // ˲���������
    private int maxFlowRate;

    // ƽ������
    private int avgFlowRate;

    // ����������Ͱ����������������ֵ���� = everyTokenSize*DEFAULT_BUCKET_SIZE 64M = 1 * 1024 *
    // 1024 * 64
    private ArrayBlockingQueue<Byte> tokenQueue = new ArrayBlockingQueue<Byte>(default_bucket_size);

    private ScheduledExecutorService scheduledExecutorService = Executors
            .newSingleThreadScheduledExecutor();

    private volatile boolean isStart = false;

    private ReentrantLock lock = new ReentrantLock(true);

    private static final byte A_CHAR = 'a';

    public TokenBucket() {
    }

    public TokenBucket(int maxFlowRate, int avgFlowRate) {
        this.maxFlowRate = maxFlowRate;
        this.avgFlowRate = avgFlowRate;
    }

    public TokenBucket(int everyTokenSize, int maxFlowRate, int avgFlowRate) {
        this.everyTokenSize = everyTokenSize;
        this.maxFlowRate = maxFlowRate;
        this.avgFlowRate = avgFlowRate;
    }

    public void addTokens(Integer tokenNum) {

        // ����Ͱ�Ѿ����ˣ��Ͳ��ټ����µ�����
        for (int i = 0; i < tokenNum; i++) {
            tokenQueue.offer(Byte.valueOf(A_CHAR));
        }
    }

    public TokenBucket build() {

        start();
        return this;
    }

    /**
     * ��ȡ�㹻�����Ƹ���
     * 
     * @return
     */
    public boolean getTokens(byte[] dataSize) {

//        Preconditions.checkNotNull(dataSize);
//        Preconditions.checkArgument(isStart,
//                "please invoke start method first !");

        int needTokenNum = dataSize.length / everyTokenSize + 1;// �������ݴ�С��Ӧ��Ͱ����

        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            boolean result = needTokenNum <= tokenQueue.size(); // �Ƿ�����㹻��Ͱ����
            if (!result) {
                return false;
            }

            int tokenCount = 0;
            for (int i = 0; i < needTokenNum; i++) {
                Byte poll = tokenQueue.poll();
                if (poll != null) {
                    tokenCount++;
                }
            }

            return tokenCount == needTokenNum;
        } finally {
            lock.unlock();
        }
    }

    public void start() {

        // ��ʼ��Ͱ���д�С
        if (maxFlowRate != 0) {
            tokenQueue = new ArrayBlockingQueue<Byte>(maxFlowRate);
        }

        // ��ʼ������������
        TokenProducer tokenProducer = new TokenProducer(avgFlowRate, this);
        scheduledExecutorService.scheduleAtFixedRate(tokenProducer, 0, 1,
                TimeUnit.SECONDS);
        isStart = true;

    }

    public void stop() {
        isStart = false;
        scheduledExecutorService.shutdown();
    }

    public boolean isStarted() {
        return isStart;
    }

    class TokenProducer implements Runnable {

        private int avgFlowRate;
        private TokenBucket tokenBucket;

        public TokenProducer(int avgFlowRate, TokenBucket tokenBucket) {
            this.avgFlowRate = avgFlowRate;
            this.tokenBucket = tokenBucket;
        }

        @Override
        public void run() {
            tokenBucket.addTokens(avgFlowRate);
        }
    }

    public static TokenBucket newBuilder() {
        return new TokenBucket();
    }

    public TokenBucket everyTokenSize(int everyTokenSize) {
        this.everyTokenSize = everyTokenSize;
        return this;
    }

    public TokenBucket maxFlowRate(int maxFlowRate) {
        this.maxFlowRate = maxFlowRate;
        return this;
    }
    
    public TokenBucket backet_size(int BUCKET_SIZE) {
    	this.default_bucket_size=BUCKET_SIZE;
    	tokenQueue = new ArrayBlockingQueue<Byte>(default_bucket_size);
        return this;
	}

    public TokenBucket avgFlowRate(int avgFlowRate) {
        this.avgFlowRate = avgFlowRate;
        return this;
    }

    private String stringCopy(String data, int copyNum) {

        StringBuilder sbuilder = new StringBuilder(data.length() * copyNum);

        for (int i = 0; i < copyNum; i++) {
            sbuilder.append(data);
        }

        return sbuilder.toString();

    }

    public static void main(String[] args) throws IOException,
            InterruptedException {

        tokenTest();
    }

    private static void arrayTest() {
        ArrayBlockingQueue<Integer> tokenQueue = new ArrayBlockingQueue<Integer>(
                10);
        tokenQueue.offer(1);
        tokenQueue.offer(1);
        tokenQueue.offer(1);
        System.out.println(tokenQueue.size());
        System.out.println(tokenQueue.remainingCapacity());
    }

    private static void tokenTest() throws InterruptedException, IOException {
        TokenBucket tokenBucket = TokenBucket.newBuilder().avgFlowRate(512).maxFlowRate(1024).build();

        BufferedWriter bufferedWriter = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream("D:/ds_test")));
        String data = "xxxx";// �ĸ��ֽ�
        for (int i = 1; i <= 1000; i++) {

            Random random = new Random();
            int i1 = random.nextInt(100);
            boolean tokens = tokenBucket.getTokens(tokenBucket.stringCopy(data,
                    i1).getBytes());
            TimeUnit.MILLISECONDS.sleep(100);
            if (tokens) {
                bufferedWriter.write("token pass --- index:" + i1);
                System.out.println("token pass --- index:" + i1);
            } else {
                bufferedWriter.write("token rejuect --- index" + i1);
                System.out.println("token rejuect --- index" + i1);
            }

            bufferedWriter.newLine();
            bufferedWriter.flush();
        }

        bufferedWriter.close();
    }

}