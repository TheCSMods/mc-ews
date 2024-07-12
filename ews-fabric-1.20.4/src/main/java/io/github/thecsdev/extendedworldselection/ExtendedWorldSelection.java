package io.github.thecsdev.extendedworldselection;

import org.jetbrains.annotations.ApiStatus.Internal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.loader.api.FabricLoader;

//Tip: Use your IDE's refactoring tools to rename the Class
public class ExtendedWorldSelection extends Object
{
	// ==================================================
	public  static final Logger LOGGER;   // WARNING: Do NOT manually assign any values to any of
	private static final String MOD_NAME; // these variables! The mod will take care of this for you!
	private static @Internal ExtendedWorldSelection INSTANCE; // <-- Do not touch this either.
	private final ExtendedWorldSelectionConfig config;        // <-- Automatically assigned as well.
	// --------------------------------------------------
	private static final String MOD_ID = "extendedworldselection";
	// ==================================================
	public ExtendedWorldSelection()
	{
		//validate instance first
		if(isModInitialized())
			throw new IllegalStateException(getModID() + " has already been initialized.");
		else if(!isInstanceValid(this))
			throw new UnsupportedOperationException("Invalid '" + getModID() + "' type: " + this.getClass().getName());
		
		//assign instance, and log the initialization
		INSTANCE = this;
		LOGGER.info("Initializing '" + getModName() + "' as '" + getClass().getSimpleName() + "'.");
		
		//initialize the config
		this.config = new ExtendedWorldSelectionConfig();
		this.config.loadFromFileOrCrash(true);
		
		//initialize the mod
		onInitializeMod();
	}
	// --------------------------------------------------
	private final void onInitializeMod()
	{
		//TODO - Initialize common stuff here (client/dedicated-server/internal-server)
	}
	// ==================================================
	public final ExtendedWorldSelectionConfig getConfig() { return this.config; }
	// --------------------------------------------------
	public static final ExtendedWorldSelection getInstance() { return INSTANCE; }
	// --------------------------------------------------
	public static final String getModName() { return MOD_NAME; }
	public static final String getModID() { return MOD_ID; }
	// --------------------------------------------------
	public static final boolean isModInitialized() { return isInstanceValid(INSTANCE); }
	private static final boolean isInstanceValid(ExtendedWorldSelection instance) { return isServer(instance) || isClient(instance); }
	// --------------------------------------------------
	public static final boolean isServer() { return isServer(INSTANCE); }
	public static final boolean isClient() { return isClient(INSTANCE); }
	//
	private static final boolean isServer(ExtendedWorldSelection arg0) { return arg0 instanceof io.github.thecsdev.extendedworldselection.server.ExtendedWorldSelectionServer; }
	private static final boolean isClient(ExtendedWorldSelection arg0) { return arg0 instanceof io.github.thecsdev.extendedworldselection.client.ExtendedWorldSelectionClient; }
	// ==================================================
	//Note: responsible for assigning static variable values. do not touch;
	static
	{
		//find mod info
		final var fl = FabricLoader.getInstance();
		if(!fl.isModLoaded(MOD_ID))
			throw new ExceptionInInitializerError("Invalid ModID value: '" + MOD_ID + "'."
					+ " Make sure you have assigned the proper value for the mod ID!");
		final var mc = fl.getModContainer(MOD_ID).get();
		
		//initialize variables
		LOGGER   = LoggerFactory.getLogger(MOD_ID);
		MOD_NAME = mc.getMetadata().getName();
	}
	// ==================================================
}