package ru.spbstu.dis;

import org.junit.Ignore;

/**
 * Created by el on 09.04.2016.
 */
@Ignore
public class MixingBubbleBlowingAlgorithm {
  private int waterSpeedManual;
  private int waterSpeedActual;
  private int timeOfPumping;
  private int topLevelSensor;
  private int bottomLevelSensor;
  private int modellingWaterLevel;
  private int sensorWaterLevel;


  public MixingBubbleBlowingAlgorithm(final int waterSpeedManual,
                                      final int waterSpeedActual,
                                      final int timeOfPumping,
                                      final int topLevelSensor,
                                      final int modellingWaterLevel,
                                      final int bottomLevelSensor) {
    this.waterSpeedManual = waterSpeedManual;
    this.waterSpeedActual = waterSpeedActual;
    this.timeOfPumping = timeOfPumping;
    this.topLevelSensor = topLevelSensor;
    this.modellingWaterLevel = modellingWaterLevel;
    this.bottomLevelSensor = bottomLevelSensor;
    if(topLevelSensor==1) sensorWaterLevel=1;
    else {
      if(bottomLevelSensor==1) sensorWaterLevel=0;
      else  if(bottomLevelSensor==0)
        sensorWaterLevel=-1;
    }
  }

  public static void OpenValves(){

  }
  public static void PumpWaterToMainTank(){

  }
  public void BlowBubbles () {
    this.OpenValves();
    while (waterSpeedManual==waterSpeedActual)
    {
      PumpWaterToMainTank();
    }
      if(sensorWaterLevel!=ConvertToTopBottom(modellingWaterLevel) && ( isOnTop
          (modellingWaterLevel) ||
          isOnBottom(modellingWaterLevel))) {
      StopStation();
      StartControllerProgram();

      }
    else return;
    }

  private int ConvertToTopBottom(final int modellingWaterLevel) {
    if(modellingWaterLevel==1 || (modellingWaterLevel>1 && modellingWaterLevel<3 )) return 0;
    if(modellingWaterLevel==3 || modellingWaterLevel>3 ) return 1;
    else return -1;
  }

  private boolean isOnBottom(final int modellingWaterLevel) {
    if(modellingWaterLevel==1) return true;
    else return false;
  }
  private boolean isOnTop(final int modellingWaterLevel) {
    if(modellingWaterLevel==3) return true;
    else return false;
  }


  private void StopStation() {
  }
  private void StartControllerProgram() {
    int tempWaterSpeed = waterSpeedManual;
    while (waterSpeedManual!=waterSpeedActual){
      StopPumping();
      tempWaterSpeed+=10;
      SetWaterSpeed(tempWaterSpeed);
      StartPumping();
    }
  }

  private void StartPumping() {

  }

  private void SetWaterSpeed(final int tempWaterSpeed) {

  }

  private void StopPumping() {
  }
}


