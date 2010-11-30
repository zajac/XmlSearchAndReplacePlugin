package org.jetbrains.plugins.xml.searchandreplace.persistence;


public class ConstraintEntry {
  private String constraintTypeClassSelected = "";
  private int id;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public ConstraintTypeSpecificEntry getConstraintTypeSpecificEntry() {
    return constraintTypeSpecificEntry;
  }

  public void setConstraintTypeSpecificEntry(ConstraintTypeSpecificEntry constraintTypeSpecificEntry) {
    this.constraintTypeSpecificEntry = constraintTypeSpecificEntry;
  }

  private ConstraintTypeSpecificEntry constraintTypeSpecificEntry = new ConstraintTypeSpecificEntry();

  public String getConstraintTypeClassSelected() {
    return constraintTypeClassSelected;
  }

  public void setConstraintTypeClassSelected(String constraintTypeClassSelected) {
    this.constraintTypeClassSelected = constraintTypeClassSelected;
  }
}
