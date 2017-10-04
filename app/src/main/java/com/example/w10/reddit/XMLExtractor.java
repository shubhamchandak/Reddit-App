package com.example.w10.reddit;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.tag;

/**
 * Created by W10 on 9/25/2017.
 */

public class XMLExtractor {

    private String mTag;
    private String mXml;
    private String mEndTag;

    public XMLExtractor(String xml, String tag) {
        this.mTag = tag;
        this.mXml = xml;
        this.mEndTag = "NONE";
    }

    public XMLExtractor(String mXml, String mTag, String mEndTag) {
        this.mTag = mTag;
        this.mXml = mXml;
        this.mEndTag = mEndTag;
    }

    /* Method Logic
    * Example -
    * String xml = <a href="https://www.w3schools.com">Visit W3Schools</a> ... (there are many tags like - href, img, etc. in xml)
    * we need list of all 'links' from xml - [https://www.w3schools.com] and others
    * so we pass the tag and the xml in method
    * say there are 5 "<a href" tags in xml string so to extract these 5 - we pass '<a href' and xml string in the method
    *Then we will split xml string with regex='<a href"' so that we will get an array of string starting from the regex
     * then we will count the no. of such tags in the xml string simply by subtracting one from array length
     * then we will substring individual array element to get the desired 'links' and add them to arrayList
    * */


    public List<String> extract(){

        List<String> result = new ArrayList<>();
        String[] splitXML = null;
        String marker = null;

        if (mEndTag.equals("NONE")){
            marker = "\"";
            splitXML = mXml.split(mTag + marker);
        } else {
            marker = mEndTag;
            splitXML = mXml.split(mTag);
        }

        Log.d("XMLExtractor", "split.length :" + splitXML.length);

        for (int i = 1; i < splitXML.length; i++){   //  first, third, fifth elements will be a tag itself and thus of no use.

            String temp = splitXML[i];

            int index = temp.indexOf(marker);

            temp = temp.substring(0, index);

            result.add(temp);

            Log.d("XMLExtractor", "result :" + temp);
        }

        return result;
    }
}
