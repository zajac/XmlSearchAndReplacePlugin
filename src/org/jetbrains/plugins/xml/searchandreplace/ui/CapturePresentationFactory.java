package org.jetbrains.plugins.xml.searchandreplace.ui;

import org.jetbrains.plugins.xml.searchandreplace.replace.CapturePresentation;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.PredicateController;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;


public class CapturePresentationFactory {
  private static CapturePresentationFactory shared = new CapturePresentationFactory();

  private Map<PredicateController, Color> uniqueColors = new HashMap<PredicateController, Color>();
  private static final Color[] COLORS = new Color[]{Color.BLUE, Color.RED, Color.GREEN, Color.white,
          Color.pink,
          Color.orange,
          Color.yellow,
          Color.magenta,
          Color.cyan};
  private CapturePresentationFactory(){}

  public static CapturePresentationFactory instance() {
    return shared;
  }

  public CapturePresentation createPresentation(PredicateController predicateController, String name) {
    CapturePresentation cp = new CapturePresentation();
    cp.setBackgroundColor(getUniqueColor(predicateController));
    cp.setTextColor(Color.BLACK);
    cp.setName(name);
    return cp;
  }

  public void paredicateControllerIsDead(PredicateController pc) {
    uniqueColors.remove(pc);
  }

  public Color getUniqueColor(PredicateController predicateController) {
    Color result = uniqueColors.get(predicateController);
    if (result == null) {
      for (Color c : COLORS) {
        if (c == Color.BLACK) continue;
        boolean found = true;
        for (Color other : uniqueColors.values()) {
          if (other == c) {
            found = false;
          }
        }
        if (found) {
          result = c;
          break;
        }
      }
      uniqueColors.put(predicateController, result);
    }
    return result;
  }
}
