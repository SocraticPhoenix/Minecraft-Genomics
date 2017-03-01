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

import com.gmail.socraticphoenix.forge.mobdna.dna.Chromosome;
import com.gmail.socraticphoenix.forge.mobdna.dna.GeneticComponents;
import com.gmail.socraticphoenix.forge.mobdna.dna.Genome;
import net.minecraft.entity.EntityCreature;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HaploidGenome implements Genome {
    public List<Chromosome> chromosomes;
    private List<List<Chromosome>> genome;

    public HaploidGenome() {
        EntityCreature creature = null;

        this.chromosomes = new ArrayList<>();
        this.genome = new ArrayList<>();
        this.genome.add(this.chromosomes);
    }

    @Override
    public GeneticComponents type() {
        return GeneticComponents.GENOME;
    }

    @Override
    public void write(NBTTagCompound compound) {
        NBTTagList list = new NBTTagList();
        for(Chromosome chromosome : this.chromosomes) {
            NBTTagCompound sub = new NBTTagCompound();
            chromosome.write(sub);
            list.appendTag(sub);
        }
        compound.setTag("haploid_genome", list);
    }

    public DiploidGenome merge(HaploidGenome other) {
        DiploidGenome genome = new DiploidGenome();
        genome.left.addAll(this.chromosomes);
        genome.right.addAll(other.chromosomes);
        return genome;
    }

    @Override
    public void read(NBTTagCompound compound) {
        this.chromosomes.clear();
        NBTTagList list = (NBTTagList) compound.getTag("haploid_genome");
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound sub = list.getCompoundTagAt(i);
            this.chromosomes.add(ChromosomeFactory.from(sub));
        }
    }

    @Override
    public List<List<Chromosome>> chromosomes() {
        return this.genome;
    }

    @Override
    public HaploidGenome makeGamete(Random random) {
        return this;
    }

}
