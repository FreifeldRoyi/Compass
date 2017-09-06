package org.freifeld.compass.boundary;

import org.freifeld.compass.controller.MongoDriver;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Path;

/**
 * @author royif
 * @since 06/09/17.
 */

@Stateless
@Path("monitor")
public class MonitorResource
{

	@EJB
	private MongoDriver driver;

	//TODO

}
