/*
 *
 */

package org.jspare.spareco.common.discovery;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.serviceproxy.ServiceException;
import io.vertx.serviceproxy.ServiceExceptionMessageCodec;

/*
  Generated Proxy code - DO NOT EDIT
  @author Roger the Robot
*/
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ConfigurationProviderVertxEBProxy implements ConfigurationProvider {

	private Vertx _vertx;
	private String _address;
	private DeliveryOptions _options;
	private boolean closed;

	public ConfigurationProviderVertxEBProxy(Vertx vertx, String address) {
		this(vertx, address, null);
	}

	public ConfigurationProviderVertxEBProxy(Vertx vertx, String address, DeliveryOptions options) {
		_vertx = vertx;
		_address = address;
		_options = options;
		try {
			_vertx.eventBus().registerDefaultCodec(ServiceException.class, new ServiceExceptionMessageCodec());
		} catch (IllegalStateException ex) {
		}
	}

	@Override
	public ConfigurationProvider getConfiguration(String alias, Handler<AsyncResult<JsonObject>> resultHandler) {
		if (closed) {
			resultHandler.handle(Future.failedFuture(new IllegalStateException("Proxy is closed")));
			return this;
		}
		JsonObject _json = new JsonObject();
		_json.put("alias", alias);
		DeliveryOptions _deliveryOptions = _options != null ? new DeliveryOptions(_options) : new DeliveryOptions();
		_deliveryOptions.addHeader("action", "getConfiguration");
		_vertx.eventBus().<JsonObject>send(_address, _json, _deliveryOptions, res -> {
			if (res.failed()) {
				resultHandler.handle(Future.failedFuture(res.cause()));
			} else {
				resultHandler.handle(Future.succeededFuture(res.result().body()));
			}
		});
		return this;
	}

	private <T> List<T> convertList(List list) {
		if (list.isEmpty()) {
			return list;
		}

		Object elem = list.get(0);
		if (!(elem instanceof Map) && !(elem instanceof List)) {
			return list;
		} else {
			Function<Object, T> converter;
			if (elem instanceof List) {
				converter = object -> (T) new JsonArray((List) object);
			} else {
				converter = object -> (T) new JsonObject((Map) object);
			}
			return (List<T>) list.stream().map(converter).collect(Collectors.toList());
		}
	}

	private <T> Map<String, T> convertMap(Map map) {
		if (map.isEmpty()) {
			return map;
		}

		Object elem = map.values().stream().findFirst().get();
		if (!(elem instanceof Map) && !(elem instanceof List)) {
			return map;
		} else {
			Function<Object, T> converter;
			if (elem instanceof List) {
				converter = object -> (T) new JsonArray((List) object);
			} else {
				converter = object -> (T) new JsonObject((Map) object);
			}
			return ((Map<String, T>) map).entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, converter::apply));
		}
	}

	private <T> Set<T> convertSet(List list) {
		return new HashSet<T>(convertList(list));
	}

	private List<Character> convertToListChar(JsonArray arr) {
		List<Character> list = new ArrayList<>();
		for (Object obj : arr) {
			Integer jobj = (Integer) obj;
			list.add((char) (int) jobj);
		}
		return list;
	}

	private Set<Character> convertToSetChar(JsonArray arr) {
		Set<Character> set = new HashSet<>();
		for (Object obj : arr) {
			Integer jobj = (Integer) obj;
			set.add((char) (int) jobj);
		}
		return set;
	}
}