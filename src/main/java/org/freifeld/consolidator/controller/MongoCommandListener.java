package org.freifeld.consolidator.controller;

import com.mongodb.event.CommandFailedEvent;
import com.mongodb.event.CommandListener;
import com.mongodb.event.CommandStartedEvent;
import com.mongodb.event.CommandSucceededEvent;

/**
 * @author royif
 * @since 06/09/17.
 */
public class MongoCommandListener implements CommandListener
{
	@Override
	public void commandStarted(CommandStartedEvent event)
	{
		//TODO
	}

	@Override
	public void commandSucceeded(CommandSucceededEvent event)
	{
		//TODO
	}

	@Override
	public void commandFailed(CommandFailedEvent event)
	{
		//TODO
	}
}
