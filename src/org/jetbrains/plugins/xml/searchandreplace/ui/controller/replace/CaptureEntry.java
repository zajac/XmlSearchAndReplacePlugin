package org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace;

import com.intellij.openapi.editor.markup.RangeHighlighter;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.captures.Capture;

public class CaptureEntry {
  RangeHighlighter range;
  Capture capture;

  public CaptureEntry(RangeHighlighter range, Capture capture) {
    this.range = range;
    this.capture = capture;
  }

}
