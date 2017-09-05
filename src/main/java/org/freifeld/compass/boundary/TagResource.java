package org.freifeld.compass.boundary;

import org.freifeld.compass.controller.MongoDriver;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.net.URI;

@Stateless
@Path("tags")
public class TagResource
{
	@Resource
	private ManagedExecutorService mes;

	@EJB
	private MongoDriver driver;

	@GET
	public Response allTags()
	{
		return Response.ok().entity(this.driver.allTags()).build();
	}

	@PUT
	@Path("{tag}")
	public Response newTag(@PathParam("tag") String tag)
	{
		this.driver.addTags(tag);
		return Response.created(URI.create("tags/" + tag)).build();
	}

	@POST
	@Path("{tag}/containers/{container}")
	public Response newContainer(@PathParam("tag") String tag, @PathParam("container") String container)
	{
		this.driver.addContainer(tag, container);
		return Response.accepted().build();
	}
}
