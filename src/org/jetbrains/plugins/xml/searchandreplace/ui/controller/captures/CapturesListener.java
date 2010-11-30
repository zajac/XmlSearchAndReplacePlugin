package org.jetbrains.plugins.xml.searchandreplace.ui.controller.captures;

import org.jetbrains.plugins.xml.searchandreplace.ui.controller.captures.Capture;

public interface CapturesListener {
  void captureBecameInvalid(Capture c);
  void captureAdded(Capture c);
}
