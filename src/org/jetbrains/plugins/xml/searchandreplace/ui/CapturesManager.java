package org.jetbrains.plugins.xml.searchandreplace.ui;

import org.jetbrains.plugins.xml.searchandreplace.replace.CapturePresentation;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace.Capture;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.ConstraintController;

import java.awt.*;
import java.util.*;
import java.util.List;


public class CapturesManager {

  private Map<Integer, ConstraintController> ids = new HashMap<Integer, ConstraintController>();
  private Map<String,  Capture> map = new HashMap<String, Capture>();

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
    Set<Integer> keys = ids.keySet();
    for (; id < 1000; ++id) {
      if (!keys.contains(id)) {
        break;
      }
    }
    ids.put(id, cc);
    return "" + id;
  }

  public void registerNewCapture(ConstraintController constraintController, Capture capture) {
    CapturePresentation cp = new CapturePresentation();
    cp.setBackgroundColor(null);
    cp.setTextColor(Color.BLUE);
    cp.setName(capture.getName());
    String uniqueId = createUniqueId(constraintController);
    cp.setIdentifier(uniqueId);
    cp.setCapture(capture);
    map.put(uniqueId, capture);
    capture.setPresentation(cp);
    for (CapturesListener observer : observers) {
      observer.captureAdded(capture);
    }
  }

  public void paredicateControllerIsDead(ConstraintController cc) {
    for (Capture c : cc.getCaptures()) {
      for (CapturesListener listener : observers) {
        listener.captureBecameInvalid(c);
      }
    }
    for (int id : ids.keySet()) {
      if (ids.get(id) == cc) {
        ids.remove(id);
        break;
      }
    }
    boolean found;
    do{
      found = false;
      for (String id : map.keySet()) {
        if (cc.getCaptures().contains(map.get(id))) {
          map.remove(id);
          found = true;
          break;
        }
      }
    } while(found);
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
  
}
