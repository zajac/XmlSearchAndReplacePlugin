package org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.persistence;


import java.util.ArrayList;
import java.util.List;

public class ConstraintEntry {
  private String constraintTypeClassSelected = "";
  private int id;

  private List<String> capturesIds = new ArrayList<String>();

  public List<String> getCapturesIds() {
    return capturesIds;
  }

  public void setCapturesIds(List<String> capturesIds) {
    this.capturesIds = capturesIds;
  }

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
