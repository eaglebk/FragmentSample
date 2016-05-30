package com.eagle.fragmenttypes;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewFragment;

/**
 * Created by 1 on 13.04.2016.
 */
public class MyWebView extends WebViewFragment {

    final String _html =
            "<html><body>" +
            "<h2>This is WebView Fragment</h2>"+
            "<p>This is <strong><font color=\"red\">sample</font></strong> text in webview fragment</p>"+
            "<svg height=\"100\" width=\"100\">"+
            "<circle cx=\"50\" cy=\"50\" r=\"40\" stroke=\"black\" stroke-width=\"3\" fill=\"red\"/>"+
            "Sorry, your browser does not support inline SVG."+
            "</svg>"+
            "</body></html>";

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        WebView webView = getWebView();
        webView.loadData(_html,"text/html",null);
    }
}
