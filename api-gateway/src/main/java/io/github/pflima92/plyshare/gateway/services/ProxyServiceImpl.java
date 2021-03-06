package io.github.pflima92.plyshare.gateway.services;

import org.jspare.core.annotation.Inject;
import org.jspare.vertx.annotation.VertxInject;

import io.github.pflima92.plyshare.common.discovery.ServiceDiscoveryHolder;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import io.vertx.servicediscovery.Record;

public class ProxyServiceImpl implements ProxyService {

	@Inject
	private ServiceDiscoveryHolder discovery;

	@VertxInject
	private Vertx vertx;

	protected HttpServerResponse badGateway(HttpServerResponse httpSrvRes) {
		return httpSrvRes.setStatusCode(502);
	}

	private String extractRequestUri(HttpServerRequest httpSrvReq, Record record) {
		return httpSrvReq.uri().substring(String.format(PROXY_PATTERN, record.getName()).length());
	}

	@Override
	public Future<Buffer> proxy(RoutingContext ctx, Record record, Buffer processedBuffer) {

		// Create Proxy Future
		Future<Buffer> future = Future.future();
		final Buffer futureBuffer = Buffer.buffer();

		// Get current request
		HttpServerRequest httpSrvReq = ctx.request();
		HttpServerResponse httpSrvRes = ctx.response();
		
		String uri = extractRequestUri(httpSrvReq, record);

		// TODO get with configurations from config()
		HttpClient httpClient = discovery.getReference(record).get();
		
		// Init HttpClient request
		HttpClientRequest httpClientReq = httpClient.request(httpSrvReq.method(), uri, httpClientRes -> {

			httpSrvRes.setChunked(true);
			httpSrvRes.setStatusCode(httpClientRes.statusCode());
			httpSrvRes.headers().setAll(httpClientRes.headers());
			httpClientRes.handler(buffer -> {
				futureBuffer.appendBuffer(buffer.copy());
				httpSrvRes.write(buffer);
			});
			httpClientRes.exceptionHandler(future::fail);
			httpClientRes.endHandler((v) -> {
				httpSrvRes.end();
				future.complete(futureBuffer);
			});
		});
		
		// Handle and proxy the request
		httpClientReq.headers().setAll(httpSrvReq.headers());
		// Handle request exceptions
		httpSrvReq.exceptionHandler(future::fail);

		// Set Request chunked
		httpClientReq.setChunked(true);
		// Handle client excpetions
		httpClientReq.exceptionHandler(future::fail);
		// Call microservice
		httpClientReq.write(processedBuffer).end();

		return future;
	}
}
