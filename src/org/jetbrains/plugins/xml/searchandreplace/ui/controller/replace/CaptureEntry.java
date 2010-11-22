package org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace;

import com.intellij.openapi.editor.RangeMarker;

public class CaptureEntry {
  RangeMarker range;
  Capture capture;

  public CaptureEntry(RangeMarker range, Capture capture) {
    this.range = range;
    this.capture = capture;
  }
}