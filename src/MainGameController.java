/*****************************************************
 CS 499-01 Senior Design
 Project Team J
 Anushka Bhattacharjee, Haley Powers, Wren Robertson
 Spring 2021
 Final Deliverable: May 4, 2021
 ****************************************************/
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

/*******************************
This is the controller class.
********************************/
public class MainGameController {
    private MainGameView view;
    private MainGameModel model;
    private SimTimer timer;

    /***********************************************************************
    This constructor creates an instance of the model, view, and the timer.
    ************************************************************************/
    public MainGameController()
    {
        model = new MainGameModel();
        view = new MainGameView(model);
        timer = new SimTimer(model, this);
    }

    /********************************
    This function updates the view.
    *********************************/
    public void UpdateView()
    {
        view.Update(model);
    }
}