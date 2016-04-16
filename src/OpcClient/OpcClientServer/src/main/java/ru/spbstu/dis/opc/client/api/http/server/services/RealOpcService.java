package ru.spbstu.dis.opc.client.api.http.server.services;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.inject.Inject;
import com.typesafe.config.Config;
import static java.util.stream.Collectors.toSet;
import org.jinterop.dcom.common.JIException;
import org.jinterop.dcom.common.JISystem;
import org.jinterop.dcom.core.JIVariant;
import org.openscada.opc.dcom.da.OPCSERVERSTATUS;
import org.openscada.opc.lib.common.AlreadyConnectedException;
import org.openscada.opc.lib.common.ConnectionInformation;
import org.openscada.opc.lib.common.NotConnectedException;
import org.openscada.opc.lib.da.AddFailedException;
import org.openscada.opc.lib.da.DuplicateGroupException;
import org.openscada.opc.lib.da.Group;
import org.openscada.opc.lib.da.Item;
import org.openscada.opc.lib.da.Server;
import org.openscada.opc.lib.da.SyncAccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Function;

public class RealOpcService implements OpcService {

  private static final Logger LOGGER = LoggerFactory.getLogger(RealOpcService.class);

  private final Server server;

  private final Group group;

  @Inject
  public RealOpcService(final Server server, final Group group) {
    this.server = server;
    this.group = group;
  }

  @Override
  public Set<String> tags() {
    try {
      return server.getFlatBrowser().browse().stream().collect(toSet());
    } catch (UnknownHostException | JIException e) {
      LOGGER.error("Error occurred during browsing tags", e);
      throw new IllegalStateException(e);
    }
  }

  @Override
  public void writeValueForTag(final String tag, final Boolean value) {
    try {
      final Item addedItem = group.addItem(tag);
      addedItem.write(new JIVariant(value));
    } catch (JIException | AddFailedException e) {
      LOGGER.error("Failed to write value", e);
      throw new IllegalStateException(e);
    }
  }

  @Override
  public void writeValueForTag(final String tag, final Float value) {
    try {
      final Item addedItem = group.addItem(tag);
      addedItem.write(new JIVariant(value));
    } catch (JIException | AddFailedException e) {
      LOGGER.error("Failed to write value", e);
      throw new IllegalStateException(e);
    }
  }

  @Override
  public Boolean readBoolean(final String tag) {
    return doOperation(tag, item -> {
      try {
        final JIVariant value = item.read(true).getValue();
        return value.getObjectAsBoolean();
      } catch (JIException e) {
        throw new IllegalStateException(e);
      }
    });
  }

  private <T> T doOperation(String tag, Function<Item, T> itemFunc) {
    try {
      final SyncAccess syncAccess = new SyncAccess(server, 200);
      syncAccess.addItem(tag, (item, itemState) -> {
        LOGGER.info("Tag {} was added successfully", tag);
      });
      syncAccess.bind();
      final Item addedItem = group.addItem(tag);
      return itemFunc.apply(addedItem);
    } catch (UnknownHostException | NotConnectedException | DuplicateGroupException | JIException | AddFailedException e) {
      LOGGER.error("Exception during tag {} read", tag);
      throw new IllegalStateException(e);
    }
  }

  @Override
  public Float readFloat(final String tag) {
    return doOperation(tag, item -> {
      try {
        final JIVariant value = item.read(true).getValue();
        return value.getObjectAsFloat();
      } catch (JIException e) {
        throw new IllegalStateException(e);
      }
    });
  }

  public static RealOpcService createForConfig(Config config) {
    final Server server = createServer(config);
    final Group group = createGroup(server);
    return new RealOpcService(server, group);
  }

  private static Group createGroup(final Server server) {
    try {
      final Group serverObject = server.addGroup("test");
      return serverObject;
    } catch (NotConnectedException | IllegalArgumentException | JIException | DuplicateGroupException | UnknownHostException e) {
      throw new IllegalStateException(e);
    }
  }

  private static Server createServer(final Config config) {
    final Config opcConfig = config.getConfig("opc").resolve();
    final String host = opcConfig.getString("host");
    final String user = opcConfig.getString("user");
    final String password = opcConfig.getString("password");
    final String clsid = opcConfig.getString("clsid");
    final ConnectionInformation ci = new ConnectionInformation();
    ci.setHost(host);
    ci.setUser(user);
    ci.setPassword(password);
    ci.setClsid(clsid);
    try {
      final Server server = new Server(ci, createExecutor());
      server.connect();
      final OPCSERVERSTATUS serverState = server.getServerState();
      LOGGER.warn("Server state {}", serverState.getBuildNumber());
      return server;
    } catch (UnknownHostException | JIException | AlreadyConnectedException e) {
      LOGGER.error("Error during connection", e);
      throw new IllegalStateException(e);
    }
  }

  private static ScheduledExecutorService createExecutor() {
    final ThreadFactoryBuilder threadFactoryBuilder = new ThreadFactoryBuilder();
    threadFactoryBuilder.setNameFormat("opc-client-scheduled-thread-pool-%d");
    return Executors.newScheduledThreadPool(1, threadFactoryBuilder.build());
  }

  static {
    try {
      JISystem.setAutoRegisteration(true);
      JISystem.setInBuiltLogHandler(false);
    } catch (IOException e) {
      LOGGER.error("Error during initialization", e);
    }
  }
}
