package org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace;

/**
 * Created by IntelliJ IDEA.
 * User: zajac
 * Date: 30.11.10
 * Time: 16:35
 * To change this template use File | Settings | File Templates.
 */
public class ReplacementControllerState {
  private String attrValue;
  private String attrName;

  public String getAttrName() {
    return attrName;
  }

  public void setAttrName(String attrName) {
    this.attrName = attrName;
  }

  public String getText() {
    return text;
  }

  private String text;

  public void setText(String text) {
    this.text = text;
  }

  public void setAttrValue(String attrValue) {
    this.attrValue = attrValue;
  }

  public String getAttrValue() {
    return attrValue;

  }
}
