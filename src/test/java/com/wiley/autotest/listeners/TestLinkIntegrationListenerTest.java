//package com.wiley.autotest.listeners;
//
//import br.eti.kinoshita.testlinkjavaapi.TestLinkAPI;
//import br.eti.kinoshita.testlinkjavaapi.model.ExecutionStatus;
//import br.eti.kinoshita.testlinkjavaapi.model.ReportTCResultResponse;
//import org.testng.IInvokedMethod;
//import org.testng.ITestContext;
//import org.testng.ITestResult;
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Test;
//
//import java.util.Properties;
//
//import static br.eti.kinoshita.testlinkjavaapi.model.ExecutionStatus.PASSED;
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.Matchers.is;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//import static org.testng.Assert.assertEquals;
//import static org.testng.ITestResult.*;
//
///**
// * User: dfedorov
// * Date: 3/20/12
// * Time: 4:27 PM
// */
//public class TestLinkIntegrationListenerTest {
//    private int countOfCallReportMethod;
//    private int countOfCallUploadScreenshotMethod;
//    private ExecutionStatus sentStatus;
//    private TestLinkIntegrationListener.TestCaseInfo testCaseInfo;
//    private ITestContext testContext;
//    private Properties testLinkInfo;
//    private ITestResult testResult;
//
//    private final TestLinkIntegrationListener mockedTestLinkIntegrationListener = new TestLinkIntegrationListener() {
//        @Override
//        protected TestLinkAPI getTestLinkAPI(Properties testLinkInfo) {
//            return mock(TestLinkAPI.class);
//        }
//
//        @Override
//        protected void uploadScreenshotsByTCResult(final TestLinkAPI api, final String testName,
//                                                   final ReportTCResultResponse testCaseExecutionInfo) {
//            ++countOfCallUploadScreenshotMethod;
//        }
//
//        @Override
//        protected ReportTCResultResponse reportTCResult(TestLinkAPI api, TestLinkIntegrationListener.TestCaseInfo testCaseInfo,
//                                                        Properties testLinkInfo, ExecutionStatus status) {
//            ++countOfCallReportMethod;
//            sentStatus = status;
//            return null;
//        }
//
//        @Override
//        protected TestCaseInfo getTestCaseInfo(IInvokedMethod method) {
//            return testCaseInfo;
//        }
//
//        @Override
//        protected String getTestName(ITestResult testResult) {
//            return "testLinkIntegrationListenerTest";
//        }
//    };
//
//    @BeforeMethod
//    public void setUp() {
//        countOfCallReportMethod = 0;
//        countOfCallUploadScreenshotMethod = 0;
//        sentStatus = ExecutionStatus.NOT_RUN;
//        testLinkInfo = new Properties();
//        testLinkInfo.put("testLink.using", "true");
//
//        testResult = mock(ITestResult.class);
//        when(testResult.isSuccess()).thenReturn(Boolean.TRUE);
//        when(testResult.getStatus()).thenReturn(SUCCESS);
//
//        testContext = mock(ITestContext.class);
//        when(testContext.getAttribute("testLinkInfo")).thenReturn(testLinkInfo);
//
//        testCaseInfo = mock(TestLinkIntegrationListener.TestCaseInfo.class);
//    }
//
//    @Test(enabled = false)
//    public void testTestLinkAnnotationIsAbsent() {
//        testCaseInfo = null;
//        mockedTestLinkIntegrationListener.afterInvocation(mock(IInvokedMethod.class), testResult, testContext);
//        assertThat(countOfCallReportMethod, is(0));
//    }
//
//    @Test(enabled = false)
//    public void testTestLinkIntegrationIsDisabled() {
//        testLinkInfo.put("testLink.using", "false");
//        mockedTestLinkIntegrationListener.afterInvocation(mock(IInvokedMethod.class), testResult, testContext);
//        assertThat(countOfCallReportMethod, is(0));
//    }
//
//    @Test(enabled = false)
//    public void testSuccessTestResult() {
//        mockedTestLinkIntegrationListener.afterInvocation(mock(IInvokedMethod.class), testResult, testContext);
//        assertThat(countOfCallReportMethod, is(1));
//        assertThat(sentStatus, is(PASSED));
//    }
//
//
//// We not need to set failed test result to test link, so this test is not valid now.
////    @Test(enabled = false)
////    public void testFailureTestResult() {
////        when(testResult.isSuccess()).thenReturn(Boolean.FALSE);
////        when(testResult.getStatus()).thenReturn(FAILURE);
////        mockedTestLinkIntegrationListener.afterInvocation(mock(IInvokedMethod.class), testResult, testContext);
////        assertThat(countOfCallReportMethod, is(1));
////        assertThat(sentStatus, is(FAILED));
////    }
//
//    @Test(enabled = false)
//    public void testSkipTestResult() {
//        when(testResult.isSuccess()).thenReturn(Boolean.FALSE);
//        when(testResult.getStatus()).thenReturn(SKIP);
//        mockedTestLinkIntegrationListener.afterInvocation(mock(IInvokedMethod.class), testResult, testContext);
//        assertThat(countOfCallReportMethod, is(0));
//    }
//
//    @Test(enabled = false)
//    public void testUploadScreenshotsAreNotRunningForSuccessTest() {
//        mockedTestLinkIntegrationListener.afterInvocation(mock(IInvokedMethod.class), testResult, testContext);
//        assertThat(countOfCallUploadScreenshotMethod, is(0));
//    }
//
////    @Test(enabled = false)
//    public void testUploadScreenshotsAreRunningForFailureTest() {
//        when(testResult.isSuccess()).thenReturn(Boolean.FALSE);
//        when(testResult.getStatus()).thenReturn(FAILURE);
//        mockedTestLinkIntegrationListener.afterInvocation(mock(IInvokedMethod.class), testResult, testContext);
//        assertThat(countOfCallUploadScreenshotMethod, is(1));
//    }
//
//    @Test(enabled = false)
//    public void shouldTruncateFullSeleniumErrorMessage() {
//        final String messageStub = "org.openqa.selenium.TimeoutException: Timed out after 10 seconds waiting for presence of element located by: By.selector: #embedded div\n" +
//                "Build info: version: '2.2.1', revision: '16551', time: '2012-04-11 21:42:35'\n" +
//                "System info: os.name: 'Linux', os.arch: 'amd64', os.version: '2.6.18-274.17.1.el5', java.version: '1.6.0_31'\n" +
//                "Driver info: driver.version: unknown";
//        final String expectedMessageStub = "org.openqa.selenium.TimeoutException: Timed out after 10 seconds waiting for presence of element located by: By.selector: #embedded div";
//
//        assertEquals(mockedTestLinkIntegrationListener.truncateMessage(messageStub), expectedMessageStub);
//    }
//
//    @Test(enabled = false)
//    public void shouldNotTruncateCustomErrorMessage() {
//        final String messageStub = "No activity is found for test";
//        assertEquals(mockedTestLinkIntegrationListener.truncateMessage(messageStub), messageStub);
//    }
//}
