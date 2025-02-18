package com.thepinkhacker.decree.data.lang;

import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

import java.util.ArrayList;
import java.util.List;

public class GenericTranslationBuilder {
    private final FabricLanguageProvider.TranslationBuilder builder;
    private final List<Node> children = new ArrayList<>();

    private GenericTranslationBuilder(FabricLanguageProvider.TranslationBuilder builder) {
        this.builder = builder;
    }

    public static GenericTranslationBuilder of(FabricLanguageProvider.TranslationBuilder builder) {
        return new GenericTranslationBuilder(builder);
    }

    public GenericTranslationBuilder child(Node child) {
        children.add(child);
        return this;
    }

    public void build() {
        for (Node child : this.children) {
            child.build("", this.builder);
        }
    }

    public static class Node {
        private final String value;
        private final List<Node> children = new ArrayList<>();

        private Node(String value) {
            this.value = value;
        }

        public static Node of(String value) {
            return new Node(value);
        }

        public Node child(Node child) {
            this.children.add(child);
            return this;
        }

        public Node child(String child, String subchild) {
            this.children.add(of(child).child(of(subchild)));
            return this;
        }

        protected void build(String context, FabricLanguageProvider.TranslationBuilder builder) {
            if (this.children.isEmpty()) {
                builder.add(context, this.value);
            } else {
                if (!context.isEmpty()) context += ".";

                for (Node child : this.children) {
                    child.build(context + this.value, builder);
                }
            }
        }
    }
}
