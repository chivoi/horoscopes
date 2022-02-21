package com.sensors.sensors;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

//@Path("/hello")
//public class HelloWorldService {
//  @GET
//  @Path("/users")
//  public Response getAllUsers() {
//    String result = "<h1>RESTful Demo Application</h1>In real world application, a collection of users will be returned !!";
//    return Response.status(200).entity(result).build();
//  }
//}

@Path("/test")
public class HelloWorldService {
  @GET
  @Path("/echo")
  public String echo() {
    return "Hello world!";
  }
}
