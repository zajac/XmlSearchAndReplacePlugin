package org.jetbrains.plugins.xml.searchandreplace.ui;

import org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace.Capture;

public interface CapturesListener {
  void captureBecameInvalid(Capture c);
  void captureAdded(Capture c);
}
