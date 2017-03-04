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

import com.gmail.socraticphoenix.forge.mobdna.capability.genome.GenomeHandler;
import com.gmail.socraticphoenix.forge.mobdna.capability.genome.GenomeProvider;
import com.gmail.socraticphoenix.forge.mobdna.util.BabyHelper;
import com.gmail.socraticphoenix.forge.mobdna.util.BabyRegistry;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import java.util.List;

public class AIEntityBreed extends EntityAIBase {
    private EntityCreature theAnimal;
    private World world;
    private EntityCreature targetMate;
    private EntityCreature overrideMate;
    private int spawnBabyDelay;
    private double moveSpeed;
    private Class<? extends EntityCreature> mateClass;

    public AIEntityBreed(EntityCreature theAnimal, double moveSpeed, Class<? extends EntityCreature> mateClass) {
        this.theAnimal = theAnimal;
        this.moveSpeed = moveSpeed;
        this.world = theAnimal.world;
        this.mateClass = mateClass;
        this.setMutexBits(3);
    }

    @Override
    public boolean shouldExecute() {
        this.targetMate = this.overrideMate == null ? this.nearbyMate() : this.overrideMate;
        return !BabyRegistry.INSTANCE.isBaby(this.theAnimal) && this.targetMate != null && this.data(this.theAnimal).mating();
    }

    @Override
    public boolean continueExecuting() {
        return this.targetMate.isEntityAlive() && this.data(this.targetMate).mating() && this.spawnBabyDelay < 60 && this.theAnimal.isEntityAlive() && this.data(this.theAnimal).mating();
    }

    @Override
    public void resetTask() {
        this.targetMate = null;
        this.overrideMate = null;
        this.spawnBabyDelay = 0;
    }

    @Override
    public void updateTask() {
        this.theAnimal.getLookHelper().setLookPositionWithEntity(this.targetMate, 10.0F, (float) this.theAnimal.getVerticalFaceSpeed());
        this.theAnimal.getNavigator().tryMoveToEntityLiving(this.targetMate, this.moveSpeed);
        this.spawnBabyDelay++;

        if (this.spawnBabyDelay >= 60 && this.theAnimal.getDistanceSqToEntity(this.targetMate) < 9.0D) {
            BabyHelper.makeBaby(this.theAnimal, this.targetMate, this.world);
            this.data(this.theAnimal).setMating(false);
            this.data(this.targetMate).setMating(false);
        }
    }

    @Override
    public void startExecuting() {
        this.overrideMate = this.targetMate;
        this.targetMate.tasks.taskEntries.stream().filter(e -> e.action instanceof AIEntityBreed).map(e -> (AIEntityBreed) e.action).forEach(e -> e.overrideMate = this.theAnimal);
    }

    private EntityCreature nearbyMate() {
        List<EntityCreature> list = this.world.getEntitiesWithinAABB(this.mateClass, this.theAnimal.getEntityBoundingBox().expandXyz(8));
        double d0 = Double.MAX_VALUE;
        EntityCreature res = null;
        for (EntityCreature creature : list) {
            if (creature.getDistanceSqToEntity(this.theAnimal) < d0 && this.canMate(creature)) {
                res = creature;
                d0 = creature.getDistanceSqToEntity(this.theAnimal);
            }
        }
        return res;
    }

    private boolean canMate(EntityCreature potential) {
        return potential != this.theAnimal && !BabyRegistry.INSTANCE.isBaby(this.theAnimal) && !BabyRegistry.INSTANCE.isBaby(potential) && this.mateClass.isInstance(potential) && this.data(potential).mating() && potential.tasks.taskEntries.stream().filter(e -> e.action instanceof AIEntityBreed).map(e -> (AIEntityBreed) e.action).anyMatch(b -> b.overrideMate == null && b.targetMate == null);
    }

    @Override
    public boolean isInterruptible() {
        return false;
    }

    private GenomeHandler data(EntityCreature creature) {
        return creature.getCapability(GenomeProvider.GENOME_HANDLER_CAPABILITY, EnumFacing.DOWN);
    }

}
