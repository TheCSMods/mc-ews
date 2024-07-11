package io.github.thecsdev.extendedworldselection.client.mixin.events;

import static io.github.thecsdev.tcdcommons.client.TCDCommonsClient.MC_CLIENT;

import java.nio.file.Path;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.thecsdev.extendedworldselection.mixin.hooks.AccessorLevelStorage;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.screen.world.WorldListWidget;
import net.minecraft.text.Text;

@Mixin(WorldListWidget.class)
public class MixinWorldListWidget
{
	/**
	 * The following {@link Mixin} prevents the game from opening the {@link CreateWorldScreen} when
	 * the currently selected "saves" directory is empty. This prevents the user from being forced to
	 * create a new world before gaining access to the {@link SelectWorldScreen}.
	 * For obvious reasons, if the user is forced to create a new world, then that means they cannot
	 * change the current "saves" directory, hence why this {@link Mixin} fixes that issue.
	 */
	@Redirect(
			method = "loadLevels",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/screen/world/CreateWorldScreen;create(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/gui/screen/Screen;)V"
				),
			require = 1,
			remap = true
		)
	private void antiAutoCreateWorldScreen(MinecraftClient client, @Nullable Screen parent) {}
	
	/**
	 * The following {@link Mixin} prevents the user from being soft-locked in a "saves" directory
	 * the game is unable to access. It does so by reverting back to the default "saves" directory
	 * whenever a "fatal error" occurs that is related to level list loading.
	 */
	@Inject(method = "showUnableToLoadScreen", at = @At("HEAD"), require = 1, remap = true)
	private void onFatalError(Text message, CallbackInfo ci)
	{
		((AccessorLevelStorage)MC_CLIENT.getLevelStorage()).setSavesDirectory(
				Path.of(System.getProperty("user.dir"), "saves")
			);
	}
}