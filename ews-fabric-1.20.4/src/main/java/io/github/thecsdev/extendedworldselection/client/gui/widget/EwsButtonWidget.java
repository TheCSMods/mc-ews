package io.github.thecsdev.extendedworldselection.client.gui.widget;

import static io.github.thecsdev.extendedworldselection.ExtendedWorldSelection.getModID;
import static io.github.thecsdev.tcdcommons.api.client.gui.widget.TClickableWidget.T_WIDGETS_TEXTURE;
import static io.github.thecsdev.tcdcommons.api.util.TextUtils.literal;
import static io.github.thecsdev.tcdcommons.client.TCDCommonsClient.MC_CLIENT;

import java.awt.Rectangle;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.ApiStatus.Internal;

import io.github.thecsdev.extendedworldselection.ExtendedWorldSelection;
import io.github.thecsdev.extendedworldselection.mixin.hooks.AccessorLevelStorage;
import io.github.thecsdev.extendedworldselection.util.EWST;
import io.github.thecsdev.tcdcommons.api.client.gui.screen.TStackTraceScreen;
import io.github.thecsdev.tcdcommons.api.client.gui.screen.explorer.TFileChooserResult;
import io.github.thecsdev.tcdcommons.api.client.gui.screen.explorer.TFileChooserResult.ReturnValue;
import io.github.thecsdev.tcdcommons.api.client.gui.screen.explorer.TFileChooserScreen;
import io.github.thecsdev.tcdcommons.api.client.gui.util.GuiUtils;
import io.github.thecsdev.tcdcommons.api.client.gui.util.UITexture;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.util.Formatting;

/**
 * {@link ExtendedWorldSelection}'s {@link ButtonWidget} that opens a menu that
 * allows the user to select another world "saves" working directory.
 */
@Internal
public final class EwsButtonWidget extends ButtonWidget
{
	// ==================================================
	private static final UITexture TEX = new UITexture(T_WIDGETS_TEXTURE, new Rectangle(20, 20, 20, 20));
	// ==================================================
	public EwsButtonWidget(int x, int y)
	{
		super(x, y, 20, 20, literal(""), EwsButtonWidget::onPress, ns -> EWST.ews());
		final var btnTooltip = literal("")
				.append(EWST.ews().formatted(Formatting.YELLOW))
				.append("\n")
				.append(EWST.gui_ewsBtn_tooltip().formatted(Formatting.GRAY))
				.append("\n\n")
				.append(EWST.gui_ewsBtn_tooltip_tips().formatted(Formatting.BLUE));
		setTooltip(Tooltip.of(btnTooltip));
	}
	// ==================================================
	protected final @Override void renderWidget(DrawContext context, int mouseX, int mouseY, float delta)
	{
		super.renderWidget(context, mouseX, mouseY, delta);
		TEX.drawTexture(context, getX() + 3, getY() + 3, 14, 14);
	}
	// ==================================================
	/**
	 * Handles {@link EwsButtonWidget} presses.
	 */
	private static final void onPress(ButtonWidget btn)
	{
		//when clicked, show a directory chooser dialog prompt
		//(also clear the tooltip due to a bug with how vanilla Screen-s handle them)
		btn.setTooltip(null);
		TFileChooserScreen.builder()
			.setParentScreen(MC_CLIENT.currentScreen)
			.setStartingPath(((AccessorLevelStorage)MC_CLIENT.getLevelStorage()).getSavesDirectory())
			.showSelectDirectoryDialog(result -> handleEwsDirSelection(result));
	}
	// --------------------------------------------------
	/**
	 * A handler that handles the user selecting a new "saves" directory.
	 * @see #createEwsButtonWidget(int, int)
	 */
	private static final void handleEwsDirSelection(TFileChooserResult result)
	{
		//handle the file chooser result; only handle approvals
		if(result.getReturnValue() != ReturnValue.APPROVE_OPTION)
			return;
		
		//handle the new directory, and check if it's fully safe to work with
		final var newDir = result.getSelectedFile().toPath();
		try
		{
			//validate read access to the directory by listing files inside of it
			if(newDir.toFile().list() == null)
				throw new IOException("Failed to validate IO read access to the directory");
			
			//validate write access to the directory by creating a temporary file and then deleting it
			//(we use this method specifically, to be 100% sure there won't be issues later on)
			try
			{
				final var tempFile = newDir.resolve(getModID() + ".access_check.tmp.txt").toFile();
				final var tempText = EWST.io_accessCheck_tempFile().getString();
				FileUtils.writeStringToFile(tempFile, tempText, StandardCharsets.UTF_8);
				tempFile.delete();
			}
			catch(Exception wExc) { throw new IOException("Failed to validate IO write access to the directory", wExc); }
		}
		catch(Exception exc)
		{
			//handle failed validation attempts by displaying an error dialog box
			final var msg = EWST.io_accessCheck_fail().getString();
			MC_CLIENT.setScreen(new TStackTraceScreen(null, new IOException(msg, exc)).getAsScreen());
			return;
		}
		
		//set the new saves directory, and re-initialize the screen
		//(a new screen instance must be created for this to work)
		((AccessorLevelStorage)MC_CLIENT.getLevelStorage()).setSavesDirectory(newDir);
		MC_CLIENT.setScreen(new SelectWorldScreen(GuiUtils.getCurrentScreenParent()));
	}
	// ==================================================
}