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
	@EnvironmentVariable
	public String getStringEnv(InjectionPoint ip)
	{
		String fieldName = ip.getMember().getName();
		return System.getenv().getOrDefault(fieldName, "--UNDEFNIED--");
	}

	@Produces
	@EnvironmentVariable
	public int getIntEnv(InjectionPoint ip)
	{
		String env = this.getStringEnv(ip);
		return Integer.parseInt(env);
	}
}
