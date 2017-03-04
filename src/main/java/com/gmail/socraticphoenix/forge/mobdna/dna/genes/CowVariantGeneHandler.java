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
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityMooshroom;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CowVariantGeneHandler implements GeneHandler {
    public static final Gene COW_VARIANT_GENE = new SimpleGene("cow_variant", "cowVar", "The gene for cow variant, including mooshroom and normal", new CowVariantGeneHandler(), 0);
    public static final Allele NORMAL = new SimpleAllele(0, "cow", "Expresses the normal cow phenotype", 0, false, COW_VARIANT_GENE);
    public static final Allele MOOSHROOM = new SimpleAllele(1, "shm", "Expresses the mooshroom cow phenotype", 1, false, COW_VARIANT_GENE);

    @Override
    public void setup(EntityCreature creature, Random random, Allele... alleles) {
        //Gene only affects spawn
    }

    @Override
    public EntityCreature modifySpawn(EntityCreature creature, Random random, Allele... alleles) {
        if(creature instanceof EntityCow) {
            Allele dominant = Allele.dominant(alleles);
            if(dominant == MOOSHROOM) {
                return new EntityMooshroom(creature.world);
            } else if (dominant == NORMAL) {
                return new EntityCow(creature.world);
            }
        }
        return null;
    }

    @Override
    public List<Allele> obtainAlleles(EntityCreature creature, Random random) {
        List<Allele> alleles = new ArrayList<>();
        if(creature instanceof EntityCow) {
            if(creature instanceof EntityMooshroom) {
                alleles.add(MOOSHROOM);
                alleles.add(COW_VARIANT_GENE.alleles().get(random.nextInt(COW_VARIANT_GENE.alleles().size())));
            } else {
                alleles.add(NORMAL);
                alleles.add(NORMAL);
            }
        }
        return alleles;
    }

}
