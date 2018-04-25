/** */
package controllers.search;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.BaseController;
import java.util.HashMap;
import org.sunbird.common.models.util.ActorOperations;
import org.sunbird.common.models.util.JsonKey;
import org.sunbird.common.models.util.LoggerEnum;
import org.sunbird.common.models.util.ProjectLogger;
import org.sunbird.common.request.ExecutionContext;
import org.sunbird.common.request.Request;
import org.sunbird.common.request.RequestValidator;
import play.libs.F.Promise;
import play.mvc.Result;

/**
 * This controller will handle all the request related user and organization search.
 *
 * @author Manzarul
 */
public class SearchController extends BaseController {

  /**
   * This method will do data search for user and organization. Search type will be decide based on
   * request object type coming with filter if objectType key is not coming then we need to do the
   * search for all the types.
   *
   * @return Promise<Result>
   */
  public Promise<Result> compositeSearch() {
    try {
      JsonNode requestData = request().body().asJson();
      ProjectLogger.log("getting search request data = " + requestData, LoggerEnum.INFO.name());
      Request reqObj = (Request) mapper.RequestMapper.mapRequest(requestData, Request.class);
      reqObj.setOperation(ActorOperations.COMPOSITE_SEARCH.getValue());
      reqObj.setRequestId(ExecutionContext.getRequestId());
      reqObj.getRequest().put(JsonKey.CREATED_BY, ctx().flash().get(JsonKey.USER_ID));
      reqObj.setEnv(getEnvironment());
      return actorResponseHandler(getActorRef(), reqObj, timeout, null, request());
    } catch (Exception e) {
      return Promise.<Result>pure(createCommonExceptionResponse(e, request()));
    }
  }

  /**
   * This method will do data Sync form Cassandra db to Elasticsearch.
   *
   * @return Promise<Result>
   */
  public Promise<Result> sync() {
    try {
      JsonNode requestData = request().body().asJson();
      ProjectLogger.log("making a call to data synch api = " + requestData, LoggerEnum.INFO.name());
      Request reqObj = (Request) mapper.RequestMapper.mapRequest(requestData, Request.class);
      RequestValidator.validateSyncRequest(reqObj);
      String operation = (String) reqObj.getRequest().get(JsonKey.OPERATION_FOR);
      if ("keycloak".equalsIgnoreCase(operation)) {
        reqObj.setOperation(ActorOperations.SYNC_KEYCLOAK.getValue());
        reqObj.setRequestId(ExecutionContext.getRequestId());
        reqObj.getRequest().put(JsonKey.CREATED_BY, ctx().flash().get(JsonKey.USER_ID));
        reqObj.setEnv(getEnvironment());
        HashMap<String, Object> map = new HashMap<>();
        map.put(JsonKey.DATA, reqObj.getRequest());
        reqObj.setRequest(map);
        return actorResponseHandler(getActorRef(), reqObj, timeout, null, request());
      } else {
        reqObj.setOperation(ActorOperations.SYNC.getValue());
        reqObj.setRequestId(ExecutionContext.getRequestId());
        reqObj.getRequest().put(JsonKey.CREATED_BY, ctx().flash().get(JsonKey.USER_ID));
        reqObj.setEnv(getEnvironment());
        HashMap<String, Object> map = new HashMap<>();
        map.put(JsonKey.DATA, reqObj.getRequest());
        reqObj.setRequest(map);
        return actorResponseHandler(getActorRef(), reqObj, timeout, null, request());
      }

    } catch (Exception e) {
      return Promise.<Result>pure(createCommonExceptionResponse(e, request()));
    }
  }
}
