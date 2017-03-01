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
import com.gmail.socraticphoenix.forge.mobdna.dna.GeneHandler;
import com.gmail.socraticphoenix.forge.mobdna.dna.GeneticComponents;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.List;

public class SimpleGene implements Gene {
    public String mnemonic;
    public String description;
    private List<Allele> alleles;
    private String id;
    private int chromosome;
    private GeneHandler handler;

    public SimpleGene(String id, String mnemonic, String description, GeneHandler handler, int chromosome) {
        this.id = id;
        this.mnemonic = mnemonic;
        this.description = description;
        this.alleles = new ArrayList<>();
        this.chromosome = chromosome;
        this.handler = handler;
    }


    @Override
    public List<Allele> alleles() {
        return this.alleles;
    }

    @Override
    public GeneHandler handler() {
        return this.handler;
    }


    @Override
    public String mnemonic() {
        return this.mnemonic;
    }

    @Override
    public String description() {
        return this.description;
    }

    @Override
    public String id() {
        return this.id;
    }

    @Override
    public int chromosome() {
        return this.chromosome;
    }

    @Override
    public GeneticComponents type() {
        return GeneticComponents.ALLELE;
    }

    @Override
    public void write(NBTTagCompound compound) {
        compound.setString("gene_id", this.id);
    }

    @Override
    public void read(NBTTagCompound compound) {

    }

}
