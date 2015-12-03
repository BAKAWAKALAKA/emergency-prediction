package ru.spbstu.dis.opc;

import com.google.common.collect.Maps;
import org.jinterop.dcom.common.JIException;
import org.jinterop.dcom.common.JISystem;
import org.openscada.opc.lib.common.AlreadyConnectedException;
import org.openscada.opc.lib.common.ConnectionInformation;
import org.openscada.opc.lib.common.NotConnectedException;
import org.openscada.opc.lib.da.AccessBase;
import org.openscada.opc.lib.da.AddFailedException;
import org.openscada.opc.lib.da.DataCallback;
import org.openscada.opc.lib.da.DuplicateGroupException;
import org.openscada.opc.lib.da.Item;
import org.openscada.opc.lib.da.ItemState;
import org.openscada.opc.lib.da.Server;
import org.openscada.opc.lib.da.SyncAccess;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

public class OPCDataReader {
  static {
    try {
      JISystem.setAutoRegisteration(true);
      JISystem.setInBuiltLogHandler(false);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private final List<String> tagsToRead;

  private final Map<String, Double> actualValues = Maps.newHashMap();

  private AccessBase opcDataAccess;

  public OPCDataReader(List<String> tagsToRead) {
    this.tagsToRead = tagsToRead;
  }

  public Map<String, Double> getActualValues() {
    return actualValues;
  }

  public OPCDataReader startReading() {
    try {
      registerRequestedTags(tagsToRead);
    } catch (UnknownHostException e) {
      e.printStackTrace();
    } catch (JIException e) {
      e.printStackTrace();
    } catch (AlreadyConnectedException e) {
      e.printStackTrace();
    } catch (NotConnectedException e) {
      e.printStackTrace();
    } catch (DuplicateGroupException e) {
      e.printStackTrace();
    }
    opcDataAccess.bind();
    return this;
  }

  private void registerRequestedTags(final List<String> tagsToRead)
  throws UnknownHostException, JIException, AlreadyConnectedException, NotConnectedException,
      DuplicateGroupException {

    final ConnectionInformation ci = new ConnectionInformation();
    ci.setHost("seal-machine1");
    ci.setUser("Administrator");
    ci.setPassword("seal");
    ci.setClsid("6F17505C-4351-46AC-BC1E-CDE34BB53FAA");
    final Server server = new Server(ci, Executors.newSingleThreadScheduledExecutor());
    server.connect();
    opcDataAccess = new SyncAccess(server, 200);
    tagsToRead.forEach(s -> {
      try {
        opcDataAccess.addItem(s, new DataCallback() {
          public void changed(Item item, ItemState state) {
            try {
              final double doubleValuue = state.getValue().getObjectAsDouble();
              actualValues.put(item.getId(), doubleValuue);
            } catch (JIException e) {
            }
          }
        });
      } catch (JIException e) {
        e.printStackTrace();
      } catch (AddFailedException e) {
        e.printStackTrace();
      }
    });
  }

  public void stopReading() {
    try {
      opcDataAccess.unbind();
    } catch (JIException e) {
      e.printStackTrace();
    }
  }
}
