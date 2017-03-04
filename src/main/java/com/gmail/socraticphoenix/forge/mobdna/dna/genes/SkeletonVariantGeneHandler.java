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
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityStray;
import net.minecraft.entity.monster.EntityWitherSkeleton;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SkeletonVariantGeneHandler implements GeneHandler {
    public static final Gene SKELETON_VARIANT_GENE = new SimpleGene("skeleton_variant", "skelVar", "The gene for skeleton variant, including wither skeleton, stray, and normal", new SkeletonVariantGeneHandler(), 0);
    public static final Allele NORMAL = new SimpleAllele(0, "skl", "Expresses the normal skeleton phenotype", 1, false, SKELETON_VARIANT_GENE);
    public static final Allele WITHER = new SimpleAllele(1, "wth", "Expresses the wither skeleton phenotype", 2, false, SKELETON_VARIANT_GENE);
    public static final Allele STRAY = new SimpleAllele(2, "sry", "Expresses the stray skeleton phenotype", 0, false, SKELETON_VARIANT_GENE);

    @Override
    public void setup(EntityCreature creature, Random random, Allele... alleles) {
        //Gene only affects spawn
    }

    @Override
    public EntityCreature modifySpawn(EntityCreature creature, Random random, Allele... alleles) {
        Allele allele = Allele.dominant(alleles);
        if(allele == NORMAL) {
            return new EntitySkeleton(creature.world);
        } else if (allele == WITHER) {
            return new EntityWitherSkeleton(creature.world);
        } else if (allele == STRAY) {
            return new EntityStray(creature.world);
        }
        return null;
    }

    @Override
    public List<Allele> obtainAlleles(EntityCreature creature, Random random) {
        List<Allele> alleles = new ArrayList<>();
        if(creature instanceof AbstractSkeleton) {
            if(creature instanceof EntityStray) {
                alleles.add(STRAY);
                alleles.add(STRAY);
            } else if (creature instanceof EntityWitherSkeleton) {
                alleles.add(WITHER);
                alleles.add(SKELETON_VARIANT_GENE.alleles().get(random.nextInt(SKELETON_VARIANT_GENE.alleles().size())));
            } else {
                alleles.add(NORMAL);
                alleles.add(random.nextBoolean() ? STRAY : NORMAL);
            }
        }
        return alleles;
    }

}
