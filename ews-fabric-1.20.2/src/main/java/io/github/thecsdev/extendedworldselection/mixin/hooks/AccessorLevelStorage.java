package io.github.thecsdev.extendedworldselection.mixin.hooks;

import java.nio.file.Path;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.level.storage.LevelStorage;

@Mixin(LevelStorage.class)
public interface AccessorLevelStorage
{
	@Mutable
	@Accessor("savesDirectory")
	public abstract void setSavesDirectory(Path savesDirectory);
	
	@Accessor("savesDirectory")
	public abstract Path getSavesDirectory();
}