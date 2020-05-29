package de.herm_detlef.java.application;

import com.google.inject.AbstractModule;

import javax.inject.Singleton;

@Singleton
public class StageModule<T> extends AbstractModule {
    private final Class<T> type;
    private final T stage;

    public StageModule( Class<T> theType, T theStage ) {
        type  = theType;
        stage = theStage;
    }

    @Override
    public void configure() {
        bind( type ).toProvider( () -> stage ).asEagerSingleton();
    }
}
