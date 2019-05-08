package rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

/**
 * 远程的实现
 *
 * @author wzy
 */
public class ServiceServerImpl extends UnicastRemoteObject implements ServiceServer {
    //服务会被存储在HashMap集合中
    HashMap serviceList;

    public ServiceServerImpl() throws RemoteException {
        setUpServices();
    }

    private void setUpServices() {
        serviceList = new HashMap();
        //构造函数调用时会将实际的通用服务初始化
        serviceList.put("Dice Rolling Service", new DiceService());
        serviceList.put("Day of the Week Service", new DayOfTheWeekService());
        serviceList.put("Visual Music Service", new MiniMusicService());
    }

    @Override
    public Object[] getServiceList() throws RemoteException {
        /*客户端会调用它以取得服务的清单，我们会送出Object的数组，只带有HashMap的key,
        * 实际服务会到用户要求时才通过getService()送出
        * */
        System.out.println("in remote");
        return serviceList.keySet().toArray();
    }

    @Override
    public Service getService(Object serviceKey) throws RemoteException {
        //根据名称返回相应服务
        Service theService = (Service) serviceList.get(serviceKey);
        return theService;
    }

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("ServiceServer", new ServiceServerImpl());
            /*,.rebind("ServiceServer",new ServiceServerImpl());*/
            System.out.println("Remote sevice is running");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}