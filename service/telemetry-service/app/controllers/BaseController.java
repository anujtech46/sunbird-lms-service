package controllers;

import org.sunbird.common.exception.ProjectCommonException;
import org.sunbird.common.models.response.Response;
import org.sunbird.common.models.response.ResponseParams;
import org.sunbird.common.models.util.JsonKey;
import org.sunbird.common.models.util.ProjectLogger;
import org.sunbird.common.models.util.ProjectUtil;
import org.sunbird.common.request.ExecutionContext;
import org.sunbird.common.responsecode.ResponseCode;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.Request;
import play.mvc.Result;
import play.mvc.Results;

/**
 * This controller we can use for writing some common method.
 * 
 * @author Manzarul
 */
public class BaseController extends Controller {
	/**
	 * This method will create failure response
	 * 
	 * @param request
	 *            Request
	 * @param code
	 *            ResponseCode
	 * @param headerCode
	 *            ResponseCode
	 * @return Response
	 */
	public static Response createFailureResponse(Request request, ResponseCode code, ResponseCode headerCode) {

		Response response = new Response();
		response.setVer(getApiVersion(request.path()));
		response.setId(getApiResponseId(request));
		response.setTs(ProjectUtil.getFormattedDate());
		response.setResponseCode(headerCode);
		response.setParams(createResponseParamObj(code));
		return response;
	}

	/**
	 * This method will create response parameters
	 * 
	 * @param code
	 *            ResponseCode
	 * @return ResponseParams
	 */
	public static ResponseParams createResponseParamObj(ResponseCode code) {

		ResponseParams params = new ResponseParams();
		if (code.getResponseCode() != 200) {
			params.setErr(code.getErrorCode());
			params.setErrmsg(code.getErrorMessage());
		}
		params.setMsgid(ExecutionContext.getRequestId());
		params.setStatus(ResponseCode.getHeaderResponseCode(code.getResponseCode()).name());
		return params;
	}

	/**
	 * This method will create data for success response.
	 * 
	 * @param request
	 *            String
	 * @param response
	 *            Response
	 * @return Response
	 */
	public static Response createSuccessResponse(Request request, Response response) {

		if (request != null) {
			response.setVer(getApiVersion(request.path()));
		} else {
			response.setVer("");
		}
		response.setId(getApiResponseId(request));
		response.setTs(ProjectUtil.getFormattedDate());
		ResponseCode code = ResponseCode.getResponse(ResponseCode.success.getErrorCode());
		code.setResponseCode(ResponseCode.OK.getResponseCode());
		response.setParams(createResponseParamObj(code));
		return response;
	}

	/**
	 * This method will provide API version.
	 *
	 * @param request
	 *            String
	 * @return String
	 */
	public static String getApiVersion(String request) {

		return request.split("[/]")[1];
	}

	/**
	 * This method will handle response in case of exception
	 *
	 * @param request
	 *            String
	 * @param exception
	 *            ProjectCommonException
	 * @return Response
	 */
	public static Response createResponseOnException(Request request, ProjectCommonException exception) {

		Response response = new Response();
		if (request != null) {
			response.setVer(getApiVersion(request.path()));
		} else {
			response.setVer("");
		}
		response.setId(getApiResponseId(request));
		response.setTs(ProjectUtil.getFormattedDate());
		response.setResponseCode(ResponseCode.getHeaderResponseCode(exception.getResponseCode()));
		ResponseCode code = ResponseCode.getResponse(exception.getCode());
		response.setParams(createResponseParamObj(code));
		return response;
	}

	/**
	 *
	 * @param path
	 *            String
	 * @param method
	 *            String
	 * @param exception
	 *            ProjectCommonException
	 * @return Response
	 */
	public static Response createResponseOnException(String path, String method, ProjectCommonException exception) {

		Response response = new Response();
		response.setVer(getApiVersion(path));
		response.setId(getApiResponseId(request()));
		response.setTs(ProjectUtil.getFormattedDate());
		response.setResponseCode(ResponseCode.getHeaderResponseCode(exception.getResponseCode()));
		ResponseCode code = ResponseCode.getResponse(exception.getCode());
		response.setParams(createResponseParamObj(code));
		return response;
	}

	/**
	 * This method will create common response for all controller method
	 *
	 * @param response
	 *            Object
	 * @param key
	 *            String
	 * @param request
	 *            Request
	 * @return Result
	 */
	public Result createCommonResponse(Object response, String key, Request request) {

		if (response instanceof Response) {
			Response courseResponse = (Response) response;
			if (!ProjectUtil.isStringNullOREmpty(key)) {
				Object value = courseResponse.getResult().get(JsonKey.RESPONSE);
				courseResponse.getResult().remove(JsonKey.RESPONSE);
				courseResponse.getResult().put(key, value);
			}
			return Results.status(ResponseCode.OK.getResponseCode(),
					Json.toJson(BaseController.createSuccessResponse(request, (Response) courseResponse)));
		} else {
			ProjectCommonException exception = (ProjectCommonException) response;
			return Results.status(exception.getResponseCode(),
					Json.toJson(BaseController.createResponseOnException(request, exception)));
		}
	}

	/**
	 * Common exception response handler method.
	 *
	 * @param e
	 *            Exception
	 * @param request
	 *            Request
	 * @return Result
	 */
	public Result createCommonExceptionResponse(Exception e, Request request) {

		ProjectLogger.log(e.getMessage(), e);
		if (request == null) {
			request = request();
		}
		ProjectCommonException exception = null;
		if (e instanceof ProjectCommonException) {
			exception = (ProjectCommonException) e;
		} else {
			exception = new ProjectCommonException(ResponseCode.internalError.getErrorCode(),
					ResponseCode.internalError.getErrorMessage(), ResponseCode.SERVER_ERROR.getResponseCode());
		}
		return Results.status(exception.getResponseCode(),
				Json.toJson(BaseController.createResponseOnException(request(), exception)));
	}

	/**
	 * 
	 * @param request
	 *            play.mvc.Http.Request
	 * @return String
	 */
	private static String getApiResponseId(play.mvc.Http.Request request) {

		return "api.telemetry.save";
	}

}


