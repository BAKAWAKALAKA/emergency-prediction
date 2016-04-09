package ru.spbstu.dis.ep.data.opc;

import org.jinterop.dcom.common.JIException;
import org.jinterop.dcom.common.JISystem;
import org.jinterop.dcom.core.JIVariant;
import org.openscada.opc.dcom.da.OPCSERVERSTATUS;
import org.openscada.opc.lib.common.AlreadyConnectedException;
import org.openscada.opc.lib.common.ConnectionInformation;
import org.openscada.opc.lib.common.NotConnectedException;
import org.openscada.opc.lib.da.AddFailedException;
import org.openscada.opc.lib.da.DataCallback;
import org.openscada.opc.lib.da.DuplicateGroupException;
import org.openscada.opc.lib.da.Group;
import org.openscada.opc.lib.da.Item;
import org.openscada.opc.lib.da.ItemState;
import org.openscada.opc.lib.da.Server;
import org.openscada.opc.lib.da.SyncAccess;
import org.openscada.opc.lib.da.browser.FlatBrowser;
import ru.spbstu.dis.ep.data.Tag;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;

public class OPCTreeReader {

    static {
        try {
            JISystem.setAutoRegisteration(true);
            JISystem.setInBuiltLogHandler(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static
    void main(String[] args) throws AlreadyConnectedException, JIException,
    UnknownHostException, NotConnectedException, DuplicateGroupException,
    AddFailedException, InterruptedException {
        final ConnectionInformation ci = new ConnectionInformation();
        ci.setHost("seal-machine1");
        ci.setUser("Administrator");
        ci.setPassword("seal");
        ci.setClsid("6F17505C-4351-46AC-BC1E-CDE34BB53FAA");
        // 2E565243-B238-11D3-842D-0008C779D775 6F17505C-4351-46AC-BC1E-CDE34BB53FAA  2E565242-B238-11D3-842D-0008C779D775
//        ci.setClsid("2E565242-B238-11D3-842D-0008C779D775");


        final Server server = new Server(ci, Executors.newSingleThreadScheduledExecutor());
        server.connect();
        OPCSERVERSTATUS serverState = server.getServerState();
        System.out.println("serverState = " + serverState.getBuildNumber());

                FlatBrowser flatBrowser = server.getFlatBrowser();

        flatBrowser.browse().forEach(System.out::println);

        SyncAccess syncAccess = new SyncAccess(server, 200);

      String item = Tag.TAG_TO_ID_MAPPING.get(Tag.REACTOR_DOWNSTREAM_ON);
        syncAccess.addItem(item, new DataCallback() {
            @Override
            public
            void changed(
                    Item item,
                    ItemState itemState) {
                System.out.println(item.getId());
                try {
                    boolean objectAsBoolean = itemState.getValue()
                                                       .getObjectAsBoolean();
                    System.out.println("objectAsBoolean = " + objectAsBoolean);
                } catch (JIException e) {
                    e.printStackTrace();
                }
            }
        });

        syncAccess.bind();
      final Group serverObject = server.addGroup("test");
      Item itemOPC = serverObject.addItem(item);
      boolean value = true;
        while (true)
        {Thread.sleep(2000);
         value = ! value;
          itemOPC.write(new JIVariant(value));
        }
    }
}
