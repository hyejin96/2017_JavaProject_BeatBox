package beatBoxProject;

import java.io.*; // 입출력과 관련된 기능 제공
import java.net.*; // 네트워크와 관련된 기능 제공하며 각종 프로토콜을 제공
import java.util.*; // 유틸리티 클래스 포함

 
public class BeatBoxServer {
	//클라이언트한테 보낼 outputStream을 담을 ArrayList
    private ArrayList<ObjectOutputStream> outputStreamList; 
    
    public static void main(String[] args) {
    	// "서버메인시작" 메세지 출력
        System.out.println("서버메인시작."); 
     // 새로운 BeatBoxServer로 이동
        new BeatBoxServer().go(); 
    }
    /* 클라이언트로 부터 데이터를 받아오는 스레드 */ 
    class MyServerThread implements Runnable {  
    	// 클라이언트로부터 데이터를 불러올 inputStream 생성
        private ObjectInputStream inputStream; 
     // 클라이언트 Socket 생성
        Socket clientSocket; 
        
        public MyServerThread(Socket socket) {
            try {
            	// 클라이언트 Socket
                clientSocket = socket; 
             // 클라이언트로부터 불러온 데이터 inputStream에 저장
                inputStream = new ObjectInputStream(clientSocket.getInputStream()); 
             // 서버 inputStream 생성 메세지 출력
                System.out.println("서버 인풋스트림생성"); 
            } catch (IOException e) {
                e.printStackTrace();
            } // 예외처리
        } // 생성자 끝
        @Override
        public void run(){
        	// Object o1 생성
            Object o1 = null;
         // Object o2 생성
            Object o2 = null; 
            try{
                while((o1 = inputStream.readObject())!=null){
                	 // Object o2에 접근한 객체를 readObject() 사용하여 Object로 반환 
                    o2 = inputStream.readObject(); 
                    // read two object 메세지 출력
                    System.out.println("read two objects"); 
                    tellEveryOne(o1, o2);
                }
            }catch(Exception e){
                e.printStackTrace();
            } //예외처리      
        } // run 끝
    } // 내부 클래스 끝
    public void go(){
    	// 클라이언트에게 보낼 outputStreamList 생성
        outputStreamList = new ArrayList<ObjectOutputStream>(); 
        
        try {
        	// 서버 Socket 포트 번호 4242로 설정 
            ServerSocket serverSocket = new ServerSocket(4242); 
            
            while(true){
            	// 대기 상태 메세지 출력
                System.out.print("클라이언트 기다리는 중.. "); 
                // 서버에서 클라이언트와 통신하기 위한 새로운 Socket을 만든다.
                Socket clntSocket = serverSocket.accept(); 
				// 클라이언트 접속 수락, 접속 성공 메세지 출력
                System.out.println(clntSocket.getInetAddress()+"에서 접속하였습니다. "); 
                // 새로운 클라이언트 outputStream 생성
                ObjectOutputStream outputStream = new ObjectOutputStream(clntSocket.getOutputStream());
                // 리스트에 outputStream 추가 
                outputStreamList.add(outputStream); 
                // 새로운 클라이언트 Socket 스레드 생성
                Thread myServerThread = new Thread(new MyServerThread(clntSocket));
                // 스레드 시작
                myServerThread.start(); 
            }
        } catch (Exception e) {
            e.printStackTrace();
        } // 예외처리
    } // go 메소드 끝
    
    public void tellEveryOne(Object one, Object two){
    	// 컬렉션 클래스인 outputStreamList를 읽기 위한 iterator 생성
        Iterator iterator = outputStreamList.iterator(); 
        // 변수에 다음 데이터가 있을 때까지 출력
        while(iterator.hasNext()){ 
            try{
            	// 데이터가 없을 때까지 계속 반복
                ObjectOutputStream outputStream = (ObjectOutputStream)iterator.next(); 
                // outputStream 객체에 Object 형식으로 적는다
                outputStream.writeObject(one); 
                // outputStream 객체에 Object 형식으로 적는다
                outputStream.writeObject(two); 
            }catch(Exception e){
                e.printStackTrace();
            } // 예외처리
        }
    } // tellEveyone 메소드 끝
} // 클래스 끝
 