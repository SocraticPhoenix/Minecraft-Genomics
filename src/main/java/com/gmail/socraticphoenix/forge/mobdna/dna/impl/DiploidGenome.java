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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DiploidGenome implements Genome {
    public List<Chromosome> left;
    public List<Chromosome> right;
    private List<List<Chromosome>> genome;

    public DiploidGenome() {
        this.left = new ArrayList<>();
        this.right = new ArrayList<>();
        this.genome = new ArrayList<>();
        this.genome.add(this.left);
        this.genome.add(this.right);
    }

    @Override
    public GeneticComponents type() {
        return GeneticComponents.ALLELE;
    }

    @Override
    public void write(NBTTagCompound compound) {
        NBTTagList left = new NBTTagList();
        for(Chromosome chromosome : this.left) {
            NBTTagCompound sub = new NBTTagCompound();
            chromosome.write(sub);
            left.appendTag(sub);
        }

        NBTTagList right = new NBTTagList();
        for(Chromosome chromosome : this.right) {
            NBTTagCompound sub = new NBTTagCompound();
            chromosome.write(sub);
            right.appendTag(sub);
        }

        compound.setTag("diploid_left_genome", left);
        compound.setTag("diploid_right_genome", right);
    }

    @Override
    public void read(NBTTagCompound compound) {
        this.left.clear();
        this.right.clear();

        NBTTagList left = (NBTTagList) compound.getTag("diploid_left_genome");
        NBTTagList right = (NBTTagList) compound.getTag("diploid_right_genome");
        for (int i = 0; i < left.tagCount(); i++) {
            NBTTagCompound sub = left.getCompoundTagAt(i);
            this.left.add(ChromosomeFactory.from(sub));
        }

        for (int i = 0; i < right.tagCount(); i++) {
            NBTTagCompound sub = right.getCompoundTagAt(i);
            this.right.add(ChromosomeFactory.from(sub));
        }
    }

    @Override
    public List<List<Chromosome>> chromosomes() {
        return this.genome;
    }

    @Override
    public HaploidGenome makeGamete(Random random) {
        HaploidGenome genome = new HaploidGenome();
        for (int i = 0; i < Math.min(this.left.size(), this.right.size()); i++) {
            boolean left = random.nextBoolean();
            Chromosome chromosome = left ? this.left.get(i) : this.right.get(i);
            genome.chromosomes.add(chromosome.cross(!left ? this.left.get(i) : this.right.get(i), random).mutate(random));
        }
        return genome;
    }

}
