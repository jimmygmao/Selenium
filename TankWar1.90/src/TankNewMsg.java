import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class TankNewMsg implements Msg{
	Tank tank;
	TankClient tc;
	int msgType=Msg.TANK_NEW_MSG;
	public TankNewMsg(TankClient tc){
		this.tc=tc;
	}
	public TankNewMsg(Tank tank){
		this.tank=tank;
	}
	/**
	 * 把坦克的数据发送给服务器端
	 * @param ds
	 * @param IP
	 * @param udpPort
	 */
	public void send(DatagramSocket ds,String IP,int udpPort) {
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		DataOutputStream dos=new DataOutputStream(baos);
		try {
			dos.writeInt(msgType);
			dos.writeInt(tank.id);
			dos.writeInt(tank.x);
			dos.writeInt(tank.y);
			dos.writeInt(tank.dir.ordinal());//枚举类型的数组的下标值
			dos.writeBoolean(tank.good);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] buf=baos.toByteArray();
		try {
			DatagramPacket dp=new DatagramPacket(buf,buf.length,new InetSocketAddress(IP,udpPort));
			ds.send(dp);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 接受数据
	 */
	public void parse(DataInputStream dis){

		try {
			int id=dis.readInt();
			/**
			 * 如果存在这个坦克，则退出
			 */
			if(tc.myTank.id==id){
				return;
			}
			TankNewMsg tnMsg=new TankNewMsg(tc.myTank);
			tc.nc.send(tnMsg);
			int x=dis.readInt();
			int y=dis.readInt();
			Direction dir=Direction.values()[dis.readInt()];
			Boolean good=dis.readBoolean();
			/**
			 * 如果存在，则将新的dir赋予它
			 * 如果不存在，则new一个坦克，添加到容器
			 */
			boolean exist=false;
			for(int i=0;i<tc.tanks.size();i++){
				Tank t=tc.tanks.get(i);
				if(t.id==id){//如果存在这个坦克
					exist=true;
					t.dir=dir;
					break;
				}
			}
			if(!exist){
				Tank t=new Tank(x,y,good,dir,tc);
				t.id=id;
				tc.tanks.add(t);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
