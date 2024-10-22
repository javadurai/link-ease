package com.linkease.util;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

@Slf4j
public class WebUtil {
    public String getTitleFromUrl(String url) {
        try {
            // Fetch the document from the provided URL
            Document doc = Jsoup.connect(url).get();
            // Extract and return the title
            return doc.title();
        } catch (IOException e) {
            log.error("Unable to retrieve title", e);
            return "Unable to retrieve title";
        }
    }

    public String getFaviconFromUrl(String url) {
        try {
            // Fetch the document from the provided URL
            Document doc = Jsoup.connect(url).get();

            // Try to find the favicon in the head of the HTML document
            Element iconElement = doc.select("link[rel~=icon]").first();

            // If an icon is found, return the absolute URL of the icon
            if (iconElement != null) {
                String faviconUrl = iconElement.absUrl("href");
                return faviconUrl.isEmpty() ? getDefaultFavicon(url) : faviconUrl;
            }

            // Fallback to the default favicon location (e.g., /favicon.ico)
            return getDefaultFavicon(url);

        } catch (IOException e) {
            log.error("Unable to retrieve favicon", e);
            return "Unable to retrieve favicon";
        }
    }

    // Helper method to get the default favicon (e.g., /favicon.ico)
    private String getDefaultFavicon(String url) {
        if (!url.endsWith("/")) {
            url += "/";
        }
        return url + "favicon.ico";  // Fallback URL for favicon
    }

    // Method to get the base URL
    public String getBaseUrl(String fullUrl) {
        try {
            URL url = new URL(fullUrl);
            return url.getProtocol() + "://" + url.getHost();  // Get protocol and host (e.g., http://example.com)
        } catch (MalformedURLException e) {
            log.error("Unable to base url", e);
            return null;
        }
    }

    // Method to fetch the title from the base URL
    public String getBaseSiteTitle(String fullUrl) {
        String baseUrl = getBaseUrl(fullUrl);
        if (baseUrl == null) {
            return "Invalid URL";
        }

        try {
            // Fetch the base URL's HTML document
            Document doc = Jsoup.connect(baseUrl).get();

            // Extract and return the title from the base URL
            return doc.title();
        } catch (IOException e) {
            log.error("Unable to retrieve title", e);
            return "Unable to retrieve title";
        }
    }
}
