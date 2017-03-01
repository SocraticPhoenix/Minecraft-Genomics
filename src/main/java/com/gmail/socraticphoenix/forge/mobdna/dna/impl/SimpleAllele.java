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
import com.gmail.socraticphoenix.forge.mobdna.dna.GeneticComponents;
import net.minecraft.nbt.NBTTagCompound;

public class SimpleAllele implements Allele {
    private Gene parent;
    private int dominance;
    private int varId;
    private boolean coDominant;
    private String mnemonic;
    private String desc;

    public SimpleAllele(int id, String mnemonic, String desc, int dominance, boolean coDominant, Gene parent) {
        this.parent = parent;
        this.dominance = dominance;
        this.coDominant = coDominant;
        this.varId = id;
        this.mnemonic = mnemonic;
        this.desc = desc;
        this.parent.alleles().add(this);
    }

    @Override
    public Gene parent() {
        return this.parent;
    }

    @Override
    public int varId() {
        return this.varId;
    }

    @Override
    public int dominance() {
        return this.dominance;
    }

    @Override
    public boolean coDominant() {
        return this.coDominant;
    }

    @Override
    public String mnemonic() {
        return this.mnemonic;
    }

    @Override
    public String description() {
        return this.desc;
    }

    @Override
    public boolean dominateTo(Allele other) {
        return this.dominance > other.dominance();
    }

    @Override
    public GeneticComponents type() {
        return GeneticComponents.ALLELE;
    }

    @Override
    public void write(NBTTagCompound compound) {
        compound.setInteger("allele_varid", this.varId);
    }

    @Override
    public void read(NBTTagCompound compound) {

    }

}
