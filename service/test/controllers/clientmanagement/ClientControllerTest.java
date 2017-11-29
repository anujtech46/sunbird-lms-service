package controllers.clientmanagement;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.GET;
import static play.test.Helpers.POST;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.route;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import play.mvc.Http.RequestBuilder;
import play.mvc.Result;
import play.test.FakeApplication;
import play.test.Helpers;


public class ClientControllerTest {
  
  public static FakeApplication app;

  @BeforeClass
  public static void beforeTest() throws Exception {
    app = Helpers.fakeApplication();
    Helpers.start(app);
  }
  
  @AfterClass
  public static void afterTest() throws Exception {
    Helpers.stop(app);
  }
  
  @Test
  public void testCreateClient() {
    RequestBuilder req =  new RequestBuilder().uri("/v1/client/key/read/0123666630233866240").method(POST);
    Result result = route(req);
    assertEquals(OK, result.status());
    assertEquals("application/json", result.contentType());
    assertTrue(contentAsString(result).contains("result"));
    assertFalse(contentAsString(result).contains("failed"));
  }
  
  @Test
  public void testUpdateClient() {
    RequestBuilder req =  new RequestBuilder().uri("/v1/client/key/read/0123666630233866240").method(GET);
    Result result = route(req);
    assertEquals(OK, result.status());
    assertEquals("application/json", result.contentType());
    assertTrue(contentAsString(result).contains("result"));
    assertFalse(contentAsString(result).contains("failed"));
  }
  
  @Test
  public void testGetMasterKey() {
    RequestBuilder req =  new RequestBuilder().uri("/v1/client/key/read/0123666630233866240").method(GET);
    Result result = route(req);
    assertEquals(OK, result.status());
    assertEquals("application/json", result.contentType());
    assertTrue(contentAsString(result).contains("result"));
    assertFalse(contentAsString(result).contains("failed"));
  }
      
}
