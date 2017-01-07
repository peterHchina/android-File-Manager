package com.example.managerfile.cache;


public abstract interface MapCache<K, V> extends Cache {
    public abstract boolean containsKey(K paramK);

    public abstract V get(K paramK);

    public abstract void put(K paramK, V paramV);

    public abstract V read(K paramK);

    public abstract void remove(K paramK);

    public abstract void write(K paramK, V paramV);
}
