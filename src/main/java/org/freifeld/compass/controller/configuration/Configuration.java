package org.freifeld.compass.controller.configuration;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

/**
 * @author royif
 * @since 25/08/17.
 */
@ApplicationScoped
public class Configuration
{
	@Produces
	@ConfigVariable
	public String getStringEnv(InjectionPoint ip)
	{
		String fieldName = ip.getMember().getName();
		return System.getenv().getOrDefault(fieldName, "--UNDEFNIED--");
	}

	@Produces
	@ConfigVariable
	public int getIntEnv(InjectionPoint ip)e
	{
		String env = this.getStringEnv(ip);
		return Integer.parseInt(env);
	}
}
