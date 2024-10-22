package com.linkease.service;

import com.linkease.dto.SiteInfo;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@Service
@Slf4j
public class SiteInfoExtractorService {

    private static final int TIMEOUT_MILLIS = 500000; // Timeout for Jsoup connection

    public Optional<String> getBaseUrl(String fullUrl) {
        try {
            URI uri = new URI(fullUrl);
            return Optional.of(uri.resolve("/").toString());
        } catch (URISyntaxException e) {
            log.error("Invalid URL syntax: {}", fullUrl, e);
            return Optional.empty();
        }
    }

    public SiteInfo getSiteInfo(String fullUrl) {
        SiteInfo siteInfo = new SiteInfo();
        if (fullUrl == null) {
            return siteInfo;  // Return empty SiteInfo for null input
        }

        // Get base URL
        Optional<String> baseUrlOptional = getBaseUrl(fullUrl);
        if (baseUrlOptional.isEmpty()) {
            return siteInfo;
        }
        String baseUrl = baseUrlOptional.get();

        try {
            // Fetch document once and reuse
            Document document = Jsoup.connect(fullUrl).timeout(TIMEOUT_MILLIS).get();
            siteInfo.setSite(getTitle(baseUrl));
            siteInfo.setTitle(getTitle(fullUrl));

            // Extract description from meta tags
            String description = getDescription(document);
            siteInfo.setDescription(description); // Add the description to SiteInfo

            // Try to find the favicon in the document
            Element iconElement = document.selectFirst("link[rel~=icon]");
            String faviconUrl = iconElement != null ? iconElement.absUrl("href") : getDefaultFavicon(baseUrl);

            // Fetch the favicon and set it in the SiteInfo object
            siteInfo.setFavicon(fetchIconBytes(faviconUrl));

        } catch (IOException e) {
            log.error("Error fetching site info for URL: {}", fullUrl, e);
        }

        return siteInfo;
    }

    private String getDefaultFavicon(String baseUrl) {
        return baseUrl.endsWith("/") ? baseUrl + "favicon.ico" : baseUrl + "/favicon.ico";
    }

    // Method to extract meta description or og:description
    private String getDescription(Document document) {
        // Try to get <meta name="description">
        Element metaDescription = document.selectFirst("meta[name=description]");
        if (metaDescription != null) {
            return metaDescription.attr("content");
        }

        // If not found, try to get <meta property="og:description">
        Element ogDescription = document.selectFirst("meta[property=og:description]");
        if (ogDescription != null) {
            return ogDescription.attr("content");
        }

        // If neither is found, return a default message
        return "No description available.";
    }

    private String getTitle(String url){
        try {
            Document doc = Jsoup.connect(url).timeout(TIMEOUT_MILLIS).get();
            // Extract the title
            return doc.title();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] fetchIconBytes(String faviconUrl) {
        try (InputStream inputStream = new URI(faviconUrl).toURL().openStream()) {
            return inputStream.readAllBytes();
        } catch (IOException | URISyntaxException e) {
            log.error("Error fetching favicon from URL: {}", faviconUrl, e);
            return new byte[0];
        }
    }
}
