package org.jetbrains.plugins.xml.searchandreplace.ui;

import org.jetbrains.plugins.xml.searchandreplace.replace.CapturePresentation;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace.Capture;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.ConstraintController;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class CapturePresentationFactory {
  private static CapturePresentationFactory shared = new CapturePresentationFactory();

  private Map<Integer, ConstraintController> ids = new HashMap<Integer, ConstraintController>();
  private Map<String,  Capture> map = new HashMap<String, Capture>();

  private CapturePresentationFactory(){}

  public static CapturePresentationFactory instance() {
    return shared;
  }

  private String createUniqueId(ConstraintController cc) {
    int id = 1;
    Set<Integer> keys = ids.keySet();
    for (; id < 1000; ++id) {
      if (!keys.contains(id)) {
        break;
      }
    }
    ids.put(id, cc);
    return "" + id;
  }

  public CapturePresentation createPresentation(ConstraintController constraintController, String name, Capture capture) {
    CapturePresentation cp = new CapturePresentation();
    cp.setBackgroundColor(Color.GRAY);
    cp.setTextColor(Color.BLACK);
    cp.setName(name);
    String uniqueId = createUniqueId(constraintController);
    cp.setIdentifier(uniqueId);
    cp.setCapture(capture);
    map.put(uniqueId, capture);
    return cp;
  }

  public void paredicateControllerIsDead(ConstraintController cc) {
    for (int id : ids.keySet()) {
      if (ids.get(id) == cc) {
        ids.remove(id);
        return;
      }
    }
  }

  public Capture findById(String captureId) {
    return map.get(captureId);
  }
}
