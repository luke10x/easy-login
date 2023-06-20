package dev.luke10x.easylogin.testutils;

import com.gargoylesoftware.htmlunit.WebClient;

public class HtmlUnitWebClientFactory {
    public static WebClient createCommonWebClient() {
        final WebClient client = new WebClient();

        var opts = client.getOptions();
        opts.setThrowExceptionOnScriptError(false);
        opts.setThrowExceptionOnFailingStatusCode(false);
        opts.setCssEnabled(false);
        opts.setRedirectEnabled(true);
        opts.setJavaScriptEnabled(false);

        return client;
    }
}
