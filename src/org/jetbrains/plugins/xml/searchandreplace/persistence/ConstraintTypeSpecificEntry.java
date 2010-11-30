package org.jetbrains.plugins.xml.searchandreplace.persistence;

/**
 * Created by IntelliJ IDEA.
 * User: zajac
 * Date: 29.11.10
 * Time: 16:02
 * To change this template use File | Settings | File Templates.
 */
public class ConstraintTypeSpecificEntry {
  private String text;

  private String textOrTag;
  private String attrName;
  private String value;
  private String comparator;

  public String getComparator() {
    return comparator;
  }

  public void setComparator(String comparator) {
    this.comparator = comparator;
  }

  public void setAttrName(String attrName) {
    this.attrName = attrName;
  }

  public String getTextOrTag() {
    return textOrTag;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public void setTextOrTag(String textOrTag) {
    this.textOrTag = textOrTag;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getAttrName() {
    return attrName;
  }
}
