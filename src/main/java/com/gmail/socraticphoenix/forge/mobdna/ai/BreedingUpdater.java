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
package com.gmail.socraticphoenix.forge.mobdna.ai;

import com.gmail.socraticphoenix.forge.mobdna.MobDNA;
import com.gmail.socraticphoenix.forge.mobdna.capability.genome.GenomeHandler;
import com.gmail.socraticphoenix.forge.mobdna.capability.genome.GenomeProvider;
import com.gmail.socraticphoenix.forge.mobdna.util.ProbabilityHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BreedingUpdater {

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent ev) {
        Entity entity = ev.getEntity();
        if(entity instanceof EntityCreature) {
            EntityCreature creature = (EntityCreature) entity;
            if(!this.data(creature).mating()) {
                if (!creature.getEntityData().hasKey("mobdna_delay")) {
                    this.setDelay(creature, (int) ProbabilityHelper.rand(MobDNA.minBreedWait(), MobDNA.maxBreedWait(), creature.getRNG()));
                } else if (this.delay(creature) == 0) {
                    this.data(creature).setMating(true);
                    this.setDelay(creature, (int) ProbabilityHelper.rand(MobDNA.minBreedWait(), MobDNA.maxBreedWait(), creature.getRNG()));
                } else {
                    this.setDelay(creature, this.delay(creature) - 1);
                }
            }
        }
    }

    private GenomeHandler data(EntityCreature creature) {
        return creature.getCapability(GenomeProvider.GENOME_HANDLER_CAPABILITY, EnumFacing.DOWN);
    }

    private int delay(EntityCreature creature) {
        return creature.getEntityData().getInteger("mobdna_delay");
    }

    private void setDelay(EntityCreature creature, int delay) {
        creature.getEntityData().setInteger("mobdna_delay", delay);
    }

}
