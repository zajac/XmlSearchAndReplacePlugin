package org.jetbrains.plugins.xml.searchandreplace.ui.controller.captures;

import org.jetbrains.plugins.xml.searchandreplace.replace.CapturePresentation;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.ConstraintController;

import java.awt.*;
import java.util.*;
import java.util.List;


public class CapturesManager {

  private Map<String, Capture> map = new HashMap<String, Capture>();

  private List<CapturesListener> observers = new ArrayList<CapturesListener>();

  public CapturesManager(){}

  public void addCapturesListener(CapturesListener l) {
    observers.add(l);
  }

  public void removeCapturesListener(CapturesListener l) {
    observers.remove(l);
  }

  private String createUniqueId(ConstraintController cc) {
    int id = 1;
    Set<String> keys = map.keySet();
    for (; id < 1000; ++id) {
      if (!keys.contains("" + id)) {
        break;
      }
    }
    return "" + id;
  }

  public void registerNewCapture(ConstraintController constraintController, Capture capture) {
    String uniqueId = createUniqueId(constraintController);
    registerNewCapture(constraintController, capture, uniqueId);
  }

  public void registerNewCapture(ConstraintController constraintController, Capture capture, String id) {
    CapturePresentation cp = new CapturePresentation();
    cp.setBackgroundColor(null);
    cp.setTextColor(Color.BLUE);
    cp.setName(capture.getName());
    cp.setIdentifier(id);
    cp.setCapture(capture);
    map.put(id, capture);
    capture.setPresentation(cp);
    for (CapturesListener observer : observers) {
      observer.captureAdded(capture);
    }
  }

  public void paredicateControllerIsDead(ConstraintController cc) {
    for (Capture c : cc.getCaptures()) {
      unregisterCapture(c);
    }
  }

  public Capture findById(String captureId) {
    return map.get(captureId);
  }

  public boolean isCaptureRegistered(Capture c) {
    for (Capture capture : map.values()) {
      if (capture == c) return true;
    }
    return false;
  }

  public void unregisterCapture(Capture c) {
    for (CapturesListener listener : observers) {
      listener.captureBecameInvalid(c);
    }
    if (c.presentation() != null) {
      map.remove(c.presentation().getIdentifier());
    }
  }
}
