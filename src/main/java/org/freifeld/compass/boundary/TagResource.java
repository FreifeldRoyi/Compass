package org.freifeld.compass.boundary;

import org.freifeld.compass.controller.MongoDriver;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

@Stateless
@Path("tags")
public class TagResource
{
	@EJB
	private MongoDriver driver;

	@GET
	public Response allTags()
	{
		return Response.ok().entity(this.driver.getAllTags()).build();
	}

	@PUT
	@Path("{tag}")
	public Response newTag(@PathParam("tag") String tag)
	{
		this.driver.addTags(tag);
		return Response.created(URI.create("tags/" + tag)).build();
	}

	@DELETE
	@Path("{tag}")
	public Response removeTag(@PathParam("tag") String tag)
	{
		this.driver.removeTags(tag);
		return Response.ok().build();
	}

	@POST
	@Path("{tag}/containers/{container}")
	public Response newContainer(@PathParam("tag") String tag, @PathParam("container") String container)
	{
		this.driver.addContainer(tag, container);
		return Response.ok().build();
	}

	@DELETE
	@Path("{tag}/containers/{container}")
	public Response removeContainer(@PathParam("tag") String tag, @PathParam("container") String container)
	{
		this.driver.removeContainer(tag, container);
		return Response.ok().build();
	}

}
