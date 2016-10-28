/*
 * Copyright (c) 2008-2016, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hazelcast.jet2;

import com.hazelcast.nio.Address;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

import java.io.IOException;

import static com.hazelcast.util.Preconditions.checkNotNull;

/**
 * Javadoc pending
 */
public class Vertex implements IdentifiedDataSerializable {

    private MetaProcessorSupplier supplier;
    private String name;
    private int parallelism = -1;

    Vertex() {
    }

    /**
     * Javadoc pending
     *
     * @param name
     * @param processorSupplier
     */
    public Vertex(String name, ProcessorSupplier processorSupplier) {
        checkNotNull(name, "name");
        checkNotNull(processorSupplier, "supplier");

        this.supplier = new DefaultMetaSupplier(processorSupplier);
        this.name = name;
    }

    /**
     * Javadoc pending
     *
     * @param name
     * @param supplier
     */
    public Vertex(String name, MetaProcessorSupplier supplier) {
        checkNotNull(name, "name");
        checkNotNull(supplier, "supplier");

        this.supplier = supplier;
        this.name = name;
    }

    /**
     * @return name of the vertex
     */
    public String getName() {
        return name;
    }

    /**
     * @return number of parallel instances of this vertex
     */
    public int getParallelism() {
        return parallelism;
    }

    /**
     * Sets the number of parallel instances of this vertex
     */
    public Vertex parallelism(int parallelism) {
        if (parallelism <= 0) {
            throw new IllegalArgumentException("Parallelism must be greater than 0");
        }
        this.parallelism = parallelism;
        return this;
    }

    /**
     * @return the processor supplier
     */
    public MetaProcessorSupplier getSupplier() {
        return supplier;
    }

    @Override
    public String toString() {
        return "Vertex{"
                + "name='" + name + '\''
                + '}';
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(name);
        out.writeObject(supplier);
        out.writeInt(parallelism);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        name = in.readUTF();
        supplier = in.readObject();
        parallelism = in.readInt();
    }

    @Override
    public int getFactoryId() {
        return JetDataSerializerHook.FACTORY_ID;
    }

    @Override
    public int getId() {
        return JetDataSerializerHook.VERTEX;
    }

    private class DefaultMetaSupplier implements MetaProcessorSupplier {

        private final ProcessorSupplier supplier;

        public DefaultMetaSupplier(ProcessorSupplier supplier) {
            this.supplier = supplier;
        }

        @Override
        public void init(MetaProcessorSupplierContext context) {

        }

        @Override
        public ProcessorSupplier get(Address address) {
            return supplier;
        }
    }
}
