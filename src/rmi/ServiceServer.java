package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * 远程服务用的RMI普通接口
 *
 * @author wzy
 */
public interface ServiceServer extends Remote {
    Object[] getServiceList() throws RemoteException;

    Service getService(Object serviceKey) throws RemoteException;
}
