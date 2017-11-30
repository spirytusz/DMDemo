package com.zspirytus.dmdemo.JavaSource.Utils;

import android.util.Log;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZSpirytus on 2017/11/30.
 */

public class XmlUtil {

    private static final String TAG = "XmlUtil";

    private static Document getDocument(String str){
        try{
            Document document = DocumentHelper.parseText(str);
            return document;
        }catch (DocumentException e){
            Log.d(TAG,"function:getDocument catch DocumentException");
            return null;
        }
    }

    public static  ArrayList<String> getAnalysisResult(String str,String methodName,boolean isSingleResponse){
        final String responseText = "Response";
        final String resultText = "Result";
        final String stringText = "string";
        ArrayList<String> result = new ArrayList<>();
        result.clear();
        Log.d(TAG,"test~: ");
        Document document = getDocument(str);
        Element target = document.getRootElement().element(methodName+responseText).element(methodName+resultText);
        if(!isSingleResponse)
            target = target.element(stringText);
        List<Element> elementList = target.elements();
        for (Element e : elementList){
            Log.d(TAG," text~: " + e.getText()+"\t"+e.getName());
            result.add(e.getText());
        }
        return result;
    }
}
