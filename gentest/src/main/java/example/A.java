/* (c) https://github.com/MontiCore/monticore */
package example;

public class A {

  protected int x;

  protected String s;

  protected B blub;

  public void addBlub(B blub) {
    this.blub = blub;
  }

  public String getS() {
    return s;
  }

  public void setS(String s) {
    this.s = s;
  }

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }
}
