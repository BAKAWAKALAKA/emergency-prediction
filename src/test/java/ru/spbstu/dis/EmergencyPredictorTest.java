package ru.spbstu.dis;

import static org.assertj.core.api.Assertions.assertThat;
import ru.spbstu.dis.kb.KnowledgeBase;
import java.util.concurrent.atomic.AtomicReference;

public class EmergencyPredictorTest {
  public void shouldInterConnectComponentsAppropriately() {
    // given
    AtomicReference<ChosenAction> actionHolder = new AtomicReference<>();
    final EmergencyPredictor emergencyPredictor = new EmergencyPredictor(
        () -> DataInput.withPressureAndLowerPressure(1, 2),
        new KnowledgeBase() {
          @Override
          public ChosenAction inferOutput(final DataInput input) {
            return new ChosenAction("Test");
          }
        },
        chosenAction -> {
          actionHolder.set(chosenAction);
        });


    // when
    emergencyPredictor.execute();

    // then
    assertThat(actionHolder.get() != null).isTrue();
    assertThat(actionHolder.get()).isEqualTo(new ChosenAction("Test"));
  }
}