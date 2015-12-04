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
import ru.spbstu.dis.Tag;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class OPCDataReader {
  private final HashMap<Tag, String> tagsToRead;

  private final Map<Tag, Double> actualValues = Maps.newHashMap();

  private AccessBase opcDataAccess;

  public OPCDataReader(HashMap<Tag, String> tagToOpcIdMapping) {
    this.tagsToRead = tagToOpcIdMapping;
  }

  public Map<Tag, Double> getActualValues() {
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

  private void registerRequestedTags(final HashMap<Tag, String> tagsToRead)
  throws UnknownHostException, JIException, AlreadyConnectedException, NotConnectedException,
      DuplicateGroupException {

    final ConnectionInformation ci = new ConnectionInformation();
    ci.setHost("seal-machine1");
    ci.setUser("Administrator");
    ci.setPassword("seal");
    //    ci.setClsid("6F17505C-4351-46AC-BC1E-CDE34BB53FAA");
    ci.setClsid("2E565242-B238-11D3-842D-0008C779D775");

    final Server server = new Server(ci, Executors.newSingleThreadScheduledExecutor());
    server.connect();
    opcDataAccess = new SyncAccess(server, 200);


    tagsToRead.forEach((tag, opcId) -> {
      try {
        opcDataAccess.addItem(opcId, new DataCallback() {
          public void changed(Item item, ItemState state) {
            try {
              final double doubleValuue = state.getValue().getObjectAsDouble();
              actualValues.put(tag, doubleValuue);
            } catch (JIException e) {
              e.printStackTrace();
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

  static {
    try {
      JISystem.setAutoRegisteration(true);
      JISystem.setInBuiltLogHandler(false);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
