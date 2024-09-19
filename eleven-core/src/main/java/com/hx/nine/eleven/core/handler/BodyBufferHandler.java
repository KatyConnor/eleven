package com.hx.nine.eleven.core.handler;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.impl.RoutingContextInternal;

public class BodyBufferHandler implements Handler<Buffer> {

	final RoutingContext context;
	final long contentLength;
	private long bodyLimit = -1;
	Buffer body;

	public BodyBufferHandler(RoutingContext context, long contentLength) {
		this.context = context;
		this.contentLength = contentLength;
		if (contentLength != -1L || this.body == null) {
			this.initBodyBuffer();
		}
	}

	@Override
	public void handle(Buffer buffer) {
		if (this.body == null) {
			this.body = buffer;
		}else {
			this.body.appendBuffer(buffer);
		}
	}

	void end(Void v) {
		((RoutingContextInternal)this.context).setBody(this.body);
		this.body = null;
		this.context.next();
	}

	public long getBodyLimit() {
		return bodyLimit;
	}

	public void setBodyLimit(long bodyLimit) {
		this.bodyLimit = bodyLimit;
	}

	private void initBodyBuffer() {
		int initialBodyBufferSize;
		if (this.contentLength < 0L) {
			initialBodyBufferSize = 1024;
		} else if (this.contentLength > 65535L) {
			initialBodyBufferSize = 65535;
		} else {
			initialBodyBufferSize = (int)this.contentLength;
		}
		if (bodyLimit != -1) {
			initialBodyBufferSize = (int) Math.min(initialBodyBufferSize, bodyLimit);
		}
		this.body = Buffer.buffer(initialBodyBufferSize);
	}
}
