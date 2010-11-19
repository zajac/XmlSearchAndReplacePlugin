package org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace;

import com.intellij.psi.xml.XmlElement;
import org.jetbrains.plugins.xml.searchandreplace.replace.CapturePresentation;
import org.jetbrains.plugins.xml.searchandreplace.search.Node;

public abstract class Capture {
  private Node node;

  public Node getNode() {
    return node;
  }

  public void setNode(Node node) {
    this.node = node;
  }

  public abstract CapturePresentation presentation();
  public abstract String value(XmlElement element);
}
