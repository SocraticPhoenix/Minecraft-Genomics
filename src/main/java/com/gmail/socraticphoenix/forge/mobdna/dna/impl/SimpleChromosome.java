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
import com.gmail.socraticphoenix.forge.mobdna.dna.Chromosome;
import com.gmail.socraticphoenix.forge.mobdna.dna.Gene;
import com.gmail.socraticphoenix.forge.mobdna.dna.GeneticComponents;
import com.gmail.socraticphoenix.forge.mobdna.util.ProbabilityHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimpleChromosome implements Chromosome {
    private List<Allele> alleles;

    public SimpleChromosome() {
        this.alleles = new ArrayList<>();
    }

    @Override
    public GeneticComponents type() {
        return GeneticComponents.CHROMOSOME;
    }

    @Override
    public void write(NBTTagCompound compound) {
        NBTTagList list = new NBTTagList();
        for(Allele allele : this.alleles) {
            NBTTagCompound sub = new NBTTagCompound();

            NBTTagCompound alleleSub = new NBTTagCompound();
            allele.write(alleleSub);

            NBTTagCompound geneSub = new NBTTagCompound();
            allele.parent().write(geneSub);

            sub.setTag("allele", alleleSub);
            sub.setTag("gene", geneSub);

            list.appendTag(sub);
        }
        compound.setTag("chromosome_alleles", list);
    }

    @Override
    public void read(NBTTagCompound compound) {
        this.alleles.clear();

        NBTTagList list = (NBTTagList) compound.getTag("chromosome_alleles");
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound sub = list.getCompoundTagAt(i);

            NBTTagCompound alleleSub = sub.getCompoundTag("allele");
            Gene gene = GeneFactory.from(sub.getCompoundTag("gene"));
            this.alleles.add(AlleleFactory.from(alleleSub, gene));
        }
    }

    @Override
    public List<Allele> alleles() {
        return this.alleles;
    }

    @Override
    public Chromosome cross(Chromosome sister, Random random) {
        SimpleChromosome chromosome = new SimpleChromosome();
        for (int i = 0; i < Math.min(this.alleles.size(), sister.alleles().size()); i++) {
            if(ProbabilityHelper.percentChance(1, random)) {
                 chromosome.alleles.add(sister.alleles().get(i));
            } else {
                chromosome.alleles.add(this.alleles.get(i));
            }
        }
        return chromosome;
    }

    @Override
    public Chromosome mutate(Random random) {
        SimpleChromosome chromosome = new SimpleChromosome();
        for (int i = 0; i < this.alleles.size(); i++) {
            Allele allele = this.alleles.get(i);
            if(ProbabilityHelper.percentChance(1, random)) {
                List<Allele> geneAlleles = allele.parent().alleles();
                chromosome.alleles.add(geneAlleles.get(random.nextInt(geneAlleles.size())));
            } else {
                chromosome.alleles.add(allele);
            }
        }
        return chromosome;
    }

}
