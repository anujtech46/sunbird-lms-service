package controllers.telemetry;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.sunbird.common.models.util.JsonKey;
import org.sunbird.common.request.HeaderParam;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import controllers.BaseController;
import controllers.DummyActor;
import play.test.FakeApplication;
import play.test.Helpers;
import util.RequestInterceptor;

/**
 * 
 * @author Mahesh Kumar Gangula
 *
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(PowerMockRunner.class)
@PrepareForTest(RequestInterceptor.class)
@PowerMockIgnore("javax.management.*")
public class TelemetryControllerTest {

	private static FakeApplication app;
	private static Map<String, String[]> headerMap;
	private static ActorSystem system;
	private static final Props props = Props.create(DummyActor.class);

	@BeforeClass
	public static void startApp() {
		app = Helpers.fakeApplication();
		Helpers.start(app);
		headerMap = new HashMap<String, String[]>();
		headerMap.put(HeaderParam.X_Consumer_ID.getName(), new String[] { "Service test consumer" });
		headerMap.put(HeaderParam.X_Device_ID.getName(), new String[] { "Some Device Id" });
		headerMap.put(HeaderParam.X_Authenticated_Userid.getName(), new String[] { "Authenticated user id" });
		headerMap.put(JsonKey.MESSAGE_ID, new String[] { "Unique Message id" });

		system = ActorSystem.create("system");
		ActorRef subject = system.actorOf(props);
		BaseController.setActorRef(subject);
	}
	
	@Test
	public void testSaveTelemetryGzip() {
		System.out.println("Working...");
	}
}
