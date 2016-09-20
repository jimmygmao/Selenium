import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
/**
 * �ͻ��˷������ݵ��������ˣ������ܻ���
 * @author lenovo
 *
 */
public class NetClient {
	public int udpPort;

	TankClient tc;
	
	DatagramSocket ds=null;
	
	public String IP;
	
	public void setUdpPort(int udpPort) {
		this.udpPort = udpPort;
	}
	
	public NetClient(TankClient tc){
		this.tc=tc;
	}
	/**
	 * ���ӵ�������
	 * @param IP
	 * @param port
	 */
	public void connect(String IP,int port){
		this.IP=IP;
		try {
			ds=new DatagramSocket(udpPort);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		Socket s=null;
		try {
			s=new Socket(IP,port);
			DataOutputStream dos=new DataOutputStream(s.getOutputStream());
			dos.writeInt(udpPort);
			DataInputStream dis=new DataInputStream(s.getInputStream());
			int id=dis.readInt();
			tc.myTank.id=id;
			if(id%2==0){
				tc.myTank.setGood(false);
			}else{
				tc.myTank.setGood(true);
			}
			System.out.println("�ͻ������ӵ�������");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(s!=null){
				try {
					s.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				s=null;
			}	
		}
		/**
		 * ̹�˵�һ������
		 * UDP����̹�˵�����
		 */
		TankNewMsg msg=new TankNewMsg(tc.myTank);	
		send(msg);
		new Thread(new UDPpecvThread()).start();
	}
	/**
	 * �ͻ��˻�ȡ̹�˵�����
	 * @param msg
	 */
	public void send(Msg msg){
		msg.send(ds,IP,TankServer.UDP_PORT);
	}
	/**
	 * �ͻ��˻�ȡ������������
	 * @author lenovo
	 *
	 */
	private class UDPpecvThread implements Runnable{
		byte[] buf=new byte[1024];
		public void run() {
			System.out.println("�߳������ɹ�");
			while(ds!=null){
				DatagramPacket dp=new DatagramPacket(buf,buf.length);//newһ������װ����
				try {
					ds.receive(dp);//�õ�����
				//	System.out.println("��Server�õ�����");
					parse(dp);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		/**
		 * ������Ϣ
		 * @param dp
		 */
		private void parse(DatagramPacket dp){

			ByteArrayInputStream bais=new ByteArrayInputStream(buf,0,dp.getLength());
			DataInputStream dis=new DataInputStream(bais);
			int msgType=0;
			try {
				msgType=dis.readInt();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Msg msg=null;
			switch(msgType){
			case Msg.TANK_NEW_MSG:
				msg=new TankNewMsg(NetClient.this.tc);//NetClient.this.tc�ڲ������������Ķ���
				msg.parse(dis);
				break;
			case Msg.TANK_MOVE_MSG:
				msg=new TankMoveMsg(NetClient.this.tc);//NetClient.this.tc�ڲ������������Ķ���
				msg.parse(dis);
				break;
			case Msg.MISSILE_MOVE_MSG:
				msg=new MissileNewMsg(NetClient.this.tc);
				msg.parse(dis);
				break;
			case Msg.TANK_DEAD_MSG:
				msg=new TankDeadMsg(NetClient.this.tc);
				msg.parse(dis);
				break;
			case Msg.MISSILE_DEAD_MSG:
				msg=new MissileDeadMsg(NetClient.this.tc);
				msg.parse(dis);
				break;
			}
		}
	}
}
