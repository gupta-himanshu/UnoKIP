package com.knoldus.sprayservices

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Failure
import scala.util.Success
import spray.http.HttpEntity
import spray.http.HttpResponse
import spray.http.StatusCodes
import spray.httpx.marshalling.ToResponseMarshaller
import spray.routing.RequestContext




object RouteRequestHandler {

  def execute[T](ctx: RequestContext)(t: => T)(implicit marshaller: ToResponseMarshaller[(StatusCodes.Success, T)]): Unit = {
    Future(t) onComplete {
      case Success(response) => ctx.complete(StatusCodes.OK, response)
      case Failure(error) =>
        ctx.complete(StatusCodes.InternalServerError)
    }
  }
}
