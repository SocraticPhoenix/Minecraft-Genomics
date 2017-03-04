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
package com.gmail.socraticphoenix.forge.mobdna.dna;

import com.gmail.socraticphoenix.forge.mobdna.dna.genes.CowVariantGeneHandler;
import com.gmail.socraticphoenix.forge.mobdna.dna.genes.SkeletonVariantGeneHandler;
import com.gmail.socraticphoenix.forge.mobdna.dna.genes.SpiderVariantGeneHandler;
import com.gmail.socraticphoenix.forge.mobdna.dna.genes.ZombieVariantGeneHandler;
import com.gmail.socraticphoenix.forge.mobdna.dna.impl.GenomeFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneRegistry {
    public static final GeneRegistry INSTANCE = new GeneRegistry();

    private List<Gene> genes;
    private Map<String, Gene> geneById;
    private Map<String, List<Allele>> variantsById;
    private List<Gene>[] chromosomes;

    public GeneRegistry() {
        this.genes = new ArrayList<>();
        this.geneById = new HashMap<>();
        this.variantsById = new HashMap<>();
        this.chromosomes = new List[GenomeFactory.CHROMOSOME_COUNT];
        for (int i = 0; i < this.chromosomes.length; i++) {
            this.chromosomes[i] = new ArrayList<>();
        }
        this.registerDefaults();
    }

    private void registerDefaults() {
        this.register(ZombieVariantGeneHandler.ZOMBIE_VARIANT_GENE);
        this.register(CowVariantGeneHandler.COW_VARIANT_GENE);
        this.register(SkeletonVariantGeneHandler.SKELETON_VARIANT_GENE);
        this.register(SpiderVariantGeneHandler.SPIDER_VARIANT_GENE);
    }

    public List<Gene> getGenesByChromosome(int chromosome) {
        return this.chromosomes[chromosome];
    }

    public void register(Gene gene) {
        this.genes.add(gene);
        this.geneById.put(gene.id(), gene);
        this.variantsById.put(gene.id(), gene.alleles());
        this.chromosomes[gene.chromosome()].add(gene);
    }

    public List<Gene> getAllGenes() {
        return this.genes;
    }

    public Gene getById(String id) {
        return this.geneById.get(id);
    }

    public List<Allele> getVariantsById(String id) {
        return this.variantsById.get(id);
    }

}
