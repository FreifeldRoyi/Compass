package org.freifeld.compass.boundary;

import org.freifeld.compass.entity.MongoDriver;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.ws.rs.Path;

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

	@EJB
	private MongoDriver driver;

}
