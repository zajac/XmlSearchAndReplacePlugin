package org.jetbrains.plugins.xml.searchandreplace.ui;

import com.intellij.psi.xml.XmlElement;
import org.jetbrains.plugins.xml.searchandreplace.search.Node;

import java.util.Map;

public interface Capture {
  CapturePresentation presentation();
  String value(Map<Node, XmlElement> matchResult);
}
