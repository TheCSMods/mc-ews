package io.github.thecsdev.extendedworldselection.client.mixin.hooks;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;

@Mixin(SelectWorldScreen.class)
public interface AccessorSelectWorldScreen
{
	/**
	 * Warning: {@link Nullable}.
	 */
	@Accessor("searchBox")
	public abstract TextFieldWidget getSearchBox();
}