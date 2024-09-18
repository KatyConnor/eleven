package com.hx.vertx.boot;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.file.FileSystem;

public class Demo {

	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		Future future = Future.future(h->{

		});

		Promise<String> promise = Promise.promise();
//		promise./
		Future future1 = promise.future();
//		future1.onComplete((AsyncResult)ar ->{
////			ar.
//		}).andThen(ar ->{
//			((AsyncResult)ar).succeeded();
//
//		});

		FileSystem fs =vertx.fileSystem();
//		fs.move()
	}
}
