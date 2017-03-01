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
package com.gmail.socraticphoenix.forge.mobdna.capability;

import com.gmail.socraticphoenix.forge.mobdna.dna.Allele;
import com.gmail.socraticphoenix.forge.mobdna.dna.Chromosome;
import com.gmail.socraticphoenix.forge.mobdna.dna.Genome;
import net.minecraft.entity.EntityCreature;
import net.minecraft.util.EnumFacing;

import java.util.List;

public class GenomeHandlerImpl implements GenomeHandler {
    private Genome genome;
    private boolean mating;

    public GenomeHandlerImpl(boolean mating) {
        this.mating = mating;
    }


    @Override
    public void setMating(boolean mating) {
        this.mating = mating;
    }

    @Override
    public void setGenome(Genome genome) {
        this.genome = genome;
    }

    @Override
    public Genome genome() {
        return this.genome;
    }

    @Override
    public boolean mating() {
        return this.mating;
    }

    @Override
    public EntityCreature doSpawn(EntityCreature self) {
        EntityCreature creature = self;
        for (int j = 0; j < this.genome().chromosomes().size(); j++) {
            Chromosome left = this.genome().chromosomes().get(0).get(j);
            Chromosome right = this.genome.chromosomes().get(1).get(j);
            for (int i = 0; i < Math.max(left.alleles().size(), right.alleles().size()); i++) {
                Allele leftAllele = left.alleles().get(i);
                Allele rightAllele = right.alleles().get(i);
                EntityCreature modified = leftAllele.parent().handler().modifySpawn(self, self.getRNG(), leftAllele, rightAllele);
                if(modified != null) {
                    creature = modified;
                }
            }
        }
        this.applyTo(creature);
        return creature;
    }

    @Override
    public void setup(EntityCreature self) {
        for(List<Chromosome> chromosome : this.genome().chromosomes()) {
            if(chromosome.size() == 2) {
                Chromosome left = chromosome.get(0);
                Chromosome right = chromosome.get(1);
                for (int i = 0; i < Math.max(left.alleles().size(), right.alleles().size()); i++) {
                    Allele leftAllele = left.alleles().get(i);
                    Allele rightAllele = right.alleles().get(i);
                    leftAllele.parent().handler().setup(self, self.getRNG(), leftAllele, rightAllele);
                }
            }
        }
    }

    private void applyTo(EntityCreature creature) {
        GenomeHandler handler = creature.getCapability(GenomeProvider.GENOME_HANDLER_CAPABILITY, EnumFacing.DOWN);
        handler.setGenome(this.genome);
        handler.setMating(this.mating);
    }

}
