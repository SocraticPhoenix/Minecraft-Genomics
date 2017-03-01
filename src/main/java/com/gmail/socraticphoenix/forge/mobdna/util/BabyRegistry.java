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

import com.google.common.base.Predicate;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class BabyRegistry {
    public static final BabyRegistry INSTANCE = new BabyRegistry();
    private Map<Class, Consumer<EntityCreature>> babyModifiers;
    private Map<Class, Predicate<EntityCreature>> isBaby;
    private Map<Class, TriFunction<EntityCreature, EntityCreature, World, EntityCreature>> babyMakers;

    public BabyRegistry() {
        this.babyModifiers = new HashMap<>();
        this.isBaby = new HashMap<>();
        this.babyMakers = new HashMap<>();
        this.registerDefaults();
    }

    private void registerDefaults() {
        register(EntityZombie.class, e -> {
            EntityZombie zombie = (EntityZombie) e;
            zombie.setChild(true);
        }, e -> {
            EntityZombie zombie = (EntityZombie) e;
            return zombie.isChild();
        });

        register(EntityAgeable.class, e -> {
            EntityAgeable ageable = (EntityAgeable) e;
            ageable.setGrowingAge(-24000);
        }, e -> {
            EntityAgeable ageable = (EntityAgeable) e;
            return ageable.isChild();
        }, (l, r, w) -> ((EntityAgeable) l).createChild((EntityAgeable) r));
    }

    public void register(Class<? extends EntityCreature> clazz, Consumer<EntityCreature> modifier, Predicate<EntityCreature> isBaby, TriFunction<EntityCreature, EntityCreature, World, EntityCreature> babyMaker) {
        this.babyModifiers.put(clazz, modifier);
        this.isBaby.put(clazz, isBaby);
        this.babyMakers.put(clazz, babyMaker);
    }

    public void register(Class<? extends EntityCreature> clazz, Consumer<EntityCreature> modifier, Predicate<EntityCreature> isBaby) {
        this.babyModifiers.put(clazz, modifier);
        this.isBaby.put(clazz, isBaby);
    }

    public void register(Class<? extends EntityCreature> clazz, Consumer<EntityCreature> modifier) {
        this.babyModifiers.put(clazz, modifier);
    }

    public boolean isBaby(EntityCreature baby) {
        for(Map.Entry<Class, Predicate<EntityCreature>> entry : this.isBaby.entrySet()) {
            if(entry.getKey().isInstance(baby)) {
                if(entry.getValue().apply(baby)) {
                    return true;
                }
            }
        }

        return false;
    }

    public EntityCreature makeBaby(EntityCreature left, EntityCreature right, World world) {
        for(Map.Entry<Class, TriFunction<EntityCreature, EntityCreature, World, EntityCreature>> entry : this.babyMakers.entrySet()) {
            if(entry.getKey().isInstance(left) && entry.getKey().isInstance(right)) {
                EntityCreature baby = entry.getValue().apply(left, right, world);
                if(baby != null) {
                    return baby;
                }
            }
        }

        return (EntityCreature) EntityRegistry.getEntry(left.getClass()).newInstance(world);
    }

    public void modify(EntityCreature baby) {
        for(Map.Entry<Class, Consumer<EntityCreature>> entry : this.babyModifiers.entrySet()) {
            if(entry.getKey().isInstance(baby)) {
                entry.getValue().accept(baby);
                break;
            }
        }
    }



}
