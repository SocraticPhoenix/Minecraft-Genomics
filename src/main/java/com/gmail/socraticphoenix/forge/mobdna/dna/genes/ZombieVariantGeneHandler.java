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
package com.gmail.socraticphoenix.forge.mobdna.dna.genes;

import com.gmail.socraticphoenix.forge.mobdna.dna.Allele;
import com.gmail.socraticphoenix.forge.mobdna.dna.Gene;
import com.gmail.socraticphoenix.forge.mobdna.dna.GeneHandler;
import com.gmail.socraticphoenix.forge.mobdna.dna.impl.SimpleAllele;
import com.gmail.socraticphoenix.forge.mobdna.dna.impl.SimpleGene;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityZombieVillager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ZombieVariantGeneHandler implements GeneHandler {
    public static final Gene ZOMBIE_VARIANT_GENE = new SimpleGene("zombie_varient", "zombVar", "The gene for zombie variant, including pig, villager, and normal", new ZombieVariantGeneHandler(), 0);
    public static final Allele VILLAGER = new SimpleAllele(0, "vil", "Expresses the villager zombie phenotype", 1, false, ZOMBIE_VARIANT_GENE);
    public static final Allele PIG = new SimpleAllele(1, "pig", "Expresses the pig zombie phenotype", 0, false, ZOMBIE_VARIANT_GENE);
    public static final Allele ZOMBIE = new SimpleAllele(2, "zom", "Expresses the normal zombie phenotype", 2, false, ZOMBIE_VARIANT_GENE);

    @Override
    public void setup(EntityCreature creature, Random random, Allele... alleles) {
        //Gene only affects spawn
    }

    @Override
    public EntityCreature modifySpawn(EntityCreature creature, Random random, Allele... alleles) {
        if(creature instanceof EntityZombie) {
            EntityZombie zombie = (EntityZombie) creature;
            Allele dominant = Allele.dominant(alleles);
            if (dominant == VILLAGER) {
                return new EntityZombieVillager(zombie.world);
            } else if(dominant == PIG) {
                return new EntityPigZombie(zombie.world);
            } else {
                return new EntityZombie(zombie.world);
            }
        }

        return null;
    }

    @Override
    public List<Allele> obtainAlleles(EntityCreature creature, Random random) {
        List<Allele> alleles = new ArrayList<>();
        if(creature instanceof EntityZombie) {
            if(creature instanceof EntityZombieVillager) {
                alleles.add(VILLAGER);
                alleles.add(random.nextBoolean() ? VILLAGER : PIG);
            } else if (creature instanceof EntityPigZombie) {
                alleles.add(PIG);
                alleles.add(PIG);
            } else {
                alleles.add(ZOMBIE);
                alleles.add(ZOMBIE_VARIANT_GENE.alleles().get(random.nextInt(ZOMBIE_VARIANT_GENE.alleles().size())));
            }
        }
        return alleles;
    }

}
