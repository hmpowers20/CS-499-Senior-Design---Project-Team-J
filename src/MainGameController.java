import java.io.File;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

public class MainGameController {
    private MainGameView view;
    private MainGameModel model;
    private SimTimer timer;

    public MainGameController()
    {
        model = new MainGameModel();
        view = new MainGameView(model);
        timer = new SimTimer(model, this);
    }

    public void UpdateView()
    {
        view.Update(model);
    }
}