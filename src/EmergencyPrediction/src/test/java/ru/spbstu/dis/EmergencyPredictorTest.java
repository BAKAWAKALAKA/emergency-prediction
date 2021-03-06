package ru.spbstu.dis;

//import static org.assertj.core.api.Assertions.assertThat;
//
//import org.junit.Ignore;
//import ru.spbstu.dis.ep.EmergencyPredictor;
//import ru.spbstu.dis.opc.client.api.http.server.data.DataInput;
//import ru.spbstu.dis.ep.kb.ChosenAction;
//import ru.spbstu.dis.ep.kb.KnowledgeBase;
//import java.util.concurrent.atomic.AtomicReference;
//@Ignore
//public class EmergencyPredictorTest {
//  public void shouldInterConnectComponentsAppropriately() {
//    // given
//    AtomicReference<ChosenAction> actionHolder = new AtomicReference<>();
//    final EmergencyPredictor emergencyPredictor = new EmergencyPredictor(
//        () -> DataInput.withPressureAndLowerPressure(1, 2),
//        new KnowledgeBase() {
//          @Override
//          public ChosenAction inferOutput(final DataInput input) {
//            return new ChosenAction("Test");
//          }
//        },
//        chosenAction -> {
//          actionHolder.set(chosenAction);
//        });
//
//
//    // when
//    emergencyPredictor.execute();
//
//    // then
//    assertThat(actionHolder.get() != null).isTrue();
//    assertThat(actionHolder.get()).isEqualTo(new ChosenAction("Test"));
//  }
//}