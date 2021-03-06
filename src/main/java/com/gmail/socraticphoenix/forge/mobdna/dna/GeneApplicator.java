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
package com.gmail.socraticphoenix.forge.mobdna.dna;

import com.gmail.socraticphoenix.forge.mobdna.capability.genome.GenomeHandler;
import com.gmail.socraticphoenix.forge.mobdna.capability.genome.GenomeProvider;
import com.gmail.socraticphoenix.forge.mobdna.capability.setup.SetupHandler;
import com.gmail.socraticphoenix.forge.mobdna.capability.setup.SetupProvider;
import com.gmail.socraticphoenix.forge.mobdna.dna.impl.GenomeFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GeneApplicator {

    @SubscribeEvent
    public void onSpawn(EntityJoinWorldEvent ev) {
        Entity entity = ev.getEntity();
        if(!entity.world.isRemote && entity instanceof EntityCreature) {
            EntityCreature creature = (EntityCreature) entity;
            GenomeHandler handler = creature.getCapability(GenomeProvider.GENOME_HANDLER_CAPABILITY, EnumFacing.DOWN);
            if(!creature.getEntityData().getBoolean("mobdna_considered")) {
                creature.getEntityData().setBoolean("mobdna_considered", true);
                handler.setGenome(GenomeFactory.createGenome(creature));
            }

            if(!this.data(creature).isSetup()) {
                this.data(creature).setSetup(true);
                handler.setup(creature);
            }
        }
    }

    private SetupHandler data(EntityCreature creature) {
        return creature.getCapability(SetupProvider.GENOME_HANDLER_CAPABILITY, EnumFacing.DOWN);
    }

}
