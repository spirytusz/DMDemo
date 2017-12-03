package com.zspirytus.dmdemo.JavaSource.Utils;

import android.util.Log;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZSpirytus on 2017/11/30.
 */

public class XmlUtil {

    private static final String TAG = "XmlUtil";

    private static Document getDocument(String str) {
        try {
            Document document = DocumentHelper.parseText(str);
            return document;
        } catch (DocumentException e) {
            Log.d(TAG, "function:getDocument catch DocumentException");
            return null;
        }
    }

    public static ArrayList<String> getAnalysisResult(String str) {
        Document document = getDocument(str);
        Element target = document.getRootElement();
        ArrayList<String> result = new ArrayList<>();
        result = getNodes(target,result);
        return result;
    }

    private static ArrayList<String> getNodes(Element node,ArrayList<String> parentList) {
        String nodeText = node.getTextTrim();
        ArrayList<String> childList = parentList;
        if (!nodeText.equals("")) {
            childList.add(nodeText);
        }
        List<Element> listElement = node.elements();
        for (Element e : listElement) {
            getNodes(e,childList);
        }
        return childList;
    }
}
