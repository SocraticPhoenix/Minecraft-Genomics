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
package com.gmail.socraticphoenix.forge.mobdna.dna.impl;

import com.gmail.socraticphoenix.forge.mobdna.dna.Allele;
import com.gmail.socraticphoenix.forge.mobdna.dna.Gene;
import com.gmail.socraticphoenix.forge.mobdna.dna.GeneRegistry;
import com.gmail.socraticphoenix.forge.mobdna.dna.Genome;
import net.minecraft.entity.EntityCreature;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;
import java.util.Random;

public class GenomeFactory {
    public static final int CHROMOSOME_COUNT = 26;
    private static final Random rand = new Random();

    public static Genome from(NBTTagCompound compound) {
        Genome genome;
        if(compound.hasKey("haploid_genome")) {
            genome = new HaploidGenome();
        } else {
            genome = new DiploidGenome();
        }
        genome.read(compound);
        return genome;
    }

    public static Genome createGenome(EntityCreature creature) {
        DiploidGenome genome = new DiploidGenome();
        for (int i = 0; i < GenomeFactory.CHROMOSOME_COUNT; i++) {
            SimpleChromosome left = new SimpleChromosome();
            SimpleChromosome right = new SimpleChromosome();

            for(Gene gene : GeneRegistry.INSTANCE.getGenesByChromosome(i)) {
                List<Allele> variants = gene.handler().obtainAlleles(creature, rand);
                if(variants != null && variants.size() == 2) {
                    if(creature.getRNG().nextBoolean()) {
                        left.alleles().add(variants.get(0));
                        right.alleles().add(variants.get(1));
                    } else {
                        left.alleles().add(variants.get(1));
                        right.alleles().add(variants.get(0));
                    }
                }
            }

            genome.left.add(left);
            genome.right.add(right);
        }
        return genome;
    }

}
