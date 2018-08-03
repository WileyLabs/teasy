package com.wiley.screenshots;

import com.google.common.collect.ImmutableMap;
import com.wiley.config.Configuration;
import com.wiley.holders.TestParamsHolder;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.json.Json;

class ChromeScreenshoter {

    private final String nodeUrl;
    private final String sessionId;

    ChromeScreenshoter() {
        if (Configuration.runWithGrid) {
            nodeUrl = Configuration.gridHubUrl;
        } else {
            nodeUrl = "http://localhost:" + TestParamsHolder.getChromePort();
        }
        sessionId = TestParamsHolder.getSessionId().toString();
    }

    <X> X getFullScreenshotAs(OutputType<X> outputType) {
        Object metrics = sendEvaluate(
                "({" +
                        "width: Math.max(window.innerWidth,document.body.scrollWidth,document.documentElement.scrollWidth)|0," +
                        "height: Math.max(window.innerHeight,document.body.scrollHeight,document.documentElement.scrollHeight)|0," +
                        "deviceScaleFactor: window.devicePixelRatio || 1," +
                        "mobile: typeof window.orientation !== 'undefined'" +
                        "})");
        sendCommandViaRestAssured("Emulation.setDeviceMetricsOverride", metrics);
        Response result = sendCommandViaRestAssured("Page.captureScreenshot",
                ImmutableMap.of("format", "png", "fromSurface", true));
        sendCommandViaRestAssured("Emulation.clearDeviceMetricsOverride", ImmutableMap.of());
        String base64EncodedPng = result.jsonPath().get("value.data");
        return outputType.convertFromBase64Png(base64EncodedPng);
    }

    private Response sendCommandViaRestAssured(String cmd, Object params) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(new Json().toJson(ImmutableMap.of("cmd", cmd, "params", params)))
                .post(nodeUrl + "/session/" + sessionId + "/chromium/send_command_and_get_result");
    }

    private Object sendEvaluate(String script) {
        Response response = sendCommandViaRestAssured("Runtime.evaluate",
                ImmutableMap.of("returnByValue", true, "expression", script));
        return response.jsonPath().get("value.result.value");
    }
}
