/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 socraticphoenix@gmail.com
 * Copyright (c) 2016 contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.gmail.socraticphoenix.forge.mobdna;

import com.gmail.socraticphoenix.forge.mobdna.ai.AIApplicator;
import com.gmail.socraticphoenix.forge.mobdna.ai.BreedingUpdater;
import com.gmail.socraticphoenix.forge.mobdna.capability.GenomeApplicator;
import com.gmail.socraticphoenix.forge.mobdna.capability.GenomeHandler;
import com.gmail.socraticphoenix.forge.mobdna.capability.GenomeHandlerImpl;
import com.gmail.socraticphoenix.forge.mobdna.capability.GenomeStorage;
import com.gmail.socraticphoenix.forge.mobdna.dna.GeneApplicator;
import com.gmail.socraticphoenix.forge.mobdna.module.ZombieGrower;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

@Mod(modid = "mobdna", name = "Minecraft Genomics", version = "0.0.0")
public class MobDNA {
    private static MobDNA instance;

    private Configuration configuration;

    public MobDNA() {
        MinecraftForge.EVENT_BUS.register(new AIApplicator());
        MinecraftForge.EVENT_BUS.register(new BreedingUpdater());
        MinecraftForge.EVENT_BUS.register(new GenomeApplicator());
        MinecraftForge.EVENT_BUS.register(new GeneApplicator());
        MinecraftForge.EVENT_BUS.register(new ZombieGrower());
        this.configuration = new Configuration(new File("config" + File.separator + "mobdna" + File.separator + "conf.cfg"));
        MobDNA.instance = this;
    }

    public static MobDNA getInstance() {
        return MobDNA.instance;
    }

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent ev) {
        this.configuration.load();
        ConfigCategory category = this.configuration.getCategory("config");
        if(!category.containsKey("grow_zombies")) {
            category.put("grow_zombies", new Property("grow_zombies", "true", Property.Type.BOOLEAN));
        }
        CapabilityManager.INSTANCE.register(GenomeHandler.class, new GenomeStorage(), GenomeHandlerImpl.class);

    }

    public boolean growsZombies() {
        return this.configuration.getCategory("config").get("grow_zombies").getBoolean();
    }

}
