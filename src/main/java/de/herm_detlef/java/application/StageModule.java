package de.herm_detlef.java.application;

import com.google.inject.AbstractModule;

import com.google.inject.name.Names;
import javafx.stage.Stage;


public class StageModule extends AbstractModule {
    private final Stage stage;
    private final String controllerName;

    public StageModule( Stage stage, String controllerName ) {
        this.stage = stage;
        this.controllerName = controllerName;
    }

    public StageModule( String controllerName ) {
        this.stage = new Stage();
        this.controllerName = controllerName;
    }

    @Override
    public void configure() {
        bind( Stage.class )
                .annotatedWith( Names.named(controllerName) )
                .toProvider( () -> stage )
                .asEagerSingleton();
    }
}
