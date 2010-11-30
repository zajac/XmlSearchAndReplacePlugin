package org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.persistence;


import java.util.ArrayList;
import java.util.HashMap;

public class PatternStorageEntry {

  private ArrayList<ConstraintEntry> entries = new ArrayList<ConstraintEntry>();
  private HashMap<Integer, ArrayList<Integer>> tree = new HashMap<Integer, ArrayList<Integer>>();
  private int root;

  public ArrayList<ConstraintEntry> getEntries() {
    return entries;
  }

  public HashMap<Integer, ArrayList<Integer>> getTree() {
    return tree;
  }

  public void setEntries(ArrayList<ConstraintEntry> entries) {
    this.entries = entries;
  }

  public void setTree(HashMap<Integer, ArrayList<Integer>> tree) {
    this.tree = tree;
  }

  public void setRoot(int root) {
    this.root = root;
  }

  public int getRoot() {
    return root;
  }
}
