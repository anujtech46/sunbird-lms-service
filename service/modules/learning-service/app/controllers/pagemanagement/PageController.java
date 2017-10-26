/**
 * 
 */
package controllers.pagemanagement;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.sunbird.common.models.util.ActorOperations;
import org.sunbird.common.models.util.JsonKey;
import org.sunbird.common.models.util.LoggerEnum;
import org.sunbird.common.models.util.ProjectLogger;
import org.sunbird.common.request.ExecutionContext;
import org.sunbird.common.request.Request;
import org.sunbird.common.request.RequestValidator;

import com.fasterxml.jackson.databind.JsonNode;

import akka.util.Timeout;
import controllers.common.BaseController;
import play.libs.F.Promise;
import play.mvc.Result;

/**
 * This controller will handle all the request related to page api's.
 * 
 * @author Amit Kumar
 */

public class PageController extends BaseController {

  /**
   * This method will allow admin to create a page for view.
   * 
   * @return Promise<Result>
   */
  public Promise<Result> createPage() {

    try {
      JsonNode requestData = request().body().asJson();
      ProjectLogger.log("getting create page data request = " + requestData,
          LoggerEnum.INFO.name());
      Request reqObj = (Request) mapper.RequestMapper.mapRequest(requestData, Request.class);
      RequestValidator.validateCreatePage(reqObj);
      reqObj.setManagerName(ActorOperations.CREATE_PAGE.getKey());
      reqObj.setOperation(ActorOperations.CREATE_PAGE.getValue());
      reqObj.setRequestId(ExecutionContext.getRequestId());
      reqObj.getRequest().put(JsonKey.CREATED_BY, ctx().flash().get(JsonKey.USER_ID));
      reqObj.setEnv(getEnvironment());
      HashMap<String, Object> map = new HashMap<>();
      map.put(JsonKey.PAGE, reqObj.getRequest());
      reqObj.setRequest(map);
      Timeout timeout = new Timeout(Akka_wait_time, TimeUnit.SECONDS);
      return actorResponseHandler(getRemoteActor(), reqObj, timeout, null, request());
    } catch (Exception e) {
      return Promise.<Result>pure(createCommonExceptionResponse(e, request()));
    }
  }


  /**
   * This method will allow admin to update already created page data.
   * 
   * @return Promise<Result>
   */
  public Promise<Result> updatePage() {

    try {
      JsonNode requestData = request().body().asJson();
      ProjectLogger.log("getting update page data request = " + requestData,
          LoggerEnum.INFO.name());
      Request reqObj = (Request) mapper.RequestMapper.mapRequest(requestData, Request.class);
      RequestValidator.validateUpdatepage(reqObj);
      reqObj.setManagerName(ActorOperations.UPDATE_PAGE.getKey());
      reqObj.setOperation(ActorOperations.UPDATE_PAGE.getValue());
      reqObj.setRequestId(ExecutionContext.getRequestId());
      reqObj.getRequest().put(JsonKey.UPDATED_BY, ctx().flash().get(JsonKey.USER_ID));
      reqObj.setEnv(getEnvironment());
      HashMap<String, Object> map = new HashMap<>();
      map.put(JsonKey.PAGE, reqObj.getRequest());
      reqObj.setRequest(map);
      Timeout timeout = new Timeout(Akka_wait_time, TimeUnit.SECONDS);
      return actorResponseHandler(getRemoteActor(), reqObj, timeout, null, request());
    } catch (Exception e) {
      return Promise.<Result>pure(createCommonExceptionResponse(e, request()));
    }
  }

  /**
   * This method will provide particular page setting data.
   * 
   * @param pageId String
   * @return Promise<Result>
   */
  public Promise<Result> getPageSetting(String pageId) {

    try {
      ProjectLogger.log("getting data for particular page settings = " + pageId,
          LoggerEnum.INFO.name());
      Request reqObj = new Request();
      reqObj.setManagerName(ActorOperations.GET_PAGE_SETTING.getKey());
      reqObj.setOperation(ActorOperations.GET_PAGE_SETTING.getValue());
      reqObj.setRequestId(ExecutionContext.getRequestId());
      reqObj.setEnv(getEnvironment());
      reqObj.getRequest().put(JsonKey.ID, pageId);
      Timeout timeout = new Timeout(Akka_wait_time, TimeUnit.SECONDS);
      return actorResponseHandler(getRemoteActor(), reqObj, timeout, null, request());
    } catch (Exception e) {
      return Promise.<Result>pure(createCommonExceptionResponse(e, request()));
    }
  }


  /**
   * This method will provide completed data for all pages which is saved in cassandra DAC.
   * 
   * @return Promise<Result>
   */
  public Promise<Result> getPageSettings() {

    try {
      ProjectLogger.log("getting page settings api called = ", LoggerEnum.INFO.name());
      Request reqObj = new Request();
      reqObj.setManagerName(ActorOperations.GET_PAGE_SETTINGS.getKey());
      reqObj.setOperation(ActorOperations.GET_PAGE_SETTINGS.getValue());
      reqObj.setRequestId(ExecutionContext.getRequestId());
      reqObj.setEnv(getEnvironment());
      Timeout timeout = new Timeout(Akka_wait_time, TimeUnit.SECONDS);
      return actorResponseHandler(getRemoteActor(), reqObj, timeout, null, request());
    } catch (Exception e) {
      return Promise.<Result>pure(createCommonExceptionResponse(e, request()));
    }
  }

  /**
   * This method will provide completed data for a particular page.
   * 
   * @return Promise<Result>
   */
  public Promise<Result> getPageData() {

    try {
      JsonNode requestData = request().body().asJson();
      ProjectLogger.log("requested data for get page  = " + requestData, LoggerEnum.INFO.name());
      Request reqObj = (Request) mapper.RequestMapper.mapRequest(requestData, Request.class);
      RequestValidator.validateGetPageData(reqObj);
      reqObj.setManagerName(ActorOperations.GET_PAGE_DATA.getKey());
      reqObj.setOperation(ActorOperations.GET_PAGE_DATA.getValue());
      reqObj.setRequestId(ExecutionContext.getRequestId());
      reqObj.setEnv(getEnvironment());
      reqObj.getRequest().put(JsonKey.CREATED_BY, ctx().flash().get(JsonKey.USER_ID));
      HashMap<String, Object> map = new HashMap<>();
      map.put(JsonKey.PAGE, reqObj.getRequest());
      map.put(JsonKey.HEADER, getAllRequestHeaders(request()));
      reqObj.setRequest(map);
      Timeout timeout = new Timeout(Akka_wait_time, TimeUnit.SECONDS);
      return actorResponseHandler(getRemoteActor(), reqObj, timeout, null, request());
    } catch (Exception e) {
      return Promise.<Result>pure(createCommonExceptionResponse(e, request()));
    }
  }

  


  /**
   * This method will allow admin to create sections for page view
   * 
   * @return Promise<Result>
   */
  public Promise<Result> createPageSection() {

    try {
      JsonNode requestData = request().body().asJson();
      ProjectLogger.log("getting create page section data request=" + requestData,
          LoggerEnum.INFO.name());
      Request reqObj = (Request) mapper.RequestMapper.mapRequest(requestData, Request.class);
      RequestValidator.validateCreateSection(reqObj);
      reqObj.setManagerName(ActorOperations.CREATE_SECTION.getKey());
      reqObj.setOperation(ActorOperations.CREATE_SECTION.getValue());
      reqObj.setRequestId(ExecutionContext.getRequestId());
      reqObj.getRequest().put(JsonKey.CREATED_BY, ctx().flash().get(JsonKey.USER_ID));
      reqObj.setEnv(getEnvironment());
      HashMap<String, Object> map = new HashMap<>();
      map.put(JsonKey.SECTION, reqObj.getRequest());
      reqObj.setRequest(map);
      Timeout timeout = new Timeout(Akka_wait_time, TimeUnit.SECONDS);
      return actorResponseHandler(getRemoteActor(), reqObj, timeout, null, request());
    } catch (Exception e) {
      return Promise.<Result>pure(createCommonExceptionResponse(e, request()));
    }
  }


  /**
   * This method will allow admin to update already created page sections
   * 
   * @return Promise<Result>
   */
  public Promise<Result> updatePageSection() {

    try {
      JsonNode requestData = request().body().asJson();
      ProjectLogger.log("getting update page section data request=" + requestData,
          LoggerEnum.INFO.name());
      Request reqObj = (Request) mapper.RequestMapper.mapRequest(requestData, Request.class);
      RequestValidator.validateUpdateSection(reqObj);
      reqObj.setManagerName(ActorOperations.UPDATE_SECTION.getKey());
      reqObj.setOperation(ActorOperations.UPDATE_SECTION.getValue());
      reqObj.setRequestId(ExecutionContext.getRequestId());
      reqObj.setEnv(getEnvironment());
      HashMap<String, Object> innerMap = new HashMap<>();
      reqObj.getRequest().put(JsonKey.UPDATED_BY, ctx().flash().get(JsonKey.USER_ID));
      innerMap.put(JsonKey.SECTION, reqObj.getRequest());
      reqObj.setRequest(innerMap);
      Timeout timeout = new Timeout(Akka_wait_time, TimeUnit.SECONDS);
      return actorResponseHandler(getRemoteActor(), reqObj, timeout, null, request());
    } catch (Exception e) {
      return Promise.<Result>pure(createCommonExceptionResponse(e, request()));
    }
  }

  /**
   * This method will provide particular page section data.
   * 
   * @param sectionId String
   * @return Promise<Result>
   */
  public Promise<Result> getSection(String sectionId) {

    try {
      ProjectLogger.log("getting data for particular page section =" + sectionId,
          LoggerEnum.INFO.name());
      Request reqObj = new Request();
      reqObj.setManagerName(ActorOperations.GET_SECTION.getKey());
      reqObj.setOperation(ActorOperations.GET_SECTION.getValue());
      reqObj.setRequestId(ExecutionContext.getRequestId());
      reqObj.setEnv(getEnvironment());
      reqObj.getRequest().put(JsonKey.ID, sectionId);
      Timeout timeout = new Timeout(Akka_wait_time, TimeUnit.SECONDS);
      return actorResponseHandler(getRemoteActor(), reqObj, timeout, null, request());
    } catch (Exception e) {
      return Promise.<Result>pure(createCommonExceptionResponse(e, request()));
    }
  }


  /**
   * This method will provide completed data for all sections stored in cassandra DAC.
   * 
   * @return Promise<Result>
   */
  public Promise<Result> getSections() {

    try {
      ProjectLogger.log("get page all section method called = ", LoggerEnum.INFO.name());
      Request reqObj = new Request();
      reqObj.setManagerName(ActorOperations.GET_ALL_SECTION.getKey());
      reqObj.setOperation(ActorOperations.GET_ALL_SECTION.getValue());
      reqObj.setRequestId(ExecutionContext.getRequestId());
      reqObj.setEnv(getEnvironment());
      Timeout timeout = new Timeout(Akka_wait_time, TimeUnit.SECONDS);
      return actorResponseHandler(getRemoteActor(), reqObj, timeout, null, request());
    } catch (Exception e) {
      return Promise.<Result>pure(createCommonExceptionResponse(e, request()));
    }
  }

}
