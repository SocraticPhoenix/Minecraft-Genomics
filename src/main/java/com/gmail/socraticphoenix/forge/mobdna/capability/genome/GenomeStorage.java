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
package com.gmail.socraticphoenix.forge.mobdna.capability.genome;

import com.gmail.socraticphoenix.forge.mobdna.dna.Genome;
import com.gmail.socraticphoenix.forge.mobdna.dna.impl.GenomeFactory;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class GenomeStorage implements Capability.IStorage<GenomeHandler> {

    @Nullable
    @Override
    public NBTBase writeNBT(Capability<GenomeHandler> capability, GenomeHandler instance, EnumFacing side) {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setBoolean("mating", instance.mating());
        NBTTagCompound genome = new NBTTagCompound();
        instance.genome().write(genome);
        compound.setTag("genome", genome);
        return compound;
    }

    @Override
    public void readNBT(Capability<GenomeHandler> capability, GenomeHandler instance, EnumFacing side, NBTBase nbt) {
        if(nbt instanceof NBTTagCompound) {
            NBTTagCompound compound = (NBTTagCompound) nbt;
            instance.setMating(compound.getBoolean("mating"));
            Genome genome = GenomeFactory.from(compound.getCompoundTag("genome"));
            instance.setGenome(genome);
        }
    }

}