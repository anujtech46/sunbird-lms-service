package controllers.metrics;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.sunbird.common.models.util.ActorOperations;
import org.sunbird.common.request.ExecutionContext;
import org.sunbird.common.request.Request;

import akka.util.Timeout;
import controllers.common.BaseController;
import play.libs.F.Promise;
import play.mvc.Result;

public class UserMetricsController extends BaseController {
  
  public Promise<Result> userCreation(String userId) {
    try {
      Map<String, Object> map = new HashMap<>();
      Request request = new Request();
      request.setEnv(getEnvironment());
      request.setRequest(map);
      request.setManagerName(ActorOperations.USER_CREATION_METRICS.getKey());
      request.setOperation(ActorOperations.USER_CREATION_METRICS.getValue());
      request.setRequest(map);
      Timeout timeout = new Timeout(Akka_wait_time, TimeUnit.SECONDS);
      request.setRequestId(ExecutionContext.getRequestId());
      return actorResponseHandler(getRemoteActor(), request, timeout, null, request());
    } catch (Exception e) {
      return Promise.<Result>pure(createCommonExceptionResponse(e, request()));
    }
  }
  
  public Promise<Result> userConsumption(String userId) {
    try {
      Map<String, Object> map = new HashMap<>();
      Request request = new Request();
      request.setEnv(getEnvironment());
      request.setRequest(map);
      request.setManagerName(ActorOperations.USER_CONSUMPTION_METRICS.getValue());
      request.setOperation(ActorOperations.USER_CONSUMPTION_METRICS.getValue());
      request.setRequest(map);
      Timeout timeout = new Timeout(Akka_wait_time, TimeUnit.SECONDS);
      request.setRequestId(ExecutionContext.getRequestId());
      return actorResponseHandler(getRemoteActor(), request, timeout, null, request());
    } catch (Exception e) {
      return Promise.<Result>pure(createCommonExceptionResponse(e, request()));
    }
  }

}
