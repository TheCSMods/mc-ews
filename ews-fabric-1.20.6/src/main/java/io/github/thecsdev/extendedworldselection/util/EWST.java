package io.github.thecsdev.extendedworldselection.util;

import static io.github.thecsdev.tcdcommons.api.util.TextUtils.translatable;

import io.github.thecsdev.extendedworldselection.ExtendedWorldSelection;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

/**
 * Translatable {@link Text}s for {@link ExtendedWorldSelection}.
 */
public final class EWST
{
	// ==================================================
	private EWST() {}
	// ==================================================
	public static final MutableText ews() { return translatable("extendedworldselection"); }
	// --------------------------------------------------
	public static final MutableText io_accessCheck_tempFile() { return translatable("extendedworldselection.io.access_check.temp_file"); }
	public static final MutableText io_accessCheck_fail()     { return translatable("extendedworldselection.io.access_check.fail"); }
	// --------------------------------------------------
	public static final MutableText gui_ewsBtn_tooltip()      { return translatable("extendedworldselection.gui.ews_button.tooltip"); }
	public static final MutableText gui_ewsBtn_tooltip_tips() { return translatable("extendedworldselection.gui.ews_button.tooltip.tips"); }
	// ==================================================
}