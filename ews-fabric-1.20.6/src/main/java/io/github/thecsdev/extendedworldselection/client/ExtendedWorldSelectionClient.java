package io.github.thecsdev.extendedworldselection.client;

import io.github.thecsdev.extendedworldselection.ExtendedWorldSelection;
import io.github.thecsdev.extendedworldselection.client.gui.widget.EwsButtonWidget;
import io.github.thecsdev.extendedworldselection.client.mixin.hooks.AccessorSelectWorldScreen;
import io.github.thecsdev.tcdcommons.api.events.client.gui.screen.ScreenEvent;
import io.github.thecsdev.tcdcommons.client.mixin.hooks.AccessorScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;

public final class ExtendedWorldSelectionClient extends ExtendedWorldSelection
{
	// ==================================================
	public ExtendedWorldSelectionClient()
	{
		//this event handler's job is to be on the lookout for the world-selection screens,
		//and to add EWS buttons to them whenever they're opened
		ScreenEvent.INIT_POST.register(screen ->
		{
			//in the world-selection screen, add a button that allows the user
			//to select the current working directory for worlds
			if(screen instanceof SelectWorldScreen swScreen)
			{
				//do not add the EWS button if it's already added
				final var screenAccessor  = (AccessorScreen)swScreen;
				if(screenAccessor.tcdcommons_children().stream().anyMatch(o -> o instanceof EwsButtonWidget))
					return;
				
				//add the EWS button otherwise
				final var screenSearchBox = ((AccessorSelectWorldScreen)screen).getSearchBox();
				final var ews = new EwsButtonWidget(screenSearchBox.getX() - 25, screenSearchBox.getY());
				screenAccessor.tcdcommons_addDrawableChild(ews);
			}
		});
	}
	// ==================================================
}