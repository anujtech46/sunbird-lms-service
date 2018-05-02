/**
 * 
 */
package controllers;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.sunbird.common.exception.ProjectCommonException;
import org.sunbird.common.models.util.JsonKey;
import org.sunbird.common.models.util.LoggerEnum;
import org.sunbird.common.models.util.ProjectLogger;
import org.sunbird.common.request.Request;
import org.sunbird.common.responsecode.ResponseCode;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import controller.mapper.RequestMapper;
import play.libs.F;
import play.libs.F.Promise;
import play.mvc.Result;
import play.mvc.Results;

/**
 * This controller will handle all sunbird telemetry request data.
 * 
 * @author Manzarul
 *
 */
public class TelemetryController extends BaseController {

	// private TelemetryDataHandlerService service =
	// TelemetryServiceFactory.getInstance();
	private ObjectMapper mapper = new ObjectMapper();
	private static int defaultSize = 1000;

	static {
		try {
			String maxCountStr = System.getenv("sunbird_telemetry_request_max_count");
			if(StringUtils.isNotBlank(maxCountStr)) {
				defaultSize = Integer.parseInt(maxCountStr);
				ProjectLogger.log("Updated default telemetry_request_max_count to " + defaultSize, LoggerEnum.INFO.name());
			} else {
				ProjectLogger.log("Default telemetry_request_max_count is " + defaultSize, LoggerEnum.INFO.name());
			}
		} catch (Exception e) {
			ProjectLogger.log("Error while setting default telemetry_request_max_count. Using default value: "+ defaultSize, LoggerEnum.ERROR.name());
		}
	}
	
	/**
	 * This method will receive the telemetry data and send it to EKStep to process
	 * it.
	 * 
	 * @return F.Promise<Result>
	 */
	public F.Promise<Result> save() {
		Request request = null;
		try {
			JsonNode requestData = null;
			String contentTypeHeader = request().getHeader("content-type");
			String encodingHeader = request().getHeader("accept-encoding");
			if ("application/json".equalsIgnoreCase(contentTypeHeader)) {
				ProjectLogger.log("Receiving telemetry in json format.", requestData, LoggerEnum.INFO.name());
				requestData = request().body().asJson();
				request = (Request) RequestMapper.mapRequest(requestData, Request.class);
			} else if ("application/zip".equalsIgnoreCase(contentTypeHeader)
					&& StringUtils.containsIgnoreCase(encodingHeader, "gzip")) {
				ProjectLogger.log("Receiving telemetry in gzip format.", LoggerEnum.INFO.name());
				request = getRequest(request().body().asRaw().asBytes());
			} else {
				throw new ProjectCommonException(ResponseCode.invalidRequestData.getErrorCode(),
						"Please provide valid headers.", ResponseCode.CLIENT_ERROR.getResponseCode());
			}

			
			
			Result result = Results.status(200);
			return Promise.<Result>pure(result);
		} catch (Exception e) {
			return Promise.<Result>pure(createCommonExceptionResult(request().path(), e));
		}

	}

	/**
	 * 
	 * @param request
	 *            play.mvc.Http.Request
	 * @return Map
	 */
	private Map<String, String> getAllRequestHeaders(play.mvc.Http.Request request) {

		Map<String, String> map = new HashMap<>();
		Map<String, String[]> headers = request.headers();
		Iterator<Entry<String, String[]>> itr = headers.entrySet().iterator();
		while (itr.hasNext()) {
			Entry<String, String[]> entry = itr.next();
			map.put(entry.getKey(), entry.getValue()[0]);
		}
		String authKey = System.getenv("ekstep_authorization");
		ProjectLogger.log("Telemetry auth value is comming as =" + authKey);
		map.put("authorization", JsonKey.BEARER
				+ "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJkNjNiMjgwZTQ1NDE0NDU4ODk4NzcwYzZhOGZiZjQ1MCJ9.Ji-22XcRrOiVy4dFAmE68wPxLkNmX4wKbTj_IB7fG6Y");
		return map;
	}

	private Request getRequest(byte[] bytes) {
		List<String> allEvents = new ArrayList<String>();
		try {
			InputStream is = new ByteArrayInputStream(bytes);
			BufferedReader bfReader = new BufferedReader(new InputStreamReader(is));
			String temp = null;
			while ((temp = bfReader.readLine()) != null) {
				Map<String, Object> row = mapper.readValue(temp, Map.class);
				Map<String, Object> data = (Map<String, Object>) row.get("data");
				if (data != null) {
					List<String> events = (List<String>) data.get("events");
					allEvents.addAll(events);
				}
			}

			if (allEvents.isEmpty()) {
				throw new ProjectCommonException(ResponseCode.invalidRequestData.getErrorCode(),
						"Please provide valid binary gzip file. File is empty.",
						ResponseCode.CLIENT_ERROR.getResponseCode());
			} else if (allEvents.size() > 1000) {
				throw new ProjectCommonException(ResponseCode.invalidRequestData.getErrorCode(),
						"Too many events to process. Max limit for a request is 1000.",
						ResponseCode.CLIENT_ERROR.getResponseCode());
			} else {
				Request request = new Request();
				Map<String, List<String>> reqMap = new HashMap<String, List<String>>();
				reqMap.put("events", allEvents);
				return request;
			}
		} catch (Exception e) {
			throw new ProjectCommonException(ResponseCode.invalidRequestData.getErrorCode(),
					"Please provide valid binary gzip file. File structure is invalid.",
					ResponseCode.CLIENT_ERROR.getResponseCode());
		}
	}
}
