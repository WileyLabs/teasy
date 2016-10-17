package com.wiley.autotest.services;

import com.wiley.autotest.selenium.SeleniumHolder;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.message.BasicHeader;
import org.openqa.selenium.Cookie;
import org.springframework.stereotype.Service;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;

import static com.wiley.autotest.selenium.SeleniumHolder.getWebDriver;

/**
 * User: dfedorov
 * Date: 8/21/12
 * Time: 11:33 AM
 */
@Service
public class CookiesService {

    public void deleteAllCookies() {
        try {
            getWebDriver().manage().deleteAllCookies();
        } catch (Exception ignored) {
        }
    }

    public void addHttpCookieFromResponseToHeader(final List<Header> headers, final HttpResponse httpResponse) {
        String cookieValue = "";
        for (Header header : httpResponse.getHeaders("Set-Cookie")) {
            List<HttpCookie> httpCookies = HttpCookie.parse(header.getValue());
            for (HttpCookie httpCookie : httpCookies) {
                if (!cookieValue.isEmpty()) {
                    cookieValue += "; ";
                }
                cookieValue += httpCookie.getName() + "=" + httpCookie.getValue();
            }
        }
        headers.add(new BasicHeader("Cookie", cookieValue));
    }

    private List<Cookie> addCookie(final HttpResponse httpResponse) {
        String cookieValue = "";
        List<Cookie> cookieList = new ArrayList<Cookie>();
        for (Header header : httpResponse.getHeaders("Set-Cookie")) {
            List<HttpCookie> httpCookies = HttpCookie.parse(header.getValue());
            for (HttpCookie httpCookie : httpCookies) {
                if (!cookieValue.isEmpty()) {
                    cookieValue += "; ";
                }
                cookieList.add(new Cookie(httpCookie.getName(), httpCookie.getValue(), httpCookie.getDomain(), httpCookie.getPath(), null, httpCookie.getSecure(), httpCookie.isHttpOnly()));
            }
        }
        return cookieList;
    }

    public void addCookiesToDriver(HttpResponse httpResponse) {
        for (Cookie cookie : addCookie(httpResponse)) {
            if (SeleniumHolder.getDriverName().equals("chrome")) {
                //to avoid WebDriverException: Failed to set the 'cookie' property on 'Document': Cookies are disabled inside 'data:' URLs.
                getWebDriver().get("chrome://settings/cookies");
            }
            getWebDriver().manage().addCookie(cookie);
        }
    }
}
