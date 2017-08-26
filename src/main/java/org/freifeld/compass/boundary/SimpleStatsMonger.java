package org.freifeld.compass.boundary;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Response;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Use this class to listen to docker stats sent from different hosts
 *
 * @author royif
 * @since 15/08/17.
 */
@Stateless
@Path("monger")
public class SimpleStatsMonger
{
	@Resource
	private ManagedExecutorService mes;

//	@GET
//	public Response getStats(@Suspended AsyncResponse response)
//	{
//		response.setTimeout(1L, TimeUnit.SECONDS);
//		response.setTimeoutHandler(res -> res.resume(Response.status(Response.Status.REQUEST_TIMEOUT).build()));
//
//		return CompletableFuture.supplyAsync(() ->
//		{
//
//		}, this.mes);
//	}
//
//	@POST
//	public Response monger(String data)
//	{
//
//	}
}
