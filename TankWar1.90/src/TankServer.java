import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
/**
 * ����TCP�ļ���������UDP�ļ���
 * �������˽������ݵ��ͻ��ˣ������͵������ͻ���
 * @author lenovo
 *
 */
public class TankServer {
	private static int ID=100;
	public static final int TCP_PORT=8888;
	public static final int UDP_PORT=6666;	
	
	List<Client> clients =new ArrayList<Client>();//������ٿͻ���
	
	public static void main(String[] args){
		new TankServer().start();//����Server
	}
	/**
	 * ����UDP�̣߳������̵߳���TCP��ʹ��
	 */
	public void start(){
		new Thread(new UDPThread()).start();//����UDP���߳�
		ServerSocket ss=null;
		try {
			ss=new ServerSocket(TCP_PORT);//�����̵߳���TCP��ʹ��
		} catch (IOException e) {
			e.printStackTrace();
		}
		while(true){
			Socket s=null;
			try {
				s=ss.accept();//��TCP���Ƚ��ܿͻ��˵�����
				DataInputStream dis=new DataInputStream(s.getInputStream());
				int udpPort=dis.readInt();
				String IP=s.getInetAddress().getHostAddress();
				Client c=new Client(IP,udpPort);//�ѽ���Ҫʹ�õ�UDP��IP���˿ںŴ��ݽ���
				clients.add(c);
				System.out.println("A Client Connect! Addr- "+s.getInetAddress()+":"+s.getPort()+"----UDP Port"+udpPort);
				DataOutputStream dos=new DataOutputStream(s.getOutputStream());
				dos.writeInt(ID++);
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				if(s!=null){
					try {
						s.close();
						s=null;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	/**
	 * ����UDP��IP���˿�
	 * @author lenovo
	 *
	 */
	public class Client{
		String IP;
		int udpPort;//����UDP�˿ں�
		public Client(String IP,int udpPort){
			this.IP=IP;
			this.udpPort=udpPort;
		}
	}
	/**
	 * ��Ϊ�������UDP��IP���˿ڣ���������UDP���߳�
	 * @author lenovo
	 *
	 */
	private class UDPThread implements Runnable{
		/**
		 * �������˻�ȡ�ͻ�������
		 */	
		byte[] buf=new byte[1024];//װ����
		public void run() {
			DatagramSocket ds=null;
			try {
				ds=new DatagramSocket(UDP_PORT);
			} catch (SocketException e) {
				e.printStackTrace();
			}
			while(ds!=null){
				DatagramPacket dp=new DatagramPacket(buf,buf.length);//newһ������װ����
				try {
					ds.receive(dp);//�õ�����
					for(int i=0;i<clients.size();i++){
						Client c=clients.get(i);
						dp.setSocketAddress(new InetSocketAddress(c.IP,c.udpPort));//��������ݸ��ݵ�ַ���ͳ�ȥ
						ds.send(dp);//����
					}
					//System.out.println("�ӿͻ����õ�����");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
