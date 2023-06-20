package dev.luke10x.easylogin.testutils;

import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.ScopeType;

import java.io.File;

public class WebArchiveFactory {
    private static final String WEBAPP_SRC = "src/main/webapp";

    public static WebArchive createCommonWebArchive() {

        final File[] libraryFiles = Maven.resolver().loadPomFromFile("build/publications/sample/pom-default.xml")
                .importDependencies(ScopeType.COMPILE, ScopeType.RUNTIME, ScopeType.TEST)
                .resolve()
                .withTransitivity()
                .asFile();

        final GenericArchive assetArchive = ShrinkWrap.create(GenericArchive.class)
                .as(ExplodedImporter.class)
                .importDirectory(WEBAPP_SRC)
                .as(GenericArchive.class);

        final WebArchive war = ShrinkWrap.create(WebArchive.class)
                .addAsLibraries(libraryFiles)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

        war.merge(assetArchive, "/", Filters.include(".*\\.(xhtml|css|xml)$"));

        return war;
    }
}
