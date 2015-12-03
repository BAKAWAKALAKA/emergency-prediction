package ru.spbstu.dis.kb;

public class DataInput {
  private double pressure;

  private double lowerPressure;

  public DataInput(final double pressure, final double lowerPressure) {
    this.pressure = pressure;
    this.lowerPressure = lowerPressure;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    final DataInput that = (DataInput) o;

    if (Double.compare(that.pressure, pressure) != 0) {
      return false;
    }
    return Double.compare(that.lowerPressure, lowerPressure) == 0;
  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    temp = Double.doubleToLongBits(pressure);
    result = (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(lowerPressure);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    return result;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("KnowledgeBaseInput{");
    sb.append("pressure=").append(pressure);
    sb.append(", lowerPressure=").append(lowerPressure);
    sb.append('}');
    return sb.toString();
  }

  public double getPressure() {
    return pressure;
  }

  public void setPressure(double pressure) {
    this.pressure = pressure;
  }

  public double getLowerPressure() {
    return lowerPressure;
  }

  public void setLowerPressure(double lowerPressure) {
    this.lowerPressure = lowerPressure;
  }
}
