package com.wiley.driver;

import com.wiley.utils.Report;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.openqa.selenium.remote.SessionId;

import java.net.MalformedURLException;
import java.net.URL;

class GridApi {

    private final URL hubUrl;
    private final SessionId sessionId;

    GridApi(URL hubUrl, SessionId sessionId) {
        this.hubUrl = hubUrl;
        this.sessionId = sessionId;
    }

    String getNodeIp() {
        String gridApiUrl = hubUrl.getProtocol() + "://" + hubUrl.getHost() + ":" + hubUrl.getPort() + "/grid/api/";
        if (RestAssured.get(gridApiUrl).getStatusCode() != 200) return hubUrl.getHost();

        Response response = RestAssured.get(gridApiUrl + "testsession?session=" + sessionId);
        try {
            return new URL(response.jsonPath().get("proxyId")).getHost();
        } catch (MalformedURLException e) {
            Report.jenkins("MalformedURLException in getNodeIp " + e.getMessage());
        }
        throw new RuntimeException("Cannot get node id by session from grid api.");
    }
}
