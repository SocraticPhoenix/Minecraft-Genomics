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
package com.gmail.socraticphoenix.forge.mobdna.util;

import com.gmail.socraticphoenix.forge.mobdna.capability.genome.GenomeHandler;
import com.gmail.socraticphoenix.forge.mobdna.capability.genome.GenomeProvider;
import com.gmail.socraticphoenix.forge.mobdna.dna.impl.DiploidGenome;
import net.minecraft.entity.EntityCreature;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import java.util.Random;

public class BabyHelper {
    private static final Random rand = new Random();

    public static void makeBaby(EntityCreature left, EntityCreature right, World world) {
        if(!world.isRemote) {
            EntityCreature baby = BabyRegistry.INSTANCE.makeBaby(left, right, world);
            GenomeHandler handler = baby.getCapability(GenomeProvider.GENOME_HANDLER_CAPABILITY, EnumFacing.DOWN);
            GenomeHandler momHandler = left.getCapability(GenomeProvider.GENOME_HANDLER_CAPABILITY, EnumFacing.DOWN);
            GenomeHandler dadHandler = right.getCapability(GenomeProvider.GENOME_HANDLER_CAPABILITY, EnumFacing.DOWN);
            if(momHandler.genome() instanceof DiploidGenome && dadHandler.genome() instanceof DiploidGenome) {
                DiploidGenome leftGenome = (DiploidGenome) momHandler.genome();
                DiploidGenome rightGenome = (DiploidGenome) dadHandler.genome();
                handler.setGenome(leftGenome.makeGamete(rand).merge(rightGenome.makeGamete(rand)));
            }
            baby = handler.doSpawn(baby);
            BabyRegistry.INSTANCE.modify(baby);
            baby.setLocationAndAngles((left.posX + right.posX) / 2, Math.max(left.posY, right.posY), (left.posZ + right.posZ) / 2, baby.rotationYaw, baby.rotationPitch);
            baby.getEntityData().setBoolean("mobdna_considered", true);
            world.spawnEntity(baby);
        }
    }

}
